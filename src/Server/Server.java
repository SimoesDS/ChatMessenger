package Server;

import Communication.ICommands;
import Communication.RequestResponseData;
import Application.Usuario;
import java.io.Closeable;
import Misc.DbUtils;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import org.omg.CORBA.Environment;

import Server.ClientHandler.AlertaDadosListener;

public class Server implements Closeable {

  private ServerSocket serverSocket;
  private ArrayList<ClientHandler> arrClientes;
  private HandlerListener handlerListener;
  private Object lock = new Object();
  
  final int maxConexoes = 30;

  public Server(String hostBD, String userBD, String passwordBD, int porta) throws IOException {
    serverSocket = new ServerSocket(porta, maxConexoes);
    arrClientes = new ArrayList<>();
    handlerListener = new HandlerListener();
  }

  public void aguardaConexoes() throws IOException {
    Socket socket;
    while ((socket = serverSocket.accept()) != null) {
    	//      System.out.println(new Date().getTime() + " Server: Aceitou conexao");
      ClientHandler cl = new ClientHandler(socket);
      cl.setAlertaDadosListener(handlerListener);
      new Thread(cl).start();
    }

  }

  @Override
  public void close() throws IOException {
    serverSocket.close();
  }

  public class HandlerListener implements AlertaDadosListener, ICommands {

    @Override
    public synchronized void AlertaDados(ClientHandler clientHandler, RequestResponseData requestResponseData) {

      switch (requestResponseData.getCommand()) {
        case AUTHENTICATE:
          if (requestResponseData.getObject() != null && requestResponseData.getObject() instanceof Usuario) {
            Usuario usertemp = (Usuario) requestResponseData.getObject();
            
            // Caso o usuario não estiver logado, retorna como paramentro os dados completo do usuario
	          if(usertemp.getId() > -1) {
	          	requestResponseData.setCommand(LOGGED);
	          	break;
	          }
	          
	          String strLogin = usertemp.getNomeLogin();
          	String strPassword = usertemp.getSenha();
          	
          	Usuario user = DbConnection.userLogin(strLogin, strPassword); 
	          
          	if (user == null) {
          		requestResponseData.setCommand(UNREGISTERED);
            	break;
          	}
          	
          	requestResponseData.setObject(user);
          	requestResponseData.setCommand(AUTHENTICATED);
          	
	          //clientHandler.setUsuario(usertemp);
	          //requestResponseData.setCommand(login(clientHandler));// Retorna os dados completo do usuario logado

          	clientHandler.setUsuario(user);
            
          	//(chave/trava) http://www.guj.com.br/t/o-que-e-synchronized/139744
	        	synchronized (lock) {
	            arrClientes.add(clientHandler);
	          }

          } else {
//            System.out.println(new Date().getTime() + " ClientHandler: Não recebeu o usuario");
            requestResponseData.setCommand(FAIL);
          }
          break;
        case HANDLERAUTHENTICATE:

          if (requestResponseData.getObject() != null && requestResponseData.getObject() instanceof Usuario) {
            Usuario usertemp = (Usuario) requestResponseData.getObject();
//            System.out.println(new Date().getTime() + " Caiu no HANDLERAUTHENTICATE usuario: " + usertemp.getNomeLogin());

            if (clientHandler.equalsUser(usertemp)) {
              requestResponseData.setCommand(AUTHENTICATED);

            } else {
              requestResponseData.setCommand(UNAUTHENTICATE);
            }

          } else {
//            System.out.println(new Date().getTime() + " ClientHandler: Não recebeu o usuario");
            requestResponseData.setCommand(FAIL);
          }
          break;

        case FAIL:
          break;

        case LIST_CONTACTS:

          break;

        case MESSAGE:
          if (requestResponseData.getObject() != null && requestResponseData.getObject() instanceof String) {
            String mensagem = (String) requestResponseData.getObject();
            if (clientHandler.getUsuario() != null) {
              requestResponseData.setIdOwner(clientHandler.getIDUsuario());
            }
//            System.out.println("sendTO " + requestResponseData.getIdDestino());
            sendTo(requestResponseData);
            requestResponseData.setCommand(SUCCESS);
            //            System.out.println("Server: de: " + clientHandler.getUsuario().getNome() + " para " + requestResponseData.getIdDestino() + ": "+ mensagem);
          } else {
//            System.out.println(new Date().getTime() + " ClientHandler: Não recebeu o usuario");
            requestResponseData.setCommand(FAIL);
          }
          break;

        default:
//          System.out.println(new Date().getTime() + " ClientHandler: Comando não encontrado");
          requestResponseData.setCommand(FAIL);
          break;
      }

    }

    @Override
    public void killClientHandler(ClientHandler clientHandler) {
      arrClientes.remove(clientHandler);
    }

    private int login(ClientHandler clientHandler) {
    	
    	if (clientHandler.getUsuario().getId() > -1)// Caso o usuario não estiver logado, retorna como paramentro os dados completo do usuario
      	return LOGGED;
    	
    	
    	String strLogin = clientHandler.getUsuario().getNomeLogin();
    	String strPassword = clientHandler.getUsuario().getSenha();
    	
    	Usuario user = DbConnection.userLogin(strLogin, strPassword); 

      if (user == null)
      	return UNREGISTERED;
     
      clientHandler.setUsuario(user);
        
      	//(chave/trava) http://www.guj.com.br/t/o-que-e-synchronized/139744
    	synchronized (lock) {
        arrClientes.add(clientHandler);
      }
    	
      return AUTHENTICATED;
    }

    private boolean isLogado(ClientHandler clientHandler) {
      for (ClientHandler onlyClientHandler : arrClientes) {
        if (clientHandler.getUsuario() != null) {
          if (clientHandler.getUsuario().getId() == onlyClientHandler.getUsuario().getId()) {
            return true;
          }
        }
      }
      return false;
    }

    private int buscarUsuarioBD(ClientHandler clientHandler) {
    	String strLogin = clientHandler.getUsuario().getNomeLogin();
    	String strPassword = clientHandler.getUsuario().getSenha();
    	
    	Usuario user = DbConnection.userLogin(strLogin, strPassword); 

      if (user != null) {
          clientHandler.setUsuario(user);
          return AUTHENTICATED;
       }
      return UNREGISTERED;
    }

    public void destinatario() {

    }

    private void notifyAll(ClientHandler user) {
      for (int i = 0; i < arrClientes.size(); i++) {
        if (!arrClientes.get(i).equals(user)) {

        }

        //mandar o usuario logado pra todos da lista exceto o proprio usuario
      }

    }

    private void sendTo(RequestResponseData requestResponseData) {
//      System.out.println("De: " + requestResponseData.getIdOwner() + " Para: " + requestResponseData.getIdDestino());
      for (ClientHandler clientHandler : arrClientes) {
        if (clientHandler.getUsuario() != null) {
          if (clientHandler.getUsuario().getId() == requestResponseData.getIdDestino()) {
            try {
//              System.out.println("Enviou a mensagem para: " + clientHandler.getUsuario().getId());
              clientHandler.enviarDados(requestResponseData);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }

  }
}

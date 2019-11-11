package Server;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import Communication.ICommands;
import Communication.RequestResponseData;
import Misc.Usuario;
import Server.ClientHandler.DataListener;

public class Server implements Closeable {

	private ServerSocket serverSocket;
	private ArrayList<ClientHandler> arrClientes;
	private HandlerListener handlerListener;
	private Object lock = new Object();
	private DBConnection dbConn;

	final int maxConexoes = 30;

	public Server(String hostBD, String userBD, String passwordBD, int porta) throws IOException {
		serverSocket = new ServerSocket(porta, maxConexoes);
		arrClientes = new ArrayList<>();
		handlerListener = new HandlerListener();
		dbConn = new DBConnection(hostBD, userBD, passwordBD);
	}

	public boolean testConnDB() {
		return dbConn.testConn();
	}
	
	public void aguardaConexoes() throws IOException {
		Socket socket;
		while ((socket = serverSocket.accept()) != null) {
			ClientHandler cl = new ClientHandler(socket);
			cl.setDataListener(handlerListener);
			new Thread(cl).start();
		}

	}

	@Override
	public void close() throws IOException {
		serverSocket.close();
	}

	private class HandlerListener implements DataListener, ICommands {

		@Override
		public synchronized void processData(ClientHandler clientHandler, RequestResponseData reqRespData) {

			switch (reqRespData.getCommand()) {
			case AUTHENTICATE:
				Usuario user = reqRespData.getUser();
				System.out.println(new Date().getTime() + " Autenticar " + user.getNomeLogin());

				reqRespData = dbConn.login(user);

				if (reqRespData == null) {
					System.out.println(new Date().getTime() + " Usuario/senha não encontrado!!");

					try {
						clientHandler.enviarDados(new RequestResponseData(UNREGISTERED));
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}

				// Confere se o ID ja esta na lista dos usuarios logado
				if (getClientHandlerEqualId(reqRespData.getUser().getId()) != null) {
					System.out.println(new Date().getTime() + " Usuário " + user.getNomeLogin() + " ja est� logado");

					try {
						clientHandler.enviarDados(new RequestResponseData(LOGGED));
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}

				reqRespData.setCommand(AUTHENTICATED);
				clientHandler.setId(reqRespData.getUser().getId());
				setStatusOfUsers(reqRespData);
				
				try {
					clientHandler.enviarDados(reqRespData);
					// (chave/trava) http://www.guj.com.br/t/o-que-e-synchronized/139744
					synchronized (lock) {
						arrClientes.add(clientHandler);
					}

					System.out
							.println(new Date().getTime() + " Usuario " + reqRespData.getUser().getNome() + " esta online!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				notifyAll(reqRespData.getUser());
				break;

			case LOGOUT:
				killClientHandler(reqRespData.getUser().getId());
				reqRespData.getUser().setOffline();
				System.out
				.println(new Date().getTime() + " Usuario " + reqRespData.getUser().getNome() + " esta offline!");
				notifyAll(reqRespData.getUser());
				break;

			case MESSAGE:
				System.out
						.println(new Date().getTime() + " Chegou mensagem de: " + reqRespData.getIdSender() + " para: " + reqRespData.getIdReceiver());

				RequestResponseData rqd = reqRespData;
				new Thread(new Runnable() {
					@Override
					public void run() {
						if (dbConn.saveMessage(rqd) > 0)
							sendTo(rqd);
					}
				}).start();
				break;

			default:
				break;
			}

		}

		@Override
		public void killClientHandler(int idUser) {
			synchronized (lock) {
				ClientHandler ch = getClientHandlerEqualId(idUser);
				if (ch != null)
					for (int i = 0; i < arrClientes.size(); i++)
						if (arrClientes.get(i).getId() == idUser)
							arrClientes.remove(i);
			}
		}

		private void notifyAll(Usuario user) {
			synchronized (lock) {
				for (ClientHandler ch : arrClientes)
					if (ch.getId() != user.getId()) {
						try {
							ch.enviarDados(new RequestResponseData(user, STATUS));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
			}
		}

		private synchronized void sendTo(RequestResponseData requestResponseData) {
			ClientHandler ch = getClientHandlerEqualId(requestResponseData.getIdReceiver());
			if (ch != null)
				try {
					ch.enviarDados(requestResponseData);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	private ClientHandler getClientHandlerEqualId(int id) {
		for (ClientHandler clientHandler : arrClientes)
			if (clientHandler.getId() == id)
				return clientHandler;
		return null;
	}

	private void setStatusOfUsers(RequestResponseData reqRespData) {
		for (Usuario user : reqRespData.getAllContacts())
			if (getClientHandlerEqualId(user.getId()) != null)
				user.setOnline();
	}
}

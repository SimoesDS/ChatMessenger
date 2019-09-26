package Server;

import Communication.ICommands;
import Communication.RequestResponseData;
import Application.Usuario;
import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import Server.ClientHandler.DataListener;

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
			ClientHandler cl = new ClientHandler(socket);
			cl.setDataListener(handlerListener);
			new Thread(cl).start();
		}

	}

	@Override
	public void close() throws IOException {
		serverSocket.close();
	}

	public class HandlerListener implements DataListener, ICommands {

		@Override
		public synchronized void processData(ClientHandler clientHandler, RequestResponseData requestResponseData) {

			switch (requestResponseData.getCommand()) {
			case AUTHENTICATE:
				Object data[] = requestResponseData.getObject();
				Usuario usertemp = (Usuario) data[0];

				// Confere se o usuario tem ID, se ja tiver ja esta autenticado
				if (usertemp.getId() > -1) {
					requestResponseData.setCommand(LOGGED);
					System.out.println(
							new Date().getTime() + " Server: usuario " + clientHandler.getUsuario().getNomeLogin() + " ja está logado");
					break;
				}

				data = DbConnection.login(usertemp);
				if (data[0] == null) {
					requestResponseData.setCommand(UNREGISTERED);
					System.out.println(new Date().getTime() + " Server: usuario/senha não encontrado!!");
					break;
				}
				
				requestResponseData.setObject((Usuario) data[0], (Object[]) data[1], (Object[]) data[2]);
				requestResponseData.setCommand(AUTHENTICATED);

				clientHandler.setUsuario((Usuario) data[0]);
				
				System.out.println(
						new Date().getTime() + " Server: usuario " + clientHandler.getUsuario().getNomeLogin() + " foi autenticado!!");

				// (chave/trava) http://www.guj.com.br/t/o-que-e-synchronized/139744
				synchronized (lock) {
					arrClientes.add(clientHandler);
				}
				break;

			case FAIL:
				break;

			case LIST_CONTACTS:

				break;

			case MESSAGE:
				System.out.println("Chegou mensagem De: " + requestResponseData.getIdOwner() + " Para: "
						+ requestResponseData.getIdDestino());
				if (clientHandler.getUsuario() != null)
					requestResponseData.setIdOwner(clientHandler.getIDUsuario());

				sendTo(requestResponseData);
				requestResponseData.setCommand(SUCCESS);
				break;

			default:
				requestResponseData.setCommand(FAIL);
				break;
			}

		}

		@Override
		public void killClientHandler(ClientHandler clientHandler) {
			arrClientes.remove(clientHandler);
		}
		
		@Override
		public boolean thisListenerAlreadyExists(ClientHandler clientHandler) {
			return getClientHandlerEqualId(clientHandler.getIDUsuario()) != null;
		}

		private void notifyAll(ClientHandler user) {
			for (int i = 0; i < arrClientes.size(); i++) {
				if (!arrClientes.get(i).equals(user)) {

				}

				// mandar o usuario logado pra todos da lista exceto o proprio usuario
			}

		}

		private void sendTo(RequestResponseData requestResponseData) {
			ClientHandler ch = getClientHandlerEqualId(requestResponseData.getIdDestino());
			if(ch != null)
				try {
					ch.enviarDados(requestResponseData);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private ClientHandler getClientHandlerEqualId(int id) {
			for (ClientHandler clientHandler : arrClientes)
				return clientHandler.getUsuario().getId() == id ? clientHandler : null;
			return null;			
		}
}

package Server;

import Communication.ICommands;
import Communication.RequestResponseData;
import Application.Usuario;
import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
			// System.out.println(new Date().getTime() + " Server: Aceitou conexao");
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
				Object data[] = requestResponseData.getObject();
				Usuario usertemp = (Usuario) data[0];

				// Confere se o usuario tem ID, se ja tiver ja esta autenticado
				if (usertemp.getId() > -1) {
					requestResponseData.setCommand(LOGGED);
					break;
				}

				String strLogin = usertemp.getNomeLogin();
				String strPassword = usertemp.getSenha();

				// Usuario user = DbConnection.userLogin(strLogin, strPassword);
				data = DbConnection.login(strLogin, strPassword);
				if (data[0] == null) {
					requestResponseData.setCommand(UNREGISTERED);
					break;
				}

				/*
				 * data[0] = user; // Usuario logado data[1] = null; // Lista de contatos
				 * data[2] = null; // Conversas
				 */
				requestResponseData.setObject((Usuario) data[0], (Object[]) data[1], (Object[]) data[2]);
				requestResponseData.setCommand(AUTHENTICATED);

				clientHandler.setUsuario((Usuario) data[0]);

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

		private void notifyAll(ClientHandler user) {
			for (int i = 0; i < arrClientes.size(); i++) {
				if (!arrClientes.get(i).equals(user)) {

				}

				// mandar o usuario logado pra todos da lista exceto o proprio usuario
			}

		}

		private void sendTo(RequestResponseData requestResponseData) {
			for (ClientHandler clientHandler : arrClientes) {
				if (clientHandler.getUsuario() != null) {
					if (clientHandler.getUsuario().getId() == requestResponseData.getIdDestino()) {
						try {
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

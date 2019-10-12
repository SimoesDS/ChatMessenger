package Server;

import Communication.ICommands;
import Misc.RequestResponseData;
import Misc.*;

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
		public synchronized void processData(ClientHandler clientHandler, RequestResponseData reqRespData) {

			switch (reqRespData.getCommand()) {
			case AUTHENTICATE:
				Usuario user = reqRespData.getUser();
				System.out.println(new Date().getTime() + " ClientHandler: chegou dados do " + user.getNomeLogin());

				// Confere se o ID ja esta na lista dos usuarios logado
				if (getClientHandlerEqualId(user.getId()) != null) {
					System.out.println(new Date().getTime() + " Server: usuario " + user.getNomeLogin() + " ja est� logado");

					try {
						clientHandler.enviarDados(new RequestResponseData(LOGGED));
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}

				reqRespData = DbConnection.login(user);
				if (reqRespData == null) {
					System.out.println(new Date().getTime() + " Server: usuario/senha não encontrado!!");

					try {
						clientHandler.enviarDados(new RequestResponseData(UNREGISTERED));
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				reqRespData.setCommand(AUTHENTICATED);

				clientHandler.setId(reqRespData.getUser().getId());

				// (chave/trava) http://www.guj.com.br/t/o-que-e-synchronized/139744
				synchronized (lock) {
					arrClientes.add(clientHandler);
				}

				System.out
						.println(new Date().getTime() + " Server: usuario " + reqRespData.getUser().getNome() + " esta online!");

				sendTo(reqRespData);
				break;

			case FAIL:
				break;

			case LIST_CONTACTS:

				break;

			case MESSAGE:
				System.out
						.println("Chegou mensagem De: " + reqRespData.getIdSender() + " Para: " + reqRespData.getIdReceiver());

				RequestResponseData rqd = reqRespData;
				new Thread(new Runnable() {
					@Override
					public void run() {
						if (DbConnection.saveMessage(rqd) > 0)
							sendTo(rqd);
					}
				}).start();
				break;

			default:
				reqRespData.setCommand(FAIL);
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

		private synchronized void sendTo(RequestResponseData requestResponseData) {
			ClientHandler ch = getClientHandlerEqualId(requestResponseData.getIdReceiver());
			if (ch != null)
				try {
					ch.enviarDados(requestResponseData);
					requestResponseData.setCommand(SUCCESS);
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
}

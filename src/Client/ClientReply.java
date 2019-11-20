package Client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import Communication.ICommands;
import Communication.IComunicacao;
import Communication.RequestResponseData;
import Communication.SocketComunicacao;

public class ClientReply implements ICommands, Runnable {

	private IComunicacao comunicacao;
	private String strHost;
	private int intPorta;

	private RequestResponseData requestResponseData;

	public ClientReply(String strHost, int intPorta, RequestResponseData reqRespData) {
		this.strHost = strHost;
		this.intPorta = intPorta;
		this.requestResponseData = reqRespData;
	}

	@Override
	public void run() {
		try {
			switch (requestResponseData.getCommand()) {
			case AUTHENTICATE:
				enviarDados(requestResponseData);
				System.out.println(
						new Date().getTime() + " Autenticar " + requestResponseData.getUser().getNomeLogin());
				break;

			case MESSAGE:
				connect();
				System.out
						.println(new Date().getTime() + " Enviar mensagem para " + requestResponseData.getIdReceiver());
				enviarDados(requestResponseData);
				break;
			case LOGOUT:
				connect();
				enviarDados(requestResponseData);
				break;

			default:
				break;
			}
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void enviarDados(Object obj) throws IOException {
		comunicacao.enviarObject(obj);
	}

	public IComunicacao connect() {
		try {
			comunicacao = new SocketComunicacao(new Socket(strHost, intPorta));
		} catch (IOException e) {
			System.out.println("Erro ao conectar no servidor!!");
			e.printStackTrace();
		}
		return comunicacao;
	}
}

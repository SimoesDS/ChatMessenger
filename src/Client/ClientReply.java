package Client;
//https://www.programiz.com/java-programming/examples/convert-outputstream-string

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import Communication.ICommands;
import Communication.IComunicacao;
import Communication.SocketComunicacao;
import Misc.RequestResponseData;
import Misc.Usuario;

public class ClientReply implements ICommands, Runnable {

	private Usuario usuario;

	private IComunicacao comunicacao;

	private String strHost;
	private int intPorta;

	private RequestResponseData requestResponseData;

	public ClientReply(String strHost, int intPorta, RequestResponseData reqRespData) {
		this.strHost = strHost;
		this.intPorta = intPorta;
		this.requestResponseData = reqRespData;
	}
	
	public ClientReply(String strHost, int intPorta, RequestResponseData reqRespData, IComunicacao conn) {
		this.strHost = strHost;
		this.intPorta = intPorta;
		this.requestResponseData = reqRespData;
		this.comunicacao = conn;
	}

	@Override
	public void run() {
		try {
			switch (requestResponseData.getCommand()) {
			case AUTHENTICATE:
				//connect();
				requestResponseData.setCommand(AUTHENTICATE);
				requestResponseData.setObject(usuario, null, null);
				enviarDados(requestResponseData);
				System.out.println(
						new Date().getTime() + " ClientReply: Autenticar " + requestResponseData.getOwner().getNomeLogin());
				
				
				break;

			case MESSAGE:
				connect();
				System.out
						.println(new Date().getTime() + " ClientReply: envia mensagem para " + requestResponseData.getIdDestino());
				enviarDados(requestResponseData);
				break;

			default:
				break;
			}
		} catch (

		UnknownHostException e1) {
			e1.printStackTrace(); // TODO: Caso der erro de alguma forma mostrar ao usuario
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
			System.out.println("Client: Erro ao conectar no servidor");
			e.printStackTrace();
		}
		return comunicacao;
	}
}

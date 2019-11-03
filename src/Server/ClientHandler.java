package Server;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

import Communication.ICommands;
import Communication.IComunicacao;
import Communication.RequestResponseData;
import Communication.SocketComunicacao;

public class ClientHandler implements Runnable, ICommands {

	private IComunicacao comunicacao;
	private int id = -1;
	private DataListener dataListener;

	public ClientHandler(Socket conexaoCliente) throws IOException {
		comunicacao = new SocketComunicacao(conexaoCliente);
	}

	@Override
	public void run() {
		try {
			Object obj = recebeDados();

			if (obj != null && obj instanceof RequestResponseData) {
				RequestResponseData rrd = (RequestResponseData) obj;
				System.out.println(new Date().getTime() + " Chegou dados de " + rrd.getIdSender());
				dataListener.processData(this, rrd);
			}

		} catch (IOException e) {
			dataListener.killClientHandler(this.getId());
		} finally {
			Thread.currentThread().interrupt();
		}
	}

	public void enviarDados(Object obj) throws IOException {
		comunicacao.enviarObject(obj);
	}

	private Object recebeDados() throws IOException {
		return comunicacao.recebeObject();
	}

	public interface DataListener {
		void processData(ClientHandler clientHandler, RequestResponseData requestResponseData);

		void killClientHandler(int idUser);
	}

	public void setDataListener(DataListener dataListener) {
		this.dataListener = dataListener;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

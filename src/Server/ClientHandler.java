package Server;

import Communication.ICommands;

import Communication.IComunicacao;
import Communication.SocketComunicacao;
import Misc.RequestResponseData;
import Misc.Usuario;
import Application.Core;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class ClientHandler implements Runnable, ICommands {

	private Socket serverClient = null;
	private IComunicacao comunicacao;
	private Usuario usuario;
	private RequestResponseData requestResponseData;
	private DataListener dataListener;

	public ClientHandler(Socket conexaoCliente) throws IOException {
		comunicacao = new SocketComunicacao(conexaoCliente);
	}

	@Override
	public void run() {
		System.out.println(new Date().getTime() + " ClientHandler: ouviu alguma coisa");
		try {
			Object obj = recebeDados();

			if (obj != null && obj instanceof RequestResponseData) {
				requestResponseData = (RequestResponseData) obj;
				System.out.println(new Date().getTime() + " ClientHandler: chegou dados do " + ((Usuario) requestResponseData.getObject()[0]).getNomeLogin());
				
				dataListener.processData(this, requestResponseData);

				System.out.println(new Date().getTime() + " ClientHandler: envia dados para o " + ((Usuario) requestResponseData.getObject()[0]).getNome());
				enviarDados(requestResponseData);
			}

		} catch (IOException e) {
			dataListener.killClientHandler(this);
		} finally {
			Thread.currentThread().interrupt();
		}
	}

	public void enviarDados(Object obj) throws IOException {
		comunicacao.enviarObject(obj);
	}

	public Object recebeDados() throws IOException {
		return comunicacao.recebeObject();
	}

	public interface DataListener {
		void processData(ClientHandler clientHandler, RequestResponseData requestResponseData);
		void killClientHandler(ClientHandler clientHandler);
	}

	public void setDataListener(DataListener dataListener) {
		this.dataListener = dataListener;
	}

	public int getIDUsuario() {
		return this.usuario.getId();
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}

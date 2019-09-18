package Server;

import Communication.ICommands;

import Communication.IComunicacao;
import Communication.RequestResponseData;
import Communication.SocketComunicacao;
import Application.Core;
import Application.Usuario;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class ClientHandler implements Runnable, ICommands {

	private Socket serverClient = null;
	private IComunicacao comunicacao;
	private Usuario usuario;
	private RequestResponseData requestResponseData;
	private AlertaDadosListener alertaDadosListener;
	public ClientHandler(Socket conexaoCliente) throws IOException {
		comunicacao = new SocketComunicacao(conexaoCliente);
	}

	@Override
	public void run() {
		System.out.println(new Date().getTime() + " ClientHandler: entrou no run()");
		try {
			Object obj = null;
			System.out.println(new Date().getTime() + " ClientHandler: vai entrar no while");
			while (true) {
				try{
					obj = recebeDados();
				}catch(IOException e){
					alertaDadosListener.killClientHandler((ClientHandler) this);
					return;
				}
				if (obj != null && obj instanceof RequestResponseData) {
					requestResponseData = (RequestResponseData) obj;
					System.out.println(new Date().getTime() + " ClientHandler: chegou alguma coisa " + requestResponseData.getObject());
					if(requestResponseData.getCommand() == MESSAGE)
						System.out.println("Chegou mensagem De: " + requestResponseData.getIdOwner() + " Para: " + requestResponseData.getIdDestino());
					alertaDadosListener.AlertaDados((ClientHandler) this, requestResponseData);

					if (requestResponseData.getCommand() == AUTHENTICATED) {
						System.out.println(new Date().getTime() + " ClientHandler: envia dados do usuario " + requestResponseData.getObject());
					}

					enviarDados(requestResponseData);
					System.out.println(new Date().getTime() + " ClientHandler: envia dados");


					obj = null;
				} else {
					System.out.println(new Date().getTime() + " ClientHandler: Finalizar Thread()");
					alertaDadosListener.killClientHandler((ClientHandler) this);
					Thread.currentThread().interrupt();
					return;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void enviarDados(Object obj) throws IOException {
		comunicacao.enviarObject(obj);
	}

	public Object recebeDados() throws IOException {
		return comunicacao.recebeObject();
	}

	public interface AlertaDadosListener {

		void AlertaDados(ClientHandler clientHandler, RequestResponseData requestResponseData);
		void killClientHandler(ClientHandler clientHandler);
	}

	public void setAlertaDadosListener(AlertaDadosListener alertaDadosListener) {
		this.alertaDadosListener = alertaDadosListener;
	}

	public int getIDUsuario() {
		return this.usuario.getId();
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public boolean equalsUser(Usuario user) {
		if(this.usuario.getId() == user.getId()  
				&& this.usuario.getNome() == user.getNome()
				&& this.usuario.getNomeLogin() == user.getNomeLogin()
				&& this.usuario.getSenha() == user.getSenha()
				&& this.usuario.getSenha() == user.getSenha()
				&& this.usuario.isOnline() == user.isOnline())
			return true;
		else return false;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}

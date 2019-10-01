/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import Communication.ICommands;
import Communication.IComunicacao;
import Communication.SocketComunicacao;
import Misc.RequestResponseData;
import Misc.Usuario;

public class ClientListener implements Runnable, ICommands {

	private IComunicacao comunicacao;
	private RequestResponseData requestResponseData;
	private AlertaTelaListener alertaTelaListener;

	private String strHost;
	private int intPorta;

	public ClientListener(String strHost, int intPorta) {
		this.strHost = strHost;
		this.intPorta = intPorta;
		this.requestResponseData = new RequestResponseData();
	}

	public ClientListener(String strHost, int intPorta, IComunicacao conn) {
		this.strHost = strHost;
		this.intPorta = intPorta;
		this.requestResponseData = new RequestResponseData();
		this.comunicacao = conn;
	}

	@Override
	public void run() {
		Object obj = null;
		// TODO: Tem que colocar a condição de saida do while quando o usuario fizer
		// logoff
		// TODO: Quando perde a conexao com o serv fica dando erro infinito
		try {
			while (true) {

				if ((obj = recebeDados()) != null) {
					if (obj instanceof RequestResponseData) {
						requestResponseData = (RequestResponseData) obj;
						switch (requestResponseData.getCommand()) {
						case AUTHENTICATED:
							if (requestResponseData != null) {
								alertaTelaListener.AlertaTela(requestResponseData);
								requestResponseData.clearObject();
							} else {
								requestResponseData = new RequestResponseData();
								requestResponseData.setCommand(AUTHENTICATE);
								enviarDados(requestResponseData);
							}
							break;

						case UNREGISTERED:
							alertaTelaListener.AlertaTela(requestResponseData);
							break;

						case LOGGED:
							alertaTelaListener.AlertaTela(requestResponseData);
							break;

						case MESSAGE:
							alertaTelaListener.AlertaTela(requestResponseData);
							requestResponseData.clearObject();
							// requestResponseData.setCommand(SUCCESS);
							// enviarDados(requestResponseData);
							break;

						case SUCCESS:
							break;
						case FAIL:
							break;

						default:
						}
					}
				}
			}
		} catch (Exception e) {
			try {
				requestResponseData.clearObject();
				enviarDados(requestResponseData);
			} catch (IOException e1) {
				// TODO: Se der erro tem que voltar para tela de login
				e.printStackTrace();
			}
		}
	}

	private Object recebeDados() throws IOException {
		return comunicacao.recebeObject();
	}

	private void enviarDados(Object obj) throws IOException {
		comunicacao.enviarObject(obj);
	}

	public void conectar() {
		try {
			comunicacao = new SocketComunicacao(new Socket(strHost, intPorta));
		} catch (IOException e) {
			System.out.println("Client: Erro ao conectar no servidor");
			e.printStackTrace();
		}
	}

	public interface AlertaTelaListener {

		void AlertaTela(RequestResponseData requestResponseData);
	}

	public void setAlertaTelaListener(AlertaTelaListener alertaTelaListener) {
		this.alertaTelaListener = alertaTelaListener;
	}

}

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
	private RequestResponseData requestResponseDataResend;
	private AlertaTelaListener alertaTelaListener;
	private Usuario usuario;

	private String strHost;
	private int intPorta;

	public ClientListener(String strHost, int intPorta, Usuario user) {
		usuario = user;
		this.strHost = strHost;
		this.intPorta = intPorta;
		this.requestResponseData = new RequestResponseData();
	}

	@Override
	public void run() {
		conectar();
		requestResponseData.setCommand(AUTHENTICATE);
		requestResponseData.setObject(usuario, null, null);
		try {
			enviarDados(requestResponseData);
		} catch (IOException ex) {
			Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
		}
		Object obj = null;
		Object data[];
	// TODO: Tem que colocar a condição de saida do while quando o usuario fizer logoff
		while (true) { 
			try {
				if ((obj = recebeDados()) != null) {
					if (obj instanceof RequestResponseData) {
						requestResponseData = (RequestResponseData) obj;
						switch (requestResponseData.getCommand()) {
						case AUTHENTICATED:
							data = requestResponseData.getObject();
							if (data != null && data[0] instanceof Usuario) {
								alertaTelaListener.AlertaTela(requestResponseData);
								requestResponseData.setCommand(SUCCESS);
								requestResponseData.clearObject();
								enviarDados(requestResponseData);
							} else {
								requestResponseData.setCommand(FAIL);
								requestResponseData.clearObject();
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
							data = requestResponseData.getObject();
							if (data != null && data[2] instanceof Object[]) {
								alertaTelaListener.AlertaTela(requestResponseData);
								requestResponseData.clearObject();
								requestResponseData.setCommand(SUCCESS);
								enviarDados(requestResponseData);
							} else {
								requestResponseData.clearObject();
								requestResponseData.setCommand(FAIL);								
								enviarDados(requestResponseData);
							}
							break;

						case SUCCESS:
							break;
						case FAIL:
							break;

						default:
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

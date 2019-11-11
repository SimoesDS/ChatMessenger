/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.IOException;
import java.net.SocketException;

import Application.Core;
import Application.HeaderPanel.KillClientListener;
import Communication.ICommands;
import Communication.IComunicacao;
import Communication.RequestResponseData;
import Misc.Usuario;

public class ClientListener extends Thread implements ICommands {

	private IComunicacao comunicacao;
	private AlertaTelaListener alertaTelaListener;
	private boolean killListener = false;

	public ClientListener(IComunicacao conn) {
		this.comunicacao = conn;
		Core.setKillClientListener(new KillClient());
	}

	@Override
	public void run() {
		Object obj = null;
		try {
			while (!killListener) {
				if ((obj = recebeDados()) != null && obj instanceof RequestResponseData) {
					RequestResponseData requestResponseData = (RequestResponseData) obj;
					alertaTelaListener.AlertaTela(requestResponseData);
				}
			}
		} catch (Exception e) {
			alertaTelaListener.AlertaTela(new RequestResponseData(LOGOUT));
			if (e instanceof SocketException) {
				SocketException se = (SocketException) e;
				if (!se.getMessage().equals("Socket closed")) {
					e.printStackTrace();
				}
			} else
				e.printStackTrace();
		}
	}

	private Object recebeDados() throws IOException {
		return comunicacao.recebeObject();
	}

	public interface AlertaTelaListener {

		void AlertaTela(RequestResponseData requestResponseData);
	}

	public void setAlertaTelaListener(AlertaTelaListener alertaTelaListener) {
		this.alertaTelaListener = alertaTelaListener;
	}

	private class KillClient implements KillClientListener {

		@Override
		public void kill(Usuario user) {
			killListener = true;
			if (comunicacao != null) {
				System.out.println("Chega por hoje!");
				comunicacao.closeConnection(); 
			}
			comunicacao = null;
			Thread.currentThread().interrupt();	
		}
	}
}

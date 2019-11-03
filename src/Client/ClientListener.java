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
		// TODO: Tem que colocar a condição de saida do while quando o usuario fizer
		// logoff
		// TODO: Quando perde a conexao com o serv fica dando erro infinito
		try {
			while (!killListener) {
				if ((obj = recebeDados()) != null && obj instanceof RequestResponseData) {
					RequestResponseData requestResponseData = (RequestResponseData) obj;
					alertaTelaListener.AlertaTela(requestResponseData);
				}
			}
		} catch (Exception e) {
			// TODO: Se der erro tem que voltar para tela de login
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
			System.out.println("Fez logout");
			killListener = true;
			comunicacao.closeConnection();
			comunicacao = null;
			Thread.currentThread().interrupt();
		}
	}
}

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

import Application.HeaderPanel.KillClientListener;
import Communication.ICommands;
import Communication.IComunicacao;
import Communication.SocketComunicacao;
import Misc.RequestResponseData;
import Misc.Usuario;

public class ClientListener extends Thread implements ICommands {

	private IComunicacao comunicacao;
	private AlertaTelaListener alertaTelaListener;
  private boolean killListener = false;

	private String strHost;
	private int intPorta;

	public ClientListener(String strHost, int intPorta) {
		this.strHost = strHost;
		this.intPorta = intPorta;
	}

	public ClientListener(String strHost, int intPorta, IComunicacao conn) {
		this.strHost = strHost;
		this.intPorta = intPorta;
		this.comunicacao = conn;
		//Core.getHeaderInstance().setKillClientListener(new KillClient());
	}

	@Override
	public void run() {
		Object obj = null;
		// TODO: Tem que colocar a condição de saida do while quando o usuario fizer
		// logoff
		// TODO: Quando perde a conexao com o serv fica dando erro infinito
		try {
      while (!killListener) {

				if ((obj = recebeDados()) != null 
					&& obj instanceof RequestResponseData) {
						RequestResponseData requestResponseData = (RequestResponseData) obj;
						alertaTelaListener.AlertaTela(requestResponseData);
				}
			}
		} catch (Exception e) {
			try {
				enviarDados(new RequestResponseData(LOGOUT));
				e.printStackTrace();
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

	class KillClient implements KillClientListener {

    @Override
    public void kill() {
      try {
        System.out.println("Setou para LOG_OUT");
        enviarDados(new RequestResponseData(LOGOUT));
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
      	killListener = true;
			}
    }
  }

}

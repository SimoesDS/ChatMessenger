/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Application.Core;

import Application.Usuario;
import Communication.ICommands;
import Communication.IComunicacao;
import Communication.RequestResponseData;
import Communication.SocketComunicacao;
import Misc.DbUtils;
import Server.ClientHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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
//    System.out.println(new Date().getTime() + " Entrou no run() da Thread ClientListener");
  	conectar();
    requestResponseData.setCommand(AUTHENTICATE);
    requestResponseData.setObject(new Object[]{ usuario, null, null});
    try {
      enviarDados(requestResponseData);
//      System.out.println(new Date().getTime() + " Thread ClientListener: dados do usuario enviado " + usuario.getNomeLogin());
    } catch (IOException ex) {
      Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
    }
    Object obj = null;
    Object data[];
    try {    	 
      while ((obj = recebeDados()) != null) {
        if (obj instanceof RequestResponseData) {
          requestResponseData = (RequestResponseData) obj;
          switch (requestResponseData.getCommand()) {
            case AUTHENTICATED:
            	data = requestResponseData.getObject(); 
            	if (data != null && data[0] instanceof Usuario) {
                
              	usuario.atualizaUser((Usuario) data[0]);
//                System.out.println(" Endereço de memoria ClientHandler " + usuario);
//                System.out.println(" Seja bem vindo " + usuario.getNome());
                alertaTelaListener.AlertaTela(requestResponseData);
              } else {
//                System.out.println("ClientHandler: Não recebeu o usuário");
                requestResponseData.setCommand(UNAUTHENTICATE);
              }

              break;

            case UNREGISTERED:
//              System.out.println(new Date().getTime() + " Client: Usuário não encontrado");
              usuario.setId(-1);
              //Mostra uma mensagem que esse usuário já esta logado
              break;

            case LOGGED:
//              System.out.println(new Date().getTime() + " Client: " + usuario.getNomeLogin() + " já está logado");
              alertaTelaListener.AlertaTela(requestResponseData);
              //Mostra uma mensagem que esse usuário já esta logado
              break;

            case MESSAGE:
            	data = requestResponseData.getObject(); 
              if (data != null && data[0] instanceof String) {
                String mensagem = (String) data[0];
//                System.out.println("chgou mensagem " + mensagem);
                alertaTelaListener.AlertaTela(requestResponseData);
              } else {
//                System.out.println("Cliente: Recebeu mensagem nula");
                //requestResponseData.setCommand(FAIL);
              }
              break;

            case SUCCESS:
//              System.out.println(new Date().getTime() + " Client: Servidor recebeu a mensagem");
              break;
            case FAIL:
//              System.out.println(new Date().getTime() + " Client: " + " Reenviar o objeto");
              enviarDados(requestResponseDataResend);
              break;

            default:
          }
        }//else
        //requisitar o reenvio
      }
    } catch (Exception e) {
      e.printStackTrace();
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

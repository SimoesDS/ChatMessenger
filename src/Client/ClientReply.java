package Client;
//https://www.programiz.com/java-programming/examples/convert-outputstream-string

import Communication.IComunicacao;

import Communication.ICommands;
import Communication.RequestResponseData;
import Communication.SocketComunicacao;
import Application.Core;
import Application.Usuario;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientReply implements ICommands, Runnable {

  private Usuario usuario;

  private IComunicacao comunicacao;

  private String strHost;
  private int intPorta;
  private String mensagem = null;
  private int idDestino;

  private RequestResponseData requestResponseData;
  
  public ClientReply(String strHost, int intPorta, RequestResponseData reqRespData) {
    this.strHost = strHost;
    this.intPorta = intPorta;
    this.requestResponseData = reqRespData;
  }

  public ClientReply(String strHost, int intPorta, Usuario user, String mensagem, int idDestino) {
    usuario = user;
    this.strHost = strHost;
    this.intPorta = intPorta;
    this.mensagem = mensagem;
    this.requestResponseData = new RequestResponseData();
    this.idDestino = idDestino;
  }
  
  

  @Override
  public void run() {
    Object obj = null;

    try {
    	if (requestResponseData.getObject()[0] instanceof Usuario) {
    		switch (requestResponseData.getCommand()) {
				case MESSAGE:
					conectar();
					System.out.println(new Date().getTime() + " Thread ClientReply: envia MESSAGE");
	        enviarDados(requestResponseData);
					break;

				default:
					break;
				}
    	}else if (usuario != null && usuario.getId() == 0) {
        conectar();
        requestResponseData.setCommand(AUTHENTICATE);
        requestResponseData.setObject( usuario, null, null);
        enviarDados(requestResponseData);
        System.out.println(new Date().getTime() + " Thread ClientReply: " + usuario.getNomeLogin() + " precisa AUTHENTICATE");
      }
    } catch (UnknownHostException e1) {
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

  public void conectar() {
    try {
      comunicacao = new SocketComunicacao(new Socket(strHost, intPorta));
    } catch (IOException e) {
      System.out.println("Client: Erro ao conectar no servidor");
      e.printStackTrace();
    }
  }
}

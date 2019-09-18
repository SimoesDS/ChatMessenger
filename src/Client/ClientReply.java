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
  private RequestResponseData requestResponseDataResend;

  public ClientReply(String strHost, int intPorta, Usuario user) {
    usuario = user;
    this.strHost = strHost;
    this.intPorta = intPorta;
    this.requestResponseData = new RequestResponseData();
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
    System.out.println(new Date().getTime() + " Entrou no run() da Thread ClientReply usuario " + usuario.getNomeLogin());
    Object obj = null;

    try {
      if (usuario != null && usuario.getId() == 0) {

        conectar();
        //new Thread(new HandlerListener()).start();
        requestResponseData.setCommand(AUTHENTICATE);
        requestResponseData.setObject(usuario);
        enviarDados(requestResponseData);
        System.out.println(new Date().getTime() + " Thread ClientReply dados enviado AUTHENTICATE");
      } else if (usuario != null) {
        conectar();
        requestResponseData.setCommand(MESSAGE);
        requestResponseData.setObject(mensagem);
        requestResponseData.setIdOwner(usuario.getId());
        requestResponseData.setIdDestino(idDestino);
        enviarDados(requestResponseData);
        System.out.println(new Date().getTime() + " Thread ClientReply dados enviado MESSAGE");
        System.out.println("Dentro do BodyPanel De: " + requestResponseData.getIdOwner() + " Para: " + requestResponseData.getIdDestino() + " O id do usuario é: " + usuario.getId());
      }

      /*while (true) {
        mensagem = "10";
        if (mensagem != null && usuario.getId() != 0) {
          requestResponseData.setCommand(MESSAGE);
          requestResponseData.setIdDestino(2);
          requestResponseData.setObject(mensagem);
          enviarDados(requestResponseData);
          requestResponseDataResend = new RequestResponseData(requestResponseData);
        }
      }*/
    } catch (UnknownHostException e1) {
      e1.printStackTrace();
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

package Communication;

import Application.Usuario;
import java.io.Serializable;

public class RequestResponseData implements Serializable, ICommands {

  private Object obj[];
  private int command;
  private int idDestino;
  private int idOwner;

  public RequestResponseData() {
  }
  
  public RequestResponseData(RequestResponseData requestResponseData) {
	  this.obj = requestResponseData.getObject();
	  this.command = requestResponseData.getCommand();
	  this.idDestino = requestResponseData.getIdDestino();	  
  }

  public RequestResponseData(int code, Usuario usuario) {
    this.obj[0] = usuario;
    this.command = code;
  }

  public Object[] getObject() {
    return obj;
  }

  public void setObject(Usuario user, Object contacts[], Object mensage[]) {
  	this.obj = new Object [] { user, contacts, mensage }; 
  }

  public int getCommand() {
    return command;
  }

  public void setCommand(int command) {
    this.command = command;
  }

  public int getIdDestino() {
    return idDestino;
  }

  public void setIdDestino(int idDestino) {
    this.idDestino = idDestino;
  }
  
  public int getIdOwner() {
    return idOwner;
  }

  public void setIdOwner(int idOwner) {
    this.idOwner = idOwner;
  }

}
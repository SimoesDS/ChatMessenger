package Misc;

import Communication.ICommands;

import java.io.Serializable;
import java.util.ArrayList;

public class RequestResponseData implements Serializable, ICommands {

	private Object obj[];
  private int command;
  private int idDestino;
  private int idOwner;
  private Usuario owner;
  private ArrayList<Usuario> contacts;
  private ArrayList<Message> messages; 

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

  public void setObject(Usuario user, Object contacts[], Object message[], ArrayList<Usuario> _contacts, ArrayList<Message> _messages) {
  	this.obj = new Object [] { user, contacts, message, _contacts, _messages }; 
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

	public ArrayList<Usuario> getContacts() {
		return contacts;
	}

	public void setContacts(ArrayList<Usuario> contacts) {
		this.contacts = contacts;
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}

	public void clearObject() {
		this.obj = null;
	}

	public Usuario getOwner() {
		return owner;
	}

	public void setOwner(Usuario owner) {
		this.owner = owner;
	}
}
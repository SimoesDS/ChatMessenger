package Misc;

import Communication.ICommands;

import java.io.Serializable;
import java.util.ArrayList;

public class RequestResponseData implements Serializable, ICommands {

	private Object obj[];
	private int command;
	private int idSender;
	private int idReceiver;
	private Usuario user;
	private ArrayList<Usuario> contacts;
	private ArrayList<Message> messages;
	private String msg;

	public RequestResponseData() {
	}

	public Object[] getObject() {
		return obj;
	}

	public void setObject(Usuario user, Object contacts[], Object message[], ArrayList<Usuario> _contacts,
			ArrayList<Message> _messages) {
		this.obj = new Object[] { user, contacts, message, _contacts, _messages };
	}

	public void setObject(Usuario user, Object contacts[], Object mensage[]) {
		this.obj = new Object[] { user, contacts, mensage };
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public int getIdDestino() {
		return idReceiver;
	}

	public void setIdDestino(int idDestino) {
		this.idReceiver = idDestino;
	}

	public int getIdOwner() {
		return idSender;
	}

	public void setIdOwner(int idOwner) {
		this.idSender = idOwner;
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
		return user;
	}

	public void setOwner(Usuario owner) {
		this.user = owner;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
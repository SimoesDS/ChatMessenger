package Misc;

import Communication.ICommands;

import java.io.Serializable;
import java.util.ArrayList;

public class RequestResponseData implements Serializable, ICommands {

	private Object genericObject[];
	private int command;

	public RequestResponseData(Usuario user, int cmd) {
		this.genericObject = new Object[] { user, null, null };
		this.command = cmd;
	}
	
	public RequestResponseData(Message msg, int cmd) {
		ArrayList<Message> messages = new ArrayList<Message>();
		messages.add(msg);
		this.genericObject = new Object[] { null, null, messages };
		this.command = cmd;
	}
	
	public RequestResponseData(ArrayList<Object> obj, int cmd) {
		if(obj.size() > 0) {
			if(obj.get(0) instanceof Usuario)
				this.genericObject = new Object[] { null, obj, null };
			else if(obj.get(0) instanceof Message)
				this.genericObject = new Object[] { null, null, obj };
		}
		this.command = cmd;
	}
	
	public RequestResponseData(Usuario user, ArrayList<Usuario> contacts, ArrayList<Message> mensage) {
		this.genericObject = new Object[] { user, contacts, mensage };
	}
	
	public RequestResponseData(int cmd) {
		this.command = cmd;
	}

	public void setCommand(int command) {
		switch (command) {
		case UNREGISTERED:
			genericObject = null;
			break;
		}
		this.command = command;
	}
	
	public int getCommand() {
		return command;
	}
	
	public int getIdReceiver() {
		if(genericObject != null && genericObject.length == 1 && genericObject[0] instanceof Message)
			return ((Message) genericObject[0]).getIdReceiver();
		return -1;
	}

	public int getIdSender() {
		if(genericObject != null && genericObject.length == 1 && genericObject[0] instanceof Message)
			return ((Message) genericObject[0]).getIdSender();
		return -1;
	}

	public ArrayList<Usuario> getAllContacts() {
		ArrayList<Usuario> contacts = new ArrayList<Usuario>();
		for (Object object : genericObject)
			if(object != null && object instanceof Usuario)
				contacts.add((Usuario) object);
		
		return contacts;
	}

	public ArrayList<Message> getAllMessages() {
		ArrayList<Message> messages = new ArrayList<Message>();
		for (Object object : genericObject)
			if(object != null && object instanceof Message)
				messages.add((Message) object);
		
		return messages;
	}

	public Usuario getUser() {
		if(genericObject != null && genericObject[0] instanceof Usuario)
			return (Usuario) genericObject[0];
		return null;
	}

	public String getMessage() {
		if(genericObject != null && genericObject.length == 1 && genericObject[0] instanceof Message)
			return ((Message) genericObject[0]).getMessage();
		
		return "";
	}
	
	public void clearObject() {
		this.genericObject = null;
	}
}
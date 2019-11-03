package Communication;

import java.io.Serializable;
import java.util.ArrayList;

import Misc.Message;
import Misc.Usuario;

public class RequestResponseData implements Serializable, ICommands {

	private static final long serialVersionUID = 6925685202055492579L;
	
	private Object genericObject;
	private int command;

	public RequestResponseData(Usuario user, int cmd) {
		this.genericObject = user;
		this.command = cmd;
	}

	public RequestResponseData(Message msg, int cmd) {
		this.genericObject = msg;
		this.command = cmd;
	}

	public RequestResponseData(Usuario user, ArrayList<Usuario> contacts, ArrayList<Message> mensage) {
		this.genericObject = new Object[] { user, contacts, mensage };
	}

	public RequestResponseData(int cmd) {
		this.command = cmd;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public int getCommand() {
		return command;
	}

	public int getIdReceiver() {
		if (command == MESSAGE && getMessage() != null)
			return getMessage().getIdReceiver();
		return -1;
	}

	public int getIdSender() {
		if (command == MESSAGE && getMessage() != null)
			return getMessage().getIdSender();
		return -1;
	}

	public ArrayList<Usuario> getAllContacts() {
		ArrayList<Usuario> contacts = new ArrayList<Usuario>();
		ArrayList listFromGenericObj = getArrayListFromGenericObject();
		for (Object obj : listFromGenericObj) {
			if (obj instanceof ArrayList) {
				ArrayList elementList = (ArrayList) obj;
				if (elementList.size() > 0 && elementList.get(0) instanceof Usuario)
					for (int i = 0; i < elementList.size(); i++)
						contacts.add((Usuario) elementList.get(i));
			}
		}
		return contacts;
	}

	public ArrayList<Message> getAllMessages() {
		ArrayList<Message> messages = new ArrayList<Message>();
		ArrayList listFromGenericObj = getArrayListFromGenericObject();
		for (Object obj : listFromGenericObj) {
			if (obj instanceof ArrayList) {
				ArrayList elementList = (ArrayList) obj;
				if (elementList.size() > 0 && elementList.get(0) instanceof Message)
					for (int i = 0; i < elementList.size(); i++)
						messages.add((Message) elementList.get(i));
			}
		}
		return messages;
	}

	public Usuario getUser() {
		if (getArrayFromGenericObject() != null) {
			Object[] obj = getArrayFromGenericObject();
			if (obj != null && obj[0] instanceof Usuario)
				return (Usuario) obj[0];
		} else if (genericObject != null && genericObject instanceof Usuario)
			return (Usuario) genericObject;
		return null;
	}

	public Message getMessage() {
		if (genericObject != null && genericObject instanceof Message)
			return (Message) genericObject;
		return null;
	}

	public String getMsg() {
		Message msg = getMessage();
		return msg != null ? msg.getMessage() : "";
	}

	private Object[] getArrayFromGenericObject() {
		if (genericObject != null && genericObject instanceof Object[])
			return (Object[]) genericObject;
		return null;
	}

	private ArrayList getArrayListFromGenericObject() {
		ArrayList listResult = new ArrayList<>();
		if (getArrayFromGenericObject() != null) {
			for (Object object : getArrayFromGenericObject())
				if (object instanceof ArrayList) {
					listResult.add(object);
				}
		}
		return listResult;
	}
}
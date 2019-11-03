package Misc;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable{

	private static final long serialVersionUID = 3800411480096432009L;
	private int _idSender = -1;
	private int _idReceiver = -1;
	private String _message = "";
	private Date _date;
	
	public Message(int idSender, int idReceiver, String message, Date date) {
		this._idSender = idSender;
		this._idReceiver = idReceiver;
		this._message = message;
		this._date = date;
	}
	
	public Message(int idSender, int idReceiver, String message) {
		this._idSender = idSender;
		this._idReceiver = idReceiver;
		this._message = message;
		this._date = new Date();
	}
	
	public int getIdReceiver() {
		return _idReceiver;
	}

	public int getIdSender() {
		return _idSender;
	}
	
	public String getMessage() {
		return _message;
	}

	public Date getDate() {
		return _date;
	}
}
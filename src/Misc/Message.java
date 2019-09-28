package Misc;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable{
	private int _idSender = -1;
	private int _idReceiver = -1;
	private String _message;
	private Date _date;
	
	public Message(int idSender, int idReceiver, String message, Date date) {
		this._idSender = idSender;
		this._idReceiver = idReceiver;
		this._message = message;
		this._date = date;
	}
	
	public int getIdReceiver() {
		return _idReceiver;
	}
	
	public void setIdReceiver(int idReceiver) {
		this._idReceiver = idReceiver;
	}
	
	public String getMessage() {
		return _message;
	}
	
	public void setMessage(String message) {
		this._message = message;
	}

	public Date getDate() {
		return _date;
	}

	public void setDate(Date _date) {
		this._date = _date;
	}

	public int getIdSender() {
		return _idSender;
	}

	public void setIdSender(int _idSender) {
		this._idSender = _idSender;
	}
}

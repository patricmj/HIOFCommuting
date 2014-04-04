package bachelor.user;

import java.util.Date;

public class Message {

	private int sender_id;
	private int receiver_id;
	private String message;
	private int sent;
	private int read;
	
	
	
	public Message(int sender_id, int receiver_id, String message, int sent,
			int read) {
		this.sender_id = sender_id;
		this.receiver_id = receiver_id;
		this.message = message;
		this.sent = sent;
		this.read = read;
	}
	
	public int getSender_id() {
		return sender_id;
	}
	public void setSender_id(int sender_id) {
		this.sender_id = sender_id;
	}
	public int getReceiver_id() {
		return receiver_id;
	}
	public void setReceiver_id(int receiver_id) {
		this.receiver_id = receiver_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getSent() {
		return sent;
	}
	public void setSent(int sent) {
		this.sent = sent;
	}
	public int getRead() {
		return read;
	}
	public void setRead(int read) {
		this.read = read;
	}
	
	
	
}

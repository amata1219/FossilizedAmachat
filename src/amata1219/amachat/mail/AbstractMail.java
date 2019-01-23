package amata1219.amachat.mail;

import java.util.UUID;

public abstract class AbstractMail {

	protected long timestamp;
	protected UUID sender, receiver;
	protected String text;

	public abstract void save(boolean apply);

	public abstract void remove(boolean apply);

	public abstract void trySend();

	public long getTimestamp(){
		return timestamp;
	}

	public UUID getSender() {
		return sender;
	}

	public void setSender(UUID sender) {
		this.sender = sender;
	}

	public UUID getReceiver() {
		return receiver;
	}

	public void setReceiver(UUID receiver) {
		this.receiver = receiver;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}

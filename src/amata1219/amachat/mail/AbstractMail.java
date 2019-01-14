package amata1219.amachat.mail;

import java.util.UUID;

import amata1219.amachat.config.Config;
import net.md_5.bungee.config.Configuration;

public abstract class AbstractMail {

	protected long timestamp;
	protected UUID sender, receiver;
	protected String text;

	public void save(boolean apply){
		Config config = MailManager.getDatabase();
		Configuration configuration = config.getConfiguration();
		String section = String.valueOf(timestamp) + ".";
		configuration.set(section + "Sender", sender.toString());
		configuration.set(section + "Receiver", receiver.toString());
		configuration.set(section + "Text", text);

		if(apply)
			config.apply();
	}

	public void remove(boolean apply){
		Config config = MailManager.getDatabase();
		config.getConfiguration().set(String.valueOf(timestamp), null);

		if(apply)
			config.apply();
	}

	public abstract boolean trySend();

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

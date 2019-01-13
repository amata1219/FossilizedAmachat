package amata1219.amachat.mail;

import java.util.UUID;

import amata1219.amachat.chat.ChatManager;
import amata1219.amachat.user.User;
import net.md_5.bungee.config.Configuration;

public class OldMail {

	private UUID receiver;
	private String text;
	private long timestamp;

	/*
	 * timestampValue:
	 *   Receiver: receiver.toString()
	 *   Text: text
	 */

	private OldMail(){

	}

	public static OldMail send(User sender, UUID receiver, String text){
		OldMail mail = new OldMail();
		mail.receiver = receiver;
		mail.text = MailManager.process(sender, mail);
		mail.timestamp = System.currentTimeMillis();
		return mail;
	}

	public static OldMail load(Configuration config, String key){
		OldMail mail = new OldMail();
		mail.receiver = UUID.fromString(config.getString(key + ".Receiver"));
		mail.text = config.getString(key + ".Text");
		mail.timestamp = Long.valueOf(key).longValue();
		return mail;
	}

	public void send(){
		ChatManager.sendMessage("", receiver);
		ChatManager.sendMessage(text, receiver);
	}

	public UUID getReceiver(){
		return receiver;
	}

	public String getText(){
		return text;
	}

	public void setText(String text){
		this.text = text;
	}

	public long getTimestamp(){
		return timestamp;
	}

}

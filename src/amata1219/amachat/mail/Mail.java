package amata1219.amachat.mail;

import java.util.UUID;

import amata1219.amachat.chat.ChatManager;
import amata1219.amachat.player.Player;
import net.md_5.bungee.config.Configuration;

public class Mail {

	private UUID receiver;
	private String text;
	private long timestamp;

	private Mail(){

	}

	public static Mail send(Player sender, UUID receiver, String text){
		Mail mail = new Mail();
		mail.receiver = receiver;
		mail.text = MailManager.process(sender, mail);
		mail.timestamp = System.currentTimeMillis();
		return mail;
	}

	public static Mail load(Configuration config, String key){
		Mail mail = new Mail();
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

package amata1219.amachat.mail;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.md_5.bungee.config.Configuration;

public class Mail extends AbstractMail {

	private Mail(long timestamp){
		this.timestamp = timestamp;
	}

	public static Mail write(UUID sender, UUID receiver, String text){
		Mail mail = new Mail(System.currentTimeMillis());
		mail.sender = sender;
		mail.receiver = receiver;
		mail.text = text;
		return mail;
	}

	public static Set<Mail> load(Configuration section){
		Set<Mail> set = new HashSet<Mail>();
		section.getKeys().forEach(key -> {
			Mail mail = new Mail(Long.valueOf(key).longValue());
			mail.sender = UUID.fromString(section.getString(key + ".Sender"));
			mail.receiver = UUID.fromString(section.getString(key + ".Receiver"));
			mail.text = section.getString(key + ".Text");
		});
		return set;
	}

	@Override
	public void send() {
	}

}

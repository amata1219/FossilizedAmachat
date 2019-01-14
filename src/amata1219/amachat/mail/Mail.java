package amata1219.amachat.mail;

import java.util.UUID;

import amata1219.amachat.user.User;
import amata1219.amachat.user.UserManager;
import net.md_5.bungee.config.Configuration;

public class Mail extends AbstractMail {

	private static boolean load;

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

	public static boolean isLoaded(){
		return load;
	}

	public static void load(Configuration section){
		if(load)
			return;

		section.getKeys().forEach(key -> {
			Mail mail = new Mail(Long.valueOf(key).longValue());
			mail.sender = UUID.fromString(section.getString(key + ".Sender"));
			mail.receiver = UUID.fromString(section.getString(key + ".Receiver"));
			mail.text = section.getString(key + ".Text");
			MailManager.addMail(mail);
		});

		load = true;
	}

	@Override
	public boolean trySend() {
		User user = UserManager.getUser(receiver);
		if(user == null)
			return false;

		user.sendMessage(MailManager.process(this));
		return true;
	}

}

package amata1219.amachat.mail;

import java.io.File;
import java.util.UUID;

import amata1219.amachat.Amachat;
import amata1219.amachat.config.Config;
import amata1219.amachat.user.User;
import amata1219.amachat.user.UserManager;
import net.md_5.bungee.config.Configuration;

public class Mail extends AbstractMail {

	private static Config config;

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

	public static void load(){
		config = Config.load(new File(Amachat.getPlugin().getDataFolder(), "mails.yml"), "mails.yml");
		Configuration configuration = config.getConfiguration();
		for(String key : configuration.getKeys()){
			Mail mail = new Mail(Long.valueOf(key).longValue());
			mail.sender = UUID.fromString(configuration.getString(key + ".Sender"));
			mail.receiver = UUID.fromString(configuration.getString(key + ".Receiver"));
			mail.text = configuration.getString(key + ".Text");
			MailManager.addMail(mail);
		}
	}

	public static Config getDatabase(){
		return config;
	}

	@Override
	public void save(boolean apply){
		Config config = Mail.config;
		Configuration configuration = config.getConfiguration();
		String section = String.valueOf(timestamp) + ".";
		configuration.set(section + "Sender", sender.toString());
		configuration.set(section + "Receiver", receiver.toString());
		configuration.set(section + "Text", text);

		if(apply)
			config.apply();
	}

	@Override
	public void remove(boolean apply){
		Config config = Mail.config;
		config.getConfiguration().set(String.valueOf(timestamp), null);

		if(apply)
			config.apply();
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

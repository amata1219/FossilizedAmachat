package amata1219.amachat.mail;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import amata1219.amachat.Amachat;
import amata1219.amachat.config.Config;
import amata1219.amachat.processor.Coloring;
import amata1219.amachat.processor.MessageFormatType;
import amata1219.amachat.processor.Processor;
import amata1219.amachat.processor.ProcessorManager;
import amata1219.amachat.user.UserManager;
import net.md_5.bungee.config.Configuration;

public class MailManager {

	private static Config config;
	private static final HashMap<MessageFormatType, String> formats = new HashMap<>();
	private static final Set<String> processorNames = new HashSet<>();
	private static final Set<AbstractMail> mails = new HashSet<>();

	private MailManager(){

	}

	public static void load(){
		if(!Amachat.getConfig().getConfiguration().getBoolean("Mail.Enable"))
			return;

		config = Config.load(new File(Amachat.getPlugin().getDataFolder(), "mails.yml"), "mails.yml");

		Config config = Amachat.getConfig();
		processorNames.addAll(config.getStringSet("Mail.Processors"));
		Processor coloring = ProcessorManager.get(Coloring.NAME);
		Configuration configuration = config.getConfiguration().getSection("Mail");
		formats.put(MessageFormatType.NORMAL, coloring.process(configuration.getString("Format.Normal")));
		formats.put(MessageFormatType.JAPANIZED, coloring.process(configuration.getString("Format.Japanized")));
		formats.put(MessageFormatType.TRANSLATION, coloring.process(configuration.getString("Format.Translation")));
	}

	public static boolean isEnable(){
		return Amachat.getConfig().getConfiguration().getBoolean("Mail.Enable");
	}

	public static Config getDatabase(){
		return config;
	}

	public static long getExpirationDays(){
		return Amachat.getConfig().getConfiguration().getLong("Mail.ExpirationDays");
	}

	public static boolean isExpired(Mail mail){
		return System.currentTimeMillis() - mail.getTimestamp() >= getExpirationDays() * 86400000;
	}

	public static String process(Mail mail){
		return ProcessorManager.process(UserManager.getPlayerName(mail.getSender()), mail.getText(), formats, processorNames);
	}

	public static Set<AbstractMail> getMails(){
		return mails;
	}

	public static Set<AbstractMail> getMails(UUID receiver){
		return mails.stream().filter(mail -> mail.getReceiver().equals(receiver)).collect(Collectors.toSet());
	}

	public static Set<AbstractMail> getMails(Class<?> clazz){
		return mails.stream().filter(clazz::isInstance).collect(Collectors.toSet());
	}

	public static Set<AbstractMail> getMails(UUID receiver, Class<?> clazz){
		return mails.stream().filter(mail -> mail.getReceiver().equals(receiver)).filter(clazz::isInstance).collect(Collectors.toSet());
	}

	public void displayMails(UUID receiver){
		getMails(receiver).forEach(AbstractMail::trySend);
	}

	public static void addMail(Mail mail){
		mails.add(mail);
		mail.save(true);
	}

	public void removeMail(Mail mail){
		mails.remove(mail);
		mail.remove(true);
	}

	public void clearMails(UUID receiver){
		Config config = Amachat.getConfig();

		getMails(receiver).forEach(mail -> {
			mails.remove(mail);
			mail.remove(false);
		});

		config.apply();
	}

}

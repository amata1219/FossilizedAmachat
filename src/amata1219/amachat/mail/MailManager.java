package amata1219.amachat.mail;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import amata1219.amachat.Amachat;
import amata1219.amachat.chat.Chat.MessageFormatType;
import amata1219.amachat.config.Config;
import amata1219.amachat.processor.Coloring;
import amata1219.amachat.processor.Processor;
import amata1219.amachat.processor.ProcessorManager;
import amata1219.amachat.user.UserManager;
import net.md_5.bungee.config.Configuration;

public class MailManager {

	private static Config config;
	private static final Map<MessageFormatType, String> FORMATS = new HashMap<>();
	private static final Set<Processor> PROCESSORS = new HashSet<>();
	private static final Set<AbstractMail> MAILS = new HashSet<>();

	private MailManager(){

	}

	public static void load(){
		if(!Amachat.getConfig().getConfiguration().getBoolean("Mail.Enable"))
			return;

		config = Config.load(new File(Amachat.getPlugin().getDataFolder(), "mails.yml"), "mails.yml");

		Configuration configuration = Amachat.getConfig().getConfiguration().getSection("Mail");
		PROCESSORS.addAll(ProcessorManager.fromProcessorNames(configuration.getStringList("Processors")));
		FORMATS.put(MessageFormatType.NORMAL, Coloring.coloring(configuration.getString("Format.Normal")));
		FORMATS.put(MessageFormatType.JAPANIZE, Coloring.coloring(configuration.getString("Format.Japanized")));
		FORMATS.put(MessageFormatType.TRANSLATE, Coloring.coloring(configuration.getString("Format.Translation")));
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
		return ProcessorManager.processAll(UserManager.getPlayerName(mail.getSender()), mail.getText(), FORMATS, PROCESSORS);
	}

	public static Set<AbstractMail> getMails(){
		return MAILS;
	}

	public static Set<AbstractMail> getMails(UUID receiver){
		return MAILS.stream().filter(mail -> mail.getReceiver().equals(receiver)).collect(Collectors.toSet());
	}

	public static Set<AbstractMail> getMails(Class<?> clazz){
		return MAILS.stream().filter(clazz::isInstance).collect(Collectors.toSet());
	}

	public static Set<AbstractMail> getMails(UUID receiver, Class<?> clazz){
		return MAILS.stream().filter(mail -> mail.getReceiver().equals(receiver)).filter(clazz::isInstance).collect(Collectors.toSet());
	}

	public void displayMails(UUID receiver){
		getMails(receiver).forEach(AbstractMail::trySend);
	}

	public static void addMail(Mail mail){
		MAILS.add(mail);
		mail.save(true);
	}

	public void removeMail(Mail mail){
		MAILS.remove(mail);
		mail.remove(true);
	}

	public void clearMails(UUID receiver){
		Config config = Amachat.getConfig();

		getMails(receiver).forEach(mail -> {
			MAILS.remove(mail);
			mail.remove(false);
		});

		config.apply();
	}

}

package amata1219.amachat.mail;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import amata1219.amachat.Amachat;
import amata1219.amachat.config.Config;
import amata1219.amachat.processor.Coloring;
import amata1219.amachat.processor.MessageFormatType;
import amata1219.amachat.processor.Processor;
import amata1219.amachat.processor.ProcessorManager;
import amata1219.amachat.processor.SupportTextProcessing;
import amata1219.amachat.user.UserManager;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;

public class MailManager implements SupportTextProcessing {

	private static MailManager instance;

	private static Config config;
	private static String format;
	private static final Map<MessageFormatType, String> MESSAGE_FORMATS = new HashMap<>();
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
		MESSAGE_FORMATS.put(MessageFormatType.NORMAL, Coloring.coloring(configuration.getString("Format.Normal")));
		MESSAGE_FORMATS.put(MessageFormatType.JAPANIZE, Coloring.coloring(configuration.getString("Format.Japanized")));
		MESSAGE_FORMATS.put(MessageFormatType.TRANSLATE, Coloring.coloring(configuration.getString("Format.Translation")));

		instance = new MailManager();
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

	public static TextComponent process(Mail mail){
		return ProcessorManager.processAll(instance, UserManager.getPlayerName(mail.getSender()), mail.getText());
	}

	public static Set<AbstractMail> getMails(){
		return MAILS;
	}

	public static Set<AbstractMail> getMails(UUID receiver){
		Set<AbstractMail> mails = new HashSet<>();
		for(AbstractMail mail : MAILS){
			if(mail.getReceiver().equals(receiver))
				mails.add(mail);
		}
		return mails;
	}

	public static Set<AbstractMail> getMails(Class<?> clazz){
		Set<AbstractMail> mails = new HashSet<>();
		for(AbstractMail mail : MAILS){
			if(clazz.isInstance(mail))
				mails.add(mail);
		}
		return mails;
	}

	public static Set<AbstractMail> getMails(UUID receiver, Class<?> clazz){
		Set<AbstractMail> mails = new HashSet<>();
		for(AbstractMail mail : MAILS){
			if(mail.getReceiver().equals(receiver) && clazz.isInstance(mail))
				mails.add(mail);
		}
		return mails;
	}

	public void displayMails(UUID receiver){
		for(AbstractMail mail : getMails(receiver))
			mail.trySend();
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

		for(AbstractMail mail : getMails(receiver)){
			MAILS.remove(mail);
			mail.remove(false);
		}

		config.apply();
	}

	@Override
	public String getFormat(){
		return format;
	}

	@Override
	public void setFormat(String format){
		MailManager.format = format;
	}

	@Override
	public Map<MessageFormatType, String> getMessageFormats(){
		return MESSAGE_FORMATS;
	}

	@Override
	public String getMessageFormat(MessageFormatType type){
		return MESSAGE_FORMATS.get(type);
	}

	@Override
	public void setMessageFormat(MessageFormatType type, String format){
		MESSAGE_FORMATS.put(type, format);
	}
	@Override

	public Set<Processor> getProcessors(){
		return PROCESSORS;
	}

	@Override
	public boolean hasProcessor(String processorName){
		return PROCESSORS.contains(ProcessorManager.getProcessor(processorName));
	}

	@Override
	public void addProcessor(Processor processor){
		PROCESSORS.add(processor);
	}

	@Override
	public void removeProcessor(String processorName){
		PROCESSORS.remove(ProcessorManager.getProcessor(processorName));
	}

}

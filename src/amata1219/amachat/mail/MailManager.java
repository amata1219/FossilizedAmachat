package amata1219.amachat.mail;

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

	private String format;
	private final Map<MessageFormatType, String> messageFormats = new HashMap<>();
	private final Set<Processor> processors = new HashSet<>();
	private final Set<AbstractMail> mails = new HashSet<>();

	private MailManager(){

	}

	public static void load(){
		if(!Amachat.getConfig().getConfiguration().getBoolean("Mail.Enable"))
			return;

		MailManager manager = new MailManager();

		Configuration configuration = Amachat.getConfig().getConfiguration().getSection("Mail");
		Map<MessageFormatType, String> messageFormats = manager.messageFormats;
		messageFormats.put(MessageFormatType.NORMAL, Coloring.coloring(configuration.getString("Format.Normal")));
		messageFormats.put(MessageFormatType.JAPANIZE, Coloring.coloring(configuration.getString("Format.Japanized")));
		messageFormats.put(MessageFormatType.TRANSLATE, Coloring.coloring(configuration.getString("Format.Translation")));
		manager.processors.addAll(ProcessorManager.fromProcessorNames(configuration.getStringList("Processors")));

		instance = manager;
	}

	public static MailManager getInstance(){
		return instance;
	}

	public static boolean isEnable(){
		return Amachat.getConfig().getConfiguration().getBoolean("Mail.Enable");
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
		return instance.mails;
	}

	public static Set<AbstractMail> getMails(UUID receiver){
		Set<AbstractMail> mails = new HashSet<>();
		for(AbstractMail mail : getMails()){
			if(mail.getReceiver().equals(receiver))
				mails.add(mail);
		}
		return mails;
	}

	public static Set<AbstractMail> getMails(Class<?> clazz){
		Set<AbstractMail> mails = new HashSet<>();
		for(AbstractMail mail : getMails()){
			if(clazz.isInstance(mail))
				mails.add(mail);
		}
		return mails;
	}

	public static Set<AbstractMail> getMails(UUID receiver, Class<?> clazz){
		Set<AbstractMail> mails = new HashSet<>();
		for(AbstractMail mail : getMails()){
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
		getMails().add(mail);
		mail.save(true);
	}

	public void removeMail(Mail mail){
		mails.remove(mail);
		mail.remove(true);
	}

	public void clearMails(UUID receiver){
		Config config = Amachat.getConfig();

		for(AbstractMail mail : getMails(receiver)){
			mails.remove(mail);
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
		this.format = format;
	}

	@Override
	public Map<MessageFormatType, String> getMessageFormats(){
		return messageFormats;
	}

	@Override
	public String getMessageFormat(MessageFormatType type){
		return messageFormats.get(type);
	}

	@Override
	public void setMessageFormat(MessageFormatType type, String format){
		messageFormats.put(type, format);
	}
	@Override

	public Set<Processor> getProcessors(){
		return processors;
	}

	@Override
	public boolean hasProcessor(String processorName){
		return processors.contains(ProcessorManager.getProcessor(processorName));
	}

	@Override
	public void addProcessor(Processor processor){
		processors.add(processor);
	}

	@Override
	public void removeProcessor(String processorName){
		processors.remove(ProcessorManager.getProcessor(processorName));
	}

}

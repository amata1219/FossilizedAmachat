package amata1219.amachat.mail;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import amata1219.amachat.Amachat;
import amata1219.amachat.chat.ChatManager;
import amata1219.amachat.config.Config;
import amata1219.amachat.processor.Coloring;
import amata1219.amachat.processor.FormatType;
import amata1219.amachat.processor.Processor;
import amata1219.amachat.processor.ProcessorManager;
import amata1219.amachat.user.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;

public class MailManager {

	private static final MailManager manager = new MailManager();
	private Config config;
	private HashMap<FormatType, String> formats = new HashMap<>();
	private Set<String> processors;
	private Set<Mail> mails;

	private MailManager(){

	}

	public static void load(){
		Config config = Amachat.getConfig();
		manager.processors = config.getStringSet("Mail.Processors");
		Map<FormatType, String> formats = manager.formats;
		Processor coloring = ProcessorManager.get(Coloring.NAME);
		Configuration conf = config.getConfiguration().getSection("Mail");
		formats.put(FormatType.NORMAL, coloring.process(conf.getString("Format.Normal")));
		formats.put(FormatType.JAPANIZED, coloring.process(conf.getString("Format.Japanized")));
		formats.put(FormatType.TRANSLATION, coloring.process(conf.getString("Format.Translation")));

		long days = conf.getLong("ExpirationDays") * 86400000;

		manager.mails = Mail.load((manager.config = Config.load(new File(Amachat.getPlugin().getDataFolder() + File.separator + "mails.yml"), "mails.yml")).getConfiguration());
	}

	public static MailManager getInstance(){
		return manager;
	}

	public Config getConfig(){
		return config;
	}

	public static boolean isEnable(){
		return Amachat.getPlugin().getConfig().getConfiguration().getBoolean("Mail.Enable");
	}

	public static String process(User player, Mail mail){
		return ProcessorManager.processAll(player, mail.getText(), manager.formats, manager.processors);
	}

	public Set<Mail> getMails(){
		return mails;
	}

	public Set<Mail> getMails(UUID receiver){
		return mails.stream().filter(mail -> mail.getReceiver().equals(receiver)).collect(Collectors.toSet());
	}

	public void displayMails(UUID receiver){
		Set<Mail> set = new HashSet<>();
		AtomicInteger i = new AtomicInteger();
		mails.stream().filter(mail -> mail.getReceiver().equals(receiver)).forEach(mail -> {
			i.incrementAndGet();
			set.add(mail);
		});
		ChatManager.sendMessage(ChatColor.AQUA + String.valueOf(i.get()) + "件のメールを受信しました。", receiver);
		set.forEach(mail -> mail.send());
	}

	public void addMail(Mail mail){
		mails.add(mail);
		Configuration conf = config.getConfiguration();
		conf.set(mail.getTimestamp() + ".Receiver", mail.getReceiver().toString());
		conf.set(mail.getTimestamp() + ".Text", mail.getText());
		config.apply();
	}

	public void removeMail(Mail mail){
		mails.remove(mail);
		config.getConfiguration().set(String.valueOf(mail.getTimestamp()), null);
		config.apply();
	}

	public void clearMails(UUID receiver){
		getMails(receiver).forEach(mail -> {
			mails.remove(mail);
			config.getConfiguration().set(String.valueOf(mail.getTimestamp()), null);
		});
		config.apply();
	}

}

package amata1219.amachat.mail;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import amata1219.amachat.Amachat;
import amata1219.amachat.Config;
import amata1219.amachat.player.Player;
import amata1219.amachat.processor.Coloring;
import amata1219.amachat.processor.FormatType;
import amata1219.amachat.processor.Processor;
import amata1219.amachat.processor.ProcessorManager;
import net.md_5.bungee.config.Configuration;

public class MailManager {

	private static MailManager instance;
	private Config config;
	private HashMap<FormatType, String> formats = new HashMap<>();
	private Set<String> processors;
	private Set<Mail> mails = new HashSet<>();

	private MailManager(){

	}

	public static void load(){
		if(isEnable())
			return;

		MailManager manager = new MailManager();

		instance = manager;

		Config config = Amachat.getPlugin().getConfig();
		manager.processors = config.getStringSet("Mail.Processors");
		Map<FormatType, String> formats = manager.formats;
		Processor coloring = ProcessorManager.get(Coloring.NAME);
		Configuration conf = config.getConfig().getSection("Mail");
		formats.put(FormatType.NORMAL, coloring.process(conf.getString("Format.Normal")));
		formats.put(FormatType.JAPANIZED, coloring.process(conf.getString("Format.Japanized")));
		formats.put(FormatType.TRANSLATION, coloring.process(conf.getString("Format.Translation")));

		manager.config = Config.load(new File(Amachat.getPlugin().getDataFolder() + File.separator + "mails.yml"), "mails.yml");
		Configuration mails = manager.config.getConfig();
		mails.getKeys().forEach(key -> Mail.load(mails, key));
	}

	public static MailManager getInstance(){
		return instance;
	}

	public Config getConfig(){
		return config;
	}

	public static boolean isEnable(){
		return Amachat.getPlugin().getConfig().getConfig().getBoolean("Mail.Enable");
	}

	public static String process(Player player, Mail mail){
		return ProcessorManager.processAll(player, mail.getText(), instance.formats, instance.processors);
	}

	public void displayMails(UUID receiver){
		for(Mail mail : mails){
			if(mail.getReceiver().equals(receiver))
				mail.send();
		}
	}

	public void addMail(Mail mail){
		mails.add(mail);
		Configuration conf = config.getConfig();
		conf.set(mail.getTimestamp() + ".Receiver", mail.getReceiver().toString());
		conf.set(mail.getTimestamp() + ".Text", mail.getText());
		config.apply();
	}

	public void removeMails(UUID receiver){
		boolean b = false;
		for(Mail mail : mails){
			if(mail.getReceiver().equals(receiver)){
				mails.remove(mail);
				config.getConfig().set(String.valueOf(mail.getTimestamp()), null);
				b = true;
			}
		}
		if(b)
			config.apply();
	}

}

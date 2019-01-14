package amata1219.amachat.bot;

import java.io.File;
import java.util.Collections;
import java.util.List;
import amata1219.amachat.chat.ChatManager;
import amata1219.amachat.config.Config;
import amata1219.amachat.config.Initializer;
import net.md_5.bungee.config.Configuration;

public class AutoMessageBot extends TaskBot {

	public static final String NAME = "AutoMessageBot";
	public static final File DIRECTORY = new File(Bot.DIRECTORY + File.separator + "AutoMessageBot");

	private List<String> messages;
	private long interval;
	private int count;

	protected AutoMessageBot(final long id){
		this.id = id;
	}

	public static AutoMessageBot load(long id){
		AutoMessageBot bot = new AutoMessageBot(id);

		(bot.config = Config.load(new File(DIRECTORY, String.valueOf(id) + ".yml"), "bot.yml", new Initializer(){

			@Override
			public void initialize(Config config) {
				Configuration configuration = config.getConfiguration();
				configuration.set("ID", id);
				configuration.set("ChatList", Collections.emptySet());
				configuration.set("Messages", Collections.emptySet());
				configuration.set("Interval", 60);
				config.apply();
			}

		})).reload();

		return bot;
	}

	@Override
	public String getName() {
		return AutoMessageBot.NAME;
	}

	@Override
	public void save(){
		if(config == null)
			return;

		Configuration configuration = config.getConfiguration();
		configuration.set("ChatList", chatIds);
		configuration.set("Messages", messages);
		configuration.set("Interval", interval);

		config.apply();
	}

	@Override
	public void reload(){
		if(config == null)
			return;

		config.reload();

		Configuration configuration = config.getConfiguration();

		chatIds = config.getLongSet("ChatList");
		messages = configuration.getStringList("Messages");
		interval = configuration.getLong("Interval");
	}

	public List<String> getMessages(){
		return messages;
	}

	public void addMessage(String message){
		messages.add(message);
	}

	public void insertMessage(int index, String message){
		messages.set(index, message);
	}

	public void removeMessage(String message){
		messages.remove(message);
	}

	public int getInterval(){
		return config.getConfiguration().getInt("Interval");
	}

	public void setInterval(int interval){
		config.getConfiguration().set("Interval", interval);
		start();
	}

	@Override
	public void run() {
		if(pause || messages.isEmpty())
			return;

		ChatManager.getChatSet(chatIds).forEach(chat -> chat.broadcast(messages.get(messages.size() > count ? count++ : (count = 0))));
	}

}

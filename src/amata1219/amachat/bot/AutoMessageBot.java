package amata1219.amachat.bot;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import amata1219.amachat.bungee.Amachat;
import amata1219.amachat.bungee.Config;
import amata1219.amachat.bungee.Initializer;
import amata1219.amachat.chat.Chat;
import amata1219.amachat.chat.ChatManager;
import amata1219.amachat.chat.Id;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.config.Configuration;

public class AutoMessageBot implements TaskBot, Id {

	public static final String NAME = "AutoMessageBot";
	public static final File DIRECTORY = new File(BungeeCord.getInstance().getPluginsFolder() + File.separator + "Bots");

	private final long id;
	private Config config;
	private Set<Long> chats;
	private List<String> messages;
	private int count;

	private ScheduledTask task;
	private boolean pause;

	private AutoMessageBot(long id){
		this.id = id;
	}

	public static AutoMessageBot load(long id){
		AutoMessageBot bot = new AutoMessageBot(id);

		Config config = bot.config = Config.load(new File(DIRECTORY, String.valueOf(id) + ".yml"), "bot.yml", new Initializer(){

			@Override
			public void done(Config config) {
				Configuration conf = config.getConfig();

				conf.set("Version", Amachat.VERSION);
				conf.set("Chats", Collections.emptySet());
				conf.set("Messages", Collections.emptySet());
				conf.set("Interval", 300);

				config.apply();
			}

		});

		Configuration conf = bot.config.getConfig();
		bot.chats = config.getLongSet("Chats");
		bot.messages = conf.getStringList("Messages");
		return bot;
	}

	@Override
	public String getName() {
		return AutoMessageBot.NAME;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public Config getConfig(){
		return config;
	}

	@Override
	public Set<Chat> getJoinedChats() {
		return ChatManager.getInstance().getChats(chats);
	}

	@Override
	public boolean isJoined(long id) {
		return chats.contains(id);
	}

	@Override
	public void joinChat(long id) {
		chats.add(id);
	}

	@Override
	public void quitChat(long id) {
		chats.remove(id);
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
		return config.getConfig().getInt("Interval");
	}

	public void setInterval(int interval){
		config.getConfig().set("Interval", interval);
		start();
	}

	@Override
	public void start() {
		if(task != null)
			stop();

		task = BungeeCord.getInstance().getScheduler().schedule(Amachat.getPlugin(), this, 0L, getInterval(), TimeUnit.SECONDS);
	}

	@Override
	public void pause() {
		pause = !pause;
	}

	@Override
	public void stop() {
		task.cancel();
	}

	@Override
	public boolean isPause() {
		return pause;
	}

	@Override
	public void run() {
		if(pause || messages.isEmpty())
			return;

		ChatManager.getInstance().getChats(chats).forEach(chat -> chat.broadcast(messages.get(messages.size() > count ? count++ : (count = 0))));
	}

}

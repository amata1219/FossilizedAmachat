package amata1219.amachat.bot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import amata1219.amachat.Config;
import amata1219.amachat.Initializer;
import amata1219.amachat.chat.Chat;
import amata1219.amachat.chat.ChatManager;
import net.md_5.bungee.config.Configuration;

public class ActionBot implements Bot, AmachatMessageEventListener4Bot {

	public static final String NAME = "ActionBot";
	public static final File DIRECTORY = new File(Bot.DIRECTORY + File.separator + "ActionBots");

	private final long id;
	private Config config;
	private Set<Long> chats;
	private Set<Action> actions;

	private ActionBot(long id){
		this.id = id;
	}

	public static ActionBot load(long id){
		ActionBot bot = new ActionBot(id);

		Config config = bot.config = Config.load(new File(DIRECTORY, String.valueOf(id) + ".yml"), "bot.yml", new Initializer(){

			@Override
			public void initialize(Config config) {
				Configuration conf = config.getConfig();
				conf.set("Chats", Collections.emptySet());
				conf.set("Actions", Collections.emptySet());
				config.apply();
			}

		});

		Configuration conf = bot.config.getConfig();
		bot.chats = config.getLongSet("Chats");
		conf.getStringList("Actions").forEach(action -> bot.actions.add(Action.decode(action)));
		return bot;
	}

	public void save(){
		Configuration conf = config.getConfig();
		conf.set("Chats", chats);
		Set<String> data = new HashSet<>();
		actions.forEach(action -> data.add(Action.encode(action)));
		conf.set("Actions", data);
		config.apply();
	}

	@Override
	public String getName() {
		return ActionBot.NAME;
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

	public static interface Action extends Serializable {

		static String encode(Action action){
			try {
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
				ObjectOutputStream outputStream = new ObjectOutputStream(byteStream);
				outputStream.writeObject(action);
				outputStream.close();
				byteStream.close();
				return Base64.getEncoder().encodeToString(byteStream.toByteArray());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		static Action decode(String data){
			try {
				return (Action) new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(data))).readObject();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		void done(AmachatMessageEvent4Bot event);

	}

	@Override
	public void onChatMessageReceived(AmachatMessageEvent4Bot event) {
		if(event.isCancelled())
			return;

		if(!chats.contains(event.getChat().getId()))
			return;

		actions.forEach(action -> action.done(event));
	}

}

package amata1219.amachat.bot;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import amata1219.amachat.Config;
import amata1219.amachat.Initializer;
import amata1219.amachat.Util;
import amata1219.amachat.chat.Chat;
import amata1219.amachat.chat.ChatManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.config.Configuration;

public class ChatBot implements Bot, AmachatMessageEventListener4Bot {

	public static final String NAME = "ChatBot";
	public static final File DIRECTORY = new File(BungeeCord.getInstance().getPluginsFolder() + File.separator + "Bots" + File.separator + "ChatBot");

	private final long id;
	private Config config;
	private Set<Long> chats;
	private HashMap<String, String> responces = new HashMap<>();

	private ChatBot(long id){
		this.id = id;
	}

	public static ChatBot load(long id){
		ChatBot bot = new ChatBot(id);

		Config config = bot.config = Config.load(new File(DIRECTORY, String.valueOf(id) + ".yml"), "bot.yml", new Initializer(){

			@Override
			public void initialize(Config config) {
				Configuration conf = config.getConfig();
				conf.set("Chats", Collections.emptySet());
				config.apply();
			}

		});

		Configuration conf = bot.config.getConfig();
		bot.chats = config.getLongSet("Chats");
		Configuration section = conf.getSection("Responces");
		section.getKeys().forEach(responce -> bot.responces.put(responce, section.getString(responce)));
		return bot;
	}

	public void save(){
		Configuration conf = config.getConfig();
		conf.set("Chats", chats);
		conf.set("Responces", null);
		responces.forEach((k, v) -> conf.set("Responces." + k, v));
		config.apply();
	}

	@Override
	public String getName() {
		return ChatBot.NAME;
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

	public Set<String> getKeywords(){
		return responces.keySet();
	}

	public boolean hasKeyword(String keyword){
		return responces.containsKey(keyword);
	}

	public Set<String> getMessages(){
		return new HashSet<>(responces.values());
	}

	public boolean hasMessage(String message){
		return responces.containsValue(message);
	}

	public void addResponce(String keyword, String responce){
		responces.put(keyword, responce);
	}

	public void removeResponce(String keyword){
		responces.remove(keyword);
	}

	@Override
	public void onChatMessageReceived(AmachatMessageEvent4Bot event) {
		if(event.isCancelled())
			return;

		Chat chat = event.getChat();
		if(!chats.contains(chat.getId()))
			return;

		String message = event.getMessage();
		for(String keyword : responces.keySet()){
			if(!message.matches(keyword))
				continue;

			//! - send to player
			String replaced = replaceHolders(responces.get(keyword), event);
			if(replaced.indexOf("!") == 1)
				event.getPlayer().send(Util.toTextComponent(replaced));
			else
				chat.broadcast(replaced);
		}
	}

	public String replaceHolders(String message, AmachatMessageEvent4Bot event){
		return message.replace("[player]", event.getPlayer().getPlayer().getName());
	}

}

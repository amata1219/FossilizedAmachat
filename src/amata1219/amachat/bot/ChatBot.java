package amata1219.amachat.bot;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import amata1219.amachat.Util;
import amata1219.amachat.bot.event.ChatEvent4Bot;
import amata1219.amachat.bot.event.Listener4Bot;
import amata1219.amachat.chat.Chat;
import amata1219.amachat.config.Config;
import amata1219.amachat.config.Initializer;
import amata1219.amachat.user.User;
import net.md_5.bungee.config.Configuration;

public class ChatBot extends Bot implements Listener4Bot {

	public static final String NAME = "ChatBot";
	public static final File DIRECTORY = new File(Bot.DIRECTORY + File.separator + "ChatBot");

	private HashMap<String, String> responces = new HashMap<>();

	private ChatBot(long id){
		this.id = id;
	}

	public static ChatBot load(long id){
		ChatBot bot = new ChatBot(id);

		(bot.config = Config.load(new File(DIRECTORY, String.valueOf(id) + ".yml"), "bot.yml", new Initializer(){

			@Override
			public void initialize(Config config) {
				Configuration configuration = config.getConfiguration();
				configuration.set("ID", id);
				configuration.set("ChatList", Collections.emptySet());
				config.apply();
			}

		})).reload();

		return bot;
	}

	@Override
	public String getName() {
		return ChatBot.NAME;
	}

	@Override
	public void save(){
		if(config == null)
			return;

		config.getConfiguration().set("ChatList", chatIds);

		config.apply();
	}

	@Override
	public void reload(){
		if(config == null)
			return;

		config.reload();

		chatIds = config.getLongSet("ChatList");
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

	public String getMessage(String keyword){
		return responces.get(keyword);
	}

	public void addResponce(String keyword, String responce){
		responces.put(keyword, responce);
	}

	public void removeResponce(String keyword){
		responces.remove(keyword);
	}

	@Override
	public void onChatMessageReceived(ChatEvent4Bot event) {
		if(event.isCancelled())
			return;

		Chat chat = event.getChat();
		if(!chatIds.contains(chat.getId()))
			return;

		String message = event.getMessage();
		for(String keyword : responces.keySet()){
			if(!message.matches(keyword))
				continue;

			//キーワードの接頭辞に!が含まれる場合発言者のみに送信
			User user = event.getUser();
			String replaced = message.replace("[player]", user.toProxiedPlayer().getName());

			if(replaced.startsWith("!"))
				user.sendMessage(Util.toTextComponent(replaced.substring(1)));
			else
				chat.broadcast(replaced);
		}
	}

}

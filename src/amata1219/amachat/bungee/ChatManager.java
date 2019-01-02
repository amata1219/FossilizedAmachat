package amata1219.amachat.bungee;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import amata1219.amachat.chat.Chat;

public class ChatManager {

	private static ChatManager instance;

	private static final Map<String, Chat> REGISTRY = new HashMap<>();
	private final Map<Long, Chat> map = new HashMap<>();

	private ChatManager(){

	}

	public static void load(){
		ChatManager manager = new ChatManager();

		instance = manager;
	}

	public static ChatManager getInstance(){
		return instance;
	}

	public static void register(Chat chat){
		REGISTRY.put(chat.getName(), chat);
	}

	public static void unregister(String chatName){
		REGISTRY.remove(chatName);
	}

	public static Chat get(String chatName){
		return REGISTRY.get(chatName);
	}

	public Chat getChat(long id){
		return map.get(id);
	}

	public Collection<Chat> getChats(){
		return map.values();
	}

	public void addChat(long id, Chat chat){
		map.put(id, chat);
	}

	public void removeChat(long id){
		map.remove(id);
	}
}

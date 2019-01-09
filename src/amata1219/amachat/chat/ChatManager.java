package amata1219.amachat.chat;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import amata1219.amachat.bungee.Logger;
import amata1219.amachat.bungee.Player;
import amata1219.amachat.bungee.PlayerManager;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatManager {

	private static ChatManager instance;

	private static final HashMap<String, Chat> REGISTRY = new HashMap<>();
	private final HashMap<Long, Chat> map = new HashMap<>();

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

	public static void sendMessage(String message, UUID uuid){
		TextComponent component = new TextComponent(message);
		Player player = PlayerManager.getInstance().getPlayer(uuid);
		if(player != null)
			player.send(component);
	}

	public static void sendMessageAndLogging(String message, UUID uuid){
		Logger.info(message);
		sendMessageAndLogging(message, uuid);
	}

	public static void sendMessage(String message, Set<UUID> uuids){
		TextComponent component = new TextComponent(message);
		PlayerManager.getInstance().getPlayers(uuids).forEach(player -> player.send(component));
	}

	public static void sendMessageAndLogging(String message, Set<UUID> uuids){
		Logger.info(message);
		sendMessageAndLogging(message, uuids);
	}

}

package amata1219.amachat.chat;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import amata1219.amachat.Amachat;
import amata1219.amachat.Util;
import amata1219.amachat.user.User;
import amata1219.amachat.user.UserManager;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatManager {

	private static final HashMap<String, Chat> REGISTRY = new HashMap<>();
	//Chat#getName(), Chat
	private static final HashMap<Long, Chat> CHAT = new HashMap<>();

	public static void load(){
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

	public static Chat getChat(long id){
		return CHAT.get(id);
	}

	public static Collection<Chat> getChats(){
		return CHAT.values();
	}

	public static Set<Chat> getChat(Set<Long> chatIdSet){
		Set<Chat> chats = new HashSet<>();
		CHAT.keySet().stream().filter(key -> chatIdSet.contains(key)).forEach(key -> chats.add(CHAT.get(key)));
		return chats;
	}

	public static void addChat(long id, Chat chat){
		CHAT.put(id, chat);
	}

	public static void removeChat(long id){
		CHAT.remove(id);
	}

	public static void sendMessage(UUID uuid, String message, boolean logging){
		User player = UserManager.getUser(uuid);
		if(player != null)
			player.sendMessage(Util.toTextComponent(message));

		if(logging)
			Amachat.info(message);
	}

	public static void sendMessage(Set<UUID> uuidSet, String message, boolean logging){
		TextComponent component = Util.toTextComponent(message);
		UserManager.getUsersByUniqueIdSet(uuidSet).forEach(player -> player.sendMessage(component));

		if(logging)
			Amachat.info(message);
	}

}

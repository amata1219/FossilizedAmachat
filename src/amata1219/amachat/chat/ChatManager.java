package amata1219.amachat.chat;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import amata1219.amachat.Amachat;
import amata1219.amachat.Util;
import amata1219.amachat.user.User;
import amata1219.amachat.user.UserManager;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatManager {

	private static final Map<Long, Chat> CHAT_MAP = new HashMap<>();
	//Chat#getId(), Chat

	public static void load(){
		VanillaChat.load();
		load(ChannelChat.class);
		load(PermissionChannelChat.class);
	}

	public static void load(Class<?> clazz){
		Method load = null;
		Method listFiles = null;
		Field DIRECTORY = null;

		try {
			load = clazz.getMethod("load", long.class);
			listFiles = File.class.getMethod("listFiles");
			DIRECTORY = clazz.getField("DIRECTORY");
			DIRECTORY.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		try {
			for(File file : (File[]) listFiles.invoke(DIRECTORY)){
				String fileName = file.getName();
				if(!Util.isYamlConfiguration(fileName))
					continue;

				long id = Util.getId(fileName);
				ChatManager.registerChat(id, (Chat) load.invoke(null, id));
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static Chat getChat(long id){
		return CHAT_MAP.get(id);
	}

	public static Collection<Chat> getChatCollection(){
		return CHAT_MAP.values();
	}

	public static Set<Chat> getChatSet(Set<Long> chatIds){
		Set<Chat> chats = new HashSet<>();
		CHAT_MAP.keySet().stream().filter(key -> chatIds.contains(key)).forEach(key -> chats.add(CHAT_MAP.get(key)));
		return chats;
	}

	public static void registerChat(long id, Chat chat){
		CHAT_MAP.put(id, chat);
	}

	public static void unregisterChat(long id){
		CHAT_MAP.remove(id);
	}

	public static void sendMessage(UUID uuid, String message, boolean logging){
		User player = UserManager.getUser(uuid);
		if(player != null)
			player.sendMessage(Util.toTextComponent(message));

		if(logging)
			Amachat.info(message);
	}

	public static void sendMessage(Set<UUID> uuids, String message, boolean logging){
		TextComponent component = Util.toTextComponent(message);
		UserManager.getUsersByUniqueIdSet(uuids).forEach(player -> player.sendMessage(component));

		if(logging)
			Amachat.info(message);
	}

}

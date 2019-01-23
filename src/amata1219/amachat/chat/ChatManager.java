package amata1219.amachat.chat;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import amata1219.amachat.Amachat;
import amata1219.amachat.Util;
import amata1219.amachat.user.UserManager;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatManager {

	private static final Map<Long, Chat> CHAT_CHATS = new HashMap<>();
	private static final Set<Long> FORCE_JOIN_CHATS = new HashSet<>();

	private static final Method exists;
	private static final Method mkdir;
	private static final Method listFiles;
	static{
		Method arg0 = null;
		Method arg1 = null;
		Method arg2 = null;
		try{
			Class<?> File = File.class;
			arg0 = File.getMethod("exists");
			arg1 = File.getMethod("mkdir");
			arg2 = File.getMethod("listFiles");
		}catch (NoSuchMethodException | SecurityException e){
			e.printStackTrace();
		}
		exists = arg0;
		mkdir = arg1;
		listFiles = arg2;
	}

	public static void load(Class<?> clazz){
		clazz.asSubclass(Chat.class);

		Method arg0 = null;
		File[] arg2 = null;
		try {
			arg0 = clazz.getMethod("load", long.class);

			Field arg1 = clazz.getField("DIRECTORY");
			arg1.setAccessible(true);
			if(!((boolean) exists.invoke(arg1)))
				mkdir.invoke(arg1);

			arg2 = (File[]) listFiles.invoke(arg1);
		}catch (NoSuchMethodException | SecurityException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
			e.printStackTrace();
		}

		final Method load = arg0;
		final File[] files = arg2;

		Arrays.stream(files).parallel()
		.map(File::getName)
		.filter(Util::isYamlConfiguration)
		.mapToLong(Util::getId)
		.mapToObj(id -> loadChat(load, id))
		.forEach(optional -> optional.ifPresent(chat -> registerChat(chat.getId(), chat)));
	}

	private static Optional<Chat> loadChat(Method load, long id){
		Chat chat = null;
		try {
			chat = (Chat) load.invoke(null, id);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return Optional.empty();
		}
		return Optional.ofNullable(chat);
	}

	public static void registerChat(long id, Chat chat){
		CHAT_CHATS.put(id, chat);
	}

	public static void unregisterChat(long id){
		CHAT_CHATS.remove(id);
	}

	public static boolean isExist(long id){
		return CHAT_CHATS.containsKey(id);
	}

	public static boolean isExist(Chat chat){
		return isExist(chat.getId());
	}

	public static Optional<Chat> getChat(long id){
		return Optional.ofNullable(CHAT_CHATS.get(id));
	}

	public static Optional<Chat> matchedChat(String aliases){
		return CHAT_CHATS.values().parallelStream()
				.filter(chat -> aliases.equals(chat.getAliases()))
				.findFirst();
	}

	public static Collection<Chat> getChats(){
		return CHAT_CHATS.values();
	}

	public static Set<Chat> getChats(Collection<Long> ids){
		return ids.parallelStream()
				.filter(ChatManager::isExist)
				.map(ChatManager::getChat)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toSet());
	}

	public static boolean isForceJoinChat(long id){
		return FORCE_JOIN_CHATS.contains(id);
	}

	public static boolean isForceJoinChat(Chat chat){
		return isForceJoinChat(chat);
	}

	public static void sendMessage(UUID uuid, String message, boolean logging){
		sendMessage(uuid, Util.toTextComponent(message), logging);
	}

	public static void sendMessage(UUID uuid, TextComponent message, boolean logging){
		UserManager.getUser(uuid).ifPresent(user -> user.sendMessage(message));

		if(logging)
			Amachat.info(message.getText());
	}

	public static void sendMessage(Set<UUID> uuids, String message, boolean logging){
		sendMessage(uuids, Util.toTextComponent(message), logging);
	}

	public static void sendMessage(Set<UUID> uuids, TextComponent message, boolean logging){
		UserManager.getUsers(uuids).parallelStream()
		.forEach(user -> user.sendMessage(message));

		if(logging)
			Amachat.info(message.getText());
	}

	public static Set<Chat> fromChatIds(Collection<Long> ids){
		return getChats(ids);
	}

	public static Set<Long> toChatIds(Collection<Chat> chats){
		return chats.parallelStream()
				.filter(ChatManager::isExist)
				.map(chat -> Long.valueOf(chat.getId()))
				.collect(Collectors.toSet());
	}

}

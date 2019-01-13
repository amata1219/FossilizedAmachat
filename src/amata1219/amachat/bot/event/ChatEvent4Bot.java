package amata1219.amachat.bot.event;

import java.util.HashSet;
import java.util.Set;

import amata1219.amachat.chat.Chat;
import amata1219.amachat.user.User;

public class ChatEvent4Bot {

	private static final Set<ChatListener4Bot> LISTENERS = new HashSet<>();

	private Chat chat;
	private User user;
	private String message;

	private boolean cancel;

	public ChatEvent4Bot(Chat chat, User user, String message){
		this.chat = chat;
		this.user = user;
		this.message = message;
	}

	public static ChatEvent4Bot call(Chat chat, User user, String message){
		ChatEvent4Bot event = new ChatEvent4Bot(chat, user, message);
		ChatEvent4Bot.LISTENERS.forEach(listener -> listener.onChatMessageReceived(event));
		return event;
	}

	public static void register(ChatListener4Bot listener){
		LISTENERS.add(listener);
	}

	public static void unregister(ChatListener4Bot listener){
		LISTENERS.remove(listener);
	}

	public Chat getChat(){
		return chat;
	}

	public User getUser(){
		return user;
	}

	public String getMessage(){
		return message;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public boolean isCancelled(){
		return cancel;
	}

	public void setCancelled(boolean cancel){
		this.cancel = cancel;
	}

}

package amata1219.amachat.bot;

import java.util.HashSet;
import java.util.Set;

import amata1219.amachat.bungee.Player;
import amata1219.amachat.chat.Chat;

public class ChatMessageEvent4Bot {

	private static final Set<ChatMessageListener4Bot> listeners = new HashSet<>();

	private Chat chat;
	private Player player;
	private String message;

	private boolean cancel;

	public ChatMessageEvent4Bot(Chat chat, Player player, String message){
		this.chat = chat;
		this.player = player;
		this.message = message;
	}

	public static void register(ChatMessageListener4Bot listener){
		listeners.add(listener);
	}

	public static void unregister(ChatMessageListener4Bot listener){
		listeners.remove(listener);
	}

	public void fire(){
		ChatMessageEvent4Bot.listeners.forEach(listener -> listener.onChatMessageReceived(this));
	}

	public Chat getChat(){
		return chat;
	}

	public Player getPlayer(){
		return player;
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

package amata1219.amachat.bot;

import java.util.HashSet;
import java.util.Set;

import amata1219.amachat.bungee.Player;
import amata1219.amachat.chat.Chat;

public class AmachatMessageEvent4Bot {

	private static final Set<AmachatMessageEventListener4Bot> listeners = new HashSet<>();

	private Chat chat;
	private Player player;
	private String message;

	private boolean cancel;

	public AmachatMessageEvent4Bot(Chat chat, Player player, String message){
		this.chat = chat;
		this.player = player;
		this.message = message;
	}

	public static AmachatMessageEvent4Bot fire(Chat chat, Player player, String message){
		AmachatMessageEvent4Bot event = new AmachatMessageEvent4Bot(chat, player, message);
		AmachatMessageEvent4Bot.listeners.forEach(listener -> listener.onChatMessageReceived(event));
		return event;
	}

	public static void register(AmachatMessageEventListener4Bot listener){
		listeners.add(listener);
	}

	public static void unregister(AmachatMessageEventListener4Bot listener){
		listeners.remove(listener);
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

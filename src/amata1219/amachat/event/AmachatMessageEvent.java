package amata1219.amachat.event;

import amata1219.amachat.chat.Chat;
import amata1219.amachat.player.Player;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class AmachatMessageEvent extends Event implements Cancellable {

	private Chat chat;
	private Player player;
	private String message;

	private boolean cancelled;

	public AmachatMessageEvent(Chat chat, Player player, String message){
		this.chat = chat;
		this.player = player;
		this.message = message;
	}

	public static AmachatMessageEvent call(Chat chat, Player player, String message){
		AmachatMessageEvent event = new AmachatMessageEvent(chat, player, message);
		BungeeCord.getInstance().getPluginManager().callEvent(event);
		return event;
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

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

}

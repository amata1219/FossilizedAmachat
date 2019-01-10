package amata1219.amachat.event;

import amata1219.amachat.chat.Chat;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class AmachatBroadcastEvent extends Event implements Cancellable {

	private Chat chat;
	private String message;

	private boolean cancelled;

	public AmachatBroadcastEvent(Chat chat, String message){
		this.chat = chat;
		this.message = message;
	}

	public static AmachatBroadcastEvent call(Chat chat, String message){
		AmachatBroadcastEvent event = new AmachatBroadcastEvent(chat, message);
		BungeeCord.getInstance().getPluginManager().callEvent(event);
		return event;
	}

	public Chat getChat(){
		return chat;
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

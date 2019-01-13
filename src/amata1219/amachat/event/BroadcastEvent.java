package amata1219.amachat.event;

import amata1219.amachat.chat.Chat;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Cancellable;

public class BroadcastEvent extends AmachatEvent implements Cancellable {

	private String message;

	private boolean cancelled;

	public BroadcastEvent(Chat chat, String message){
		this.message = message;
	}

	public static BroadcastEvent call(Chat chat, String message){
		BroadcastEvent event = new BroadcastEvent(chat, message);
		BungeeCord.getInstance().getPluginManager().callEvent(event);
		return event;
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

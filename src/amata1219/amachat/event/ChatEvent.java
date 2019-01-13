package amata1219.amachat.event;

import amata1219.amachat.chat.Chat;
import amata1219.amachat.user.User;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Cancellable;

public class ChatEvent extends AmachatEvent implements Cancellable {

	private User player;
	private String message;

	private boolean cancelled;

	public ChatEvent(Chat chat, User player, String message){
		this.chat = chat;
		this.player = player;
		this.message = message;
	}

	public static ChatEvent call(Chat chat, User player, String message){
		ChatEvent event = new ChatEvent(chat, player, message);
		BungeeCord.getInstance().getPluginManager().callEvent(event);
		return event;
	}

	public User getPlayer(){
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

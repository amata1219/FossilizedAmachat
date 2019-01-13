package amata1219.amachat.event;

import amata1219.amachat.chat.Chat;
import net.md_5.bungee.api.plugin.Event;

public abstract class AmachatEvent extends Event {

	protected Chat chat;

	public Chat getChat(){
		return chat;
	}

}

package amata1219.amachat.chat;

import amata1219.amachat.bungee.Player;
import amata1219.amachat.processor.FormatType;

public interface Chat {

	String getName();

	void chat(Player player, String message);

	boolean qualsType(Chat chat);

	String getFormat(FormatType type);

}

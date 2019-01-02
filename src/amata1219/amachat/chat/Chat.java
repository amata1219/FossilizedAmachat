package amata1219.amachat.chat;

import amata1219.amachat.bungee.Player;

public interface Chat {

	String getName();

	void chat(Player player, String message);

	boolean equals(Chat chat);

}

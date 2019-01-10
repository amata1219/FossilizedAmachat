package amata1219.amachat.bot;

import java.util.Set;

import amata1219.amachat.bungee.Config;
import amata1219.amachat.chat.Chat;

public interface Bot {

	String getName();

	Config getConfig();

	Set<Chat> getJoinedChats();

	boolean isJoined(long id);

	void joinChat(long id);

	void quitChat(long id);

}

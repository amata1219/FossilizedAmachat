package amata1219.amachat.bot;

import java.io.File;
import java.util.Set;

import amata1219.amachat.Config;
import amata1219.amachat.chat.Chat;
import net.md_5.bungee.BungeeCord;

public interface Bot {

	static final File DIRECTORY = new File(BungeeCord.getInstance().getPluginsFolder() + File.separator + "Bots");

	String getName();

	long getId();

	Config getConfig();

	Set<Chat> getJoinedChats();

	boolean isJoined(long id);

	void joinChat(long id);

	void quitChat(long id);

}

package amata1219.amachat.bot;

import java.io.File;
import java.util.Set;

import amata1219.amachat.Amachat;
import amata1219.amachat.chat.Chat;
import amata1219.amachat.chat.ChatManager;
import amata1219.amachat.config.Config;

public abstract class Bot {

	public static final File DIRECTORY = new File(Amachat.DIRECTORY + File.separator + "Bot");

	protected long id;
	protected Config config;
	protected Set<Long> chatIds;
	protected Set<Chat> chats;

	public abstract String getName();

	public long getId(){
		return id;
	}

	public void save(){
		if(config == null)
			return;

		config.getConfiguration().set("ChatList", ChatManager.toChatIds(chats));

		config.apply();
	}

	public void reload(){
		if(config == null)
			return;

		config.reload();

		chats = ChatManager.fromChatIds(config.getLongSet("ChatList"));
	}

	public Config getConfig(){
		return config;
	}

	public Set<Chat> getChats(){
		return ChatManager.getChats(chatIds);
	}

	public boolean isJoined(long id){
		return chatIds.contains(id);
	}

	public void joinChat(long id){
		chatIds.add(id);
	}

	public void quitChat(long id){
		chatIds.remove(id);
	}

	public void clearChat(){
		chatIds.clear();
	}

}

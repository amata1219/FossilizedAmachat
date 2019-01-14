package amata1219.amachat.bot;

import java.io.File;
import java.util.Set;

import amata1219.amachat.Amachat;
import amata1219.amachat.chat.Chat;
import amata1219.amachat.chat.ChatManager;
import amata1219.amachat.config.Config;

public abstract class Bot {

	public static final File DIRECTORY = new File(Amachat.getPlugin().getDataFolder() + File.separator + "BotData");

	protected long id;
	protected Config config;
	protected Set<Long> chatIdSet;

	public abstract String getName();

	public long getId(){
		return id;
	}

	public void save(){

	}

	public void reload(){

	}

	public Config getConfig(){
		return config;
	}

	public Set<Chat> getJoinedChatSet(){
		return ChatManager.getChat(chatIdSet);
	}

	public boolean isJoined(Chat chat){
		return isJoined(chat.getId());
	}

	public void joinChat(Chat chat){
		joinChat(chat.getId());
	}

	public void quitChat(Chat chat){
		quitChat(chat.getId());
	}

	public boolean isJoined(long id){
		return chatIdSet.contains(id);
	}

	public void joinChat(long id){
		chatIdSet.add(id);
	}

	public void quitChat(long id){
		chatIdSet.remove(id);
	}

	public void clearChat(){
		chatIdSet.clear();
	}

}

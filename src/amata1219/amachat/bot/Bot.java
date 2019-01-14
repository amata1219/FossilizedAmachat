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
	protected Set<Long> chatIds;

	public abstract String getName();

	public long getId(){
		return id;
	}

	public void save(){
		if(config == null)
			return;

		config.getConfiguration().set("ChatList", chatIds);

		config.apply();
	}

	public void reload(){
		if(config == null)
			return;

		config.reload();

		chatIds = config.getLongSet("ChatList");
	}

	public Config getConfig(){
		return config;
	}

	public Set<Chat> getJoinedChatSet(){
		return ChatManager.getChatSet(chatIds);
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

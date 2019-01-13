package amata1219.amachat.chat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import amata1219.amachat.Amachat;
import amata1219.amachat.bot.event.ChatEvent4Bot;
import amata1219.amachat.config.Config;
import amata1219.amachat.event.ChatEvent;
import amata1219.amachat.prefix.PrefixManager;
import amata1219.amachat.processor.FormatType;
import amata1219.amachat.processor.ProcessorManager;
import amata1219.amachat.user.User;
import net.md_5.bungee.BungeeCord;

public abstract class Chat {

	public static final File DIRECTORY = new File(BungeeCord.getInstance().getPluginsFolder() + File.separator + "ChatData");

	protected long id;
	protected Config config;
	protected boolean chat;
	protected Map<FormatType, String> formats = new HashMap<>();
	protected Set<String> processorNames;
	protected Set<UUID> users, mutedUsers, bannedUsers;
	protected String joinMessage, quitMessage;

	public String getName(){
		return null;
	}

	public long getId(){
		return id;
	}

	public void save(){
		//フィールド書き換え時
	}

	public void reload(){
		//コンフィグファイル書き換え時
	}

	public void chat(User user, String message){
		UUID uuid = user.getUniqueId();

		Chat match = PrefixManager.matchChat(message);
		if(match != null && match.isJoin(uuid)){
			match.chat(user, message);
			return;
		}

		if(isMuted(uuid)){
			Amachat.quietInfo("Muted-" + message);
			return;
		}

		ChatEvent4Bot event4Bot = ChatEvent4Bot.call(this, user, message);
		message = event4Bot.getMessage();
		if(event4Bot.isCancelled()){
			Amachat.quietInfo("Cancelled-" + message);
			return;
		}

		ChatEvent event = ChatEvent.call(this, user, message);
		message = event.getMessage();
		if(event.isCancelled()){
			Amachat.quietInfo("Cancelled-" + message);
			return;
		}

		ChatManager.sendMessage(users, ProcessorManager.processAll(user, message, formats, processorNames), true);
	}

	public void broadcast(String message){

	}

	public Config getConfig(){
		return config;
	}

	public boolean canChat(){
		return chat;
	}

	public void setChat(boolean chat){
		this.chat = chat;
	}

	public String getFormat(FormatType type){
		return formats.get(type);
	}

	public void setFormat(FormatType type, String format){
		formats.put(type, format);
	}

	public Set<String> getProcessors(){
		return processorNames;
	}

	public boolean hasProcessor(String processorName){
		return processorNames.contains(processorName);
	}

	public void addProcessor(String processorName){
		processorNames.add(processorName);
	}

	public void removeProcessor(String processorName){
		processorNames.remove(processorName);
	}

	public Set<UUID> getUsers(){
		return users;
	}

	public boolean isJoin(UUID uuid){
		return users.contains(uuid);
	}

	public void join(UUID uuid){

	}

	public void quit(UUID uuid){

	}

	public void kick(UUID uuid, String reason){

	}

	public Set<UUID> getMutedUsers(){
		return mutedUsers;
	}

	public boolean isMuted(UUID uuid){
		return mutedUsers.contains(uuid);
	}

	public void mute(UUID uuid){

	}

	public void unmute(UUID uuid, String reason){

	}

	public Set<UUID> getBannedUsers(){
		return bannedUsers;
	}

	public boolean isBanned(UUID uuid){
		return bannedUsers.contains(uuid);
	}

	public void ban(UUID uuid, String reason){

	}

	public void unban(UUID uuid){

	}

}

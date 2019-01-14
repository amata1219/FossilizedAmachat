package amata1219.amachat.chat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import amata1219.amachat.Amachat;
import amata1219.amachat.bot.event.ChatEvent4Bot;
import amata1219.amachat.config.Config;
import amata1219.amachat.event.BroadcastEvent;
import amata1219.amachat.event.ChatEvent;
import amata1219.amachat.prefix.PrefixManager;
import amata1219.amachat.processor.FormatType;
import amata1219.amachat.processor.ProcessorManager;
import amata1219.amachat.user.User;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.config.Configuration;

public abstract class Chat {

	public static final File DIRECTORY = new File(BungeeCord.getInstance().getPluginsFolder() + File.separator + "ChatData");

	protected long id;
	protected Config config;
	protected boolean chat;
	protected Map<FormatType, String> formats = new HashMap<>();
	protected Set<String> processorNames;
	protected Set<UUID> users, mutedUsers, bannedUsers;
	protected String joinMessage, quitMessage;

	public abstract String getName();

	public long getId(){
		return id;
	}

	public void save(){
		//フィールド書き換え時
		if(config == null)
			return;

		Configuration configuration = config.getConfiguration();

		configuration.set("CanChat", chat);
		configuration.set("JoinMessage", joinMessage);
		configuration.set("QuitMessage", quitMessage);

		formats.forEach((k, v) -> {
			String type = k.name();
			configuration.set(Character.toUpperCase(type.charAt(0)) + type.substring(1), v);
		});

		configuration.set("Processors", processorNames);
		config.set("Users", users);
		config.set("MutedUsers", mutedUsers);
		config.set("BannedUsers", bannedUsers);

		config.apply();
	}

	public void reload(){
		//コンフィグファイル書き換え時
		if(config == null)
			return;

		config.reload();

		Configuration configuration = config.getConfiguration();

		chat = configuration.getBoolean("CanChat");
		joinMessage = configuration.getString("JoinMessage");
		quitMessage = configuration.getString("QuitMessage");

		formats.clear();
		configuration.getSection("Formats").getKeys().forEach(type -> formats.put(FormatType.valueOf(type.toUpperCase()), configuration.getString("Formats." + type)));

		processorNames = config.getStringSet("Processors");
		users = config.getUniqueIdSet("Users");
		mutedUsers = config.getUniqueIdSet("MutedUsers");
		bannedUsers = config.getUniqueIdSet("BannedUsers");
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

		ChatManager.sendMessage(users, ProcessorManager.process(user, message, formats, processorNames), true);
	}

	public void broadcast(String message){
		BroadcastEvent event = BroadcastEvent.call(this, message);
		if(event.isCancelled()){
			Amachat.quietInfo("Cancelled-" + event.getMessage());
			return;
		}

		ChatManager.sendMessage(users, message, true);
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
		users.add(uuid);
		ChatManager.sendMessage(uuid, joinMessage, false);
	}

	public void quit(UUID uuid){
		users.remove(uuid);
		ChatManager.sendMessage(uuid, quitMessage, false);
	}

	public void kick(UUID uuid, String reason){
		users.remove(uuid);
		ChatManager.sendMessage(uuid, reason, false);
	}

	public Set<UUID> getMutedUsers(){
		return mutedUsers;
	}

	public boolean isMuted(UUID uuid){
		return mutedUsers.contains(uuid);
	}

	public void mute(UUID uuid, String reason){
		mutedUsers.add(uuid);
		ChatManager.sendMessage(uuid, reason, false);
	}

	public void unmute(UUID uuid){
		mutedUsers.remove(uuid);
	}

	public Set<UUID> getBannedUsers(){
		return bannedUsers;
	}

	public boolean isBanned(UUID uuid){
		return bannedUsers.contains(uuid);
	}

	public void ban(UUID uuid, String reason){
		bannedUsers.remove(uuid);
		ChatManager.sendMessage(uuid, reason, false);
	}

	public void unban(UUID uuid){
		bannedUsers.add(uuid);
	}

}

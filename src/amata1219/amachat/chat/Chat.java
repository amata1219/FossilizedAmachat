package amata1219.amachat.chat;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import amata1219.amachat.Amachat;
import amata1219.amachat.bot.event.ChatEvent4Bot;
import amata1219.amachat.config.Config;
import amata1219.amachat.event.BroadcastEvent;
import amata1219.amachat.event.ChatEvent;
import amata1219.amachat.prefix.Prefix;
import amata1219.amachat.prefix.PrefixManager;
import amata1219.amachat.processor.Coloring;
import amata1219.amachat.processor.Processor;
import amata1219.amachat.processor.ProcessorManager;
import amata1219.amachat.user.User;
import net.md_5.bungee.config.Configuration;

public abstract class Chat {

	public static final File DIRECTORY = new File(Amachat.DIRECTORY + File.separator + "Chat");

	protected long id;
	protected Config config;
	protected String aliases, description;
	protected boolean chat, quit;
	protected String joinMessage, quitMessage;
	protected String format;
	protected Map<MessageFormatType, String> messageFormats = new HashMap<>();
	protected Set<Processor> processors = new HashSet<>();
	protected Set<UUID> users, mutedUsers, bannedUsers;
	protected Map<UUID, Long> expires = new HashMap<>();

	public abstract String getName();

	public long getId(){
		return id;
	}

	public void save(){
		//フィールド書き換え時
		if(config == null)
			return;

		Configuration configuration = config.getConfiguration();

		configuration.set("Aliases", aliases);
		configuration.set("Description", description);
		configuration.set("CanChat", chat);
		configuration.set("CanQuit", quit);
		configuration.set("JoinMessage", joinMessage);
		configuration.set("QuitMessage", quitMessage);
		configuration.set("Format", Coloring.inverse(format));
		messageFormats.forEach((type, messageFormat) -> configuration.set(type.toCamelCase(), Coloring.inverse(messageFormat)));
		configuration.set("Processors", ProcessorManager.toProcessorNames(processors));
		config.set("Users", users);
		config.set("MutedUsers", mutedUsers);
		config.set("BannedUsers", bannedUsers);
		config.set("Expires", null);
		expires.forEach((uuid, time) -> configuration.set("Expires." + uuid.toString(), time.longValue()));

		config.apply();
	}

	public void reload(){
		//コンフィグファイル書き換え時
		if(config == null)
			return;

		config.reload();

		Configuration configuration = config.getConfiguration();

		aliases = configuration.getString("Aliases");
		description = configuration.getString("Description");
		chat = configuration.getBoolean("CanChat");
		quit = configuration.getBoolean("CanQuit");
		joinMessage = configuration.getString("JoinMessage");
		quitMessage = configuration.getString("QuitMessage");
		format = Coloring.coloring(configuration.getString("Format"));
		messageFormats.clear();
		configuration.getSection("Formats").getKeys().forEach(type -> messageFormats.put(MessageFormatType.valueOf(type.toUpperCase()), Coloring.coloring(configuration.getString("Formats." + type))));
		processors = ProcessorManager.fromProcessorNames(configuration.getStringList("Processors"));
		users = config.getUniqueIdSet("Users");
		mutedUsers = config.getUniqueIdSet("MutedUsers");
		bannedUsers = config.getUniqueIdSet("BannedUsers");
		expires.clear();
		configuration.getSection("Expires").getKeys().forEach(uuid -> expires.put(UUID.fromString(uuid), configuration.getLong("Expires." + uuid)));
	}

	public void chat(User user, String message){
		if(!chat)
			return;

		UUID uuid = user.getUniqueId();

		Chat match = PrefixManager.matchChat(message);
		if(match != null && match != this && match.isJoin(uuid)){
			match.chat(user, PrefixManager.removePrefix((Prefix) match, message));
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

		ChatManager.sendMessage(users, ProcessorManager.processAll(this, user, message), true);
	}

	public void broadcast(String message){
		BroadcastEvent event = BroadcastEvent.call(this, message);
		if(event.isCancelled()){
			Amachat.quietInfo("Cancelled-" + event.getMessage());
			return;
		}

		ChatManager.sendMessage(users, ProcessorManager.processAll(this, message), true);
	}

	public Config getConfig(){
		return config;
	}

	public String getAliases() {
		return aliases;
	}

	public void setAliases(String aliases) {
		this.aliases = aliases;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean canChat(){
		return chat;
	}

	public void setChat(boolean chat){
		this.chat = chat;
	}

	public String getFormat(){
		return format;
	}

	public Map<MessageFormatType, String> getMessageFormats(){
		return messageFormats;
	}

	public String getMessageFormat(MessageFormatType type){
		return messageFormats.get(type);
	}

	public void setMessageFormat(MessageFormatType type, String format){
		messageFormats.put(type, format);
	}

	public Set<Processor> getProcessors(){
		return processors;
	}

	public boolean hasProcessor(Processor processor){
		return processors.contains(processor);
	}

	public void addProcessor(Processor processor){
		processors.add(processor);
	}

	public void removeProcessor(Processor processor){
		processors.remove(processor);
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

	public static enum MessageFormatType {

		NORMAL,
		JAPANIZE,
		TRANSLATE;

		public String toCamelCase(){
			String name = this.name();
			return name.charAt(0) + name.toLowerCase().substring(1);
		}

	}

}

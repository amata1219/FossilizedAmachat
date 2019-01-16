package amata1219.amachat.chat;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import amata1219.amachat.Amachat;
import amata1219.amachat.config.Config;
import amata1219.amachat.event.ChatEvent;
import amata1219.amachat.prefix.Prefix;
import amata1219.amachat.prefix.PrefixManager;
import amata1219.amachat.processor.Coloring;
import amata1219.amachat.processor.ProcessorManager;
import amata1219.amachat.user.User;
import net.md_5.bungee.config.Configuration;

public class RoomChat extends Chat {

	public static final String NAME = "RoomChat";

	protected UUID owner;
	protected Set<UUID> guests = new HashSet<>();
	protected String inviteMessage;

	protected RoomChat(final long id){
		this.id = id;
	}

	public static RoomChat create(UUID owner, Set<UUID> guests){
		RoomChat chat = new RoomChat(System.currentTimeMillis());

		(chat.config = Config.load(new File(Chat.DIRECTORY, "room.yml"), "chat.yml")).reload();

		chat.owner = owner;
		chat.guests = guests;

		return chat;
	}

	@Override
	public String getName() {
		return RoomChat.NAME;
	}

	@Override
	public void save(){

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
		inviteMessage = configuration.getString("InviteMessage");
	}

	@Override
	public void chat(User user, String message){
		UUID uuid = user.getUniqueId();

		Chat match = PrefixManager.matchChat(message);
		if(match != null && match != this && match.isJoin(uuid)){
			match.chat(user, PrefixManager.removePrefix((Prefix) match, message));
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

	@Override
	public boolean canChat(){
		return true;
	}

	@Override
	public void setChat(boolean chat){
	}

	@Override
	public void join(UUID uuid){
		guests.remove(uuid);
		users.add(uuid);
		ChatManager.sendMessage(uuid, joinMessage, false);
	}

	@Override
	public void kick(UUID uuid, String reason){
	}

	@Override
	public Set<UUID> getMutedUsers(){
		return Collections.emptySet();
	}

	@Override
	public boolean isMuted(UUID uuid){
		return false;
	}

	@Override
	public void mute(UUID uuid, String reason){
	}

	@Override
	public void unmute(UUID uuid){
	}

	@Override
	public Set<UUID> getBannedUsers(){
		return Collections.emptySet();
	}

	@Override
	public boolean isBanned(UUID uuid){
		return false;
	}

	@Override
	public void ban(UUID uuid, String reason){
	}

	@Override
	public void unban(UUID uuid){
	}

	public boolean isInvalid(){
		return users.isEmpty();
	}

	public Set<UUID> getGuests(){
		return guests;
	}

	public boolean isInvited(UUID uuid){
		return guests.contains(uuid);
	}

	public void invite(UUID uuid){
		addGuest(uuid);
		ChatManager.sendMessage(uuid, inviteMessage, false);
	}

	public void addGuest(UUID uuid){
		guests.add(uuid);
	}

	public void removeGuest(UUID uuid){
		guests.remove(uuid);
	}

}

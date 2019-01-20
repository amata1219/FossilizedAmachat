package amata1219.amachat.user;

import java.io.File;
import java.util.Set;
import java.util.UUID;

import amata1219.amachat.Amachat;
import amata1219.amachat.Util;
import amata1219.amachat.chat.Chat;
import amata1219.amachat.chat.ChatManager;
import amata1219.amachat.chat.VanillaChat;
import amata1219.amachat.command.Command;
import amata1219.amachat.config.Config;
import amata1219.amachat.config.Initializer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

public final class User {

	private final UUID uuid;
	private Config config;
	private boolean mute, ban, autoJapanize;
	private long destination;
	private Set<Chat> mutedChat;
	private Set<UUID> mutedUsers;

	private User(UUID uuid){
		this.uuid = uuid;
	}

	public static User load(UUID uuid){
		User player = new User(uuid);

		(player.config = Config.load(new File(Amachat.getPlugin().getDataFolder() + File.separator + "UserData", uuid.toString() + ".yml"), "uuid.yml", new Initializer(){

			@Override
			public void initialize(Config config) {
				Configuration configuration = config.getConfiguration();
				configuration.set("UUID", uuid.toString());
				configuration.set("LastPlayed", System.currentTimeMillis());
				config.apply();
			}

		})).reload();

		return player;
	}

	public void save(){
		//フィールド書き換え時
		Configuration configuration = config.getConfiguration();

		configuration.set("Mute", mute);
		configuration.set("Ban", ban);
		configuration.set("AutoJapanize", autoJapanize);
		configuration.set("LastDestination", destination);
		configuration.set("MutedChat", ChatManager.toChatIds(mutedChat));
		configuration.set("MutedUsers", Util.toStringSet(mutedUsers));

		config.apply();
	}

	public void reload(){
		//コンフィグファイル書き換え時
		config.reload();

		Configuration configuration = config.getConfiguration();

		mute = configuration.getBoolean("Mute");
		ban = configuration.getBoolean("Banned");
		autoJapanize = configuration.getBoolean("AutoJapanize");
		destination = configuration.getLong("LastDestination");
		mutedChat = ChatManager.fromChatIds(config.getLongSet("MutedChat"));
		mutedUsers = config.getUniqueIdSet("MutedUsers");
	}

	public UUID getUniqueId(){
		return uuid;
	}

	public ProxiedPlayer toProxiedPlayer(){
		return BungeeCord.getInstance().getPlayer(uuid);
	}

	public String getName(){
		return toProxiedPlayer().getName();
	}

	public void chat(String message){
		getDestination().chat(this, message);
	}

	public void sendMessage(TextComponent component){
		if(component == null)
			return;

		toProxiedPlayer().sendMessage(component);
	}

	public void sendMessage(String message){
		sendMessage(Util.toTextComponent(message));
	}

	public void success(String message){
		sendMessage(ChatColor.AQUA + message);
	}

	public void warn(String message){
		sendMessage(ChatColor.RED + message);
	}

	public boolean isMuted(){
		return mute;
	}

	public void mute(String reason){
		this.mute = true;
	}

	public void unmute(){
		this.mute = false;
	}

	public boolean isBanned(){
		return ban;
	}

	public void ban(String reason){
		this.ban = true;
	}

	public void unban(){
		this.ban = false;
	}

	public boolean isAutoJapanize(){
		return autoJapanize;
	}

	public void setAutoJapanize(boolean autoJapanize){
		this.autoJapanize = autoJapanize;
	}

	public Chat getDestination(){
		Chat chat = ChatManager.getChat(destination);
		return chat == null ? ChatManager.getChat(VanillaChat.ID) : chat;
	}

	public long getDestinationId(){
		return destination;
	}

	public void setDestination(Chat chat){
		destination = chat.getId();
	}

	public void setDestination(long address){
		this.destination = address;
	}

	public void removeDestination(){
		destination = VanillaChat.ID;
	}

	public Set<Chat> getMutedChat(){
		return mutedChat;
	}

	public boolean isMutedChat(Chat chat){
		return mutedChat.contains(chat);
	}

	public void addMutedChat(Chat chat){
		mutedChat.add(chat);
	}

	public void addMutedChat(long id){
		Chat chat = ChatManager.getChat(id);
		if(chat != null)
			addMutedChat(chat);
	}

	public void removeMutedChat(Chat chat){
		mutedChat.remove(chat);
	}

	public void removeMutedChat(long id){
		mutedChat.remove(id);
	}

	public Set<UUID> getMutedUsers(){
		return mutedUsers;
	}

	public boolean isMutedUser(UUID uuid){
		return mutedUsers.contains(uuid);
	}

	public void addMutedUser(UUID uuid){
		mutedUsers.add(uuid);
	}

	public void removeMutedUser(UUID uuid){
		mutedUsers.remove(uuid);
	}

	public boolean hasPermission(int hashCode){
		if(hashCode == 0)
			return true;

		return toProxiedPlayer().hasPermission(Command.PERMISSIONS.get(hashCode));
	}

}

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
import amata1219.amachat.processor.ProcessorManager;
import amata1219.amachat.user.User;
import net.md_5.bungee.config.Configuration;

public class RoomChat extends Chat {

	public static final String NAME = "RoomChat";

	protected UUID owner;
	protected Set<UUID> invitees = new HashSet<>();
	protected String inviteMessage;

	protected RoomChat(final long id){
		this.id = id;
	}

	public static RoomChat create(UUID owner, Set<UUID> guests){
		RoomChat chat = new RoomChat(System.currentTimeMillis());

		(chat.config = Config.load(new File(Chat.DIRECTORY, "room.yml"), "chat.yml")).reload();

		chat.owner = owner;
		chat.invitees = guests;

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

		super.reload();

		config.reload();
		Configuration configuration = config.getConfiguration();
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
	public String tryJoin(UUID uuid){
		if(users.contains(uuid))
			return "§cこのチャットに既に参加しています。";

		if(bannedUsers.contains(uuid))
			return "§cこのチャットには参加出来ません。";

		if(!invitees.contains(uuid))
			return "§cこのチャットに招待されていません。";

		users.add(uuid);
		ChatManager.sendMessage(uuid, joinMessage, false);
		return null;
	}

	@Override
	public String tryLeave(UUID uuid){
		if(!users.contains(uuid))
			return "§cこのチャットに参加していません。";

		users.remove(uuid);
		ChatManager.sendMessage(uuid, leaveMessage, false);
		return null;
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

	public Set<UUID> getInvitees(){
		return invitees;
	}

	public boolean isInvited(UUID uuid){
		return invitees.contains(uuid);
	}

	public void tryInvite(UUID uuid){
		addInvitee(uuid);
		ChatManager.sendMessage(uuid, inviteMessage, false);
	}

	public void addInvitee(UUID uuid){
		invitees.add(uuid);
	}

	public void removeInvitee(UUID uuid){
		invitees.remove(uuid);
	}

}

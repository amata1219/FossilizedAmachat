package amata1219.amachat.chat;

import java.io.File;
import java.util.UUID;
import java.util.stream.Collectors;

import amata1219.amachat.Amachat;
import amata1219.amachat.bot.event.ChatEvent4Bot;
import amata1219.amachat.config.Config;
import amata1219.amachat.config.Initializer;
import amata1219.amachat.event.ChatEvent;
import amata1219.amachat.prefix.Prefix;
import amata1219.amachat.prefix.PrefixManager;
import amata1219.amachat.processor.FormatType;
import amata1219.amachat.processor.ProcessorManager;
import amata1219.amachat.user.User;
import amata1219.amachat.user.UserManager;
import net.md_5.bungee.config.Configuration;

public class PermissionChannelChat extends Permission {

	public static final String NAME = "PermissionChannelChat";
	public static final File DIRECTORY = new File(Chat.DIRECTORY + File.separator + "PermissionChannelChat");

	protected PermissionChannelChat(final long id){
		this.id = id;
	}

	public static PermissionChannelChat load(long id){
		PermissionChannelChat chat = new PermissionChannelChat(id);

		(chat.config = Config.load(new File(DIRECTORY, String.valueOf(id) + ".yml"), "chat.yml", new Initializer(){

			@Override
			public void initialize(Config config) {
				config.getConfiguration().set("ID", id);
				config.apply();
			}

		})).reload();

		return chat;
	}

	@Override
	public String getName() {
		return ChannelChat.NAME;
	}

	@Override
	public void save(){
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
		configuration.set("Permissions", permissions);

		config.apply();
	}

	@Override
	public void reload(){
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
		permissions = config.getStringSet("Permissions");
	}

	@Override
	public void chat(User user, String message){
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

		if(!hasPermissions(user)){
			Amachat.quietInfo("Blocked-" + message);
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

		ChatManager.sendMessage(users.stream().filter(id -> hasPermissions(UserManager.getUser(id))).collect(Collectors.toSet()), ProcessorManager.process(user, message, formats, processorNames), true);
	}

}

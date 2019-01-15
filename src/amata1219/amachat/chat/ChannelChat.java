package amata1219.amachat.chat;

import java.io.File;

import amata1219.amachat.config.Config;
import amata1219.amachat.config.Initializer;
import amata1219.amachat.prefix.Prefix;
import amata1219.amachat.processor.FormatType;
import net.md_5.bungee.config.Configuration;

public class ChannelChat extends Prefix {

	public static final String NAME = "ChannelChat";
	public static final File DIRECTORY = new File(Chat.DIRECTORY + File.separator + "ChannelChat");

	protected ChannelChat(final long id){
		this.id = id;
	}

	public static ChannelChat load(long id){
		ChannelChat chat = new ChannelChat(id);

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

		configuration.set("Aliases", aliases);
		configuration.set("Description", description);
		configuration.set("CanChat", chat);
		configuration.set("JoinMessage", joinMessage);
		configuration.set("QuitMessage", quitMessage);

		messageFormats.forEach((k, v) -> {
			String type = k.name();
			configuration.set(Character.toUpperCase(type.charAt(0)) + type.substring(1), v);
		});

		configuration.set("Processors", processorNames);
		config.set("Users", users);
		config.set("MutedUsers", mutedUsers);
		config.set("BannedUsers", bannedUsers);
		configuration.set("Prefix", prefix);

		config.apply();
	}

	@Override
	public void reload(){
		if(config == null)
			return;

		config.reload();

		Configuration configuration = config.getConfiguration();

		aliases = configuration.getString("Aliases");
		description = configuration.getString("Description");
		chat = configuration.getBoolean("CanChat");
		joinMessage = configuration.getString("JoinMessage");
		quitMessage = configuration.getString("QuitMessage");

		messageFormats.clear();
		configuration.getSection("Formats").getKeys().forEach(type -> messageFormats.put(FormatType.valueOf(type.toUpperCase()), configuration.getString("Formats." + type)));

		processorNames = config.getStringSet("Processors");
		users = config.getUniqueIdSet("Users");
		mutedUsers = config.getUniqueIdSet("MutedUsers");
		bannedUsers = config.getUniqueIdSet("BannedUsers");
		prefix = configuration.getString("Prefix");
	}

}

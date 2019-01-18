package amata1219.amachat.chat;

import java.io.File;
import java.util.UUID;

import amata1219.amachat.config.Config;
import amata1219.amachat.config.Initializer;
import amata1219.amachat.prefix.Prefix;
import amata1219.amachat.processor.Coloring;
import amata1219.amachat.processor.MessageFormatType;
import amata1219.amachat.processor.ProcessorManager;
import net.md_5.bungee.config.Configuration;

public class ChannelChat extends Prefix {

	public static final String NAME = "ChannelChat";
	public static final File DIRECTORY = new File(Chat.DIRECTORY + File.separator + NAME);

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
		configuration.set("CanQuit", leave);
		configuration.set("JoinMessage", joinMessage);
		configuration.set("LeaveMessage", leaveMessage);
		configuration.set("Format", Coloring.inverse(format));
		messageFormats.forEach((type, messageFormat) -> configuration.set(type.toCamelCase(), Coloring.inverse(messageFormat)));
		configuration.set("Processors", ProcessorManager.toProcessorNames(processors));
		config.set("Users", users);
		config.set("MutedUsers", mutedUsers);
		config.set("BannedUsers", bannedUsers);
		config.set("Expires", null);
		expires.forEach((uuid, time) -> configuration.set("Expires." + uuid.toString(), time.longValue()));
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
		leave = configuration.getBoolean("CanQuit");
		joinMessage = configuration.getString("JoinMessage");
		leaveMessage = configuration.getString("LeaveMessage");
		format = Coloring.coloring(configuration.getString("Format"));
		messageFormats.clear();
		configuration.getSection("Formats").getKeys().forEach(type -> messageFormats.put(MessageFormatType.valueOf(type.toUpperCase()), Coloring.coloring(configuration.getString("Formats." + type))));
		processors = ProcessorManager.fromProcessorNames(configuration.getStringList("Processors"));
		users = config.getUniqueIdSet("Users");
		mutedUsers = config.getUniqueIdSet("MutedUsers");
		bannedUsers = config.getUniqueIdSet("BannedUsers");
		expires.clear();
		configuration.getSection("Expires").getKeys().forEach(uuid -> expires.put(UUID.fromString(uuid), configuration.getLong("Expires." + uuid)));
		prefix = configuration.getString("Prefix");
	}

}

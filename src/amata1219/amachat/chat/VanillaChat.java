package amata1219.amachat.chat;

import java.io.File;
import amata1219.amachat.config.Config;
import amata1219.amachat.config.Initializer;
import amata1219.amachat.processor.FormatType;
import net.md_5.bungee.config.Configuration;

public class VanillaChat extends Chat {

	public static final String NAME = "VanillaChat";
	public static final long ID = 0L;

	private VanillaChat(){

	}

	public static VanillaChat load(){
		VanillaChat chat = new VanillaChat();

		chat.config = Config.load(new File(Chat.DIRECTORY, "vanilla.yml"), "chat.yml", new Initializer(){

			@Override
			public void initialize(Config config) {
				config.getConfiguration().set("ID", ID);
				config.apply();
			}

		});

		chat.reload();

		return chat;
	}

	@Override
	public String getName() {
		return VanillaChat.NAME;
	}

	@Override
	public long getId(){
		return VanillaChat.ID;
	}

	@Override
	public void save() {
		Configuration configuration = config.getConfiguration();

		configuration.set("CanChat", chat);
		configuration.set("JoinMessage", joinMessage);
		configuration.set("QuitMessage", quitMessage);

		formats.forEach((k, v) -> {
			String type = k.name();
			type = Character.toUpperCase(type.charAt(0)) + type.substring(1);
			configuration.set(type, v);
		});

		configuration.set("Processors", processorNames);
		configuration.set("Users", users);
		configuration.set("MutedUsers", mutedUsers);
		configuration.set("BannedUsers", bannedUsers);

		config.apply();
	}

	@Override
	public void reload() {
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

}

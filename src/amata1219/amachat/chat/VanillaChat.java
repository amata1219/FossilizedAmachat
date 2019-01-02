package amata1219.amachat.chat;

import java.io.File;
import java.util.List;
import java.util.UUID;

import amata1219.amachat.bungee.Amachat;
import amata1219.amachat.bungee.Config;
import amata1219.amachat.bungee.Initializer;
import amata1219.amachat.bungee.Player;
import amata1219.amachat.bungee.ProcessorManager;
import amata1219.amachat.processor.Processor;
import net.md_5.bungee.config.Configuration;

public class VanillaChat implements Chat {

	public static final String NAME = "Vanilla";

	private Config config;
	private List<Processor> processor;
	private List<UUID> mutedPlayers;

	private VanillaChat(){

	}

	public static VanillaChat load(){
		VanillaChat chat = new VanillaChat();

		Config config = chat.config = Config.load(new File(Amachat.getPlugin().getDataFolder(), "vanilla.yml"), "chat.yml", new Initializer(){

			@Override
			public void done(Config config) {
				Configuration conf = config.getConfig();

				conf.set("Version", Amachat.VERSION);
				conf.set("BannedPlayers", null);

				config.apply();
			}

		});

		chat.processor = ProcessorManager.get(config.getConfig().getStringList("Processors"));
		chat.mutedPlayers = config.getUniqueIdList("MutedPlayers");

		return chat;
	}

	@Override
	public String getName() {
		return VanillaChat.NAME;
	}

	@Override
	public void chat(Player player, String message) {
		if(mutedPlayers.contains(player.getUniqueId()))
			return;
	}

	@Override
	public boolean equals(Chat chat) {
		return chat instanceof VanillaChat;
	}

	public boolean canChat(){
		return config.getConfig().getBoolean("Chat");
	}

}

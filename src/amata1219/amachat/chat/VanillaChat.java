package amata1219.amachat.chat;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import amata1219.amachat.bot.ChatMessageEvent4Bot;
import amata1219.amachat.bungee.Amachat;
import amata1219.amachat.bungee.Config;
import amata1219.amachat.bungee.Initializer;
import amata1219.amachat.bungee.Player;
import amata1219.amachat.bungee.PlayerManager;
import amata1219.amachat.bungee.ProcessorManager;
import amata1219.amachat.processor.Processor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;

public class VanillaChat implements Chat {

	public static final String NAME = "Vanilla";

	private Config config;
	private Set<String> processors;
	private Set<UUID> mutedPlayers;

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

		chat.processors = new HashSet<>(config.getConfig().getStringList("Processors"));
		chat.mutedPlayers = config.getUniqueIdSet("MutedPlayers");

		return chat;
	}

	@Override
	public String getName() {
		return VanillaChat.NAME;
	}

	@Override
	public void chat(Player player, String message) {
		if(!canChat())
			return;

		if(mutedPlayers.contains(player.getUniqueId()))
			return;

		ChatMessageEvent4Bot event = new ChatMessageEvent4Bot(this, player, message);
		event.fire();
		if(event.isCancelled())
			return;

		String text = event.getMessage();
		for(Processor processor : ProcessorManager.get(processors))
			text = processor.prosess(text);
		
		

		TextComponent component = new TextComponent(message);
		PlayerManager.getInstance().getPlayers(mutedPlayers).forEach(p -> p.send(component));
	}

	@Override
	public boolean equals(Chat chat) {
		return chat instanceof VanillaChat;
	}

	public boolean canChat(){
		return config.getConfig().getBoolean("Chat");
	}

}

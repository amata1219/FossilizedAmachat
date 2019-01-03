package amata1219.amachat.bungee;

import java.io.File;
import java.util.Set;
import java.util.UUID;

import amata1219.amachat.chat.Chat;
import amata1219.amachat.chat.ChatManager;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;

public class Player {

	private UUID uuid;
	private Config config;
	private Set<UUID> mutePlayers;

	private long address = 0L;

	private Player(){

	}

	public static Player load(UUID uuid){
		Player player = new Player();

		player.uuid = uuid;
		player.config = Config.load(new File(Amachat.getPlugin().getDataFolder() + File.separator + "PlayerData", uuid.toString() + ".yml"), "uuid.yml", new Initializer(){

			@Override
			public void done(Config config) {
				Configuration conf = config.getConfig();

				conf.set("Version", Amachat.VERSION);
				conf.set("UUID", uuid.toString());

				config.apply();
			}

		});

		player.mutePlayers = player.config.getUniqueIdSet("MutedPlayers");

		return player;
	}

	public UUID getUniqueId(){
		return uuid;
	}

	public String getName(){
		return Amachat.getPlugin().getProxy().getPlayer(uuid).getName();
	}

	public boolean isMuted(){
		return config.getConfig().getBoolean("Mute");
	}

	public boolean isBanned(){
		return config.getConfig().getBoolean("Ban");
	}

	public Set<UUID> getMutePlayers(){
		return mutePlayers;
	}

	public Chat getAddress(){
		return ChatManager.getInstance().getChat(address);
	}

	public void setAddress(long id){
		address = id;
	}

	public void removeAddress(){
		address = 0L;
	}

	public void chat(String message){
		Chat chat = getAddress();
		if(chat == null)
			return;

		chat.chat(this, message);
	}

	public void send(TextComponent component){
		Amachat.getPlugin().getProxy().getPlayer(uuid).sendMessage(component);
	}

}

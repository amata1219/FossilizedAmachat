package amata1219.amachat.player;

import java.io.File;
import java.util.Set;
import java.util.UUID;

import amata1219.amachat.Amachat;
import amata1219.amachat.Config;
import amata1219.amachat.Initializer;
import amata1219.amachat.Util;
import amata1219.amachat.chat.Chat;
import amata1219.amachat.chat.ChatManager;
import amata1219.amachat.chat.VanillaChat;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

public class Player {

	private UUID uuid;
	private Config config;
	private boolean mute;
	private boolean ban;
	private Set<UUID> muted;

	private long address = 0L;

	private Player(){

	}

	public static Player load(UUID uuid){
		Player player = new Player();

		player.uuid = uuid;
		player.config = Config.load(new File(Amachat.getPlugin().getDataFolder() + File.separator + "PlayerData", uuid.toString() + ".yml"), "uuid.yml", new Initializer(){

			@Override
			public void initialize(Config config) {
				Configuration conf = config.getConfig();
				conf.set("UUID", uuid.toString());
				config.apply();
			}

		});

		player.muted = player.config.getUniqueIdSet("Muted");
		return player;
	}

	public void save(){
		Configuration conf = config.getConfig();
		conf.set("Mute", mute);
		conf.set("Ban", ban);
		conf.set("Muted", Util.toStringSet(muted));
		config.apply();
	}

	public UUID getUniqueId(){
		return uuid;
	}

	public ProxiedPlayer getPlayer(){
		return BungeeCord.getInstance().getPlayer(uuid);
	}

	public String getName(){
		return Amachat.getPlugin().getProxy().getPlayer(uuid).getName();
	}

	public boolean isMuted(){
		return mute;
	}

	public boolean isBanned(){
		return ban;
	}

	public Set<UUID> getMutePlayers(){
		return muted;
	}

	public Chat getAddress(){
		ChatManager manager = ChatManager.getInstance();
		Chat chat = manager.getChat(address);
		return chat == null ? manager.getChat(VanillaChat.ID) : chat;
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

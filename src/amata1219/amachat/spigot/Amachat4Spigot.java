package amata1219.amachat.spigot;

import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class Amachat4Spigot extends JavaPlugin {

	private static Amachat4Spigot plugin;

	@Override
	public void onEnable(){
		plugin = this;

		SpigotConfig config = new SpigotConfig(this, "settings.yml");
		config.saveDefaultConfig();

		FileConfiguration conf = config.getConfig();
		if(conf.getBoolean("PlayerDeath"))
			PlayerDeathListener.load();

		if(conf.getBoolean("PlayerAdvancementDone"))
			PlayerAdvancementDoneListener.load();

		if(conf.getBoolean("Dynmap"))
			DynmapBridge.load(getServer().getPluginManager().getPlugin("Dynmap"));

		if(conf.getBoolean("DiscordSRV"))
			DiscordSRVBridge.load(getServer().getPluginManager().getPlugin("DiscordSRV"));

		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}

	@Override
	public void onDisable(){
		HandlerList.unregisterAll((JavaPlugin) this);
		getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
		DynmapBridge.unload();
		DiscordSRVBridge.unload();
	}

	public static Amachat4Spigot getPlugin(){
		return plugin;
	}

	public static void sendPluginMessage(String... messages){
		Optional<? extends Player> player = plugin.getServer().getOnlinePlayers().stream().findFirst();
		if(!player.isPresent())
			return;

		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		Stream.of(messages).forEach(out::writeUTF);
		player.get().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}

}

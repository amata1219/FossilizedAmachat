package amata1219.amachat.spigot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import amata1219.amachat.Util;

public class Amachat extends JavaPlugin implements Listener, PluginMessageListener {

	private static Amachat plugin;

	@Override
	public void onEnable() {
		plugin = this;
	}

	@Override
	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return true;
	}

	public static Amachat getPlugin(){
		return plugin;
	}

	@Override
	public void onPluginMessageReceived(String tag, Player player, byte[] data) {
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		Player player = e.getPlayer();
		player.sendPluginMessage(this, "BungeeCord", Util.toByteArray(player.getUniqueId(), e.getMessage()));
		//uuid, message
	}

}

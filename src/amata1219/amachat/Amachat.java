package amata1219.amachat;

import java.io.File;

import amata1219.amachat.config.Config;
import amata1219.amachat.user.User;
import amata1219.amachat.user.UserManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class Amachat extends Plugin {

	private static Amachat plugin;

	public static final float VERSION = 1.0F;
	private Config config;

	@Override
	public void onEnable() {
		plugin = this;

		config = Config.load(new File(getDataFolder() + File.separator + "config.yml"), "config.yml");

		/*
		 * ProcessorManager
		 * ChatManager
		 * BotManager
		 * PlayerManager
		 */
	}

	@Override
	public void onDisable() {
	}

	public static Amachat getPlugin() {
		return plugin;
	}

	public static Config getConfig(){
		return plugin.config;
	}

	@SuppressWarnings("deprecation")
	public static void chat(final ProxiedPlayer sender, String message){
		if(sender == null || message == null)
			return;

		UserManager manager = UserManager.getInstance();
		final User player = manager.getPlayer(sender.getUniqueId());
		if(player == null && !manager.fix(sender))
			return;

		Amachat.getPlugin().getExecutorService().execute(new Runnable(){

			@Override
			public void run() {
				player.getAddress().chat(player, message);
			}

		});
	}

	public static void info(String message){
		plugin.getLogger().info(message);
	}

	public static void quietInfo(String message){
		info(ChatColor.GRAY + message);
	}
}

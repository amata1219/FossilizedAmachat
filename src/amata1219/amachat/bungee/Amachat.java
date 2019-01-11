package amata1219.amachat.bungee;

import java.io.File;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class Amachat extends Plugin implements Listener {

	private static Amachat plugin;
	public static final String VERSION = "1.0";

	private Config config;

	@Override
	public void onEnable() {
		plugin = this;

		config = Config.load(new File(getDataFolder() + File.separator + "config.yml"), "config.yml", new Initializer(){

			@Override
			public void done(Config config) {
			}

		});

		getProxy().getPluginManager().registerListener(this, this);
	}

	@Override
	public void onDisable() {

	}

	public static Amachat getPlugin() {
		return plugin;
	}

	public Config getConfig(){
		return config;
	}

	@EventHandler
	public void onChat(ChatEvent e) {
		if(!(e.getSender() instanceof ProxiedPlayer))
			return;

		e.setCancelled(true);
		Amachat.chat((ProxiedPlayer) e.getSender(), e.getMessage());
	}

	@SuppressWarnings("deprecation")
	public static void chat(final ProxiedPlayer sender, String message){
		if(sender == null || message == null)
			return;

		PlayerManager manager = PlayerManager.getInstance();
		final Player player = manager.getPlayer(sender.getUniqueId());
		if(player == null && !manager.fix(sender))
			return;

		Amachat.getPlugin().getExecutorService().execute(new Runnable(){

			@Override
			public void run() {
				player.getAddress().chat(player, message);
			}

		});
	}
}

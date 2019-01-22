package amata1219.amachat.spigot;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent;
import github.scarsz.discordsrv.util.DiscordUtil;

public class DiscordSRVBridge implements PluginMessageListener {

	private static DiscordSRVBridge bridge;

	public static void load(Plugin plugin){
		if(!(plugin instanceof DiscordSRV))
			return;

		bridge = new DiscordSRVBridge();
		DiscordSRV.api.subscribe(bridge);
		Amachat4Spigot amachat = Amachat4Spigot.getPlugin();
		amachat.getServer().getMessenger().registerIncomingPluginChannel(amachat, "BungeeCord", bridge);
	}

	public static void unload(){
		DiscordSRV.api.unsubscribe(bridge);
		Amachat4Spigot amachat = Amachat4Spigot.getPlugin();
		amachat.getServer().getMessenger().unregisterIncomingPluginChannel(amachat, "BungeeCord", bridge);
	}

	@Override
	public void onPluginMessageReceived(String tag, Player player, byte[] data) {
		if(!tag.equals("BungeeCord") && ! tag.equals("bungeecord:main"))
			return;

		ByteArrayDataInput in = ByteStreams.newDataInput(data);
		if(!in.readUTF().equals("Amachat"))
			return;

		if(!in.readUTF().equals("Dynmap"))
			return;

		DiscordUtil.queueMessage(DiscordSRV.getPlugin().getMainTextChannel(), in.readUTF());
	}

	@Subscribe
	public void onReceived(DiscordGuildMessageReceivedEvent e){
		Amachat4Spigot.sendPluginMessage("Amachat", "DiscordSRV", e.getAuthor().getName(), e.getMessage().toString());
	}

}

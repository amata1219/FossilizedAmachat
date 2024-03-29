package amata1219.amachat.spigot;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.dynmap.DynmapAPI;
import org.dynmap.DynmapWebChatEvent;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class DynmapBridge implements Listener, PluginMessageListener {

	private static DynmapBridge bridge;
	private DynmapAPI api;

	private DynmapBridge(){

	}

	public static void load(Plugin plugin){
		if(!(plugin instanceof DynmapAPI))
			return;

		bridge = new DynmapBridge();
		bridge.api = (DynmapAPI) plugin;

		Amachat4Spigot amachat = Amachat4Spigot.getPlugin();
		amachat.getServer().getPluginManager().registerEvents(bridge, amachat);
		amachat.getServer().getMessenger().registerIncomingPluginChannel(amachat, "BungeeCord", bridge);
	}

	public static void unload(){
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

		if(!in.readUTF().equals("DiscordSRV"))
			return;

		api.sendBroadcastToWeb(in.readUTF(), in.readUTF());
	}

	@EventHandler
	public void onChat(DynmapWebChatEvent e){
		if(!e.isCancelled())
			Amachat4Spigot.sendPluginMessage("Amachat", "Dynmap", e.getName(), e.getMessage());
	}

}

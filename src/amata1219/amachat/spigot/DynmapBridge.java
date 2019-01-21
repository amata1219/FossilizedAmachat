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

	private DynmapAPI api;

	private DynmapBridge(){

	}

	public static void load(Plugin plugin){
		if(!(plugin instanceof DynmapAPI))
			return;

		DynmapBridge bridge = new DynmapBridge();
		bridge.api = (DynmapAPI) plugin;
	}

	@Override
	public void onPluginMessageReceived(String tag, Player player, byte[] data) {
		if(!tag.equals("BungeeCord") && ! tag.equals("bungeecord:main"))
			return;

		ByteArrayDataInput stream = ByteStreams.newDataInput(data);
		if(!stream.readUTF().equals("Amachat"))
			return;

		if(!stream.readUTF().equals("Dynmap"))
			return;

		if(stream.readUTF().equals("Chat"))
			api.postPlayerMessageToWeb(player, stream.readUTF());
		else if(stream.equals("Broadcast"))
			api.sendBroadcastToWeb(null, stream.readUTF());
	}

	@EventHandler
	public void onChat(DynmapWebChatEvent e){

	}

}

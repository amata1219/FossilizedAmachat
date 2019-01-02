package amata1219.amachat.bungee;

import java.util.UUID;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import amata1219.amachat.MessageChannel;
import amata1219.amachat.chat.Chat;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class Amachat extends Plugin implements Listener {

	private static Amachat plugin;

	public static final String VERSION = "1.0";

	@Override
	public void onEnable() {
		plugin = this;

		getProxy().getPluginManager().registerListener(this, this);
	}

	@Override
	public void onDisable() {
	}

	public static Amachat getPlugin(){
		return plugin;
	}

	@EventHandler
	public void onReceive(PluginMessageEvent e){
		String tag = e.getTag();
		if(!tag.equals("BungeeCord") || !tag.equals("bungeecord:main"))
			return;

		ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
		MessageChannel channel = MessageChannel.newInstance(in);

		channel.read(in);
		UUID uuid = UUID.fromString(channel.getMessage());
		Player player = PlayerManager.getInstance().getPlayer(uuid);
		Chat chat = player.getAddress();

		channel.read(in);
	}

	public void registerIChat(Chat iChat){

	}

	public void unregisterIChat(String chatName){

	}

}

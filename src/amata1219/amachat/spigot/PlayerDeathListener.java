package amata1219.amachat.spigot;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

	public static void load(){
		Amachat4Spigot amachat = Amachat4Spigot.getPlugin();
		amachat.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), amachat);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onDeath(PlayerDeathEvent e){
		String message = e.getDeathMessage();
		if(message == null || message.isEmpty())
			return;

		Amachat4Spigot.sendPluginMessage("Amachat", "PlayerDeath", message.replace("_", "\\_").replace("*", "\\*").replace("~", "\\~"));
	}

}

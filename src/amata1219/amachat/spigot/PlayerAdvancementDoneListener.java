package amata1219.amachat.spigot;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.advancement.Advancement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class PlayerAdvancementDoneListener implements Listener {

	public static void load(){
		Amachat4Spigot amachat = Amachat4Spigot.getPlugin();
		amachat.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), amachat);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onDone(PlayerAdvancementDoneEvent e){
		Advancement advancement = e.getAdvancement();
		if(advancement == null)
			return;

		String name = advancement.getKey().getKey();
		if(name == null)
			return;

		String message = Arrays.stream(name
				.substring(name.lastIndexOf("/") + 1).toLowerCase().split("_")).map((s) -> {
					return s.substring(0, 1).toUpperCase() + s.substring(1);
				}).collect(Collectors.joining(" "));

		Amachat4Spigot.sendPluginMessage("Amachat", "PlayerAdvancementDone", e.getPlayer().getName(), message);
	}

}

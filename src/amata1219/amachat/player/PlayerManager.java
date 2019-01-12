package amata1219.amachat.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import amata1219.amachat.Amachat;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerManager implements Listener {

	private static PlayerManager instance;

	private final Map<UUID, Player> players = new HashMap<>();

	private PlayerManager(){

	}

	public static void load(){
		PlayerManager manager = new PlayerManager();

		Amachat plugin = Amachat.getPlugin();
		plugin.getProxy().getPluginManager().registerListener(plugin, manager);

		instance = manager;
	}

	public static PlayerManager getInstance(){
		return instance;
	}

	@EventHandler
	public void onLogin(PostLoginEvent e){
		UUID uuid = e.getPlayer().getUniqueId();
		players.put(uuid, Player.load(uuid));
	}

	@EventHandler
	public void onLogout(PlayerDisconnectEvent e){
		players.remove(e.getPlayer().getUniqueId());
	}

	public Player getPlayer(UUID uuid){
		return players.get(uuid);
	}

	public Set<Player> getPlayers(){
		return players.values().stream().collect(Collectors.toSet());
	}

	public Set<Player> getPlayers(Set<UUID> uuids){
		return players.values().stream().filter(player -> uuids.contains(player.getUniqueId())).collect(Collectors.toSet());
	}

	public boolean fix(ProxiedPlayer player){
		if(player == null || !player.isConnected())
			return false;

		UUID uuid = player.getUniqueId();
		if(!players.containsKey(uuid))
			return false;

		players.put(uuid, Player.load(uuid));
		return true;
	}

}

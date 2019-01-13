package amata1219.amachat.user;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import amata1219.amachat.Amachat;
import amata1219.amachat.config.Config;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

public class UserManager implements Listener {

	private static UserManager instance;

	private final Map<UUID, User> players = new HashMap<>();
	private Config config;
	private BiMap<UUID, String> cache = HashBiMap.create();

	private UserManager(){

	}

	public static void load(){
		UserManager manager = new UserManager();

		Amachat plugin = Amachat.getPlugin();
		plugin.getProxy().getPluginManager().registerListener(plugin, manager);

		manager.config = Config.load(new File(Amachat.getPlugin().getDataFolder(), "players.yml"), "players.yml");
		Configuration conf = manager.config.getConfiguration();
		conf.getKeys().forEach(uuid -> manager.cache.put(UUID.fromString(uuid), conf.getString(uuid)));

		instance = manager;
	}

	public void unload(){
		cache.forEach((k, v) -> config.getConfiguration().set(k.toString(), v));
		config.apply();
	}

	public static UserManager getInstance(){
		return instance;
	}

	public Config getConfig(){
		return config;
	}

	@EventHandler
	public void onLogin(PostLoginEvent e){
		ProxiedPlayer player = e.getPlayer();
		UUID uuid = player.getUniqueId();
		players.put(uuid, User.load(uuid));
		cache.put(uuid, player.getName());
	}

	@EventHandler
	public void onLogout(PlayerDisconnectEvent e){
		players.remove(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onChat(ChatEvent e) {
		if(!(e.getSender() instanceof ProxiedPlayer))
			return;

		e.setCancelled(true);
		Amachat.chat((ProxiedPlayer) e.getSender(), e.getMessage());
	}

	public User getPlayer(UUID uuid){
		return players.get(uuid);
	}

	public Set<User> getPlayers(){
		return players.values().stream().collect(Collectors.toSet());
	}

	public Set<User> getPlayers(Set<UUID> uuids){
		return players.values().stream().filter(player -> uuids.contains(player.getUniqueId())).collect(Collectors.toSet());
	}

	public boolean fix(ProxiedPlayer player){
		if(player == null || !player.isConnected())
			return false;

		UUID uuid = player.getUniqueId();
		if(!players.containsKey(uuid))
			return false;

		players.put(uuid, User.load(uuid));
		return true;
	}

	public static boolean isExist(String playerName){
		return instance.cache.inverse().containsKey(playerName);
	}

	public static boolean isExist(UUID uuid){
		return instance.cache.containsKey(uuid);
	}

	public static UUID getUniqueId(String playerName){
		return instance.cache.inverse().get(playerName);
	}

	public static String getPlayerName(UUID uuid){
		return instance.cache.get(uuid);
	}

}

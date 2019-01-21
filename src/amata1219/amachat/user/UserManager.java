package amata1219.amachat.user;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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

	private static Config config;
	private static final Map<UUID, User> USERS = new HashMap<>();
	private static final BiMap<UUID, String> CACHE = HashBiMap.create();

	private UserManager(){

	}

	public static void load(){
		UserManager manager = new UserManager();

		Amachat plugin = Amachat.getPlugin();
		plugin.getProxy().getPluginManager().registerListener(plugin, manager);

		Configuration configuration = (config = Config.load(new File(Amachat.getPlugin().getDataFolder(), "players.yml"), "players.yml")).getConfiguration();
		configuration.getKeys().forEach(uuid -> CACHE.put(UUID.fromString(uuid), configuration.getString(uuid)));
	}

	public static void unload(){
		CACHE.forEach((k, v) -> config.getConfiguration().set(k.toString(), v));
		config.apply();
	}

	public static Config getConfig(){
		return config;
	}

	@EventHandler
	public void onLogin(PostLoginEvent e){
		ProxiedPlayer player = e.getPlayer();
		UUID uuid = player.getUniqueId();
		USERS.put(uuid, User.load(uuid));
		CACHE.put(uuid, player.getName());
	}

	@EventHandler
	public void onLogout(PlayerDisconnectEvent e){
		USERS.remove(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onChat(ChatEvent e) {
		if(!(e.getSender() instanceof ProxiedPlayer))
			return;

		e.setCancelled(true);
		Amachat.chat((ProxiedPlayer) e.getSender(), e.getMessage());
	}

	public static boolean isOnline(String playerName){
		return Amachat.getPlugin().getProxy().getPlayer(playerName) != null;
	}

	public static User getUser(UUID uuid){
		return USERS.get(uuid);
	}

	public static User getUser(String playerName){
		return getUser(getUniqueId(playerName));
	}

	public static Set<User> getUsers(){
		return new HashSet<>(USERS.values());
	}

	public static Set<User> getUsersByUniqueIdSet(Set<UUID> uuids){
		Set<User> users = new HashSet<>();
		for(UUID uuid : uuids){
			if(USERS.containsKey(uuid))
				users.add(getUser(uuid));
		}
		return users;
	}

	public static boolean fix(ProxiedPlayer player){
		if(player == null || !player.isConnected())
			return false;

		UUID uuid = player.getUniqueId();
		if(!USERS.containsKey(uuid))
			return false;

		USERS.put(uuid, User.load(uuid));
		return true;
	}

	public static boolean isExist(String playerName){
		return CACHE.inverse().containsKey(playerName);
	}

	public static boolean isExist(UUID uuid){
		return CACHE.containsKey(uuid);
	}

	public static UUID getUniqueId(String playerName){
		return CACHE.inverse().get(playerName);
	}

	public static String getPlayerName(UUID uuid){
		return CACHE.get(uuid);
	}

}

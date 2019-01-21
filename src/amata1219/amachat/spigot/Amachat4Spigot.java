package amata1219.amachat.spigot;

import org.bukkit.plugin.java.JavaPlugin;

public class Amachat4Spigot extends JavaPlugin {

	private static Amachat4Spigot plugin;

	@Override
	public void onEnable(){
		plugin = this;
	}

	@Override
	public void onDisable(){

	}

	public static Amachat4Spigot getPlugin(){
		return plugin;
	}

}

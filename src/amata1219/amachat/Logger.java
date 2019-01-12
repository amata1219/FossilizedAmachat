package amata1219.amachat;

import net.md_5.bungee.api.ChatColor;

public class Logger {

	public static void info(String message){
		Amachat.getPlugin().getLogger().info(message);
	}

	public static void grayInfo(String message){
		info(ChatColor.GRAY + message);
	}

}

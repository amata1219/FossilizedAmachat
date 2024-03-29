package amata1219.amachat;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import amata1219.amachat.bot.ActionBot;
import amata1219.amachat.bot.AutoMessageBot;
import amata1219.amachat.bot.BotManager;
import amata1219.amachat.bot.ChatBot;
import amata1219.amachat.chat.ChannelChat;
import amata1219.amachat.chat.ChatManager;
import amata1219.amachat.chat.PermissionChannelChat;
import amata1219.amachat.chat.PrivateChat;
import amata1219.amachat.chat.VanillaChat;
import amata1219.amachat.command.AmachatCommand;
import amata1219.amachat.command.BroadcastCommand;
import amata1219.amachat.command.ChatCommand;
import amata1219.amachat.command.Command;
import amata1219.amachat.command.MessageCommand;
import amata1219.amachat.config.Config;
import amata1219.amachat.mail.Mail;
import amata1219.amachat.mail.MailManager;
import amata1219.amachat.processor.Coloring;
import amata1219.amachat.processor.GoogleIME;
import amata1219.amachat.processor.GoogleTranslate;
import amata1219.amachat.processor.NGWordMask;
import amata1219.amachat.processor.PlaceHolder;
import amata1219.amachat.user.UserManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class Amachat extends Plugin implements Listener {

	private static Amachat plugin;

	public static final float VERSION = 1.0F;
	public static final File DIRECTORY = new File(BungeeCord.getInstance().getPluginsFolder() + File.separator + "Amachat");
	private HashMap<String, Command> commands = new HashMap<>();
	private Config config;

	@Override
	public void onEnable() {
		plugin = this;

		if(!DIRECTORY.exists())
			DIRECTORY.mkdirs();

		commands.put("amachat", new AmachatCommand("amachat", "amachat.command.amachat", "amacha"));
		commands.put("chat", new ChatCommand("chat", "amachat.command.chat", "c"));
		commands.put("broadcast", new BroadcastCommand("broadcast", "amachat.command.broadcast", "bcast", "bc"));
		commands.put("message", new MessageCommand("message", "amachat.command.message", "msg"));
		commands.values().stream().forEach(Amachat::registerCommand);

		config = Config.load(new File(getDataFolder() + File.separator + "config.yml"), "config.yml");

		Coloring.load();
		GoogleIME.load();
		GoogleTranslate.load();
		PlaceHolder.load();
		NGWordMask.load();

		VanillaChat.load();
		ChatManager.load(ChannelChat.class);
		ChatManager.load(PermissionChannelChat.class);

		BotManager.load(ChatBot.class);
		BotManager.load(AutoMessageBot.class);
		BotManager.load(ActionBot.class);

		UserManager.load();
		PrivateChat.load();
		MailManager.load();
		Mail.load();

		getProxy().getPluginManager().registerListener(this, this);
	}

	@Override
	public void onDisable() {
	}

	public static Amachat getPlugin() {
		return plugin;
	}

	public static Config getConfig(){
		return plugin.config;
	}

	@EventHandler
	public void onReceived(PluginMessageEvent e){
		String tag = e.getTag();
		if(!tag.equals("BungeeCord") && !tag.equals("bungeecord:main"))
			return;

		ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
		if(!in.readUTF().equals("Amachat"))
			return;

		String channel = in.readUTF();

		/*
			PlayerDeath - -2095361037

			PlayerAdvancementDone - 2016805697

			Dynmap - 2061913187

			DiscordSRV - -1767718581
		 */

		switch(channel.hashCode()){
		case -2095361037:

			break;
		case 2016805697:

			break;
		case 2061913187:

			break;
		case -1767718581:

			break;
		default:
			return;
		}
	}

	@EventHandler
	public void onComplete(TabCompleteEvent e){
		Connection connection = e.getSender();
		if(!(connection instanceof ProxiedPlayer))
			return;

		List<String> suggestions = e.getSuggestions();
		if(suggestions.isEmpty())
			return;

		Command command = commands.get(suggestions.get(0).toLowerCase());
		if(command != null)
			command.complete((CommandSender) connection, Util.toArguments(suggestions));
	}

	public static void registerCommand(Command command){
		plugin.commands.put(command.getName(), command);
		plugin.getProxy().getPluginManager().registerCommand(plugin, command);
	}

	public static void unregisterCommand(String commandName){
		plugin.getProxy().getPluginManager().unregisterCommand(plugin.commands.get(commandName));
		plugin.commands.remove(commandName);
	}

	@SuppressWarnings("deprecation")
	public static void chat(final ProxiedPlayer sender, String message){
		if(sender == null || message == null)
			return;

		UserManager.getUser(sender.getUniqueId()).ifPresent(user -> Amachat.getPlugin().getExecutorService().execute(() -> user.getDestination().chat(user, message)));
	}

	public static void info(String message){
		plugin.getLogger().info(message);
	}

	public static void quietInfo(String message){
		info(ChatColor.GRAY + message);
	}
}

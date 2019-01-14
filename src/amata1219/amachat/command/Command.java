package amata1219.amachat.command;

import net.md_5.bungee.api.CommandSender;

public abstract class Command extends net.md_5.bungee.api.plugin.Command {

	public Command(String name, String permission, String[] aliases) {
		super(name, permission, aliases);
	}

	public abstract void complete(CommandSender sender, String[] args);

}

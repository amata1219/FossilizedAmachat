package amata1219.amachat.command;

import net.md_5.bungee.api.CommandSender;

public interface Command {

	void execute(CommandSender sender, String[] args);

	void complete(CommandSender sender, String[] args);

}

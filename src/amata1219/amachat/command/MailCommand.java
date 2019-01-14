package amata1219.amachat.command;

import net.md_5.bungee.api.CommandSender;

public class MailCommand extends Command {

	/*
	 * mail
	 *   read
	 *   send [playerName] [text]
	 *   clear
	 */

	public MailCommand(String name, String permission, String... aliases) {
		super(name, permission, aliases);
	}

	@Override
	public void complete(CommandSender sender, String[] args) {
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
	}


}

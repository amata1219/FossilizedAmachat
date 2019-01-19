package amata1219.amachat.command;

import amata1219.amachat.Util;
import amata1219.amachat.user.User;
import net.md_5.bungee.api.CommandSender;

public class BroadcastCommand extends Command {

	/*
	 * broadcast
	 *   [message]
	 */

	public BroadcastCommand(String name, String permission, String... aliases) {
		super(name, permission, aliases);
	}

	@Override
	public void complete(CommandSender sender, String[] args) {
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!Util.isProxiedPlayer(sender))
			return;

		User user = Util.toUser(sender);
		if(args.length == 0)
			user.warn("メッセージを指定して下さい");
		else
			user.getDestination().broadcast(String.join(" ", args));
	}
}

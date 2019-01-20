package amata1219.amachat.command;

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
		User user = Command.isUser(sender);
		if(user == null)
			return;

		if(args.length == 0){
			user.warn("メッセージを入力して下さい。");
			return;
		}

		user.getDestination().broadcast(Arguments.newInstance(args).concatenateArguments(0, args.length - 1));
	}
}

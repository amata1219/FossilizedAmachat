package amata1219.amachat.command;

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
		Command.isUser(sender).ifPresent(user -> {
			Arguments arguments = Arguments.newInstance(args);
			if(!arguments.hasArgument(0)){
				user.warn("メッセージを入力して下さい。");
				return;
			}

			user.getDestination().broadcast(arguments.concatenateArguments(0, arguments.getLastIndex()));
		});
	}
}

package amata1219.amachat.command;

import net.md_5.bungee.api.CommandSender;

public class AmachatCommand extends Command {

	/*
	 * amachat
	 *   reload [id]
	 *   join [id]
	 *   move [id]
	 *   leave [id]
	 *   info [id]
	 *   mute [id]
	 *   see
	 *
	 */

	/*
	 * switch(Command.get(args, 0)){
		case "info":

		case "join":

		case "leave":

		case "see":

		case "move":

		case "mute":
		}
	 */

	public AmachatCommand(String name, String permission, String... aliases) {
		super(name, permission, aliases);
	}

	@Override
	public void complete(CommandSender sender, String[] args) {
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Cmd cmd = Cmd.newInstance(args);
		switch(cmd.get(0)){
		case "test":
			if(cmd.isNumber(1)){

			}
			break;
		case "info":
			break;
		}
	}

}

package amata1219.amachat.command;

import java.util.UUID;

import amata1219.amachat.Util.TextBuilder;
import amata1219.amachat.chat.Chat;
import amata1219.amachat.chat.ChatManager;
import amata1219.amachat.chat.VanillaChat;
import amata1219.amachat.user.User;
import amata1219.amachat.user.UserManager;
import net.md_5.bungee.api.ChatColor;
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
	 *   chat
	 *
	 */

	/*
	 * 	info - 3237038

		join - 3267882

		leave - 102846135

		chat - 3052376

		move - 3357649

		mute - 3363353

	 */

	public AmachatCommand(String name, String permission, String... aliases) {
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

		Arguments arguments = new Arguments(args);
		Chat chat = arguments.getChat(1);
		if(chat == null)
			chat = user.getDestination();

		switch(arguments.getHashCodeOfArgument(0)){
		case 3237038://info
			TextBuilder builder = TextBuilder.newInstanace()
			.append("§b")
			.append(chat.getAliases())
			.append("§7(ID: ")
			.append(chat.getId())
			.append(")")
			.newLine()
			.append("§7")
			.append(chat.getDescription())
			.newLine()
			.append("§7参加者:");
			for(UUID uuid : chat.getUsers()){
				builder.append(" ");
				builder.append(UserManager.getPlayerName(uuid), builder.getLastLineLength() > 50);
			}
			user.sendMessage(builder.getString());
			break;
		case 3267882://join
			user.sendMessage(chat.tryJoin(user.getUniqueId()));
			break;
		case 102846135://leave
			user.sendMessage(chat.tryLeave(user.getUniqueId()));
			break;
		case 3052376://chat
			chat.chat(user, arguments.concatenateArguments(arguments.isChat(1) ? 2 : 1, arguments.args.length - 1));
			break;
		case 3357649://move

		case 3363353://mute
		}
	}

}

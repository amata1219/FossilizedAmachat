package amata1219.amachat.command;

import java.util.UUID;

import amata1219.amachat.Util.TextBuilder;
import amata1219.amachat.chat.Chat;
import amata1219.amachat.user.User;
import amata1219.amachat.user.UserManager;
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
	 *   chat
	 *   list
	 */

	/*
	 * 	info - 3237038

		join - 3267882

		leave - 102846135

		chat - 3052376

		move - 3357649

		mute - 3363353

		list - 3322014

	 */

	public static void main(String[] args){
		hash("");
		System.out.close();
	}

	public static void hash(String s){
		System.out.println(s.hashCode());
		System.out.flush();
	}

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
		int hashCode = arguments.getHashCodeOfArgument(0);
		if(!user.hasPermission(hashCode)){
			user.warn("このコマンドの実行は許可されていません。");
			return;
		}

		Chat chat = null;
		switch(hashCode){
		case 0:

			break;
		case 3237038://info
			chat = arguments.getChat(1, user);
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
				builder.append(UserManager.getPlayerName(uuid), builder.getLastLineLength() > 40);
			}
			user.sendMessage(builder.getString());
			break;
		case 3267882://join
			if((chat = arguments.getChat(1)) != null)
				user.sendMessage(chat.tryJoin(user.getUniqueId()));
			break;
		case 102846135://leave
			if((chat = arguments.getChat(1)) != null)
				user.sendMessage(chat.tryLeave(user.getUniqueId()));
			break;
		case 3052376://chat
			chat = arguments.getChat(1);
			(chat == null ? user.getDestination() : chat).chat(user, arguments.concatenateArguments(chat == null ? 1 : 2, arguments.args.length - 1));
			break;
		case 3357649://move
			chat = arguments.getChat(1);
			if(chat == null){
				user.warn("移動先のチャットを指定して下さい。");
				break;
			}
			String from = user.getDestination().getAliases();
			user.setDestination(chat);
			user.success("チャットを移動しました§7(" + from + " > " + chat.getAliases() + ")§b。");
			break;
		case 3363353://mute
			chat = arguments.getChat(1, user);
			if(user.getMutedChat().contains(chat)){
				user.addMutedChat(chat);
				user.success("チャットをミュートを解除しました§7(" + chat.getAliases() + ")§b。");
			}else{
				user.removeMutedChat(chat);
				user.success("チャットをミュートしました§7(" + chat.getAliases() + ")§b。");
			}
			break;
		}
	}

}

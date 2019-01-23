package amata1219.amachat.command;

import java.util.UUID;

import amata1219.amachat.Util.TextBuilder;
import amata1219.amachat.chat.Chat;
import amata1219.amachat.chat.ChatManager;
import amata1219.amachat.user.User;
import amata1219.amachat.user.UserManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class ChatCommand extends Command {

	/*
	 * 	info - 3237038, i - 105

		join - 3267882, j - 106

		leave - 102846135

		chat - 3052376

		move - 3357649, m - 109

		pmute - 106797705, p - 112

		cmute - 94791932, c - 99

		list - 3322014, l - 108

	 */

	public static void main(String[] args){
		hash("PlayerDeath");
		hash("PlayerAdvancementDone");
		hash("Dynmap");
		hash("DiscordSRV");
		System.out.close();
	}

	public static void hash(String s){
		System.out.println(s + " - " + s.hashCode());
		System.out.flush();
	}

	public ChatCommand(String name, String permission, String... aliases) {
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
		case 105:
		case 3237038:
			chat = arguments.getChat(1, user);
			TextBuilder info = TextBuilder.newInstanace()
			.append(ChatColor.BLUE)
			.append(chat.getAliases())
			.append(ChatColor.GRAY)
			.append("(ID: ")
			.append(chat.getId())
			.append(")")
			.newLine()
			.append(ChatColor.GRAY)
			.append(chat.getDescription())
			.newLine()
			.append(ChatColor.GRAY)
			.append("参加者:");
			for(UUID uuid : chat.getUsers()){
				info.append(" ");
				info.append(UserManager.getPlayerName(uuid), info.getLastLineLength() > 40);
			}
			user.sendMessage(info.getString());
			break;
		case 106:
		case 3267882:
			if((chat = arguments.getChat(1)) != null)
				user.sendMessage(chat.tryJoin(user.getUniqueId()));
			break;
		case 102846135:
			if((chat = arguments.getChat(1)) != null)
				user.sendMessage(chat.tryLeave(user.getUniqueId()));
			break;
		case 3052376:
			chat = arguments.getChat(1, user);
			chat.chat(user, arguments.concatenateArguments(chat == null ? 1 : 2, arguments.getLastIndex()));
			break;
		case 109:
		case 3357649:
			chat = ChatManager.getChat(arguments.getArgument(1));
			if(chat == null){
				user.warn("移動先のチャットを指定して下さい。");
				break;
			}
			String from = user.getDestination().getAliases();
			user.setDestination(chat);
			user.success(from + "から" + chat.getAliases() + "に移動しました。");
			break;
		case 112:
		case 106797705:
			String playerName = arguments.getArgument(1);
			if(!UserManager.isExist(playerName)){
				user.warn("指定されたプレイヤーは存在しません。");
				break;
			}

			UUID playerUUID = UserManager.getUniqueId(playerName);
			if(user.isMutedUser(playerUUID)){
				user.removeMutedUser(playerUUID);
				user.success(playerName + "さんのミュートを解除しました。");
			}else{
				user.addMutedUser(playerUUID);
				user.success(playerName + "さんをミュートしました。");
			}
		case 99:
		case 94791932:
			chat = arguments.getChat(1, user);
			if(user.getMutedChat().contains(chat)){
				user.removeMutedChat(chat);
				user.success(chat.getAliases() + "のミュートを解除しました。");
			}else{
				user.addMutedChat(chat);
				user.success(chat.getAliases() + "をミュートしました。");
			}
			break;
		case 108:
		case 3322014:
			TextBuilder list = TextBuilder.newInstanace()
			.append(ChatColor.BLUE)
			.append("チャット一覧");
			boolean b = false;
			for(Chat ch : ChatManager.getChats()){
				if(!ch.isHide()){
					list.newLine()
					.append(b ? ChatColor.RESET : ChatColor.GRAY)
					.append("・")
					.append(ch.getAliases())
					.append(" - ")
					.append(ch.getDescription());
					b = !b;
				}
			}
			user.sendMessage(list.getString());
			break;
		default:
			break;
		}
	}

}


package amata1219.amachat.command;

import java.util.Optional;
import java.util.UUID;

import amata1219.amachat.Util.TextBuilder;
import amata1219.amachat.chat.Chat;
import amata1219.amachat.chat.ChatManager;
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
		Command.isUser(sender).ifPresent(user -> {
			Arguments arguments = new Arguments(args);
			int hashCode = arguments.getHashCodeOfArgument(0);
			if(!user.hasPermission(hashCode)){
				user.warn("このコマンドの実行は許可されていません。");
				return;
			}

			switch(hashCode){
			case 0:

				break;
			case 105:
			case 3237038:
				Chat chat1 = arguments.getChat(1, user);
				TextBuilder info = TextBuilder.newInstanace()
				.append(ChatColor.BLUE)
				.append(chat1.getAliases())
				.append(ChatColor.GRAY)
				.append("(ID: ")
				.append(chat1.getId())
				.append(")")
				.newLine()
				.append(ChatColor.GRAY)
				.append(chat1.getDescription())
				.newLine()
				.append(ChatColor.GRAY)
				.append("参加者:");
				for(UUID uuid : chat1.getUsers()){
					info.append(" ");
					info.append(UserManager.getPlayerName(uuid), info.getLastLineLength() > 40);
				}
				user.sendMessage(info.getString());
				break;
			case 106:
			case 3267882:
				arguments.getChat(1).ifPresent(chat -> user.sendMessage(chat.tryJoin(user.getUniqueId())));
				break;
			case 102846135:
				arguments.getChat(1).ifPresent(chat -> user.sendMessage(chat.tryLeave(user.getUniqueId())));
				break;
			case 3052376:
				Chat chat2 = arguments.getChat(1, user);
				chat2.chat(user, arguments.concatenateArguments(chat2 == null ? 1 : 2, arguments.getLastIndex()));
				break;
			case 109:
			case 3357649:
				Optional<Chat> chat3 = ChatManager.matchedChat(arguments.getArgument(1));
				if(!chat3.isPresent()){
					user.warn("指定されたチャットは存在しません。");
					break;
				}

				Chat chat4 = chat3.get();
				String from = user.getDestination().getAliases();
				user.setDestination(chat4);
				user.success(from + "から" + chat4.getAliases() + "に移動しました。");
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
				Chat chat5 = arguments.getChat(1, user);
				if(user.getMutedChat().contains(chat5)){
					user.removeMutedChat(chat5);
					user.success(chat5.getAliases() + "のミュートを解除しました。");
				}else{
					user.addMutedChat(chat5);
					user.success(chat5.getAliases() + "をミュートしました。");
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
		});


	}

}


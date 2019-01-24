package amata1219.amachat.command;

import amata1219.amachat.chat.PrivateChat;
import amata1219.amachat.processor.ProcessorManager;
import amata1219.amachat.user.UserManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class MessageCommand extends Command {

	public MessageCommand(String name, String permission, String... aliases) {
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
				user.warn("プレイヤー名を指定して下さい。");
				return;
			}

			if(!arguments.hasArgument(1)){
				user.warn("本文を記述して下さい。");
				return;
			}

			String receiverName = arguments.getArgument(0);
			if(!UserManager.isOnline(receiverName)){
				user.warn("指定されたプレイヤーは存在しません。");
				return;
			}

			TextComponent component = ProcessorManager.processAll(PrivateChat.getInstance(), UserManager.getPlayerName(user.getUniqueId()), receiverName, arguments.concatenateArguments(1, arguments.getLastIndex()));
			user.sendMessage(component);
			UserManager.getUser(receiverName).ifPresent(receiver -> receiver.sendMessage(component));
		});

	}

}

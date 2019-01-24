package amata1219.amachat.command;

import java.util.Optional;

import com.google.common.collect.ImmutableMap;

import amata1219.amachat.Util;
import amata1219.amachat.Util.TextBuilder;
import amata1219.amachat.chat.Chat;
import amata1219.amachat.chat.ChatManager;
import amata1219.amachat.user.User;
import amata1219.amachat.user.UserManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public abstract class Command extends net.md_5.bungee.api.plugin.Command {

	public static final ImmutableMap<Integer, String> PERMISSIONS = new ImmutableMap.Builder<Integer, String>()
			.put(3237038, "amachat.amachat.info")
			.put(105, "amachat.amachat.info")
			.put(3267882, "amachat.amachat.join")
			.put(106, "amachat.amachat.join")
			.put(102846135, "amachat.amachat.leave")
			.put(3052376, "amachat.amachat.chat")
			.put(3357649, "amachat.amachat.move")
			.put(109, "amachat.amachat.move")
			.put(106797705, "amachat.amachat.pmute")
			.put(112, "amachat.amachat.pmute")
			.put(94791932, "amachat.amachat.cmute")
			.put(99, "amachat.amachat.cmute")
			.put(3322014, "amachat.amachat.list")
			.put(108, "amachat.amachat.list")
			.build();

	public Command(String name, String permission, String[] aliases) {
		super(name, permission, aliases);
	}

	public abstract void complete(CommandSender sender, String[] args);

	public static Optional<User> isUser(CommandSender sender){
		if(sender instanceof ProxiedPlayer)
			return UserManager.getUser(((ProxiedPlayer) sender).getUniqueId());

		sender.sendMessage(Util.toTextComponent(ChatColor.RED + "ゲーム内から実行して下さい。"));
		return Optional.empty();
	}

	public static class Arguments {

		public final String[] args;

		public Arguments(String[] args){
			this.args = args;
		}

		public static Arguments newInstance(String[] args){
			return new Arguments(args);
		}

		public int getLastIndex(){
			return args.length - 1;
		}

		public boolean hasArgument(int index){
			return index < args.length;
		}

		public String getArgument(int index){
			if(!hasArgument(index))
				return "";

			return args[index];
		}

		public String concatenateArguments(int from, int to){
			if(from > to)
				return "";

			TextBuilder builder = TextBuilder.newInstanace();
			for(; from <= (to >= args.length ? args.length : to); from++)
				builder.append(" ").append(getArgument(from));

			return builder.getString().trim();
		}

		public int getHashCodeOfArgument(int index){
			return getArgument(index).hashCode();
		}

		public Optional<Long> getNumberResult(int index){
			String argument = getArgument(index);
			for(int i = 0; i < argument.length(); i++){
				if(!Character.isDigit(argument.charAt(i)))
					return Optional.empty();
			}
			return Optional.ofNullable(Long.valueOf(argument));
		}

		public Optional<Chat> getChat(int index){
			return ChatManager.matchedChat(getArgument(index));
		}

		public Chat getChat(int index, User user){
			Optional<Chat> chat = getChat(index);
			return chat.isPresent() ? chat.get() : user.getDestination();
		}

	}

}

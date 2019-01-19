package amata1219.amachat.command;

import com.google.common.collect.ImmutableMap;

import amata1219.amachat.Util;
import amata1219.amachat.chat.Chat;
import amata1219.amachat.chat.ChatManager;
import amata1219.amachat.user.User;
import amata1219.amachat.user.UserManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public abstract class Command extends net.md_5.bungee.api.plugin.Command {

	public static final ImmutableMap<Integer, String> PERMISSIONS = new ImmutableMap.Builder<Integer, String>()
			.put(3237038, "info")
			.build();

	public Command(String name, String permission, String[] aliases) {
		super(name, permission, aliases);
	}

	public abstract void complete(CommandSender sender, String[] args);

	public static User isUser(CommandSender sender){
		if(sender instanceof ProxiedPlayer)
			return UserManager.getUser(((ProxiedPlayer) sender).getUniqueId());

		sender.sendMessage(Util.toTextComponent(ChatColor.RED + "ゲーム内から実行して下さい。"));
		return null;
	}

	public static class Arguments {

		public final String[] args;

		public Arguments(String[] args){
			this.args = args;
		}

		public static Arguments newInstance(String[] args){
			return new Arguments(args);
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
			String s = "";
			if(from > to)
				return s;

			for(; from <= to; from++)
				s = getArgument(from);
			return s;
		}

		public int getHashCodeOfArgument(int index){
			return getArgument(index).hashCode();
		}

		public Result<Long> getNumberResult(int index){
			Long result;
			try{
				result = Long.valueOf(getArgument(index));
			}catch(NumberFormatException e){
				return new Result<Long>(Long.valueOf(-1), true);
			}
			return new Result<Long>(result);
		}

		public Chat getChat(int index){
			Result<Long> result = getNumberResult(index);
			if(result.isInvalid())
				return null;

			return ChatManager.getChat(result.getResult());
		}

		public Chat getChat(int index, User user){
			Chat chat = getChat(index);
			return chat == null ? user.getDestination() : chat;
		}

	}

	public static class Result<T> {

		public final T result;
		public final boolean invalid;

		public Result(T result){
			this.result = result;
			this.invalid = false;
		}

		public Result(T result, boolean invalid){
			this.result = result;
			this.invalid = invalid;
		}

		public T getResult(){
			return result;
		}

		public boolean isInvalid(){
			return invalid;
		}

	}

}

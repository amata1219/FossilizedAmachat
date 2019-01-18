package amata1219.amachat.command;

import net.md_5.bungee.api.CommandSender;

public abstract class Command extends net.md_5.bungee.api.plugin.Command {

	public Command(String name, String permission, String[] aliases) {
		super(name, permission, aliases);
	}

	public abstract void complete(CommandSender sender, String[] args);

	public static class Cmd {

		public String[] args;

		public Cmd(String[] args){
			this.args = args;
		}

		public static Cmd newInstance(String[] args){
			return new Cmd(args);
		}

		public String get(int index){
			if(args.length <= index)
				return "";

			return args[index];
		}

		public boolean isNumber(int index){
			try{
				Integer.valueOf(get(index));
			}catch(Exception e){
				return false;
			}
			return true;
		}
	}

}

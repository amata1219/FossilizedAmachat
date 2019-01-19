package amata1219.amachat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import amata1219.amachat.user.User;
import amata1219.amachat.user.UserManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Util {

	public static TextComponent toTextComponent(String message){
		if(message == null)
			return null;

		return new TextComponent(message);
	}

	public static TextComponent emptyTextComponent(){
		return toTextComponent("");
	}

	public static <T> Set<T> listToSet(List<T> list){
		return new HashSet<T>(list);
	}

	public static String getArgument(String[] args, int index){
		if(args.length <= index)
			return "";
		else
			return args[index];
	}

	public static Set<String> toStringSet(Set<UUID> uuids){
		Set<String> set = new HashSet<>();
		uuids.forEach(uuid -> set.add(uuid.toString()));
		return set;
	}

	public static boolean isYamlConfiguration(String fileName){
		return fileName.endsWith(".yml");
	}

	public static long getId(String fileName){
		return Long.valueOf(fileName.substring(0, fileName.length() - 4)).longValue();
	}

	public static String[] toArguments(String... args){
		return args;
	}

	public static String[] toArguments(List<String> suggestions){
		suggestions.remove(0);

		int size = suggestions.size();
		String[] args = new String[size];
		for(int i = 0; i < size; i++)
			args[i] = suggestions.get(i);

		return args;
	}

	public static boolean isProxiedPlayer(CommandSender sender){
		boolean b = sender instanceof ProxiedPlayer;
		if(!b)
			sender.sendMessage(toTextComponent(ChatColor.RED + "ゲーム内から実行して下さい。"));
		return b;
	}

	public static User toUser(CommandSender sender){
		return UserManager.getUser(((ProxiedPlayer) sender).getUniqueId());
	}

	public static class TextBuilder {

		public static final int MAX_LENGTH = Integer.MAX_VALUE - 8;

		public char[] characters;
		private int count;

		private TextBuilder(int capacity){
			characters = new char[capacity];
		}

		public static TextBuilder newInstanace(){
			return new TextBuilder(16);
		}

		public TextBuilder append(String s){
			int length = s.length();
			ensureCapacity(count + length);
			s.getChars(0, s.length(), characters, count);
			count += length;
			return this;
		}

		public TextBuilder append(String s, boolean b){
			if(b)
				newLine();

			return append(s);
		}

		public TextBuilder appent(int i){
			return append(String.valueOf(i));
		}

		public TextBuilder append(long l){
			return append(String.valueOf(l));
		}

		public TextBuilder newLine(){
			return append("\n");
		}

		public void ensureCapacity(int capacity){
			if(capacity - characters.length > 0)
				characters = Arrays.copyOf(characters, newCapacity(capacity));
		}

		public int newCapacity(int capacity){
			int newCapacity = (characters.length << 1) + 2;
			if(newCapacity - capacity < 0)
				newCapacity = capacity;
			if(newCapacity < 0 || MAX_LENGTH - newCapacity > 0)
				newCapacity = MAX_LENGTH;
			return newCapacity;
		}

		public String getString(){
			return new String(characters);
		}

		public int count(){
			return count;
		}

		public int startPointOfLastLine(){
			int i = 1;
			for(; i < count(); i++);{
				if(characters[i] == '\n')
					return i + 1;
			}
			return -1;
		}

		public int getLastLineLength(){
			return count - startPointOfLastLine() - 1;
		}

		public void send(User user){
			user.sendMessage(getString());
		}

	}

}

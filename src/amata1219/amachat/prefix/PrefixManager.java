package amata1219.amachat.prefix;

import java.util.HashSet;
import java.util.Set;

import amata1219.amachat.chat.Chat;

public class PrefixManager {

	private static PrefixManager instance;

	private final Set<Prefix> prefixes = new HashSet<>();

	private PrefixManager(){

	}

	public static void load(){
		PrefixManager manager = new PrefixManager();

		instance = manager;
	}

	public static PrefixManager getInstance(){
		return instance;
	}

	public Set<Prefix> getPrefixes(){
		return prefixes;
	}

	public void addPrefix(Prefix prefix){
		if(!(prefix instanceof Chat))
			throw new UnsupportedOperationException("must implements amata1219.amachat.chat.Chat");

		prefixes.add(prefix);
	}

	public void removePrefix(Prefix prefix){
		prefixes.remove(prefix);
	}

	public static Chat matchChat(String message){
		int length = message.length();

		for(Prefix prefix : instance.prefixes){
			String s = prefix.getPrefix();
			if(length > s.length() && message.startsWith(s))
				return (Chat) prefix;
		}

		return null;
	}

}

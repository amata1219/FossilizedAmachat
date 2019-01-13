package amata1219.amachat.prefix;

import java.util.HashSet;
import java.util.Set;

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
		prefixes.add(prefix);
	}

	public void removePrefix(Prefix prefix){
		prefixes.remove(prefix);
	}

	public static Prefix matchChat(String message){
		int length = message.length();

		for(Prefix prefix : instance.prefixes){
			String s = prefix.getPrefix();
			if(length > s.length() && message.startsWith(s))
				return prefix;
		}

		return null;
	}

}

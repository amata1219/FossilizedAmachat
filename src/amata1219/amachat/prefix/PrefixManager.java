package amata1219.amachat.prefix;

import java.util.HashSet;
import java.util.Set;

public class PrefixManager {

	private static final Set<Prefix> PREFIXES = new HashSet<>();

	private PrefixManager(){

	}

	public static Set<Prefix> getPrefixes(){
		return PREFIXES;
	}

	public static void addPrefix(Prefix prefix){
		PREFIXES.add(prefix);
	}

	public static void removePrefix(Prefix prefix){
		PREFIXES.remove(prefix);
	}

	public static Prefix matchChat(String message){
		int length = message.length();

		for(Prefix prefix : PREFIXES){
			String s = prefix.getPrefix();
			if(length > s.length() && message.startsWith(s))
				return prefix;
		}

		return null;
	}

}

package amata1219.amachat.prefix;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PrefixManager {

	private static final Map<Long, Prefix> PREFIXES = new HashMap<>();

	private PrefixManager(){

	}

	public static Collection<Prefix> getPrefixes(){
		return PREFIXES.values();
	}

	public static void registerPrefix(Prefix prefix){
		PREFIXES.put(prefix.getId(), prefix);
	}

	public static void unregisterPrefix(long id){
		PREFIXES.remove(id);
	}

	public static Optional<Prefix> matchChat(String message){
		return PREFIXES.values().parallelStream().filter(prefix -> message.startsWith(prefix.getPrefix())).findFirst();
	}

	public static String removePrefix(Prefix prefix, String message){
		return message.substring(prefix.getPrefix().length());
	}

}

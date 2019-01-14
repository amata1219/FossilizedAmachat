package amata1219.amachat.bot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BotManager {

	private static final Map<Long, Bot> BOT_MAP = new HashMap<>();

	private BotManager(){

	}

	public static Bot getBot(long id){
		return BOT_MAP.get(id);
	}

	public static Collection<Bot> getBots(){
		return BOT_MAP.values();
	}

	public static void registerBot(long id, Bot chat){
		BOT_MAP.put(id, chat);
	}

	public static void registerBot(long id){
		BOT_MAP.remove(id);
	}
}

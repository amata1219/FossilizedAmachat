package amata1219.amachat.bot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BotManager {

	private static BotManager instance;

	private static final Map<String, Bot> REGISTRY = new HashMap<>();
	private final Map<Long, Bot> map = new HashMap<>();

	private BotManager(){

	}

	public static void load(){
		BotManager manager = new BotManager();

		instance = manager;
	}

	public static BotManager getInstance(){
		return instance;
	}

	public static void register(Bot bot){
		REGISTRY.put(bot.getName(), bot);
	}

	public static void unregister(String botName){
		REGISTRY.remove(botName);
	}

	public static Bot get(String chatName){
		return REGISTRY.get(chatName);
	}

	public Bot getBot(long id){
		return map.get(id);
	}

	public Collection<Bot> getBots(){
		return map.values();
	}

	public void addBot(long id, Bot chat){
		map.put(id, chat);
	}

	public void removeBot(long id){
		map.remove(id);
	}
}

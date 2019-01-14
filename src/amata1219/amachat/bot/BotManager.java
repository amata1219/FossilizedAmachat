package amata1219.amachat.bot;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import amata1219.amachat.Util;

public class BotManager {

	private static final Map<Long, Bot> BOT_MAP = new HashMap<>();

	private BotManager(){

	}

	public static void load(Class<?> clazz){
		Method load = null;
		Method listFiles = null;
		Field DIRECTORY = null;

		try {
			load = clazz.getMethod("load", long.class);
			listFiles = File.class.getMethod("listFiles");
			DIRECTORY = clazz.getField("DIRECTORY");
			DIRECTORY.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		try {
			for(File file : (File[]) listFiles.invoke(DIRECTORY)){
				String fileName = file.getName();
				if(!Util.isYamlConfiguration(fileName))
					continue;

				long id = Util.getId(fileName);
				registerBot(id, (Bot) load.invoke(null, id));
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
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

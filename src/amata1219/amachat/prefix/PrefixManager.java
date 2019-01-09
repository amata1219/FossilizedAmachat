package amata1219.amachat.prefix;

import java.util.HashMap;

import amata1219.amachat.chat.Chat;

public class PrefixManager {

	private static PrefixManager instance;

	private final HashMap<String, Prefix> map = new HashMap<>();

	private PrefixManager(){

	}

	public static void load(){
		PrefixManager manager = new PrefixManager();

		instance = manager;
	}

	public static PrefixManager getInstance(){
		return instance;
	}

	public static Chat matchChat(String message){

	}

}

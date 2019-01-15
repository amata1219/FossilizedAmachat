package amata1219.amachat.processor;

import net.md_5.bungee.api.ChatColor;

public final class Coloring implements Processor {

	public static final String NAME = "Coloring";

	@Override
	public String getName() {
		return Coloring.NAME;
	}

	@Override
	public String process(String text) {
		return Coloring.coloring(text);
	}

	public static String coloring(String text){
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public static String inverse(String text){
		char[] characters = text.toCharArray();
		for(int i = 0; i < characters.length - 1; ++i){
			if(characters[i] != '§' || "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(characters[(i + 1)]) == -1)
				continue;

			characters[i] = 38;
			characters[(i + 1)] = Character.toLowerCase(characters[(i + 1)]);
		}

		return new String(characters);
	}

}

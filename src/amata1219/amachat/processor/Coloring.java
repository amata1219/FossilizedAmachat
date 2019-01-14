package amata1219.amachat.processor;

import net.md_5.bungee.api.ChatColor;

public class Coloring implements Processor {

	public static final String NAME = "Coloring";

	@Override
	public String getName() {
		return Coloring.NAME;
	}

	@Override
	public String process(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public static String inverse(String text){
		char[] b = text.toCharArray();
		for (int i = 0; i < b.length - 1; ++i) {
			if ((b[i] != '§') || ("0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[(i + 1)]) <= -1))
				continue;
			b[i] = 38;
			b[(i + 1)] = Character.toLowerCase(b[(i + 1)]);
		}

		return new String(b);
	}

}

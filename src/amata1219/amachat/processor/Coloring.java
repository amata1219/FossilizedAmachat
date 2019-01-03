package amata1219.amachat.processor;

import net.md_5.bungee.api.ChatColor;

public class Coloring implements Processor {

	public static final String NAME = "Coloring";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String process(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

}

package amata1219.amachat.processor;

import java.util.HashMap;
import java.util.Map;

import amata1219.amachat.Amachat;
import net.md_5.bungee.config.Configuration;

public final class PlaceHolder implements Processor {

	public static final String NAME = "PlaceHolder";

	private Map<String, String> holders = new HashMap<>();

	private PlaceHolder(){

	}

	public static void load(){
		Configuration configuration = Amachat.getConfig().getConfiguration().getSection("PlaceHolder");
		if(!configuration.getBoolean("Enable"))
			return;

		PlaceHolder processor = new PlaceHolder();

		configuration.getStringList("Holders").forEach(holder -> {
			String[] strs = holder.split("[:]");
			processor.holders.put(strs[0], strs[1]);
		});

		ProcessorManager.register(processor);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String process(String text) {
		for(String key : holders.keySet())
			text = text.replace(key, holders.get(key));
		return text;
	}

}

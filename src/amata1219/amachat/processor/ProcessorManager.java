package amata1219.amachat.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import amata1219.amachat.bungee.Config;
import amata1219.amachat.bungee.Player;

public class ProcessorManager {

	private static ProcessorManager instance;

	private static final Map<String, Processor> REGISTRY = new HashMap<>();

	private ProcessorManager(){

	}

	public static void load(){
		ProcessorManager manager = new ProcessorManager();

		instance = manager;
	}

	public static ProcessorManager getInstance(){
		return instance;
	}

	public static void register(Processor processor){
		REGISTRY.put(processor.getName(), processor);
	}

	public static void unregister(String processorName){
		REGISTRY.remove(processorName);
	}

	public static Processor get(String processorName){
		return REGISTRY.get(processorName);
	}

	public static Set<Processor> get(List<String> processorNames){
		return REGISTRY.values().stream().filter(processor -> processorNames.contains(processor.getName())).collect(Collectors.toSet());
	}

	public static Set<Processor> get(Set<String> processorNames){
		return REGISTRY.values().stream().filter(processor -> processorNames.contains(processor.getName())).collect(Collectors.toSet());
	}

	public static String processAll(Player player, String text, Map<FormatType, String> formats, Set<String> processorNames){
		String message = text;
		FormatType type = FormatType.NORMAL;
		if(processorNames.contains(GoogleIME.NAME) && GoogleIME.canConvert(text))
			type = FormatType.JAPANIZED;
		else if(processorNames.contains(GoogleTranslate.NAME) && GoogleTranslate.canTranslate(text))
			type = FormatType.TRANSLATION;

		for(Processor processor : get(processorNames))
			message = processor.process(message);

		String format = formats.get(type).replace("[player]", player.getName());
		switch(type){
		case NORMAL:
			return format.replace("[message]", message);
		case JAPANIZED:
			return format.replace("[message]", text).replace("[japanized]", message);
		case TRANSLATION:
			return format.replace("[message]", text).replace("[translation]", message);
		default:
			return "";
		}
	}

}

package amata1219.amachat.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import amata1219.amachat.user.User;
import amata1219.amachat.user.UserManager;

public class ProcessorManager {

	private static final Map<String, Processor> REGISTRY = new HashMap<>();

	private ProcessorManager(){

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

	public static String process(User speaker, String text, Map<FormatType, String> formats, Set<String> processorNames){
		return process(UserManager.getPlayerName(speaker.getUniqueId()), text, formats, processorNames);
	}

	public static String process(String playerName, String text, Map<FormatType, String> formats, Set<String> processorNames){
		String message = text;
		if(processorNames.contains(Coloring.NAME))
			message = get(Coloring.NAME).process(message);

		FormatType type = FormatType.NORMAL;
		if(processorNames.contains(GoogleIME.NAME) && GoogleIME.canJapanize(text))
			type = FormatType.JAPANIZED;
		else if(processorNames.contains(GoogleTranslate.NAME) && GoogleTranslate.canTranslate(text))
			type = FormatType.TRANSLATION;

		for(Processor processor : get(processorNames))
			message = processor.process(message);

		String format = formats.get(type).replace("[player]", playerName);
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

	public static String process(String text, Set<String> processorNames){
		String message = text;
		for(Processor processor : get(processorNames.stream().filter(processorName -> !processorName.equals(GoogleIME.NAME) && ! processorName.equals(GoogleTranslate.NAME)).collect(Collectors.toSet())))
			message = processor.process(message);
		return message;
	}

}

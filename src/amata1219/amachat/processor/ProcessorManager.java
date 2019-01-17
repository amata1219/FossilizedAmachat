package amata1219.amachat.processor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.collect.ImmutableMap;

import amata1219.amachat.Util;
import amata1219.amachat.chat.Chat;
import amata1219.amachat.chat.Chat.MessageFormatType;
import amata1219.amachat.user.User;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class ProcessorManager {

	private static final Map<String, Processor> PROCESSORS = new HashMap<>();
	private static final Map<String, Filter> FORE_FILTERS = new HashMap<>();
	private static final Map<String, Filter> AFT_FILTERS = new HashMap<>();
	private final static Map<String, Filter> DEFAULT_FILTERS = ImmutableMap.of();
	static{
		DEFAULT_FILTERS.put(Coloring.NAME, new Filter(){

			@Override
			public boolean shouldFilter(Chat chat, User user, String text) {
				return chat.getProcessors().contains(Coloring.NAME);
			}

			@Override
			public String process(Chat chat, User user, String text) {
				return getProcessor(Coloring.NAME).process(text);
			}

		});

		DEFAULT_FILTERS.put(GoogleIME.NAME, new Filter(){

			@Override
			public boolean shouldFilter(Chat chat, User user, String text) {
				if(user == null)
					return false;

				return user.isAutoJapanize() && GoogleIME.canJapanize(text) && chat.getProcessors().contains(getProcessor(GoogleIME.NAME));
			}

			@Override
			public String process(Chat chat, User user, String text) {
				return chat.getMessageFormat(MessageFormatType.JAPANIZE).replace(PlaceHolders.CONVERTED, getProcessor(GoogleIME.NAME).process(text));
			}

		});

		DEFAULT_FILTERS.put(GoogleTranslate.NAME, new Filter(){

			@Override
			public boolean shouldFilter(Chat chat, User user, String text) {
				return GoogleTranslate.canTranslate(text) && chat.getProcessors().contains(getProcessor(GoogleTranslate.NAME));
			}

			@Override
			public String process(Chat chat, User user, String text) {
				return chat.getMessageFormat(MessageFormatType.TRANSLATE).replace(PlaceHolders.CONVERTED, getProcessor(GoogleTranslate.NAME).process(text));
			}

		});
	}

	private ProcessorManager(){

	}

	public static void registerProcessor(Processor processor){
		PROCESSORS.put(processor.getName(), processor);
	}

	public static void unregisterProcessor(String processorName){
		PROCESSORS.remove(processorName);
	}

	public static Processor getProcessor(String processorName){
		return PROCESSORS.get(processorName);
	}

	public static void registerForeFilter(String filterName, Filter filter){
		FORE_FILTERS.put(filterName, filter);
	}

	public static void unregisterForeFilter(String filterName){
		FORE_FILTERS.remove(filterName);
	}

	public static Filter getForeFilter(String filterName){
		return FORE_FILTERS.get(filterName);
	}

	public static void registerAftFilter(String filterName, Filter filter){
		AFT_FILTERS.put(filterName, filter);
	}

	public static void unregisterAftFilter(String filterName){
		AFT_FILTERS.remove(filterName);
	}

	public static Filter getAftFilter(String filterName){
		return AFT_FILTERS.get(filterName);
	}

	public static String processAll(Chat chat, User user, String original){
		AtomicReference<String> text = new AtomicReference<>(original);

		Filter filter = DEFAULT_FILTERS.get(Coloring.NAME);
		if(filter.shouldFilter(chat, user, text.get()))
			text.set(filter.process(chat, user, text.get()));

		FORE_FILTERS.values().forEach(foreFilter -> {
			if(foreFilter.shouldFilter(chat, user, text.get()))
				text.set(foreFilter.process(chat, user, text.get()));
		});

		Set<Processor> processors = chat.getProcessors();
		processors.stream()
		.filter(processor -> !DEFAULT_FILTERS.containsKey(processor.getName()) && !FORE_FILTERS.containsKey(processor.getName()) && AFT_FILTERS.containsKey(processor.getName()))
		.forEach(processor -> text.set(processor.process(text.get())));

		AFT_FILTERS.values().forEach(aftFilter -> {
			if(aftFilter.shouldFilter(chat, user, text.get()))
				text.set(aftFilter.process(chat, user, text.get()));
		});

		filter = DEFAULT_FILTERS.get(GoogleIME.NAME);
		if((filter = DEFAULT_FILTERS.get(GoogleIME.NAME)).shouldFilter(chat, user, text.get()))
			text.set(filter.process(chat, user, text.get()).replace(PlaceHolders.ORIGINAL, original));
		else if((filter = DEFAULT_FILTERS.get(GoogleTranslate.NAME)).shouldFilter(chat, user, text.get()))
			text.set(filter.process(chat, user, text.get()).replace(PlaceHolders.ORIGINAL, original));
		else
			text.set(chat.getMessageFormat(MessageFormatType.NORMAL).replace(PlaceHolders.CONVERTED, text.get()));

		return applyFormat(chat, user, text.get());
	}

	public static String processAll(Chat chat, String original){
		AtomicReference<String> text = new AtomicReference<>(original);

		Filter filter = DEFAULT_FILTERS.get(Coloring.NAME);
		if(filter.shouldFilter(chat, null, text.get()))
			text.set(filter.process(chat, null, text.get()));

		FORE_FILTERS.values().forEach(foreFilter -> {
			if(foreFilter.shouldFilter(chat, null, text.get()))
				text.set(foreFilter.process(chat, null, text.get()));
		});

		chat.getProcessors().stream()
		.filter(processor -> !DEFAULT_FILTERS.containsKey(processor.getName()) && !FORE_FILTERS.containsKey(processor.getName()) && AFT_FILTERS.containsKey(processor.getName()))
		.forEach(processor -> text.set(processor.process(text.get())));

		AFT_FILTERS.values().forEach(aftFilter -> {
			if(aftFilter.shouldFilter(chat, null, text.get()))
				text.set(aftFilter.process(chat, null, text.get()));
		});

		filter = DEFAULT_FILTERS.get(GoogleIME.NAME);
		if((filter = DEFAULT_FILTERS.get(GoogleIME.NAME)).shouldFilter(chat, null, text.get()))
			text.set(getProcessor(GoogleIME.NAME).process(text.get()));
		else if((filter = DEFAULT_FILTERS.get(GoogleTranslate.NAME)).shouldFilter(chat, null, text.get()))
			text.set(getProcessor(GoogleTranslate.NAME).process(text.get()));

		return text.get();
	}

	public static String applyFormat(Chat chat, User user, String text){
		String[] args0 = chat.getFormat().split(PlaceHolders.CHAT);
		TextComponent message = null;
		if(args0.length <= 1){

		}else{
			for(int i = 0; i < args0.length; i++){
				String[] args1 = args0[i].split(PlaceHolders.PLAYER);
				if(args1.length <= 1)
					continue;

				TextComponent tc0 = Util.toTextComponent(args1[0]);
				for(int j = 1; j < args1.length; j++){
					TextComponent tc1 = Util.toTextComponent(user.getName());
					tc1.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/message " + user.getName()));
					tc1.addExtra(args1[j]);
					tc0.addExtra(tc1);
				}
				/*TextComponent aliases = Util.toTextComponent(chat.getAliases());
				aliases.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/amachat move " + chat.getId()));
				message.addExtra(aliases);
				message.addExtra(args[i]);*/
			}
		}
		format.replaceAll(PlaceHolders.MESSAGE, text);
		return chat.getFormat().replace(PlaceHolders.CHAT, chat.getAliases()).replace(PlaceHolders.PLAYER, user.getName());
	}

	public static Set<Processor> fromProcessorNames(Collection<String> processorNames){
		Set<Processor> processors = new HashSet<>();
		processorNames.forEach(processorName -> processors.add(getProcessor(processorName)));
		return processors;
	}

	public static Set<String> toProcessorNames(Collection<Processor> processors){
		Set<String> processorNames = new HashSet<>();
		processors.forEach(processor -> processorNames.add(processor.getName()));
		return processorNames;
	}

	public static class PlaceHolders {

		public static final String CHAT = "[chat]";
		public static final String PLAYER = "[player]";
		public static final String MESSAGE = "[message]";
		public static final String ORIGINAL = "[original]";
		public static final String CONVERTED = "[converted]";

	}

	public static interface Filter {

		boolean shouldFilter(Chat chat, User user, String text);

		String process(Chat chat, User user, String text);

	}

}

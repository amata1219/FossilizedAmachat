package amata1219.amachat.processor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import amata1219.amachat.chat.Chat;
import amata1219.amachat.chat.Chat.MessageFormatType;
import amata1219.amachat.processor.Processor.Filter;
import amata1219.amachat.user.User;

public class ProcessorManager {

	private static final Map<String, Processor> PROCESSORS = new HashMap<>();

	private ProcessorManager(){

	}

	public static void register(Processor processor){
		PROCESSORS.put(processor.getName(), processor);
	}

	public static void unregister(Processor processor){
		PROCESSORS.remove(processor.getName());
	}

	public static Processor get(String processorName){
		return PROCESSORS.get(processorName);
	}

	private final static Set<String> FILTER = ImmutableSet.of(GoogleIME.NAME, GoogleTranslate.NAME);
	private final static Map<String, Filter> FILTERS = ImmutableMap.of();
	private final static Filter COLORING_FILTER = new Filter(){

		@Override
		public boolean shouldFilter(Chat chat, User user, String text) {
			return chat.getProcessors().contains(Coloring.NAME);
		}

		@Override
		public String process(Chat chat, User user, String text) {
			return get(Coloring.NAME).process(text);
		}

	};

	public static String processAll(Chat chat, User user, String original){
		AtomicReference<String> text = new AtomicReference<>(original);

		if(COLORING_FILTER.shouldFilter(chat, user, text.get()))
			text.set(COLORING_FILTER.process(chat, user, text.get()));

		Set<Processor> processors = chat.getProcessors();
		processors.stream()
		.filter(processor -> !FILTER.contains(processor.getName()))
		.forEach(processor -> text.set(processor.process(text.get())));

		Processor proc = null;
		String message = null;
		if(user.isAutoJapanize() && GoogleIME.canJapanize(text.get()) && processors.contains(proc = get(GoogleIME.NAME)))
			message = chat.getMessageFormat(MessageFormatType.JAPANIZE).replace(PlaceHolders.ORIGINAL, original).replace(PlaceHolders.CONVERTED, proc.process(text.get()));
		else if(GoogleTranslate.canTranslate(text.get()) && processors.contains(proc = get(GoogleTranslate.NAME)))
			message = chat.getMessageFormat(MessageFormatType.TRANSLATE).replace(PlaceHolders.ORIGINAL, original).replace(PlaceHolders.CONVERTED, proc.process(text.get()));
		else
			message = chat.getMessageFormat(MessageFormatType.NORMAL).replace(PlaceHolders.ORIGINAL, original);

		return applyFormat(chat, user, message);
	}

	public static String processAll(Chat chat, User user, String original, Set<String> filter){
		AtomicReference<String> text = new AtomicReference<>(original);

		Set<Processor> processors = chat.getProcessors();
		Processor proc = null;
		if(processors.contains(proc = get(Coloring.NAME)))
			text.set(proc.process(text.get()));

		processors.stream()
		.filter(processor -> !FILTER.contains(processor.getName()))
		.forEach(processor -> text.set(processor.process(text.get())));

		String message = null;
		if(user.isAutoJapanize() && GoogleIME.canJapanize(text.get()) && processors.contains(proc = get(GoogleIME.NAME)))
			message = chat.getMessageFormat(MessageFormatType.JAPANIZE).replace(PlaceHolders.ORIGINAL, original).replace(PlaceHolders.CONVERTED, proc.process(text.get()));
		else if(GoogleTranslate.canTranslate(text.get()) && processors.contains(proc = get(GoogleTranslate.NAME)))
			message = chat.getMessageFormat(MessageFormatType.TRANSLATE).replace(PlaceHolders.ORIGINAL, original).replace(PlaceHolders.CONVERTED, proc.process(text.get()));
		else
			message = chat.getMessageFormat(MessageFormatType.NORMAL).replace(PlaceHolders.ORIGINAL, original);

		return applyFormat(chat, user, message);
	}

	public static String processAll(Chat chat, String original){
		AtomicReference<String> text = new AtomicReference<>(original);

		Set<Processor> processors = chat.getProcessors();
		Processor proc = null;
		if(processors.contains(proc = get(Coloring.NAME)))
			text.set(proc.process(text.get()));

		processors.stream()
		.filter(processor -> !FILTER.contains(processor.getName()))
		.forEach(processor -> text.set(processor.process(text.get())));

		if(GoogleIME.canJapanize(text.get()) && processors.contains(proc = get(GoogleIME.NAME)))
			text.set(proc.process(text.get()));
		else if(GoogleTranslate.canTranslate(text.get()) && processors.contains(proc = get(GoogleTranslate.NAME)))
			text.set(proc.process(text.get()));

		return text.get();
	}

	public static String applyFormat(Chat chat, User user, String message){
		return chat.getFormat().replace(PlaceHolders.CHAT, chat.getAliases()).replace(PlaceHolders.PLAYER, user.getName()).replace(PlaceHolders.MESSAGE, message);
	}

	public static Set<Processor> fromProcessorNames(Collection<String> processorNames){
		Set<Processor> processors = new HashSet<>();
		processorNames.forEach(processorName -> processors.add(get(processorName)));
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

}

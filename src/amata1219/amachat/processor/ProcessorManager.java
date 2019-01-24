package amata1219.amachat.processor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

import amata1219.amachat.Util;
import amata1219.amachat.chat.Chat;
import amata1219.amachat.processor.filter.ColoringFilter;
import amata1219.amachat.processor.filter.Filter;
import amata1219.amachat.processor.filter.GoogleIMEFilter;
import amata1219.amachat.processor.filter.GoogleTranslateFilter;
import amata1219.amachat.user.User;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class ProcessorManager {

	private static final Map<String, Processor> PROCESSORS = new HashMap<>();
	private static final Map<String, Filter> FORE_FILTERS = new HashMap<>();
	private static final Map<String, Filter> AFT_FILTERS = new HashMap<>();
	private final static Map<String, Filter> DEFAULT_FILTERS = ImmutableMap.of(Coloring.NAME, new ColoringFilter()
			, GoogleIME.NAME, new GoogleIMEFilter(), GoogleTranslate.NAME, new GoogleTranslateFilter());

	private ProcessorManager(){

	}

	public static void registerProcessor(Processor processor){
		PROCESSORS.put(processor.getName(), processor);
	}

	public static void unregisterProcessor(String processorName){
		PROCESSORS.remove(processorName);
	}

	public static Optional<Processor> getProcessor(String processorName){
		return Optional.ofNullable(PROCESSORS.get(processorName));
	}

	public static void registerForeFilter(String filterName, Filter filter){
		FORE_FILTERS.put(filterName, filter);
	}

	public static void unregisterForeFilter(String filterName){
		FORE_FILTERS.remove(filterName);
	}

	public static Optional<Filter> getForeFilter(String filterName){
		return Optional.ofNullable(FORE_FILTERS.get(filterName));
	}

	public static void registerAftFilter(String filterName, Filter filter){
		AFT_FILTERS.put(filterName, filter);
	}

	public static void unregisterAftFilter(String filterName){
		AFT_FILTERS.remove(filterName);
	}

	public static Optional<Filter> getAftFilter(String filterName){
		return Optional.ofNullable(AFT_FILTERS.get(filterName));
	}

	public static TextComponent processAll(Chat chat, User user, String original){
		return processAll(chat, user.getName(), original);
	}

	public static TextComponent processAll(SupportTextProcessing supporter, String playerName, String original){
		AtomicReference<String> text = new AtomicReference<>(original);

		Filter coloring = DEFAULT_FILTERS.get(Coloring.NAME);
		if(coloring.should(supporter, text.get()))
			text.set(coloring.filter(supporter, text.get()));

		FORE_FILTERS.values().parallelStream()
		.filter(fore -> fore.should(supporter, text.get()))
		.forEach(fore -> fore.filter(supporter, text.get()));

		supporter.getProcessors().parallelStream()
		.filter(processor -> !DEFAULT_FILTERS.containsKey(processor.getName()))
		.filter(processor -> !FORE_FILTERS.containsKey(processor.getName()))
		.filter(processor -> !AFT_FILTERS.containsKey(processor.getName()))
		.forEach(processor -> text.set(processor.process(text.get())));

		AFT_FILTERS.values().parallelStream()
		.filter(fore -> fore.should(supporter, text.get()))
		.forEach(fore -> fore.filter(supporter, text.get()));

		Filter filter = null;
		if((filter = DEFAULT_FILTERS.get(GoogleIME.NAME)).should(supporter, text.get()))
			text.set(filter.filter(supporter, text.get()).replace(PlaceHolders.ORIGINAL, original));
		else if((filter = DEFAULT_FILTERS.get(GoogleTranslate.NAME)).should(supporter, text.get()))
			text.set(filter.filter(supporter, text.get()).replace(PlaceHolders.ORIGINAL, original));
		else
			text.set(filter.filter(supporter, text.get()).replace(PlaceHolders.ORIGINAL, original));

		return applyFormat(supporter, playerName, text.get());
	}

	public static TextComponent processAll(SupportTextProcessing supporter, String original){
		AtomicReference<String> text = new AtomicReference<>(original);

		Filter coloring = DEFAULT_FILTERS.get(Coloring.NAME);
		if(coloring.should(supporter, text.get()))
			text.set(coloring.filter(supporter, text.get()));

		FORE_FILTERS.values().parallelStream()
		.filter(fore -> fore.should(supporter, text.get()))
		.forEach(fore -> fore.filter(supporter, text.get()));

		supporter.getProcessors().parallelStream()
		.filter(processor -> !DEFAULT_FILTERS.containsKey(processor.getName()))
		.filter(processor -> !FORE_FILTERS.containsKey(processor.getName()))
		.filter(processor -> !AFT_FILTERS.containsKey(processor.getName()))
		.forEach(processor -> text.set(processor.process(text.get())));

		AFT_FILTERS.values().parallelStream()
		.filter(fore -> fore.should(supporter, text.get()))
		.forEach(fore -> fore.filter(supporter, text.get()));

		Filter filter = DEFAULT_FILTERS.get(GoogleIME.NAME);
		if(((GoogleIMEFilter) (filter = DEFAULT_FILTERS.get(GoogleIME.NAME))).should(supporter, text.get()))
			text.set(((GoogleIMEFilter) filter).filter(supporter, text.get()));
		else if((filter = DEFAULT_FILTERS.get(GoogleTranslate.NAME)).should(supporter, text.get()))
			text.set(filter.filter(supporter, text.get()));

		return Util.toTextComponent(text.get());
	}

	public static TextComponent processAll(SupportTextProcessing supporter, String senderName, String receiverName, String original){
		AtomicReference<String> text = new AtomicReference<>(original);

		Filter coloring = DEFAULT_FILTERS.get(Coloring.NAME);
		if(coloring.should(supporter, text.get()))
			text.set(coloring.filter(supporter, text.get()));

		FORE_FILTERS.values().parallelStream()
		.filter(fore -> fore.should(supporter, text.get()))
		.forEach(fore -> fore.filter(supporter, text.get()));

		supporter.getProcessors().parallelStream()
		.filter(processor -> !DEFAULT_FILTERS.containsKey(processor.getName()))
		.filter(processor -> !FORE_FILTERS.containsKey(processor.getName()))
		.filter(processor -> !AFT_FILTERS.containsKey(processor.getName()))
		.forEach(processor -> text.set(processor.process(text.get())));

		AFT_FILTERS.values().parallelStream()
		.filter(fore -> fore.should(supporter, text.get()))
		.forEach(fore -> fore.filter(supporter, text.get()));

		Filter filter = DEFAULT_FILTERS.get(GoogleIME.NAME);
		if(((GoogleIMEFilter) (filter = DEFAULT_FILTERS.get(GoogleIME.NAME))).should(supporter, text.get()))
			text.set(((GoogleIMEFilter) filter).filter(supporter, text.get()));
		else if((filter = DEFAULT_FILTERS.get(GoogleTranslate.NAME)).should(supporter, text.get()))
			text.set(filter.filter(supporter, text.get()));

		return applyFormat(supporter, senderName, receiverName, text.get());
	}

	public static TextComponent applyFormat(SupportTextProcessing supporter, String playerName, String text){
		String[] a = supporter.getFormat().replace("[message]", text).split(PlaceHolders.CHAT);
		if(a.length == 0)
			return Util.emptyTextComponent();

		TextComponent base = Util.emptyTextComponent();
		if(a.length == 1){
			String[] b = a[0].split(PlaceHolders.PLAYER);
			if(b.length == 0)
				return Util.emptyTextComponent();

			if(b.length == 1){
				base.addExtra(b[0]);
			}else{
				for(int i = 0; i < b.length - 1; i++){
					base.addExtra(b[i]);
					TextComponent add = Util.toTextComponent(playerName);
					add.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/message " + playerName));
					base.addExtra(add);
				}
				base.addExtra(b[b.length - 1]);
			}
		}else{
			for(int i = 0; i < a.length - 1; i++){
				String[] b = a[0].split(PlaceHolders.PLAYER);
				if(b.length == 0)
					return Util.emptyTextComponent();

				if(b.length == 1){
					base.addExtra(b[0]);
				}else{
					for(int j = 0; j < b.length - 1; j++){
						base.addExtra(b[i]);
						TextComponent add = Util.toTextComponent(playerName);
						add.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/message " + playerName));
						base.addExtra(add);
					}
					base.addExtra(b[b.length - 1]);
				}

				if(supporter instanceof Chat){
					Chat chat = (Chat) supporter;
					TextComponent add = Util.toTextComponent(chat.getAliases());
					add.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/amachat move " + chat.getId()));
					base.addExtra(add);
				}
			}
			base.addExtra(a[a.length - 1]);
		}
		return base;
	}

	public static TextComponent applyFormat(SupportTextProcessing supporter, String senderName, String receiverName, String text){
		String[] a = supporter.getFormat().replace("[message]", text).split(PlaceHolders.CHAT);
		if(a.length == 0)
			return Util.emptyTextComponent();

		TextComponent base = Util.emptyTextComponent();
		if(a.length == 1){
			String[] b = a[0].split(PlaceHolders.PLAYER);
			if(b.length == 0)
				return Util.emptyTextComponent();

			if(b.length == 1){
				base.addExtra(b[0]);
			}else{
				for(int i = 0; i < b.length - 1; i++){
					base.addExtra(b[i]);
					TextComponent add = Util.toTextComponent(senderName);
					add.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/message " + senderName));
					base.addExtra(add);
				}
				base.addExtra(b[b.length - 1]);
			}
		}else{
			for(int i = 0; i < a.length - 1; i++){
				String[] b = a[0].split(PlaceHolders.PLAYER);
				if(b.length == 0)
					return Util.emptyTextComponent();

				if(b.length == 1){
					base.addExtra(b[0]);
				}else{
					for(int j = 0; j < b.length - 1; j++){
						base.addExtra(b[i]);
						TextComponent add = Util.toTextComponent(senderName);
						add.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/message " + senderName));
						base.addExtra(add);
					}
					base.addExtra(b[b.length - 1]);
				}

				TextComponent add = Util.toTextComponent(receiverName);
				add.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/message " + receiverName));
				base.addExtra(add);
			}
			base.addExtra(a[a.length - 1]);
		}
		return base;
	}

	public static Set<Processor> fromProcessorNames(Collection<String> processorNames){
		return processorNames.parallelStream()
		.map(ProcessorManager::getProcessor)
		.filter(Optional::isPresent)
		.map(Optional::get)
		.collect(Collectors.toSet());
	}

	public static Set<String> toProcessorNames(Collection<Processor> processors){
		return processors.parallelStream()
				.map(Processor::getName)
				.collect(Collectors.toSet());
	}

	public static class PlaceHolders {

		public static final String CHAT = "[chat]";
		public static final String PLAYER = "[player]";
		public static final String SENDER = "[sender]";
		public static final String RECEIVER = "[receiver]";
		public static final String MESSAGE = "[message]";
		public static final String ORIGINAL = "[original]";
		public static final String CONVERTED = "[converted]";

	}

}

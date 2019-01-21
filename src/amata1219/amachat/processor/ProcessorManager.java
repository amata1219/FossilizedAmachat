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

	public static TextComponent processAll(Chat chat, User user, String original){
		return processAll(chat, user.getName(), original);
	}

	public static TextComponent processAll(SupportTextProcessing supporter, String playerName, String original){
		AtomicReference<String> text = new AtomicReference<>(original);

		Filter filter = DEFAULT_FILTERS.get(Coloring.NAME);
		if(filter.should(supporter, text.get()))
			text.set(filter.filter(supporter, text.get()));

		for(Filter foreFilter : FORE_FILTERS.values()){
			if(foreFilter.should(supporter, text.get()))
				text.set(foreFilter.filter(supporter, text.get()));
		}

		for(Processor processor : supporter.getProcessors()){
			if(!DEFAULT_FILTERS.containsKey(processor.getName()) && !FORE_FILTERS.containsKey(processor.getName()) && AFT_FILTERS.containsKey(processor.getName()))
				text.set(processor.process(text.get()));
		}

		for(Filter aftFilter : AFT_FILTERS.values()){
			if(aftFilter.should(supporter, text.get()))
				text.set(aftFilter.filter(supporter, text.get()));
		}

		filter = DEFAULT_FILTERS.get(GoogleIME.NAME);
		if((filter = DEFAULT_FILTERS.get(GoogleIME.NAME)).should(supporter, text.get()))
			text.set(filter.filter(supporter, text.get()).replace(PlaceHolders.ORIGINAL, original));
		else if((filter = DEFAULT_FILTERS.get(GoogleTranslate.NAME)).should(supporter, text.get()))
			text.set(filter.filter(supporter, text.get()).replace(PlaceHolders.ORIGINAL, original));
		else
			text.set(supporter.getMessageFormat(MessageFormatType.NORMAL).replace(PlaceHolders.CONVERTED, text.get()));

		return applyFormat(supporter, playerName, text.get());
	}

	public static TextComponent processAll(SupportTextProcessing supporter, String original){
		AtomicReference<String> text = new AtomicReference<>(original);

		Filter filter = DEFAULT_FILTERS.get(Coloring.NAME);
		if(filter.should(supporter, text.get()))
			text.set(filter.filter(supporter, text.get()));

		for(Filter foreFilter : FORE_FILTERS.values()){
			if(foreFilter.should(supporter, text.get()))
				text.set(foreFilter.filter(supporter, text.get()));
		}

		for(Processor processor : supporter.getProcessors()){
			if(!DEFAULT_FILTERS.containsKey(processor.getName()) && !FORE_FILTERS.containsKey(processor.getName()) && AFT_FILTERS.containsKey(processor.getName()))
				text.set(processor.process(text.get()));
		}

		for(Filter aftFilter : AFT_FILTERS.values()){
			if(aftFilter.should(supporter, text.get()))
				text.set(aftFilter.filter(supporter, text.get()));
		}

		filter = DEFAULT_FILTERS.get(GoogleIME.NAME);
		if(((GoogleIMEFilter) (filter = DEFAULT_FILTERS.get(GoogleIME.NAME))).should(supporter, text.get()))
			text.set(((GoogleIMEFilter) filter).filter(supporter, text.get()));
		else if((filter = DEFAULT_FILTERS.get(GoogleTranslate.NAME)).should(supporter, text.get()))
			text.set(filter.filter(supporter, text.get()));

		return Util.toTextComponent(text.get());
	}

	public static TextComponent processAll(SupportTextProcessing supporter, String senderName, String receiverName, String original){
		AtomicReference<String> text = new AtomicReference<>(original);

		Filter filter = DEFAULT_FILTERS.get(Coloring.NAME);
		if(filter.should(supporter, text.get()))
			text.set(filter.filter(supporter, text.get()));

		for(Filter foreFilter : FORE_FILTERS.values()){
			if(foreFilter.should(supporter, text.get()))
				text.set(foreFilter.filter(supporter, text.get()));
		}

		for(Processor processor : supporter.getProcessors()){
			if(!DEFAULT_FILTERS.containsKey(processor.getName()) && !FORE_FILTERS.containsKey(processor.getName()) && AFT_FILTERS.containsKey(processor.getName()))
				text.set(processor.process(text.get()));
		}

		for(Filter aftFilter : AFT_FILTERS.values()){
			if(aftFilter.should(supporter, text.get()))
				text.set(aftFilter.filter(supporter, text.get()));
		}

		filter = DEFAULT_FILTERS.get(GoogleIME.NAME);
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
		Set<Processor> processors = new HashSet<>();
		for(String processorName : processorNames){
			Processor processor = getProcessor(processorName);
			if(processor != null)
				processors.add(processor);
		}
		return processors;
	}

	public static Set<String> toProcessorNames(Collection<Processor> processors){
		Set<String> processorNames = new HashSet<>();
		for(Processor processor : processors){
			if(PROCESSORS.containsKey(processor.getName()))
				processorNames.add(processor.getName());
		}
		return processorNames;
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

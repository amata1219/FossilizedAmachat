package amata1219.amachat.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import amata1219.amachat.Amachat;
import amata1219.amachat.processor.Coloring;
import amata1219.amachat.processor.MessageFormatType;
import amata1219.amachat.processor.Processor;
import amata1219.amachat.processor.ProcessorManager;
import amata1219.amachat.processor.SupportTextProcessing;
import net.md_5.bungee.config.Configuration;

public class PrivateChat implements SupportTextProcessing {

	private static PrivateChat instance;

	private String format;
	private final Map<MessageFormatType, String> messageFormats = new HashMap<>();
	private Set<Processor> processors;

	private PrivateChat(){

	}

	public static void load(){
		PrivateChat chat = new PrivateChat();

		Configuration configuration = Amachat.getConfig().getConfiguration().getSection("PrivateChat");
		Map<MessageFormatType, String> messageFormats = chat.messageFormats;
		messageFormats.put(MessageFormatType.NORMAL, Coloring.coloring(configuration.getString("Format.Normal")));
		messageFormats.put(MessageFormatType.JAPANIZE, Coloring.coloring(configuration.getString("Format.Japanized")));
		messageFormats.put(MessageFormatType.TRANSLATE, Coloring.coloring(configuration.getString("Format.Translation")));
		chat.processors.addAll(ProcessorManager.fromProcessorNames(configuration.getStringList("Processors")));

		instance = chat;
	}

	public static PrivateChat getInstance(){
		return instance;
	}

	@Override
	public String getFormat() {
		return format;
	}

	@Override
	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	public Map<MessageFormatType, String> getMessageFormats() {
		return messageFormats;
	}

	@Override
	public String getMessageFormat(MessageFormatType type) {
		return messageFormats.get(type);
	}

	@Override
	public void setMessageFormat(MessageFormatType type, String format) {
		messageFormats.put(type, format);
	}

	@Override
	public Set<Processor> getProcessors() {
		return processors;
	}

	@Override
	public boolean hasProcessor(String processorName) {
		return processors.contains(ProcessorManager.getProcessor(processorName));
	}

	@Override
	public void addProcessor(Processor processor) {
		processors.add(processor);
	}

	@Override
	public void removeProcessor(String processorName) {
		processors.remove(ProcessorManager.getProcessor(processorName));
	}

}

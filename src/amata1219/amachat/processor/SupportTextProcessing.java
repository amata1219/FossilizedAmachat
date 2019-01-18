package amata1219.amachat.processor;

import java.util.Map;
import java.util.Set;

public interface SupportTextProcessing {

	String getFormat();

	void setFormat(String format);

	Map<MessageFormatType, String> getMessageFormats();

	String getMessageFormat(MessageFormatType type);

	void setMessageFormat(MessageFormatType type, String format);

	 Set<Processor> getProcessors();

	 boolean hasProcessor(String processorName);

	 void addProcessor(Processor processor);

	 void removeProcessor(String processorName);

}

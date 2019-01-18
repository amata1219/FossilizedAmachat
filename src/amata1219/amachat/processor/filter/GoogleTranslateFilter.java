package amata1219.amachat.processor.filter;

import amata1219.amachat.processor.GoogleTranslate;
import amata1219.amachat.processor.MessageFormatType;
import amata1219.amachat.processor.ProcessorManager.PlaceHolders;
import amata1219.amachat.processor.SupportTextProcessing;

public class GoogleTranslateFilter implements Filter {

	@Override
	public boolean should(SupportTextProcessing supporter, String text) {
		return supporter.hasProcessor(GoogleTranslate.NAME) && GoogleTranslate.canTranslate(text);
	}

	@Override
	public String filter(SupportTextProcessing supporter, String text) {
		return supporter.getMessageFormat(MessageFormatType.TRANSLATE).replace(PlaceHolders.CONVERTED, GoogleTranslate.translate(text));
	}

}

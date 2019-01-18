package amata1219.amachat.processor.filter;

import amata1219.amachat.processor.GoogleIME;
import amata1219.amachat.processor.MessageFormatType;
import amata1219.amachat.processor.SupportTextProcessing;
import amata1219.amachat.processor.ProcessorManager.PlaceHolders;
import amata1219.amachat.user.User;

public class GoogleIMEFilter implements SupportUserFilter {

	@Override
	public boolean should(SupportTextProcessing supporter, String text) {
		return supporter.hasProcessor(GoogleIME.NAME) && GoogleIME.canJapanize(text);
	}

	@Override
	public String filter(SupportTextProcessing supporter, String text) {
		return supporter.getMessageFormat(MessageFormatType.JAPANIZE).replace(PlaceHolders.CONVERTED, GoogleIME.japanize(text));
	}

	@Override
	public boolean should(SupportTextProcessing supporter, User user, String text) {
		return user.isAutoJapanize() && should(supporter, text);
	}

	@Override
	public String filter(SupportTextProcessing supporter, User user, String text) {
		return filter(supporter, text);
	}

}

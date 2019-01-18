package amata1219.amachat.processor.filter;

import amata1219.amachat.processor.SupportTextProcessing;
import amata1219.amachat.user.User;

public interface SupportUserFilter extends Filter {

	boolean should(SupportTextProcessing supporter, User user, String text);

	String filter(SupportTextProcessing supporter, User user, String text);

}

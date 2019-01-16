package amata1219.amachat.processor;

import amata1219.amachat.chat.Chat;
import amata1219.amachat.user.User;

public interface Processor {

	String getName();

	String process(String text);

	public static interface Filter {

		boolean shouldFilter(Chat chat, User user, String text);

		String process(Chat chat, User user, String text);

	}

}

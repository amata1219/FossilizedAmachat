package amata1219.amachat.prefix;

import amata1219.amachat.chat.Chat;

public interface Prefix extends Chat{

	boolean hasPrefix();

	String getPrefix();

	void setPrefix(String prefix);

}

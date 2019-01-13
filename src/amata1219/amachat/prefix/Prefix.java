package amata1219.amachat.prefix;

import amata1219.amachat.chat.Chat;

public abstract class Prefix extends Chat {

	protected String prefix;

	public boolean hasPrefix(){
		return prefix != null;
	}

	public String getPrefix(){
		return prefix;
	}

	public void setPrefix(String prefix){
		this.prefix = prefix;
	}

}

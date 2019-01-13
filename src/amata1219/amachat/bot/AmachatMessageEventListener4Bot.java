package amata1219.amachat.bot;

public interface AmachatMessageEventListener4Bot {

	void onChatMessageReceived(AmachatMessageEvent4Bot event);

	default void load(){
		AmachatMessageEvent4Bot.register(this);
	}

	default void unload(){
		AmachatMessageEvent4Bot.unregister(this);
	}

}

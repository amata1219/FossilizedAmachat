package amata1219.amachat.bot.event;

public interface Listener4Bot {

	void onChatMessageReceived(ChatEvent4Bot event);

	default void load(){
		ChatEvent4Bot.register(this);
	}

	default void unload(){
		ChatEvent4Bot.unregister(this);
	}

}

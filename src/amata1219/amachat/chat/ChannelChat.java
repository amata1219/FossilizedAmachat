package amata1219.amachat.chat;

import java.util.Set;
import java.util.UUID;

import amata1219.amachat.bungee.Player;

public class ChannelChat implements Id {

	public static final String NAME = "ChannelChat";

	private final long id;

	private Set<UUID> member;

	private ChannelChat(){
		id = System.nanoTime();
	}

	private ChannelChat(final long id){
		this.id = id;
	}

	public static ChannelChat newInstance(){
		ChannelChat chat = new ChannelChat();

		return chat;
	}

	@Override
	public String getName() {
		return ChannelChat.NAME;
	}

	@Override
	public void chat(Player player, String message) {
	}

	@Override
	public boolean equals(Chat chat) {
		return false;
	}

	@Override
	public long getId() {
		return 0;
	}

}

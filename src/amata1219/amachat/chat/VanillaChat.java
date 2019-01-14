package amata1219.amachat.chat;

import java.io.File;

import amata1219.amachat.config.Config;
import amata1219.amachat.config.Initializer;

public class VanillaChat extends Chat {

	public static final String NAME = "VanillaChat";
	public static final long ID = 0L;

	private VanillaChat(){

	}

	public static void load(){
		VanillaChat chat = new VanillaChat();

		(chat.config = Config.load(new File(Chat.DIRECTORY, "vanilla.yml"), "chat.yml", new Initializer(){

			@Override
			public void initialize(Config config) {
				config.getConfiguration().set("ID", ID);
				config.apply();
			}

		})).reload();

		ChatManager.registerChat(ID, chat);
	}

	@Override
	public String getName() {
		return VanillaChat.NAME;
	}

	@Override
	public long getId(){
		return VanillaChat.ID;
	}

}

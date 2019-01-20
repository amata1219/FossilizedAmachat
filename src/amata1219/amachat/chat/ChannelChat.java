package amata1219.amachat.chat;

import java.io.File;
import amata1219.amachat.config.Config;
import amata1219.amachat.config.Initializer;
import amata1219.amachat.prefix.Prefix;
import net.md_5.bungee.config.Configuration;

public class ChannelChat extends Prefix {

	public static final String NAME = "ChannelChat";
	public static final File DIRECTORY = new File(Chat.DIRECTORY + File.separator + NAME);

	protected ChannelChat(final long id){
		this.id = id;
	}

	public static ChannelChat load(long id){
		ChannelChat chat = new ChannelChat(id);

		(chat.config = Config.load(new File(DIRECTORY, String.valueOf(id) + ".yml"), "chat.yml", new Initializer(){

			@Override
			public void initialize(Config config) {
				config.getConfiguration().set("ID", id);
				config.apply();
			}

		})).reload();

		return chat;
	}

	@Override
	public String getName() {
		return ChannelChat.NAME;
	}

	@Override
	public void save(){
		if(config == null)
			return;

		super.save();

		Configuration configuration = config.getConfiguration();
		configuration.set("Prefix", prefix);
		config.apply();
	}

	@Override
	public void reload(){
		if(config == null)
			return;

		super.reload();

		config.reload();
		Configuration configuration = config.getConfiguration();
		prefix = configuration.getString("Prefix");
	}

}

package amata1219.amachat.chat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import amata1219.amachat.bungee.Amachat;
import amata1219.amachat.bungee.Config;
import amata1219.amachat.bungee.Initializer;
import amata1219.amachat.bungee.Player;
import amata1219.amachat.processor.Coloring;
import amata1219.amachat.processor.FormatType;
import amata1219.amachat.processor.Processor;
import amata1219.amachat.processor.ProcessorManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.config.Configuration;

public class ChannelChat implements Chat, Id {

	public static final String NAME = "Channel";
	public static final File DIRECTORY = new File(BungeeCord.getInstance().getPluginsFolder() + File.separator + "Channels");

	private final long id;
	private Config config;
	private Map<FormatType, String> formats = new HashMap<>();
	private Set<String> processors;
	private Set<UUID> players;
	private Set<UUID> muted;
	private Set<UUID> banned;

	private ChannelChat(final long id){
		this.id = id;
	}

	public static ChannelChat load(long id){
		ChannelChat chat = new ChannelChat(id);

		Config config = chat.config = Config.load(new File(DIRECTORY, String.valueOf(id) + ".yml"), "chat.yml", new Initializer(){

			@Override
			public void done(Config config) {
				Configuration conf = config.getConfig();

				conf.set("Version", Amachat.VERSION);

				config.apply();
			}

		});

		Configuration conf = config.getConfig();
		Map<FormatType, String> formats = chat.formats;
		Processor coloring = ProcessorManager.get(Coloring.NAME);
		formats.put(FormatType.NORMAL, coloring.process(conf.getString("Format.Normal")));
		formats.put(FormatType.JAPANIZED, coloring.process(conf.getString("Format.Japanized")));
		formats.put(FormatType.TRANSLATION, coloring.process(conf.getString("Format.Translation")));

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
	public boolean qualsType(Chat chat) {
		return false;
	}

	@Override
	public long getId() {
		return 0;
	}

	@Override
	public String getFormat(FormatType type) {
		return null;
	}

}

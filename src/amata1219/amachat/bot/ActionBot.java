package amata1219.amachat.bot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import amata1219.amachat.bot.event.ChatEvent4Bot;
import amata1219.amachat.bot.event.Listener4Bot;
import amata1219.amachat.config.Config;
import amata1219.amachat.config.Initializer;
import net.md_5.bungee.config.Configuration;

public class ActionBot extends Bot implements Listener4Bot {

	public static final String NAME = "ActionBot";
	public static final File DIRECTORY = new File(Bot.DIRECTORY + File.separator + "ActionBots");

	private Set<Action> actions = new HashSet<>();

	private ActionBot(long id){
		this.id = id;
	}

	public static ActionBot load(long id){
		ActionBot bot = new ActionBot(id);

		(bot.config = Config.load(new File(DIRECTORY, String.valueOf(id) + ".yml"), "bot.yml", new Initializer(){

			@Override
			public void initialize(Config config) {
				Configuration configuration = config.getConfiguration();
				configuration.set("ID", id);
				configuration.set("ChatList", Collections.emptySet());
				configuration.set("Actions", Collections.emptySet());
				config.apply();
			}

		})).reload();

		return bot;
	}

	@Override
	public String getName() {
		return ActionBot.NAME;
	}

	@Override
	public void save(){
		if(config == null)
			return;

		Configuration configuration = config.getConfiguration();

		configuration.set("ChatList", chatIds);
		Set<String> data = new HashSet<>();
		actions.forEach(action -> data.add(Action.encode(action)));
		configuration.set("Actions", data);

		config.apply();
	}

	@Override
	public void reload(){
		if(config == null)
			return;

		config.reload();

		Configuration configuration = config.getConfiguration();

		chatIds = config.getLongSet("ChatList");
		actions.clear();
		configuration.getStringList("Actions").forEach(action -> actions.add(Action.decode(action)));
	}

	public static interface Action extends Serializable {

		static String encode(Action action){
			try {
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
				ObjectOutputStream outputStream = new ObjectOutputStream(byteStream);
				outputStream.writeObject(action);
				outputStream.close();
				byteStream.close();
				return Base64.getEncoder().encodeToString(byteStream.toByteArray());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		static Action decode(String data){
			try {
				return (Action) new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(data))).readObject();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		void done(ChatEvent4Bot event);

	}

	@Override
	public void onChatMessageReceived(ChatEvent4Bot event) {
		if(event.isCancelled())
			return;

		if(!chatIds.contains(event.getChat().getId()))
			return;

		actions.forEach(action -> action.done(event));
	}

}

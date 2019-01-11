package amata1219.amachat.chat;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import amata1219.amachat.bot.AmachatMessageEvent4Bot;
import amata1219.amachat.bungee.Config;
import amata1219.amachat.bungee.Initializer;
import amata1219.amachat.bungee.Logger;
import amata1219.amachat.bungee.Player;
import amata1219.amachat.event.AmachatBroadcastEvent;
import amata1219.amachat.event.AmachatMessageEvent;
import amata1219.amachat.prefix.Prefix;
import amata1219.amachat.processor.Coloring;
import amata1219.amachat.processor.FormatType;
import amata1219.amachat.processor.Processor;
import amata1219.amachat.processor.ProcessorManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;

public class ChannelChat implements Chat, Id, Prefix {

	public static final String NAME = "ChannelChat";
	public static final File DIRECTORY = new File(BungeeCord.getInstance().getPluginsFolder() + File.separator + "Channels");

	private final long id;
	private Config config;
	private Map<FormatType, String> formats = new HashMap<>();
	private Set<String> processors;
	private Set<UUID> players;
	private Set<UUID> muted;
	private Set<UUID> banned;
	private String prefix;

	private ChannelChat(final long id){
		this.id = id;
	}

	public static ChannelChat load(long id){
		ChannelChat chat = new ChannelChat(id);

		Config config = chat.config = Config.load(new File(DIRECTORY, String.valueOf(id) + ".yml"), "chat.yml", new Initializer(){

			@Override
			public void done(Config config) {
				Configuration conf = config.getConfig();
				conf.set("BannedPlayers", Collections.emptyList());
				config.apply();
			}

		});

		Configuration conf = chat.config.getConfig();
		Map<FormatType, String> formats = chat.formats;
		Processor coloring = ProcessorManager.get(Coloring.NAME);
		formats.put(FormatType.NORMAL, coloring.process(conf.getString("Format.Normal")));
		formats.put(FormatType.JAPANIZED, coloring.process(conf.getString("Format.Japanized")));
		formats.put(FormatType.TRANSLATION, coloring.process(conf.getString("Format.Translation")));
		chat.processors = config.getStringSet("Processors");
		chat.players = config.getUniqueIdSet("Players");
		chat.muted = config.getUniqueIdSet("MutedPlayers");
		chat.banned = config.getUniqueIdSet("BannedPlayers");
		chat.prefix = conf.getString("Prefix");
		return chat;
	}

	@Override
	public void save(){
		Configuration conf = config.getConfig();
		conf.set("Processors", processors);
		conf.set("Players", players);
		conf.set("MutedPlayers", muted);
		conf.set("BannedPlayers", banned);
		conf.set("Prefix", prefix);
		config.apply();
	}

	@Override
	public void chat(Player player, String message) {
		if(muted.contains(player.getUniqueId())){
			Logger.grayInfo("Muted@" + message);
			return;
		}

		AmachatMessageEvent4Bot event4bot = AmachatMessageEvent4Bot.fire(this, player, message);
		if(event4bot.isCancelled()){
			Logger.grayInfo("Cancelled@" + event4bot.getMessage());
			return;
		}

		AmachatMessageEvent event = AmachatMessageEvent.call(this, player, message);
		if(event.isCancelled()){
			Logger.grayInfo("Cancelled@" + event.getMessage());
			return;
		}

		ChatManager.sendMessage(ProcessorManager.processAll(player, event.getMessage(), formats, processors), players);
	}

	@Override
	public void broadcast(String message){
		AmachatBroadcastEvent event = AmachatBroadcastEvent.call(this, message);
		if(event.isCancelled()){
			Logger.info(ChatColor.GRAY + "Cancelled@" + event.getMessage());
			return;
		}

		ChatManager.sendMessageAndLogging(event.getMessage(), players);
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public String getName() {
		return ChannelChat.NAME;
	}

	@Override
	public Config getConfig() {
		return config;
	}

	@Override
	public boolean canChat() {
		return config.getConfig().getBoolean("Chat");
	}

	@Override
	public void setChat(boolean chat) {
		config.getConfig().set("Chat", chat);
		config.apply();
	}

	@Override
	public String getFormat(FormatType type) {
		return formats.get(type);
	}

	@Override
	public void setFormat(FormatType type, String format) {
		formats.put(type, format);
	}

	@Override
	public Set<String> getProcessors() {
		return processors;
	}

	@Override
	public boolean hasProcessor(String processorName) {
		return processors.contains(processorName);
	}

	@Override
	public void addProcessor(String processorName) {
		processors.add(processorName);
	}

	@Override
	public void removeProcessor(String processorName) {
		processors.remove(processorName);
	}

	@Override
	public Set<UUID> getPlayers() {
		return players;
	}

	@Override
	public boolean isJoin(UUID uuid) {
		return players.contains(uuid);
	}

	@Override
	public void join(UUID uuid) {
		players.add(uuid);
	}

	@Override
	public void quit(UUID uuid) {
		players.remove(uuid);
	}

	@Override
	public void kick(UUID uuid, String reason) {
		quit(uuid);
		ChatManager.sendMessage(reason, uuid);
	}

	@Override
	public Set<UUID> getMutedPlayers() {
		return muted;
	}

	@Override
	public boolean isMuted(UUID uuid) {
		return muted.contains(uuid);
	}

	@Override
	public void mute(UUID uuid) {
		muted.add(uuid);
	}

	@Override
	public void unmute(UUID uuid) {
		muted.remove(uuid);
	}

	@Override
	public Set<UUID> getBannedPlayers() {
		return banned;
	}

	@Override
	public boolean isBanned(UUID uuid) {
		return banned.contains(uuid);
	}

	@Override
	public void ban(UUID uuid) {
		banned.add(uuid);
	}

	@Override
	public void unban(UUID uuid) {
		banned.remove(uuid);
	}

	@Override
	public boolean hasPrefix() {
		return prefix != null;
	}

	@Override
	public String getPrefix() {
		return prefix;
	}

	@Override
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}

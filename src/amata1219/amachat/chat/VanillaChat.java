package amata1219.amachat.chat;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import amata1219.amachat.Config;
import amata1219.amachat.Logger;
import amata1219.amachat.Util;
import amata1219.amachat.bot.AmachatMessageEvent4Bot;
import amata1219.amachat.event.AmachatBroadcastEvent;
import amata1219.amachat.event.AmachatMessageEvent;
import amata1219.amachat.player.Player;
import amata1219.amachat.prefix.PrefixManager;
import amata1219.amachat.processor.Coloring;
import amata1219.amachat.processor.FormatType;
import amata1219.amachat.processor.Processor;
import amata1219.amachat.processor.ProcessorManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;

public class VanillaChat implements Chat {

	public static final String NAME = "VanillaChat";
	public static final long ID = 0L;

	private Config config;
	private boolean chat;
	private HashMap<FormatType, String> formats = new HashMap<>();
	private Set<String> processors;
	private Set<UUID> players;
	private Set<UUID> muted;

	private VanillaChat(){

	}

	public static VanillaChat load(){
		VanillaChat chat = new VanillaChat();
		Config config = chat.config = Config.load(new File(Chat.DIRECTORY, "vanilla.yml"), "chat.yml");
		Configuration conf = chat.config.getConfig();
		chat.chat = conf.getBoolean("CanChat");
		Map<FormatType, String> formats = chat.formats;
		Processor coloring = ProcessorManager.get(Coloring.NAME);
		formats.put(FormatType.NORMAL, coloring.process(conf.getString("Format.Normal")));
		formats.put(FormatType.JAPANIZED, coloring.process(conf.getString("Format.Japanized")));
		formats.put(FormatType.TRANSLATION, coloring.process(conf.getString("Format.Translation")));
		chat.processors = new HashSet<>(config.getConfig().getStringList("Processors"));
		chat.players = config.getUniqueIdSet("Players");
		chat.muted = config.getUniqueIdSet("Muted");
		return chat;
	}

	@Override
	public void save(){
		Configuration conf = config.getConfig();
		conf.set("CanChat", chat);
		formats.forEach((k, v) -> {
			String section = k.name();
			conf.set(Character.toUpperCase(section.charAt(0)) + section.substring(1), v);
		});
		conf.set("Processors", processors);
		conf.set("Players", Util.toStringSet(players));
		conf.set("Muted", Util.toStringSet(muted));
		config.apply();
	}

	@Override
	public void chat(Player player, String message) {
		if(muted.contains(player.getUniqueId())){
			Logger.info(ChatColor.GRAY + "Muted@" + message);
			return;
		}

		Chat matched = PrefixManager.matchChat(message);
		if(matched != null && matched.isJoin(player.getUniqueId())){
			matched.chat(player, message);
			return;
		}

		AmachatMessageEvent4Bot event4bot = AmachatMessageEvent4Bot.fire(this, player, message);
		if(event4bot.isCancelled()){
			Logger.info(ChatColor.GRAY + "Cancelled@" + event4bot.getMessage());
			return;
		}

		AmachatMessageEvent event = AmachatMessageEvent.call(this, player, event4bot.getMessage());
		if(event.isCancelled()){
			Logger.info(ChatColor.GRAY + "Cancelled@" + event.getMessage());
			return;
		}

		ChatManager.sendMessageAndLog(ProcessorManager.processAll(player, event.getMessage(), formats, processors), players);
	}

	@Override
	public void broadcast(String message){
		AmachatBroadcastEvent event = AmachatBroadcastEvent.call(this, message);
		if(event.isCancelled()){
			Logger.info(ChatColor.GRAY + "Cancelled@" + event.getMessage());
			return;
		}

		ChatManager.sendMessageAndLog(event.getMessage(), players);
	}

	@Override
	public String getName() {
		return VanillaChat.NAME;
	}

	@Override
	public long getId() {
		return VanillaChat.ID;
	}

	@Override
	public Config getConfig() {
		return config;
	}

	@Override
	public boolean canChat() {
		return chat;
	}

	@Override
	public void setChat(boolean chat) {
		this.chat = chat;
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
		return Collections.emptySet();
	}

	@Override
	public boolean isBanned(UUID uuid) {
		return false;
	}

	@Override
	public void ban(UUID uuid) {
	}

	@Override
	public void unban(UUID uuid) {
	}

}

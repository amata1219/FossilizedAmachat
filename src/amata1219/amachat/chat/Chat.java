package amata1219.amachat.chat;

import java.util.Set;
import java.util.UUID;

import amata1219.amachat.bungee.Config;
import amata1219.amachat.bungee.Player;
import amata1219.amachat.processor.FormatType;

public interface Chat {

	String getName();

	void save();

	void chat(Player player, String message);

	void broadcast(String message);

	Config getConfig();

	boolean canChat();

	void setChat(boolean chat);

	String getFormat(FormatType type);

	void setFormat(FormatType type, String format);

	Set<String> getProcessors();

	boolean hasProcessor(String processorName);

	void addProcessor(String processorName);

	void removeProcessor(String processorName);

	Set<UUID> getPlayers();

	boolean isJoin(UUID uuid);

	void join(UUID uuid);

	void quit(UUID uuid);

	void kick(UUID uuid, String reason);

	Set<UUID> getMutedPlayers();

	boolean isMuted(UUID uuid);

	void mute(UUID uuid);

	void unmute(UUID uuid);

	Set<UUID> getBannedPlayers();

	boolean isBanned(UUID uuid);

	void ban(UUID uuid);

	void unban(UUID uuid);

}

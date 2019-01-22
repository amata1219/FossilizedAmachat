package amata1219.amachat.spigot;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class SpigotConfig {

	private FileConfiguration config = null;
	private final File configFile;
	private final String file;
	private final Plugin plugin;

	public SpigotConfig(Plugin plugin, String fileName) {
		this.plugin = plugin;
		this.file = fileName;
		configFile = new File(plugin.getDataFolder(), file);
	}

	public void saveDefaultConfig() {
		if (!configFile.exists()) {
			plugin.saveResource(file, false);
		}
	}

	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(configFile);

		final InputStream defConfigStream = plugin.getResource(file);
		if (defConfigStream == null) {
			return;
		}

		config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
	}

	public FileConfiguration getConfig() {
		if (config == null) {
			reloadConfig();
		}
		return config;
	}

}

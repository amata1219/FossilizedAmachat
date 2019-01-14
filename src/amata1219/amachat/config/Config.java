package amata1219.amachat.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import com.google.common.io.ByteStreams;

import amata1219.amachat.Amachat;
import amata1219.amachat.Util;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Config{

	private static final ConfigurationProvider PROVIDER = ConfigurationProvider.getProvider(YamlConfiguration.class);

	private Configuration configuration;
	private File file;

	private Config(){

	}

	public static Config load(File file, String resource){
		return load(file, null, resource, null, null);
	}

	public static Config load(File file, String resource, Initializer initializer){
		return load(file, null, resource, initializer, null);
	}

	public static Config load(File file, String resource, Initializer initializer, Updater updater){
		return load(file, null, resource, initializer, updater);
	}

	public static Config load(File file, Plugin plugin, String resource){
		return load(file, plugin, resource, null, null);
	}

	public static Config load(File file, Plugin plugin, String resource, Initializer initializer){
		return load(file, plugin, resource, initializer, null);
	}

	public static Config load(File file, Plugin plugin, String resource, Initializer initializer, Updater updater){
		Config config = new Config();

		config.file = file;

		if(initializer != null && config.saveDefault(plugin, resource)){
			Initializer.setVersion(config, false);
			initializer.initialize(config);
		}else if(updater != null && Updater.isOld(config)){
			Initializer.setVersion(config, false);
			updater.update(config);
		}

		return config;
	}

	public Configuration getConfiguration(){
		return configuration;
	}

	public boolean saveDefault(Plugin plugin, String resource){
		boolean flag = false;

		if(!file.exists()){
			try{
				Amachat.getPlugin().getDataFolder().mkdirs();

				file.createNewFile();
			}catch(IOException e){

			}

			try(FileOutputStream output = new FileOutputStream(file);
					InputStream input = plugin == null ? Amachat.getPlugin().getResourceAsStream(resource) : plugin.getResourceAsStream(resource)){

					ByteStreams.copy(input, output);
					flag = true;
			}catch(IOException e){
				e.printStackTrace();
			}
		}

		try{
            configuration = Config.PROVIDER.load(file);
		}catch(IOException e){
			e.printStackTrace();
		}

		return flag;
	}

	public void save(){
		try{
			Config.PROVIDER.save(configuration, file);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void reload(){
		try{
            configuration = Config.PROVIDER.load(file);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void apply(){
		save();
		reload();
	}

	public void set(String path, Set<UUID> uuids){
		configuration.set(path, Util.toStringSet(uuids));
	}

	public Set<String> getStringSet(String path){
		return Util.listToSet(configuration.getStringList(path));
	}

	public Set<Integer> getIntegerSet(String path){
		return Util.listToSet(configuration.getIntList(path));
	}

	public Set<Long> getLongSet(String path){
		return Util.listToSet(configuration.getLongList(path));
	}

	public UUID getUniqueId(String path){
		Object def = configuration.getDefault(path);
		return UUID.fromString(configuration.getString(path, (def instanceof String) ? (String) def : ""));
	}

	public Set<UUID> getUniqueIdSet(String path) {
		Set<?> set = new HashSet<>(configuration.getList(path));
		Set<UUID> result = new HashSet<>();

		for(Iterator<?> localIterator = set.iterator(); localIterator.hasNext();){
			Object object = localIterator.next();

			if(object instanceof String)
				result.add(UUID.fromString((String) object));
		}

		return result;
	}

}

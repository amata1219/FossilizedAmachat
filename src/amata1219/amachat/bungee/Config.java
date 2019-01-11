package amata1219.amachat.bungee;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import com.google.common.io.ByteStreams;

import amata1219.amachat.Util;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Config{

	private Configuration config;
	private File file;

	/*
	 * datafolder
	 * config.yml
	 * vanilla.yml
	 * room.yml
	 * - players
	 *   uuid.yml
	 * - channels
	 *   id.yml
	 */

	private Config(){

	}

	public static Config load(File path, String resource, Initializer initializer){
		Config config = new Config();

		config.file = path;

		if(config.saveDefault(resource))
			initializer.done(config);

		return config;
	}

	public Configuration getConfig(){
		return config;
	}

	public boolean saveDefault(String resource){
		boolean flag = false;

		if(!file.exists()){
			try{
				Amachat.getPlugin().getDataFolder().mkdirs();

				file.createNewFile();
			}catch(IOException e){

			}

			try(FileOutputStream output = new FileOutputStream(file);
					InputStream input = Amachat.getPlugin().getResourceAsStream(resource)){

					ByteStreams.copy(input, output);
					flag = true;
			}catch(IOException e){
				e.printStackTrace();
			}
		}

		try{
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		}catch(IOException e){
			e.printStackTrace();
		}

		return flag;
	}

	public void save(){
		try{
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void reload(){
		try{
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void apply(){
		save();
		reload();
	}

	public Set<String> getStringSet(String path){
		return Util.listToSet(config.getStringList(path));
	}

	public Set<Integer> getIntegerSet(String path){
		return Util.listToSet(config.getIntList(path));
	}

	public Set<Long> getLongSet(String path){
		return Util.listToSet(config.getLongList(path));
	}

	public UUID getUniqueId(String path){
		Object def = config.getDefault(path);
		return UUID.fromString(config.getString(path, (def instanceof String) ? (String) def : ""));
	}

	public Set<UUID> getUniqueIdSet(String path) {
		Set<?> set = new HashSet<>(config.getList(path));
		Set<UUID> result = new HashSet<>();

		for(Iterator<?> localIterator = set.iterator(); localIterator.hasNext();){
			Object object = localIterator.next();

			if(object instanceof String)
				result.add(UUID.fromString((String) object));
		}

		return result;
	}

}

package amata1219.amachat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.chat.TextComponent;

public class Util {

	public static TextComponent toTextComponent(String message){
		return new TextComponent(message);
	}

	public static byte[] toByteArray(Object... objs){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();

		for(Object obj : objs)
			out.writeUTF(obj.toString());

		return out.toByteArray();
	}

	public static <T> Set<T> listToSet(List<T> list){
		return new HashSet<T>(list);
	}

	public static String getArgument(String[] args, int index){
		if(args.length <= index)
			return "";
		else
			return args[index];
	}

	public static Set<String> toStringSet(Set<UUID> uuids){
		Set<String> set = new HashSet<>();
		uuids.forEach(uuid -> set.add(uuid.toString()));
		return set;
	}

	public static boolean isYamlConfiguration(String fileName){
		return fileName.endsWith(".yml");
	}

	public static long getId(String fileName){
		return Long.valueOf(fileName.substring(0, fileName.length() - 4)).longValue();
	}

	public static String[] toArgs(String... args){
		return args;
	}

}

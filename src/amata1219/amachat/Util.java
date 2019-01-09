package amata1219.amachat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

}

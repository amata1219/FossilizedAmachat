package amata1219.amachat;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class Util {

	public static byte[] toByteArray(Object... objs){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();

		for(Object obj : objs)
			out.writeUTF(obj.toString());

		return out.toByteArray();
	}

}

package amata1219.amachat;

import com.google.common.io.ByteArrayDataInput;

public class MessageChannel {

	private String message;

	private MessageChannel(){

	}

	public static MessageChannel newInstance(ByteArrayDataInput in){
		return new MessageChannel();
	}

	public void read(ByteArrayDataInput in){
		message = in.readUTF();
	}

	public String getMessage(){
		return message;
	}

}

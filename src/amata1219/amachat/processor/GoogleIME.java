package amata1219.amachat.processor;

public class GoogleIME implements Processor {

	public static final String NAME = "GoogleIME";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String process(String text) {
		return null;
	}

	public static boolean canConvert(String text){
		return text.length() == text.getBytes().length;
	}

}

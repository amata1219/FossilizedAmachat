package amata1219.amachat.processor;

public class GoogleTranslate implements Processor {

	public static final String NAME = "GoogleTranslate";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String process(String text) {
		return null;
	}

	public static boolean canTranslate(String text){
		return false;
	}

}

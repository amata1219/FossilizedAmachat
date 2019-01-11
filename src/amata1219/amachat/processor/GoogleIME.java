package amata1219.amachat.processor;

public class GoogleIME implements Processor {

	public static final String NAME = "GoogleIME";

	private static final String URL = "http://www.google.com/transliterate?langpair=ja-Hira|ja&text=";
	private static final String[][] TABLE = new String[55][5];
	static{

	}

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

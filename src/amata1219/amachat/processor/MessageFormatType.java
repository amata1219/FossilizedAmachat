package amata1219.amachat.processor;

public enum MessageFormatType {

	NORMAL,
	JAPANIZE,
	TRANSLATE;

	public String toCamelCase(){
		String name = this.name();
		return name.charAt(0) + name.toLowerCase().substring(1);
	}

}

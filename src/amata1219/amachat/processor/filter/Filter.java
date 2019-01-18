package amata1219.amachat.processor.filter;

import amata1219.amachat.processor.SupportTextProcessing;

public interface Filter {

	boolean should(SupportTextProcessing supporter, String text);

	String filter(SupportTextProcessing supporter, String text);

}

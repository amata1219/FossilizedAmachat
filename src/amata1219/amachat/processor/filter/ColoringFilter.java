package amata1219.amachat.processor.filter;

import amata1219.amachat.processor.Coloring;
import amata1219.amachat.processor.SupportTextProcessing;

public class ColoringFilter implements Filter {

	@Override
	public boolean should(SupportTextProcessing supporter, String text) {
		return supporter.hasProcessor(Coloring.NAME);
	}

	@Override
	public String filter(SupportTextProcessing supporter, String text) {
		return Coloring.coloring(text);
	}

}

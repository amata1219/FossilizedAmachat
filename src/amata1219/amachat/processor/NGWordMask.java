package amata1219.amachat.processor;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import amata1219.amachat.Amachat;
import net.md_5.bungee.config.Configuration;

public final class NGWordMask implements Processor {

	public static final String NAME = "NGWordMask";
	private List<String> words;

	private NGWordMask(){

	}

	public static void load(){
		Configuration configuration = Amachat.getConfig().getConfiguration().getSection("NGWordMask");
		if(!configuration.getBoolean("Enable"))
			return;

		NGWordMask processor = new NGWordMask();

		processor.words = configuration.getStringList("NGWords");

		ProcessorManager.register(processor);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String process(String text) {
		AtomicReference<String> reference = new AtomicReference<>();
		words.forEach(word -> reference.set(reference.get().replace(word, getAsterisk(word.length()))));
		return reference.get();
	}

	public String getAsterisk(int length){
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < length; i++)
			builder.append("*");
		return builder.toString();
	}

}

package amata1219.amachat.config;

import amata1219.amachat.Amachat;

public interface Initializer {

	static void setVersion(Config config, boolean save){
		config.getConfiguration().set("Version", Amachat.VERSION);
		if(save)
			config.apply();
	}

	void initialize(Config config);

}

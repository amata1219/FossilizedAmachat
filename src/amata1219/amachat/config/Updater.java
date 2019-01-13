package amata1219.amachat.config;

import amata1219.amachat.Amachat;

public interface Updater {

	static boolean isOld(Config config){
		return config.getConfiguration().getFloat("VERSION") < Amachat.VERSION;
	}

	void update(Config config);

}

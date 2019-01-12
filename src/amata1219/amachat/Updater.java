package amata1219.amachat;

public interface Updater {

	static boolean isOld(Config config){
		return config.getConfig().getFloat("VERSION") < Amachat.VERSION;
	}

	void update(Config config);

}

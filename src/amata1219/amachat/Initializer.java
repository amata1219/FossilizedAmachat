package amata1219.amachat;

public interface Initializer {

	static void setVersion(Config config, boolean apply){
		config.getConfig().set("Version", Amachat.VERSION);
		if(apply)
			config.apply();
	}

	void initialize(Config config);

}

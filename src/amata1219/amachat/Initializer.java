package amata1219.amachat;

public interface Initializer {

	static void setVersion(Config config, boolean save){
		config.getConfig().set("Version", Amachat.VERSION);
		if(save)
			config.apply();
	}

	void initialize(Config config);

}

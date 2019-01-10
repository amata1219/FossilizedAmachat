package amata1219.amachat.bot;

public interface TaskBot extends Bot, Runnable {

	void start();

	void pause();

	void stop();

	boolean isPause();

}

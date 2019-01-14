package amata1219.amachat.bot;

import java.util.concurrent.TimeUnit;

import amata1219.amachat.Amachat;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public abstract class TaskBot extends Bot implements Runnable {

	protected ScheduledTask task;
	protected long interval;
	protected boolean pause;

	public void start() {
		if(task != null)
			stop();

		task = BungeeCord.getInstance().getScheduler().schedule(Amachat.getPlugin(), this, 0L, interval, TimeUnit.SECONDS);
	}

	public boolean isPause(){
		return pause;
	}

	public void setPause(boolean pause){
		this.pause = pause;
	}

	public void stop() {
		task.cancel();
	}

	@Override
	public abstract void run();
}

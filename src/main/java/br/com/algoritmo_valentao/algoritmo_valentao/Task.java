package br.com.algoritmo_valentao.algoritmo_valentao;

import java.util.TimerTask;

public class Task extends TimerTask {
	private Runnable run;
	private long time;
	
	public Task(Runnable run, long time) {
		super();
		this.run = run;
		this.time = time;
	}
	
	public long getTime() {
		return time;
	}

	public void run() {
		this.run.run();
	}
	
}

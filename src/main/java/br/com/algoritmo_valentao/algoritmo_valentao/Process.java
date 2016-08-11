package br.com.algoritmo_valentao.algoritmo_valentao;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class Process extends TimerTask {
	private int id;
	private Runnable run;
	private long time;	
	
	public Process(Runnable run, long time) {
		super();
		this.run = run;
		this.time = time;
	}			

	public Process(Function<Timer, String> xTimer, Timer t, long time) {
		this.run = () -> xTimer.apply(t);
		this.time = time;
	}

	public long getTime() {
		return time;
	}

	public void run() {
		this.run.run();
	}
	
}

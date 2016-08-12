package br.com.algoritmo_valentao.algoritmo_valentao;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class Process extends TimerTask {
	private int id;
	private Runnable run;
	private long time;
	private boolean isCoord; // se é coordenador
	private boolean isRunning; // se é coordenador	
	
	public Process(Runnable run, long time) {
		super();
		this.run = run;
		this.time = time;
		this.isCoord = false;
		this.isRunning = false;
	}			

	public Process(Function<Timer, String> xTimer, Timer t, long time) {
		this.run = () -> xTimer.apply(t);
		this.time = time;
		this.isCoord = false;
		this.isRunning = false;
	}

	public void run() {
		this.isRunning = true;
		this.run.run();
	}
	
	public void stop() {
		this.isRunning = false;
		this.run.wait();		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Runnable getRun() {
		return run;
	}

	public void setRun(Runnable run) {
		this.run = run;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public boolean isCoord() {
		return isCoord;
	}

	public void setCoordTrue() {
		this.isCoord = true;
	}

	public void setCoordFalse() {
		this.isCoord = false;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
}

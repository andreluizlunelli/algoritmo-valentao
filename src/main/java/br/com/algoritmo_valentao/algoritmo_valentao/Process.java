package br.com.algoritmo_valentao.algoritmo_valentao;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class Process extends TimerTask {
	public static final String RESPOND_OK = "Ok";
	public static final String RESPOND_ERROR = "Erro";
	
	private int id;
	private Runnable run;
	private long time;
	private boolean isCoord; // se é coordenador
	private boolean isRunning; // se é coordenador	
	
	private void initProcess() {
		this.isCoord = false;
		this.isRunning = true;		
	}
	
	public Process(Runnable run, long time) {
		this.initProcess();
		this.run = run;
		this.time = time;
	}			

	public Process(Function<Timer, String> xTimer, Timer t, long time) {
		this.initProcess();
		this.run = () -> xTimer.apply(t);
		this.time = time;
	}

	public Process(Function<Processor, String> xProcess, Processor p, long time) {
		this.initProcess();
		this.run = () -> xProcess.apply(p);
		this.time = time;
	}

	public void run() {
		this.isRunning = true;
		this.run.run();
	}
	
	public void stop() {
		this.isRunning = false;
		try {
			this.run.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public String respond() {
		return isRunning() ? RESPOND_OK : RESPOND_ERROR;
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

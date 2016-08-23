package br.com.algoritmo_valentao.algoritmo_valentao;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

import br.com.algoritmo_valentao.algoritmo_valentao.Processor.FunctionConsulta;

public class Process extends TimerTask {
	public static final String RESPOND_OK = "Ok";
	public static final String RESPOND_ERROR = "Erro";
	
	private int id;
	private Runnable run;
	private long time;
	private boolean isCoord; // se é coordenador
	private boolean isRunning; 	
	
	private void initProcess() {
		this.isCoord = false;
		this.isRunning = true;				
	}
	
	public Process(Runnable run, long time) {
		this.initProcess();
		this.run = run;
		this.time = time;
	}				
	
	public Process(FunctionConsulta run, Processor processor, long time) {
		this.initProcess();
		FunctionConsulta<Processor, Process, String> fconsulta = run;
		if (run != null) {
			this.run = () -> fconsulta.apply(processor, this);			
		}
		this.time = time;
	}				
	
	public Process(Object runnable, Object parameter, long time) {
		this.initProcess();
		Function<Object, Object> f1 = (Function<Object, Object>) runnable;
		this.run = () -> f1.apply(parameter);
		this.time = time;
	}

	public void run() {
		setRunningTrue();
		this.run.run();
	}
	
	public void stop() {
		setRunningFalse();
		setCoordFalse();
		this.cancel();		
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

	public void setRunningTrue() {
		this.isRunning = true;
	}
	
	public void setRunningFalse() {
		this.isRunning = false;
	}

	/**
	 * Se o numero do random for par, retorna true
	 * @return
	 */
	public boolean isWantBeCoor() {
		Random random = new Random();
		int n = random.nextInt();
		return (n % 2) == 0; 
	}

	@Override
	public String toString() {
		return "Process [id=" + id + ", run=" + run + ", time=" + time + ", isCoord=" + isCoord + ", isRunning="
				+ isRunning + "]";
	}
	
}

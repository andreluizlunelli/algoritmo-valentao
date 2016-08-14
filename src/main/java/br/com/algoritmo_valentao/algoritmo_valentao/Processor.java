package br.com.algoritmo_valentao.algoritmo_valentao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.Vector;
import java.util.function.Function;

public class Processor {
	
	public static void main(String[] args) {
    	Processor.facadeMethod();
	}
	
	private final long timeCoord = 1000 * 2;
	private final long timeNewProcess = 1000 * 3;
	private final long timeRmProcessNotCoord = 1000 * 5;
	private final long timeRmProcessCoord = 1000 * 10;
	private final long timeCloseAllProcess = 1000 * 20;
	private final Timer timer = new Timer();
	private final List<Process> listProcess = new Vector<Process>();

	public static void facadeMethod() {
		Processor processor = new Processor();
		processor.createListProcess();
		processor.scheduleTasks();
		processor.selectFirstCoord();				
	}

	private void createListProcess() {				
		
		/*
		 * Processo para finalizar o programa 
		 */
		schedule(new Process(getCloseAllProcessFunction(), timer, timeCloseAllProcess));
		/*
		 * Processo para consultar o coordenador
		 */
		schedule(new Process(getCoordConsultFunction(), this, timeCoord));
		/*
		 * Criar processos aleatórios
		 */
		addProcessOnList(new Process(getNewProcessFunction(), this.listProcess, timeNewProcess));
		/*
		 * Remover processos aleatório que não seja coordenador
		 */
		schedule(new Process(getRmProcessNotCoordFunction(), this, timeRmProcessNotCoord));
		/*
		 * Remover coordenador
		 */
		schedule(new Process(getRmCoordProcessFunction(), this, timeRmProcessCoord));				
	}
	
	private synchronized void addProcessOnList(Process p) {
		listProcess.add(p);
	}
	
	private Function<Timer, String> getCloseAllProcessFunction() {
		out("TimerTask:Fechar toda aplicacao");
		Function<Timer, String> xTimer = x -> {
			x.cancel(); 
			x.purge(); 
			out("F:Finaliza tasks"); 
			return "";
		};
		return xTimer;
	}
	
	private Function<Processor, String> getCoordConsultFunction() {
		out("TimerTask:Consultar coordenador");
		Function<Processor, String> xConsult = x -> {
			int i = 0;
			for (Process p : x.listProcess) {
				if (p.isCoord()) {
					if (p.respond() != Process.RESPOND_OK) {
						out("nao esta respondendo, Eleição!");
						x.election(); // TODO TEM QUE PEGAR O PROCESSO QUE CHAMOU ESSA FUNÇÃO
					} else {
						out("Processo "+i+" "+Process.RESPOND_OK);
					}
					break;
				}
				i++;
			}
			return "";
		};
		return xConsult;
	}
	
	private Function<List<Process>, String> getNewProcessFunction() {
		out("TimerTask:Criar novos processos");
		Function<List<Process>, String> xNewProcess = x -> {
			out(String.format("Novo processo:%d", (x.size()+1)));
			Process p = new Process(getCoordConsultFunction(), this, timeNewProcess);
			x.add(p);
			schedule(p);
			return "";
		};
		return xNewProcess;
	}
	
	private Function<Processor, String> getRmProcessNotCoordFunction() {
		out("TimerTask:Remover processo que não é coordenador");
		Function<Processor, String> xRmProcess = x -> {
			Process processStopped = null;
			int n;
			do {
				Random random = new Random();
				n = random.nextInt(x.listProcess.size());
				processStopped = x.listProcess.get(n);
			} while (processStopped.isCoord()); // se n for coordenador pode matar
			out(String.format("Processo parado:%d", n));
			processStopped.stop();
			return "";
		};
		return xRmProcess;
	}
	
	private Function<Processor, String> getRmCoordProcessFunction() {
		out("TimerTask:Remover processo coordenador");
		Function<Processor, String> xRmProcess = x -> {
			Process processStopped = x.getListCoord();
			out(String.format("Processo coordenador parado:%d", processStopped.getId()));
			processStopped.stop();
			return "";
		};
		return xRmProcess;
	}
	
	private void schedule(Process p) {
		timer.scheduleAtFixedRate(p, p.getTime(), p.getTime());
	}
	
	private void scheduleTasks() {
		for (Process p : listProcess) {
			schedule(p);				
		}		
	}
	
	private void out(String s) {
		System.out.println(s);
	}
	
	/**
	 * TODO metodo não está tratando se, houver outro coordenador, ele não pesquisa p remover, tem que pesquisar e remover
	 * @param index
	 */
	private void selectCoord(int index) {
		Process process = listProcess.get(index);
		if (!process.isRunning()) {
			return;
		} 
		process.setCoordTrue();		
		listProcess.set(index, process);
	}
	
	private void selectFirstCoord() {
		this.selectCoord(0);
	}
	
	private Process getListCoord() {
		int i = 0;
		for (Process process : listProcess) {
			if (process.isRunning() && process.isCoord()) {
				process.setId(i);
				return process;
			}
			i++;
		}
		return null;
	}
	
	private Process election(Process consultationProcess, Processor processor) {
		
	}
}

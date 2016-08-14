package br.com.algoritmo_valentao.algoritmo_valentao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
	private final long timeCloseAllProcess = 1000 * 15;
	private final Timer timer = new Timer();
	private List<Process> listProcess = new Vector<Process>();

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
		listProcess.add(new Process(getCloseAllProcessFunction(), timer, timeCloseAllProcess));
		/*
		 * Processo para consultar o coordenador
		 */
		listProcess.add(new Process(getCoordConsultFunction(), this, timeCoord));
		/*
		 * Criar processos aleatórios
		 */
		listProcess.add(new Process(getNewProcessFunction(), this.listProcess, timeNewProcess));
		/*
		 * Remover processos aleatório que não seja coordenador
		 */
		listProcess.add(new Process(() -> out("Remover processo que NÃO seja coordenador "+timeRmProcessNotCoord), timeRmProcessNotCoord));
		/*
		 * Remover coordenador
		 */
		listProcess.add(new Process(() -> out("Remover processo que SEJA coordenador "+timeRmProcessCoord), timeRmProcessCoord));				
	}
	
	private Function<Timer, String> getCloseAllProcessFunction() {
		Function<Timer, String> xTimer = x -> {
			x.cancel(); 
			x.purge(); 
			out("F:Finaliza tasks"); 
			return "";
		};
		return xTimer;
	}
	
	private Function<Processor, String> getCoordConsultFunction() {
		out("F:Consultar coordenador");
		Function<Processor, String> xConsult = x -> {
			for (Process p : x.listProcess) {
				if (p.isCoord()) {
					if (p.respond() != Process.RESPOND_OK) {
						out("nao esta respondendo, Eleição!");
						break;
					} else {
						out(Process.RESPOND_OK);
						return Process.RESPOND_OK;
					}
				}
			}
			return "";
		};
		return xConsult;
	}
	
	private Function<List<Process>, String> getNewProcessFunction() {
		out("F:Criar novos processos");
		Function<List<Process>, String> xNewProcess = x -> {
			x.add(new Process(() -> out("Novo processo"), timeNewProcess));
			return "";
		};
		return xNewProcess;
	}
	
	private void scheduleTasks() {
		for (Process t : listProcess) {
			timer.scheduleAtFixedRate(t, t.getTime(), t.getTime());				
		}		
	}
	
	private void out(String s) {
		System.out.println(s);
	}
	
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


}

package br.com.algoritmo_valentao.algoritmo_valentao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.function.Function;

public class Processor {
	private long timeCoord = 1000 * 2;
	private long timeNewProcess = 1000 * 3;
	private long timeRmProcessNotCoord = 1000 * 5;
	private long timeRmProcessCoord = 1000 * 10;
	private long timeCloseAllProcess = 1000 * 15;
	private final Timer timer = new Timer();
	private List<Process> listProcess = new ArrayList<Process>();

	public static void facadeMethod() {
		Processor processor = new Processor();
		processor.createListProcess();
		processor.scheduleTasks();
		processor.selectFirstCoord();				
	}

	private void createListProcess() {
		listProcess.add(new Process(getCloseAllProcessFunction(), timer, timeCloseAllProcess));
		listProcess.add(new Process(getCoordConsultFunction(), this, timeCoord));
		listProcess.add(new Process(() -> out("Criar novo processo "+timeNewProcess), timeNewProcess));
		listProcess.add(new Process(() -> out("Remover processo que NÃO seja coordenador "+timeRmProcessNotCoord), timeRmProcessNotCoord));
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
						out("Eleição!");
						break;
					} else {
						return Process.RESPOND_OK;
					}
				}
			}
			// se chegou aqui eleição tbm
			return "nao achado";
		};
		return xConsult;
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

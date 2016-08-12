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
	private long timeCloseAllProcess = 1000 * 10;
	private final Timer timer = new Timer();
	private List<Process> listProcess = new ArrayList<Process>();

	public static void facadeMethod() {
		Processor processor = new Processor();
		processor.createListProcess();
		processor.scheduleTasks();
		processor.selectFirstCoord();
		
		/* TEM QUE TESTAR NO PC QUE TENHA JAVA8
		 * NO createListProcess PRECISA ADD UMA Function PEGANDO A POSIÇAO DO COORDENADOR
		 * NÃO VAI EXISTIR AI, ELE CHAMA O selectFirstCoord
		 */
	}

	private void createListProcess() {
		listProcess.add(new Process(() -> out("Consultar coordenador "+timeCoord), timeCoord));
		listProcess.add(new Process(() -> out("Criar novo processo "+timeNewProcess), timeNewProcess));
		listProcess.add(new Process(() -> out("Remover processo que NÃO seja coordenador "+timeRmProcessNotCoord), timeRmProcessNotCoord));
		listProcess.add(new Process(() -> out("Remover processo que SEJA coordenador "+timeRmProcessCoord), timeRmProcessCoord));		
		Function<Timer, String> xTimer = x -> {x.cancel(); x.purge(); out("Finaliza tasks"); return "";};
		listProcess.add(new Process(xTimer, timer, timeCloseAllProcess));
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

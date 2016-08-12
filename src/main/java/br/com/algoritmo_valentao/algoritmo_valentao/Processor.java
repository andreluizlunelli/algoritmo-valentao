package br.com.algoritmo_valentao.algoritmo_valentao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.function.Function;

public class Processor {
	private static long timeCoord = 1000 * 2;
	private static long timeNewProcess = 1000 * 3;
	private static long timeRmProcessNotCoord = 1000 * 5;
	private static long timeRmProcessCoord = 1000 * 10;
	private static long timeCloseAllProcess = 1000 * 60;
	private static final Timer timer = new Timer();
	private static List<Process> listProcess = new ArrayList<Process>();

	private static void createListProcess() {
		listProcess.add(new Process(() -> out("Consultar coordenador "+timeCoord), timeCoord));
		listProcess.add(new Process(() -> out("Criar novo processo "+timeNewProcess), timeNewProcess));
		listProcess.add(new Process(() -> out("Remover processo que NÃO seja coordenador "+timeRmProcessNotCoord), timeRmProcessNotCoord));
		listProcess.add(new Process(() -> out("Remover processo que SEJA coordenador "+timeRmProcessCoord), timeRmProcessCoord));		
		Function<Timer, String> xTimer = x -> {x.cancel(); x.purge(); out("Finaliza tasks"); return "";};
		listProcess.add(new Process(xTimer, timer, timeCloseAllProcess));
	}
	
	private static void scheduleTasks() {
		for (Process t : listProcess) {
			timer.scheduleAtFixedRate(t, t.getTime(), t.getTime());				
		}		
	}
	
	private static void out(String s) {
		System.out.println(s);
	}

	public static void initProcess() {
		createListProcess();
		scheduleTasks();
	}

}

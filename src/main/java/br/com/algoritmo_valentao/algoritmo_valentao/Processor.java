package br.com.algoritmo_valentao.algoritmo_valentao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

public class Processor {
	private static long timeCoord = 1000 * 2;
	private static long timeNewProcess = 1000 * 3;
	private static long timeRmProcessNotCoord = 1000 * 5;
	private static long timeRmProcessCoord = 1000 * 10;
	private static final Timer timer = new Timer();
	private static List<Task> listProcess = new ArrayList<Task>();

	private static void createListProcess() {
		listProcess.add(new Task(() -> System.out.println("Consultar coordenador "+timeCoord), timeCoord));
		listProcess.add(new Task(() -> System.out.println("Criar novo processo "+timeNewProcess), timeNewProcess));
		listProcess.add(new Task(() -> System.out.println("Remover processo que NÃO seja coordenador "+timeRmProcessNotCoord), timeRmProcessNotCoord));
		listProcess.add(new Task(() -> System.out.println("Remover processo que SEJA coordenador "+timeRmProcessCoord), timeRmProcessCoord));
		listProcess.add(new Task(() -> new Runnable() {		
			/*
			 * Usado para finalizar a execução de todas as tasks depois de um minuto
			 */
			@Override
			public void run() {
				System.out.println("Finaliza tasks");
				timer.cancel();
				timer.purge();
			}
		}, 1000 * 11));
	}
	
	private static void scheduleTasks() {
		for (Task t : listProcess) {
			timer.scheduleAtFixedRate(t, t.getTime(), t.getTime());				
		}		
	}
	
	public static void teste() {
		timer.cancel();
		timer.purge();
	}

	public static void initProcess() {
		createListProcess();
		scheduleTasks();
	}

}

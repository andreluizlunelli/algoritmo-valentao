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

	private final long timeCoordConsult = 1000 * 2;
	private final long timeNewProcess = 1000 * 3;
	private final long timeRmProcessNotCoord = 1000 * 5;
	private final long timeRmProcessCoord = 1000 * 10;
	private final long timeCloseAllProcess = 1000 * 30;
	private final Timer timer = new Timer();
	private final List<Process> listProcess = new Vector<Process>();

	public static void facadeMethod() {
		Processor processor = new Processor();
		processor.createProcess();
	}

	private void createProcess() {

		/*
		 * Processo para finalizar o programa
		 */
		schedule(new Process(getCloseAllProcessFunction(), timer, timeCloseAllProcess));
		/*
		 * Remover processos aleatório que não seja coordenador
		 */
		schedule(new Process(getRmProcessNotCoordFunction(), this, timeRmProcessNotCoord));
		/*
		 * Remover coordenador
		 */
		schedule(new Process(getRmCoordProcessFunction(), this, timeRmProcessCoord));
		/*
		 * Criar processos aleatórios
		 */
		schedule(new Process(getNewProcessFunction(), this, timeNewProcess));
	}

	private synchronized void addProcessOnList(Process p) {
		listProcess.add(p);
	}

	private Function<Timer, String> getCloseAllProcessFunction() {
		Function<Timer, String> xTimer = x -> {
			out("TimerTask:Fechar toda aplicacao");
			x.cancel();
			x.purge();
			out("F:Finaliza tasks");
			return "";
		};
		return xTimer;
	}

	private FunctionConsulta<Processor, Process, String> getCoordConsultFunction() {
		FunctionConsulta<Processor, Process, String> xConsult = (processor, process) -> {
			out("TimerTask:Consultar coordenador");
			synchronized (processor) {				
				for (Process p : processor.getListProcess()) {
					if (p.isCoord()) {
						continue;
					}
					Process coord = processor.getCoordFromListProcessor();
					if (coord == null) {
						out("coordenador null, Eleição!");
						coord = new Election(p, processor).election();
						if (coord.respond() == Process.RESPOND_OK) {
							out("Novo processo coordenador " + coord.getId());
						} else {
							out("Ocorreu algo errado"+coord.toString());
						}
					} else if (coord.respond() != Process.RESPOND_OK) {
						out("Nao esta respondendo, Eleição!");
						coord.setCoordFalse();
						coord = new Election(p, processor).election();
						if (coord.respond() == Process.RESPOND_OK) {
							out("Novo processo coordenador " + coord.getId());
						} else {
							out("Ocorreu algo errado"+coord.toString());
						}
					} else {
						out("Processo " + p.getId() + " " + Process.RESPOND_OK);
					}
					break;
				}
				return "";
			}
		};
		return xConsult;
	}

	private Function<Processor, String> getNewProcessFunction() {
		Function<Processor, String> xNewProcess = x -> {
			out("TimerTask:Criar novo processo");
			int id = x.getListProcess().size();
			out(String.format("Novo processo:%d", id));
			/*
			 * Processo para consultar o coordenador
			 */
			Process p = new Process(getCoordConsultFunction(), this, timeCoordConsult);
			p.setId(id);
			x.addProcessOnList(p);
			schedule(p);
			return "";
		};
		return xNewProcess;
	}

	private Function<Processor, String> getRmProcessNotCoordFunction() {
		Function<Processor, String> xRmProcess = x -> {
			out("TimerTask:Remover processo que não é coordenador");
			Process processStopped = null;
			int n;
			do {
				Random random = new Random();
				n = random.nextInt(x.getListProcess().size());
				processStopped = x.getListProcess().get(n);
			} while (processStopped.isCoord()); // se n for coordenador pode
												// matar
			out(String.format("Processo parado:%d", n));
			processStopped.stop();
			return "";
		};
		return xRmProcess;
	}

	private Function<Processor, String> getRmCoordProcessFunction() {
		Function<Processor, String> xRmProcess = x -> {
			out("TimerTask:Remover processo coordenador");
			Process processStopped = x.getCoordFromListProcessor();
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
	 * TODO metodo não está tratando se, houver outro coordenador, ele não
	 * pesquisa p remover, tem que pesquisar e remover
	 * 
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

	private Process getCoordFromListProcessor() {
		for (Process process : this.getListProcess()) {
			if (process.isCoord()) {
				return process;
			}
		}
		return null;
	}

	public synchronized List<Process> getListProcess() {
		return listProcess;
	}

	@FunctionalInterface
	interface FunctionConsulta<A, B, R> {
		public R apply(A a, B b);
	}

}

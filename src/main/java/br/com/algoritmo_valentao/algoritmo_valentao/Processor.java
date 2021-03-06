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

	/*
	 * TODO Fazer metodo para dar um dump na classe Processor para olhar como
	 * esta os valores e qual o tempo sendo executado
	 * 
	 * TODO O coordenador está se consultando, certo ou errado? n sei
	 */

	public static void main(String[] args) {
		Processor.facadeMethod();
	}

	private final long timeCoordConsult = 1000 * 2;
	private final long timeNewProcess = 1000 * 3;
	private final long timeRmProcessNotCoord = 1000 * 5;
	private final long timeRmProcessCoord = 1000 * 10;
	private final long timeCloseAllProcess = 1000 * 60;
	private final Timer timer = new Timer();
	private final List<Process> listProcess = new Vector<Process>();

	public static void facadeMethod() {
		Processor processor = new Processor();
		processor.createProcess();
	}

	private void createProcess() {
		out("Início");
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
		/*
		 * Processo para consultar o coordenador
		 */
		schedule(new Process(getExecConsultFunction(), this, timeCoordConsult));
	}

	private FunctionConsulta<Processor, Process, String> getExecConsultFunction() {
		FunctionConsulta<Processor, Process, String> xConsult = (processor, process) -> {
			out("TimerTask:Executar busca e chama funcao para consultar coordenador");
			Process pRandom = processor.getRandomConsultProcess();
			if (pRandom == null) {
				return null;
			} else if (!pRandom.isRunning()) {
				out("Ops! Não pode ser um processo nulo");
			}
			Runnable run2 = pRandom.getRun();
			run2.run(); // aqui chama a função dentro do
						// getCoordConsultFunction()
			return "";
		};
		return xConsult;
	}

	private synchronized void addProcessOnList(Process p) {
		listProcess.add(p);
	}

	private Function<Timer, String> getCloseAllProcessFunction() {
		Function<Timer, String> xTimer = x -> {
			out("TimerTask:Fechar toda aplicacao");
			x.cancel();
			x.purge();
			out("Finalizado com sucesso");
			return "";
		};
		return xTimer;
	}

	private FunctionConsulta<Processor, Process, String> getCoordConsultFunction() {
		FunctionConsulta<Processor, Process, String> xConsult = (processor, process) -> {
			out("TimerTask:Consultar coordenador");
			Process coord = processor.getCoordFromListProcessor();
			if (coord == null || (coord.respond() != Process.RESPOND_OK)) {
				out("Nao esta respondendo, Eleição!");
				if (coord != null) {
					coord.setCoordFalse();
				}
				coord = new Election(process, processor).election();
				if (coord.respond() == Process.RESPOND_OK) {
					out("Novo processo coordenador " + coord.getId());
				} else {
					out("Ocorreu algo errado" + coord.toString());
				}
			} else {
				out("Processo " + process.getId() + " consultou coordenador: " + coord.getId() + " "
						+ Process.RESPOND_OK);
			}
			return "";
		};
		return xConsult;
	}

	private Function<Processor, String> getNewProcessFunction() {
		Function<Processor, String> xNewProcess = x -> {
			out("TimerTask:Criar novo processo");
			int id = x.getListProcess().size();
			out(String.format("Novo processo:%d", id));
			Process p = new Process(getCoordConsultFunction(), this, timeCoordConsult);
			p.setId(id);
			x.addProcessOnList(p);
			return "";
		};
		return xNewProcess;
	}

	private Function<Processor, String> getRmProcessNotCoordFunction() {
		Function<Processor, String> xRmProcess = x -> {
			out("TimerTask:Remover processo que não é coordenador");
			if (x.getListProcess().size() < 2) {
				out("Só tem um processo na lista, não remove");
				return "";
			}
			Process processStopped = null;
			int n;
			do {
				Random random = new Random();
				n = random.nextInt(x.getListProcess().size());
				processStopped = x.getListProcess().get(n);
				
				// se n for coordenador nem estiver parado, pode matar 
				if (processStopped.isCoord()) {
					processStopped = null;
				} else if (!processStopped.isRunning()) {
					processStopped = null;
				}
			} while (processStopped == null); 
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

	private Process getRandomConsultProcess() {
		if (getListProcess().size() == 0) {
			out("Opa! Lista vazia!");
			return null;
		} else if (getListProcess().size() == 1) {
			out("Só tem um elemento!");
			Process isRunning = getListProcess().get(0);
			if (isRunning.isRunning())
				return isRunning;
			else
				return null;
		}
		Process returnn = null;
		do {
			int randomIndex = getIntBetween(0, (getListProcess().size() - 1));
			returnn = getListProcess().get(randomIndex);
			if (returnn.isCoord()) {
				returnn = null;
			} else if (!returnn.isRunning()) {
				returnn = null;
			}
		} while (returnn == null);
		return returnn;
	}

	Random random = new Random();

	private int getIntBetween(int min, int max) {
		return random.nextInt((max + 1) - min) + min;
	}

	@FunctionalInterface
	interface FunctionConsulta<A, B, R> {
		public R apply(A a, B b);
	}

}

package br.com.algoritmo_valentao.algoritmo_valentao;

import java.util.ArrayList;
import java.util.List;

public class Election {
	private Process callToElection;
	private Processor processor;
	private List<Process> wantBeCoord = new ArrayList<>();
	
	public Election(Process callToElection, Processor processor) {
		super();
		this.callToElection = callToElection;
		this.processor = processor;
	}

	public Process election() {
		Process coord = null;
		for (Process p : processor.getListProcess()) {
			if (callToElection.getId() < p.getId() ) { // se tiver processos maiores que o chamador, ele faz o convite p se tornar coordenador
				if (p.isWantBeCoor()) {
					addWant(p);
				}
			} 
		}
		if (wantBeCoord.size() > 1) {
			coord = getProcessWithMajorID();
		} else {
//			se no final das contas não tiver um coordenador, o callToElection se devolve como coordenador
			if (coord == null) {
				coord = callToElection;
			}		
		}
		coord.setCoordTrue();
		return coord;
	}
	
	private void addWant(Process p) {
		this.wantBeCoord.add(p);
	}
	
	private Process getProcessWithMajorID() {
		int majorId = 0;
		Process majorProcess = null;
		for (Process process : wantBeCoord) {
			if (process.getId() > majorId) {
				majorId = process.getId();
				majorProcess = process;
			}
		}
		return majorProcess;
	}

}

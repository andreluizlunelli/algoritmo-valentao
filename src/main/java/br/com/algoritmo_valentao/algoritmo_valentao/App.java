package br.com.algoritmo_valentao.algoritmo_valentao;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
    	long tempoConsultarCoordenador = 1000 * 2;
    	long tempoCriarNovoProcesso = 1000 * 3;
    	long tempoEliminaProcessoDiferenteCoordenador = 1000 * 5;    	
    	long tempoEliminaProcessoCoordenador = 1000 * 10;    	
        Timer timer = new Timer();
        TimerTask taskConsultarCoordenador = new TimerTask() {			
			@Override
			public void run() {
				System.out.println("consultar coordenador");
			}
		};
		
		new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		};
		
        TimerTask taskCriarNovoProcesso = new TimerTask() {			
			@Override
			public void run() {
				System.out.println("consultar coordenador");
			}
		};
		timer.scheduleAtFixedRate(taskConsultarCoordenador, tempoConsultarCoordenador, tempoConsultarCoordenador);		
		timer.scheduleAtFixedRate(taskCriarNovoProcesso, tempoConsultarCoordenador, tempoConsultarCoordenador);
		
	}
}

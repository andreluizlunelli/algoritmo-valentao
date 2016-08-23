package br.com.algoritmo_valentao.algoritmo_valentao;

import java.util.Timer;
import java.util.TimerTask;

import junit.framework.TestCase;

public class Main extends TestCase {
	public void testApp()
    {
		long tempoConsultarCoordenador = 2000;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {			
			@Override
			public void run() {
				System.out.println("chamou");
			}
		};
		timer.scheduleAtFixedRate(task, tempoConsultarCoordenador, tempoConsultarCoordenador);
		
    }
}

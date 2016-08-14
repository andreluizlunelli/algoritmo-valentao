package br.com.algoritmo_valentao.algoritmo_valentao;

import java.util.List;
import java.util.Random;
import java.util.Vector;
import br.com.algoritmo_valentao.algoritmo_valentao.Processo;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Hello world!
 *
 */
public class App 
{   
    Timer timer;
    
    int tempo = 0;
    
    int task2 = 0;
    int task3 = 0;
    int task5 = 0;
    int task10 = 0;
    
    ArrayList<Processo> processos = new ArrayList<Processo>();
    Processo coordenador;
    Random random = new Random();

    public App()
    {   
        this.timer = new Timer();    
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run()
            {
                /**
                 * inicializa o programa com 10 processo e elege um coordenador
                 */
                if (tempo == 0)
                {
                    System.out.println("\n----------");
                    System.out.println("Start");
                    System.out.println("----------");
                    System.out.println("\nCriando processos iniciais");
                    
                    for (int p = 0; p < 10; p++)
                    {            
                        processos.add(
                            criarProcesso()
                        );
                    }
                    
                    coordenador = processos.get(random.nextInt(processos.size()));
                    System.out.println("\nEleito coordenador id#"+coordenador.getId());
                }
                
                /**
                 * Tarefas a serem executadas em determinados intervalos
                 */
                if (task2 == 2)
                {                    
                    task2 = 0;
                }
                if (task3 == 3)
                {
                    task3 = 0;
                    
                    Processo processo = criarProcesso();
                    processos.add(processo);
                    System.out.println("\nCriado processo id#"+processo.getId());
                }
                else if (task5 == 5)
                {
                    
                    task5 = 0;
                }
                else if (task10 == 10)
                {
                    
                    task10 = 0;
                }
                
                /**
                 * Finaliza o programa
                 */
                if (tempo == 60)
                {
                    timer.cancel();
                    
                    System.out.println("\n----------");
                    System.out.println("Stop");
                    System.out.println("----------");
                }
                
                tempo++;
                task2++;
                task3++;
                task5++;
                task10++;
            }
        }, 2000, 1000);
    }
    
    public Processo criarProcesso()
    {
        return new Processo(
            this.random.nextInt()
        );
    }
    
    public Processo elegerCoordenador()
    {
        return null;
    }    

    public static void main(String args[]) {
        new App();
    }
}

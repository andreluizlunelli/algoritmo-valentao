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
                    
                    elegerCoordenador();
                }
                
                /**
                 * processo aleatório consulta o coordenador
                 */
                if (task2 == 2)
                {                    
                    task2 = 0;
                }
                /**
                 * novo	processo	é criado
                 */
                if (task3 == 3)
                {
                    task3 = 0;
                    
                    Processo processo = criarProcesso();
                    processos.add(processo);
                    System.out.println("\nCriado processo id#"+processo.getId());
                }
                /**
                 * processo	aleatório	(sem	ser	o	coordenador)	é eliminado
                 */
                if (task5 == 5)
                {
                    eliminarProcessoAleatorio();
                    task5 = 0;
                }
                /**
                 * 	coordenador	é desativado
                 */
                if (task10 == 10)
                {
                    Processo desativado = desativarCoordenador();
                    
                    if (desativado != null)
                    {
                        System.out.println("\nCoordenador id#"+desativado.getId()+" desativado");
                    }
                    
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
    
    public void elegerCoordenador()
    {
        int max = 0;
        for (int p = 0; p < processos.size(); p++)
        {
            Processo processo = processos.get(p);
            
            if (processo.getId() > max)
            {
                coordenador = processo;
            }
        }
        
        System.out.println("\nEleito coordenador id#"+coordenador.getId());
    }   
    
    public Processo desativarCoordenador()
    {
        Processo tmp = coordenador;
        coordenador = null;
        
        if (tmp != null)
        {
            for (int p = 0; p < processos.size(); p++)
            {
                if (processos.get(p).getId() == tmp.getId())
                {
                    processos.remove(p);
                    return tmp;
                }
            }
        }
        return null;
    }
    
    public int numeroAleatorio()
    {
        return random.nextInt(((processos.size() - 1) - 0) + 1) + 0;
    }
    
    public void eliminarProcessoAleatorio()
    {
        int n = numeroAleatorio();
        Processo tmp = processos.get(n);
        
        if (coordenador != null)
        {
            while (tmp.getId() == coordenador.getId())
            {
                n = numeroAleatorio();
                tmp = processos.get(n);
            }
        }
        
        processos.remove(n);
        
        System.out.println("\nProcesso id#"+tmp.getId()+" removido");
         
    }

    public static void main(String args[]) {
        new App();
    }
}

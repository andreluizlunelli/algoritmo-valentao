package br.com.algoritmo_valentao.algoritmo_valentao;

import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Hello world!
 *
 */
public class App 
{
    private List<Processo> processos = new Vector<Processo>();
    private Processo coordenador;
    
    public static void main( String[] args )
    {                
        for (int p = 0; p < 10; p++){
            Random id = new Random();
            this.processos.add(new Processo(id.nextInt()));
        }
    }
    
    public Processo eleicao(Processo[] processos){
        return null;
    }
}

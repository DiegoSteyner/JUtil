package jutil.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import jutil.abstracts.AbstractUtils;

/**
 * Classe utilitária para gerenciar um TimerTask
 * 
 * @author Diego Steyner
 */
public class TimerTaskUtil extends AbstractUtils
{
    private Timer timer = new Timer();

    /**
     * Construtor Padrao
     */
    public TimerTaskUtil()
    {
    }

    /**
     * Método que cria uma tarefa de TimerTask única, que ira chamar um ActionListener uma única vez após o tempo configurado
     *
     * @param action O {@link ActionListener} que deverá ser chamado
     * @param initialTime A quantidade de milessegundos que a tarefa deve esperar até chamar o {@link ActionListener}
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public void setNonRepetitiveTimer(final ActionListener action, int initialTime) throws Exception
    {
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_LAST, "TimerTask"));
            }
        }, (initialTime));
    }

    /**
     * Método que cria uma tarefa de TimerTask que ira chamar um ActionListener de tempo em tempo
     *
     * @param action O {@link ActionListener} que deverá ser chamado
     * @param initialTime O tempo em milessegundos que ele deve esperar para fazer a primeira chamada ao {@link ActionListener}
     * @param repeatEach O intervalo de tempo que ele deve ficar chamando o {@link ActionListener}
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public void setRepetitiveTimer(final ActionListener action, int initialTime, final int repeatEach) throws Exception
    {
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_LAST, "TimerTask"));
            }
        }, (initialTime), (repeatEach));
    }

    /**
     * Método que cancela o timer em execucao
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public void cancelTimer() throws Exception
    {
        timer.cancel();
    }

    /**
     * Método que remove todas as tarefas de TimerTask que ja foram canceladas
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public void purgeCanceledTimers() throws Exception
    {
        timer.purge();
    }
}

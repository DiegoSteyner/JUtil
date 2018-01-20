package jutil.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

/**
 * Classe utilitária que observa um diretório em busca de eventos pré configurados
 * 
 * @author Diego Steyner
 */
public class FileWatchThread extends Thread
{
    private Path           watchDir;
    private WatchService   watcher;
    private ActionListener action;
    private Kind<Path>   type;

    /**
     * Construtor parametrizado
     * 
     * @param path O diretorio que se deseja monitorar
     * @param action O {@link ActionListener} que será acionado caso o evento ocorra no diretório
     * @param type O {@link StandardWatchEventKinds} desejado.
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     * @see Atenção, Essa classe funciona como filha de {@link Thread}, dessa forma, todas as regras aplicáveis a {@link Thread}, tais como 
     * this.start(), this.stop(), this.yeld() e vinculações de threads, ciclo de vida, e qualquer outra ação aplicável a 
     * {@link Thread} também são aplicáveis também a ela.
     */
    public FileWatchThread(String path, ActionListener action, Kind<Path> type) throws Exception
    {
        this.watchDir = Paths.get(path);
        this.watcher = watchDir.getFileSystem().newWatchService();
        this.type = type;
        this.action = action;
        this.watchDir.register(watcher, this.type);
    }

    /**
     * Método sobrescrito, não é necessário documentação
     * 
     * @see java.lang.Thread#run();
     */
    @Override
    public void run()
    {
        try
        {
            WatchKey watchKey = watcher.take();
            List<WatchEvent<?>> events = watchKey.pollEvents();
            for (WatchEvent<?> event : events)
            {
                if (event.kind() == type)
                {
                    action.actionPerformed(new ActionEvent(event.context(), ActionEvent.ACTION_LAST, event.kind().name()));
                }
            }
            
            watchKey.reset();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

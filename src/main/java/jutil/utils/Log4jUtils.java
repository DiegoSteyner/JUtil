package jutil.utils;

import java.io.File;

import jutil.abstracts.AbstractUtils;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/**
 * Classe utilitária para se trabalhar com o Log4j
 * 
 * @author Diego Steyner
 */
public class Log4jUtils extends AbstractUtils
{
    /**
     * Layout padrão
     */
    public static final String LAYOUT_DEFAULT_1 = "%d{ISO8601} [%t] %-5p %c %x - %m%n";
    
    /**
     * Layout somente texto
     */
    public static final String LAYOUT_ONLY_TEXT = "%m%n";
    
    /**
     * Tamanho padrão do Log
     */
    public static final String TAMANHO_DEFAULT  = "10MB";
    
    private Logger rootLogger = Logger.getRootLogger();
    private Logger logger = null;

    /**
     * Construtor parametrizado
     * 
     * @param clazz A classe ao qual o Log pertence
     */
    public Log4jUtils(Class<?> clazz)
    {
        logger = Logger.getLogger(clazz);
    }

    /**
     * Construtor parametrizado para criação do log e o do appender
     * 
     * @param clazz A classe ao qual o Log pertence
     * @param appenderName O nome do appender a ser criado
     * @param layout O Layout do appender
     * @param dirLog O diretório onde o log deve ser salvo
     * @param fileName O nome do arquivo de log
     * @param maxArquivos O número máximo de arquivos a serem criados antes do Roolback
     * @param maxFileSize O tamanho máximo de cada arquivo de log
     */
    public Log4jUtils(Class<?> clazz, String appenderName, String layout, File dirLog, String fileName, int maxArquivos, String maxFileSize)
    {
        logger = Logger.getLogger(clazz);
        
        try
        {
            addAppenderToLogger(getFileAppender(appenderName, getPatternLayout(layout), dirLog, fileName, maxArquivos, maxFileSize));
            addAppenderToLogger(getConsoleAppender(getPatternLayout(layout)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Construtor parametrizado para criação do log
     * 
     * @param name O nome do objeto de log
     */
    public Log4jUtils(String name)
    {
        logger = Logger.getLogger(name);
    }

    /**
     * Construtor parametrizado para criação do log e o do appender
     * 
     * @param name O nome do objeto de log
     * @param appenderName O nome do appender a ser criado
     * @param layout O Layout do appender
     * @param dirLog O diretório onde o log deve ser salvo
     * @param fileName O nome do arquivo de log
     * @param maxArquivos O número máximo de arquivos a serem criados antes do Roolback
     * @param maxFileSize O tamanho máximo de cada arquivo de log
     */
    public Log4jUtils(String name, String appenderName, String layout, File dirLog, String fileName, int maxArquivos, String maxFileSize)
    {
        logger = Logger.getLogger(name);
        
        try
        {
            addAppenderToLogger(getFileAppender(appenderName, getPatternLayout(layout), dirLog, fileName, maxArquivos, maxFileSize));
            addAppenderToLogger(getConsoleAppender(getPatternLayout(layout)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Método que adicina um appender a este objeto de log
     * 
     * @param appender O appender que se deseja adicionar
     * @throws Exception Caso algum erro ocorra uma excessao será lançada
     */
    public void addAppenderToLogger(Appender... appender) throws Exception
    {
        for (int i = 0; i < appender.length; i++)
        {
            logger.addAppender(appender[i]);
        }
    }
    
    /**
     * Método que cria um appender para o console
     * 
     * @param layout O {@link Layout} que o console deve ter
     * 
     * @return O {@link ConsoleAppender} criado
     * @throws Exception Caso algum erro ocorra uma excessao será lançada
     */
    public ConsoleAppender getConsoleAppender(Layout layout) throws Exception
    {
        return(new ConsoleAppender(layout));
    }

    /**
     * Método que cria um appender para o console
     * 
     * @param layout O patter para o Layout que o console deve ter
     * 
     * @return O {@link ConsoleAppender} criado
     * @throws Exception Caso algum erro ocorra uma excessao será lançada
     */
    public ConsoleAppender getConsoleAppender(String layout) throws Exception
    {
        return(getConsoleAppender(getPatternLayout(layout)));
    }
    
    /**
     * Método que cria um {@link FileAppender} para o objeto de log
     * 
     * @param appenderName O nome do appender a ser criado
     * @param layout O Layout do appender
     * @param dirLog O diretório onde o log deve ser salvo
     * @param fileName O nome do arquivo de log
     * @param maxArquivos O número máximo de arquivos a serem criados antes do Roolback
     * @param maxFileSize O tamanho máximo de cada arquivo de log
     * 
     * @return O {@link FileAppender} criado
     * @throws Exception Caso algum erro ocorra uma excessao será lançada 
     */
    public RollingFileAppender getFileAppender(String appenderName, String layout, File dirLog, String fileName, int maxArquivos, String maxFileSize) throws Exception
    {
        return(getFileAppender(appenderName, getPatternLayout(layout), dirLog, fileName, maxArquivos, maxFileSize));
    }
    
    /**
     * Método que cria um {@link FileAppender} para o objeto de log
     * 
     * @param appenderName O nome do appender a ser criado
     * @param layout O Layout do appender
     * @param dirLog O diretório onde o log deve ser salvo
     * @param fileName O nome do arquivo de log
     * @param maxArquivos O número máximo de arquivos a serem criados antes do Roolback
     * @param maxFileSize O tamanho máximo de cada arquivo de log
     * 
     * @return O {@link FileAppender} criado
     * @throws Exception Caso algum erro ocorra uma excessao será lançada 
     */
    public RollingFileAppender getFileAppender(String appenderName, PatternLayout layout, File dirLog, String fileName, int maxArquivos, String maxFileSize) throws Exception
    {
        RollingFileAppender fileAppender;
        
        fileAppender = new RollingFileAppender(layout, new File(dirLog, fileName).getAbsolutePath());
        
        fileAppender.setName(appenderName);
        fileAppender.setMaxBackupIndex(maxArquivos);
        fileAppender.setMaxFileSize(maxFileSize);
        
        return(fileAppender);
    }
    
    /**
     * Método que cria um objeto de Layout com o pattern padrão
     * 
     * @return O objeto de Layout criado
     */
    public PatternLayout getPatternLayout()
    {
        return(new PatternLayout(LAYOUT_DEFAULT_1));
    }

    /**
     * Método que cria um objeto de layout com o pattern especificado
     * 
     * @param layout O Layout desejado
     * @return O objeto de Layout criado
     */
    public PatternLayout getPatternLayout(String layout)
    {
        return(new PatternLayout(layout));
    }

    /**
     * Metodo que retorna o Valor da variavel rootLogger
     *
     * @return O valor da variavel rootLogger
     */
    public Logger getRootLogger()
    {
        return rootLogger;
    }

    /**
     * Metodo que seta o Valor da variavel rootLogger
     *
     * @param rootLogger O valor ser setado na variável rootLogger
     */
    public void setRootLogger(Logger rootLogger)
    {
        this.rootLogger = rootLogger;
    }

    /**
     * Metodo que retorna o Valor da variavel logger
     *
     * @return O valor da variavel logger
     */
    public Logger getLogger()
    {
        return logger;
    }

    /**
     * Metodo que seta o Valor da variavel logger
     *
     * @param logger O valor ser setado na variável logger
     */
    public void setLogger(Logger logger)
    {
        this.logger = logger;
    }
}

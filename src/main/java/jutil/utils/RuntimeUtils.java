package jutil.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import jutil.abstracts.AbstractUtils;
import jutil.data.PrintOutput;

/**
 * Classe utilitária para se trabalhar com execuções de comandos externos
 * 
 * @author Diego Steyner
 */
public class RuntimeUtils extends AbstractUtils 
{
    
    /**
     * Construtor padrão privado
     */
    private RuntimeUtils()
    {
        throw EXCEPTION_CONSTRUTOR;
    }
    
    /**
     * Método que executa um programa via linha de comando </br></br>
     * 
     * Obs: Esse Método não executa chamadas ao powershell ou ao BeanBash pois a forma de tratamemto de saída desses programas segue uma regra diferente, caso haja necessidade de execução
     * de comandos powershell, use a JPowerShell (https://github.com/profesorfalken/jPowerShell) e em caso de necessidade de execução do BeanBash, é necessário implementar toda a execução
     * de comandos através das classes {@link ProcessBuilder} ou do uso da API Apache Commons Exec (https://commons.apache.org/proper/commons-exec/)
     * 
     * @param executable O endereço do executável
     * @param args Os parâmetros do executável
     * @param executionDir O diretório de execução
     * @param print O {@link PrintOutput} com os objetos de impressão desejados
     * @param charset O charset no qual a leitura da saída deve ser feita
     * 
     * @return A String de saída do programa
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static synchronized String execProgramByCommand(String executable, String args, String executionDir, PrintOutput print, String charset) throws Exception
    {
        String[] cmdarray = new String[2];
        StringBuilder retorno = new StringBuilder();
        Process p = null;
        int lidos = -1;
        byte[] buffer = new byte[4096];
        
        cmdarray[0] = executable;
        cmdarray[1] = args;
        
        if(executionDir != null)
        {
            p = Runtime.getRuntime().exec(cmdarray, null, new File(executionDir));
        }
        else if(args == null)
        {
            p = Runtime.getRuntime().exec(executable);
        }
        
        // ATENÇÃO!!
        //
        // A leitura da saída de programas externos devem ocorrer sequencialmente assim que ela começa a ser escrita pois, alguns programas não implementam corretamente a cadeia de finalização
        // da execução gerando 'HANGS' no método e congelando a aplicação, por isso, não altere a forma de leitura a menos que tenha controle sobre o programa que será chamado/lido.
        BufferedInputStream in = new BufferedInputStream(p.getInputStream());
        
        while ((lidos = in.read(buffer, 0, buffer.length)) != -1) 
        {
            retorno.append(new String(buffer, 0, lidos, Charset.forName(charset)));
            if(print != null)
            {
                if(print.getPrintOut() != null)
                {
                    print.getPrintOut().println(retorno.toString());
                }
                
                if(print.getComponentOut() != null)
                {
                    print.getComponentOut().setText(retorno.toString());
                }
            }
        }
 
        // Esperando o processo terminar
        p.waitFor();
        in.close();
        
        return(retorno.toString());
    }
    
    /**
     * Método que executa um comando no sistema operacional.</br></br>
     * 
     * Obs: Esse Método não executa chamadas ao powershell ou ao BeanBash pois a forma de tratamemto de saída desses programas segue uma regra diferente, caso haja necessidade de execução
     * de comandos powershell, use a JPowerShell (https://github.com/profesorfalken/jPowerShell) e em caso de necessidade de execução do BeanBash, é necessário implementar toda a execução
     * de comandos através das classes {@link ProcessBuilder} ou do uso da API Apache Commons Exec (https://commons.apache.org/proper/commons-exec/)
     * 
     * @param command O comando que se deseja executar
     * @param print O {@link PrintOutput} com os objetos de impressão desejados
     * @param charset O charset no qual a leitura da saída deve ser feita
     * 
     * @return A saída do comando
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static String execSystemCommand(String command, PrintOutput print, String charset) throws Exception
    {
        String str = "";
        StringBuilder retorno = new StringBuilder();
        BufferedReader read = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream(), Charset.forName(charset)));

        while ((str = read.readLine()) != null)
        {
            retorno.append(str).append("\n");
            
            if(print != null)
            {
                if(print.getPrintOut() != null)
                {
                    print.getPrintOut().println(retorno.toString());
                }
                
                if(print.getComponentOut() != null)
                {
                    print.getComponentOut().setText(retorno.toString());
                }
            }
        }

        read.close();
        return (retorno.toString());
    }
    
    /**
     * Método que faz o execução da thread atual ficar em pausa por um determinado tempo
     * 
     * @param segundos Os Segundos em espera
     */
    public static synchronized void wait(int segundos)
    {
        try
        {
            Thread.sleep((segundos * 1000));
        }
        catch (InterruptedException ex)
        {
        }
    }
}

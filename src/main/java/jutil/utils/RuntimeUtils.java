package jutil.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import jutil.abstracts.AbstractUtils;
import jutil.data.dtos.PrintOutputDTO;

/**
 * Classe utilitária para se trabalhar com execuções de comandos externos
 * 
 * @author Diego Steyner
 */
public final class RuntimeUtils extends AbstractUtils 
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
     * @param print O {@link PrintOutputDTO} com os objetos de impressão desejados
     * @param charset O charset no qual a leitura da saída deve ser feita
     * 
     * @return A String de saída do programa
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static synchronized String execProgramByCommand(String executable, String args, String executionDir, PrintOutputDTO print, String charset) throws Exception
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
     * @param print O {@link PrintOutputDTO} com os objetos de impressão desejados
     * @param charset O charset no qual a leitura da saída deve ser feita
     * 
     * @return A saída do comando
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static String execSystemCommand(String command, PrintOutputDTO print, String charset) throws Exception
    {
        String str = "";
        StringBuilder retorno = new StringBuilder();
        Process proc = Runtime.getRuntime().exec(command);
		BufferedReader read = new BufferedReader(new InputStreamReader(proc.getInputStream(), Charset.forName(charset)));

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
     * Método que executa um comando do windows utilizando o pré-cadastro do CMD
     * 
     * @param command O comando a ser executado
     * @param print O {@link PrintOutputDTO} a ser usado para impressão do comando
     * @param charset O chartset a user usado
     * 
     * @return A String com o retorno do comando
     * @throws Exception Caso alguma erro ocorra, uma exceção será lançada
     */
    public static String execSystemCommandCmd(String command, PrintOutputDTO print, String charset) throws Exception
    {
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
		builder.redirectErrorStream(true);
		
		Process process = builder.start();
		BufferedReader read = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName(charset)));
		
		String line;
        StringBuilder retorno = new StringBuilder();
		
		while ((line = read.readLine()) != null)
        {
            retorno.append(line).append("\n");
            
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
     * Método que executa um comando do windows utilizando o pré-cadastro do CMD
     * 
     * @param command O comando a ser executado
     * @param charset O chartset a user usado
     * 
     * @return Um {@link BufferedReader} com a saída do comando.
     * @throws Exception Caso alguma erro ocorra, uma exceção será lançada
     */
    public static BufferedReader execSystemCommandCmd(String command, String charset) throws Exception
    {
    	ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
    	builder.redirectErrorStream(true);
    	
    	Process process = builder.start();
    	BufferedReader read = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName(charset)));
    	
    	return(read);
    }

    /**
     * Método que cria uma linha de comando capaz de executar um comando CMD em processo totalmente novo.</br></br>
     * Obs: Caso um novo programa seja chamado de dentro do CMD, os dois programas pertecerão a mesma execução, e, a depender do funcionamento do subprograma</br></br>
     * vinculado ao programa principal, um pode influenciar no funcionamento do outro, mas nenhum dos dois influenciará no funcionamento da rotina atual.
     * 
     * @param cmdCommand O comando CMD
     * 
     * @return A comando montado
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static String getCommandToExecWindowsCMDAsNewProcess(String cmdCommand) throws Exception
    {
        StringBuilder command = new StringBuilder();
        command.append("rundll32 SHELL32.DLL,ShellExec_RunDLL cmd /c \" ").append(cmdCommand).append(" \"");
        
        return(command.toString());
    }
}

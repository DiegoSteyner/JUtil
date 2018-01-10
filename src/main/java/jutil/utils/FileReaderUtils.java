package jutil.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.regex.Pattern;

import jutil.abstracts.AbstractUtils;

/**
 * Classe utilitaria para trabalhar com leitura de arquivos
 * 
 * @since Essa classe tem como objetivo manter a consistência dos objetos em memória, devido a isso, ela sofre
 * degradação de performance na quando o arquivo é superior a 1.000.000 de linhas.
 * 
 * @author Diego Steyner
 */
public class FileReaderUtils extends AbstractUtils
{
    /**
     * Construtor privado
     */
    private FileReaderUtils()
    {
        throw EXCEPTION_CONSTRUTOR;
    }
    
    /**
     * Método que retorna o conteúdo de um arquivo
     * 
     * @param file O arquivo que se deseja ler
     * @param charset O nome do charset no qual o arquivo está escrito
     * 
     * @return Um {@link LinkedList} contendo em cada posição uma linha do arquivo
     * @throws Exception Caso algum erro ocorra uma exceção será lançada
     */
    public static String getContentFileByRawMethod(File file, String charset) throws Exception 
    {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName(charset)));
        StringBuilder retorno = new StringBuilder();
        String temp = "";
        
        while ((temp = buffer.readLine()) != null)
        {
            retorno.append(temp);
        }

        buffer.close();
        return (retorno.toString());
        
    }
    
    /**
     * Método que retorna o conteúdo de um arquivo
     * 
     * @param file O arquivo que se deseja ler
     * @param charset O nome do charset no qual o arquivo está escrito
     * 
     * @return Um {@link LinkedList} contendo em cada posição uma linha do arquivo
     * @throws Exception Caso algum erro ocorra uma exceção será lançada
     */
    public static LinkedList<String> getContentFile(File file, String charset) throws Exception
    {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName(charset)));
        LinkedList<String> retorno = new LinkedList<String>();

        while (retorno.add(buffer.readLine()))
        {
            if (retorno.getLast() == null)
            {
                retorno.removeLast();
                break;
            }
        }

        buffer.close();
        return (retorno);
    }

    /**
     * Método que retorna o conteúdo de um arquivo 
     * 
     * @param in O {@link InputStreamReader} do arquivo
     * 
     * @return Um {@link LinkedList} contendo em cada posição uma linha do arquivo
     * @throws Exception Caso algum erro ocorra uma exceção será lançada
     */
    public static LinkedList<String> getContentFile(InputStreamReader in) throws Exception
    {
        BufferedReader buffer = new BufferedReader(in);
        LinkedList<String> retorno = new LinkedList<String>();

        while (retorno.add(buffer.readLine()))
        {
            if (retorno.getLast() == null)
            {
                retorno.removeLast();
                break;
            }
        }

        buffer.close();
        return (retorno);
    }

    /**
     * Método que retorna o conteúdo de um arquivo
     * 
     * @param file O arquivo que se deseja ler
     * @param pattern A expressão regular que se deseja que seja avaliada para cada linha adicionada, caso a linha passe na avaliação da expressão, ela é adicionada.
     * @param charset O nome do charset no qual o arquivo está escrito
     * @param stopCaseFind Se True, O a leitura do arquivo é interrompida assim que a linha é encontrada
     * 
     * @return Um {@link LinkedList} contendo em cada posição uma linha do arquivo
     * @throws Exception Caso algum erro ocorra uma exceção será lançada
     */
    public static LinkedList<String> getContentFile(File file, String pattern, String charset, boolean stopCaseFind) throws Exception
    {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName(charset)));
        LinkedList<String> retorno = new LinkedList<String>();
        
        Pattern pat = Pattern.compile(StringUtils.ifNullOrEmptyGet(Boolean.TRUE, pattern, ""));

        while (retorno.add(buffer.readLine()))
        {
            if (retorno.getLast() == null)
            {
                retorno.removeLast();
                break;
            }
            else if (!pat.matcher(retorno.getLast()).find())
            {
                retorno.removeLast();
            }
            else if (stopCaseFind)
            {
                break;
            }
        }

        buffer.close();
        return (retorno);
    }

    /**
     * Método que testa se o arquivo está vazio. Esse Método considera os espaços como Conteúdo do arquivo.
     * 
     * @param file O arquivo a ser testado
     * @param charset O nome do charset no qual o arquivo está escrito
     * 
     * @return Se True, o arquivo está vazio
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static boolean fileIsEmpty(File file, String charset) throws Exception
    {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName(charset)));
        boolean retorno = buffer.ready();
        buffer.close();

        return (!retorno);
    }

    /**
     * Método que cria o {@link BufferedReader} para um arquivo
     * 
     * @param file O arquivo que se deseja transformar
     * @param charset O charset no qual o arquivo foi escrito
     * 
     * @return O {@link BufferedReader} criado
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static BufferedReader fileToBufferedReader(File file, String charset) throws Exception
    {
        return (new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName(charset))));
    }

    /**
     * Método que adicionad um {@link ByteArrayInputStream} para um {@link BufferedReader}
     * 
     * @param in O {@link ByteArrayInputStream} a ser adicionado
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static BufferedReader byteArrayInputStreamToBufferedReader(ByteArrayInputStream in) throws Exception
    {
        return (new BufferedReader(new InputStreamReader(in)));
    }

    /**
     * Método que adiciona um {@link InputStream} em um {@link BufferedReader}
     * 
     * @param in O {@link InputStreamReader} a ser adicionado
     * @param charset O charset no qual o {@link InputStreamReader} foi lido
     *  
     * @return O objeto do tipo {@link BufferedReader}
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static BufferedReader inputStreamToBufferedReader(InputStream in, String charset) throws Exception
    {
        return (new BufferedReader(new InputStreamReader(in, Charset.forName(charset))));
    }
    
    /**
     * Método que criar um {@link InputStreamReader} para o arquivo
     * 
     * @param file O arquivo que se deseja transformar
     * @param charset O charset no qual o arquivo foi escrito
     * 
     * @return O Objeto do tipo {@link InputStreamReader}
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static InputStreamReader fileToInputStreamReader(File file, String charset) throws Exception
    {
        return (new InputStreamReader(new FileInputStream(file), Charset.forName(charset)));
    }
}

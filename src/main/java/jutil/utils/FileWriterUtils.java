
package jutil.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import jutil.abstracts.AbstractUtils;

/**
 * Classe utilitaria para trabalhar com escrita de arquivos
 * 
 * @author Diego Steyner
 */
public final class FileWriterUtils extends AbstractUtils 
{
    /**
     * Construtor privado
     */
    private FileWriterUtils()
    {
        throw EXCEPTION_CONSTRUTOR;
    }
    
    /**
     * Método que escreve conteúdo para um arquivo
     * 
     * @param file O arquivo a ser escrito
     * @param content O conteúdo a ser escrito
     * @param charset O charset no qual o conteúdo deve ser escrito
     * @param append Se True, Será automaticamente adicionado a quebra de linha para que a próxima escrita ocorra na próxima linha, caso seja passado como false,
     * o controle de novas linhas deve ser feito através do caracter de controle \n
     * 
     * @return Se True, O conteúdo foi escrito com suscesso
     * @throws IOException Caso ocorra algum erro na escrita, uma exceção será lançada
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static boolean writerContent(File file, String content, String charset, boolean append, boolean createNewLine) throws IOException
    {
        PrintWriter w = new PrintWriter(new FileWriter(file, append), true);
        
        if(createNewLine)
        {
            w.println(new String(content.getBytes(Charset.forName(charset))));
        }
        else 
        {
            w.print(new String(content.getBytes(Charset.forName(charset))));
        }
        
        w.close();
        return(Boolean.TRUE);
    }
    
    public static void writeContentToFile(File file, String content, Charset charset) throws IOException
    {
		try (FileWriter writer = new FileWriter(file); BufferedWriter bw = Files.newBufferedWriter(file.toPath(), charset)) 
		{
			bw.write(content);
		}
    }
    
    public static void wrteContentBuffer(Path file, byte[] data) throws IOException
    {
		try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(file))) 
		{
			out.write(data, 0, data.length);
		} 
    }
    
    /**
     * Método que limpa o conteúdo de um arquivo
     * 
     * @param file O arquivo a ser escrito
     * @param charset O charset no qual o conteúdo deve ser escrito
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static void clearFile(File file, String charset) throws Exception
    {
        PrintWriter w = new PrintWriter(new FileWriter(file, false), true);
        
        w.print(new String("".getBytes(Charset.forName(charset))));
        w.close();
    }
    
    /**
     * Metodo que retorna um {@link PrintWriter}
     * 
     * @param arquivo O arquivo para ser escrito
     * @param append Se o {@link PrintWriter} deve automaticamente adicionar a quebra de linha
     * @param autoFlush Se o {@link PrintWriter} deve automaticamente executar um FLUSH no arquivo 
     * 
     * @return O {@link PrintWriter} criado
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static PrintWriter getPrintWriter(File arquivo, boolean append, boolean autoFlush) throws Exception
    {
        return (new PrintWriter(new FileWriter(arquivo, append), autoFlush));
    }
}

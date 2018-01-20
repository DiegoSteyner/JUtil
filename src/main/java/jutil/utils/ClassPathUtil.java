package jutil.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;

import jutil.abstracts.AbstractUtils;


/**
 * Classe utilitária para se trabalhar com o classpath da aplicação
 * 
 * Limitacoes:
 * <ul>
 * 	<li>Nao vai trocar itens existentes, apenas adiciona-los</li>
 * 	<li>Trabalha somente com URLClassLoaders</li>
 * </ul>
 * 
 * @author Diego Steyner
 */
public final class ClassPathUtil extends AbstractUtils
{
    /**
     * Construtor Privado
     */
    public ClassPathUtil()
    {
    }

    /**
     * Adiciona um arquivo ao ClassPath do programa
     * 
     * @param file O caminho do arquivo
     * @see #addURL(URL)
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public void addFile(File file) throws Exception
    {
        addURL(file.toURI().toURL());
    }

    /**
     * Adiciona uma url ao ClassPath do programa
     * 
     * @param url A URL do arquivo a ser adicionada
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public void addURL(URL url) throws Exception
    {
        Method method = (URLClassLoader.class).getDeclaredMethod("addURL", new Class[] { URL.class });
        method.setAccessible(true);
        method.invoke(((URLClassLoader) ClassLoader.getSystemClassLoader()), new Object[] { url });
    }

    /**
     * Metodo que verifica se um arquivo está ou não adicionado no ClassPath da aplicação
     * 
     * @param file O Arquivo a ser verificado
     * 
     * @return Se True, O arquivo já está no ClassPath
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public boolean containsInClassPath(File file) throws Exception
    {
        return (new ArrayList<File>(Arrays.asList(getClassPath())).contains(new File(file.toURI().toURL().toString().replace("/", "\\"))));
    }

    /**
     * Método que retorna todos os arquivos adicionados no classPath da aplicação
     * 
     * @return Um vetor de {@link File} do ClassPath
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public File[] getClassPath() throws Exception
    {
        ArrayList<File> files = new ArrayList<File>();
        
        try
        {
            URL urls[] = ((URLClassLoader) ClassLoader.getSystemClassLoader()).getURLs();

            for (URL url : urls)
            {
                files.add(new File(url.toString()));
            }
            
            return (files.toArray(new File[files.size()]));
        }
        finally
        {
            files.clear();
        }
    }
}

package jutil.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import jutil.abstracts.AbstractUtils;

/**
 * Classe utilitaria para trabalhar com o ClassLoader
 * 
 * @author Diego Steyner
 */
public class ClassLoaderUtils extends AbstractUtils
{
    /**
     * Construtor privado
     */
    private ClassLoaderUtils()
    {
        throw EXCEPTION_CONSTRUTOR;
    }
    
    /**
     * Método que retorna o ClassLoader atual
     * 
     * @return O {@link ClassLoader} atual
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static ClassLoader getCurrentClassLoad() throws Exception
    {
        return (Thread.currentThread().getContextClassLoader());
    }

    /**
     * Método que retorna todos os {@link ClassLoader} atuais carregados no ClassLoader
     * 
     * @param clazzloader O {@link ClassLoader} que se deseja listar
     * 
     * @return Um vetor com todos os {@link ClassLoader} carregados
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static ClassLoader[] getAllLoader(ClassLoader clazzloader) throws Exception
    {
        List<ClassLoader> retorno = new ArrayList<ClassLoader>();

        while (clazzloader != null)
        {
            retorno.add(clazzloader);
            clazzloader = clazzloader.getParent();
        }

        return (retorno.toArray(new ClassLoader[retorno.size()]));
    }

    /**
     * Método que retorna a classe primária do {@link ClassLoader}
     * 
     * @param clazz A Classe que está no {@link ClassLoader}
     * 
     * @return A Classe primária carregada
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static Class<?> getPrimaryClassLoader(Class<?> clazz) throws Exception
    {
        while (clazz != java.lang.ClassLoader.class)
        {
            if(clazz.getSuperclass() == null)
            {
                break;
            }
            
            clazz = clazz.getSuperclass();
        }

        return (clazz);
    }

    /**
     * Método que retorna todas as classes carregadas no {@link ClassLoader}
     * 
     * @param loader O {@link ClassLoader} que se deseja listar
     * 
     * @return Um vetor contendo todas as Classes no {@link ClassLoader}
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static Class<?>[] getClassInClassload(ClassLoader loader) throws Exception
    {
        Field fields = getPrimaryClassLoader(loader.getClass()).getDeclaredField("classes");

        fields.setAccessible(true);
        Vector<?> classes = (Vector<?>) fields.get(loader);
        List<Class<?>> retorno = new ArrayList<Class<?>>();

        for (Iterator<?> it = classes.iterator(); it.hasNext();)
        {
            retorno.add(((Class<?>) it.next()));
        }

        return (retorno.toArray(new Class[retorno.size()]));
    }

    /**
     * Método que retorna a Classe que foi a chamadora do ClassLoader
     * 
     * @param clazz A Classe presente no {@link ClassLoader}
     * @param loader O {@link ClassLoader}
     * 
     * @return A {@link Class} que foi a chamadora
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static Class<?> getClassCaller(Class<?> clazz, ClassLoader loader) throws Exception
    {
        ClassLoader c[] = getAllLoader(loader);

        for (ClassLoader a : c)
        {
            Class<?> i[] = ClassLoaderUtils.getClassInClassload(a);

            for (int k = 0; k < i.length; k++)
            {
                if (clazz.getName().equals(i[k].getName()))
                {
                    return (i[k - 1]);
                }
            }
        }

        return (null);
    }

    /**
     * Método que imprime todas as classes em todos os {@link ClassLoader}
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static void printAllLoadersAndClass() throws Exception
    {
        ClassLoader c[] = getAllLoader(getCurrentClassLoad());

        for (ClassLoader a : c)
        {
            printLn("Classes no ClassLoader " + a);
            printLn();

            Class<?> i[] = ClassLoaderUtils.getClassInClassload(a);

            for (Class<?> b : i)
            {
                printLn("\t" + b.getName());
            }

            printLn();
        }
    }
}

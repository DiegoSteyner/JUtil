package jutil.abstracts;

import java.io.PrintStream;

/**
 * Classe abstrata para heranca e padronização que possam a vim ser necessárias
 * 
 * @author Diego Steyner
 */
public abstract class AbstractUtils
{
    /**
     * Exceção padronizada para prevenção de inicialização via reflection nas classes estáticas
     */
    protected static final UnsupportedOperationException EXCEPTION_CONSTRUTOR = new UnsupportedOperationException("Esse Construtor é privado e por isso essa classe não pode ser instânciada.");
    
    /**
     * {@link PrintStream} da System
     */
    protected static PrintStream                         out                  = System.out;
    
    /**
     * Método utilitário padronizado de impressão
     */
    public static synchronized void printLn()
    {
        out.println();
    }
    
    /**
     * Método utilitário padronizado de impressão
     * 
     * @param str A String que se deseja imprimir
     */
    public static synchronized void printLn(String str)
    {
        out.println(str);
    }

    /**
     * Método utilitário padronizado de impressão
     * 
     * @param str A(s) String que se deseja imprimir
     */
    public static synchronized void printLn(String... str)
    {
        for (String a : str)
        {
            out.println(a);
        }
    }

    /**
     * Método utilitário padronizado de impressão
     * 
     * @param str O(s) Object que se deseja imprimir
     */
    public static synchronized void printLn(Object... str)
    {
        for (Object a : str)
        {
            printLn(a.toString());
        }
    }

    /**
     * Método utilitário padronizado de impressão
     * 
     * @param list A lista que se deseja imprimir
     */
    public static synchronized void printLn(java.util.List<String> list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            out.println(list.get(i));
        }
    }
    
    /**
     * Método utilitário padronizado de impressão
     * 
     * @param str O inteiro que se deseja imprimir
     */
    public static synchronized void printLn(int str)
    {
        out.println(str);
    }
}

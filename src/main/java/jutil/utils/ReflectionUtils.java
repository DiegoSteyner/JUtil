package jutil.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import jutil.abstracts.AbstractUtils;

/**
 * Classe de Utilitarios para se trabalhar com reflection
 * 
 * @author Diego Steyner
 */
public class ReflectionUtils extends AbstractUtils
{
    /**
     * Construtor privado
     */
    public ReflectionUtils()
    {
    }

    /**
     * Método que invoca um Método de uma outra classe, isso permite invovar métodos privados e protegidos
     * 
     * @param clazz A classe que contem o método
     * @param methods O nome do Método (case sensitive!).
     * @param args Os argumentos do Método no tipo aceito por ele
     * 
     * @return {@link Object} - O valor de retorno como um objeto caso o Método tenha valor de retorno, caso seja <code>void</code> sera retornado <code>null</code>
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public Object invokeUnaccessible(Class<?> clazz, String methods, Object... args) throws Exception
    {
        args = arrayObjectsIsValid(args);

        Class<?>[] types = new Class<?>[args.length];
        
        for (int i = 0; i < args.length; i++)
        {
            types[i] = args[i].getClass();
        }

        Method execMétodo = getCorrectDeclaratedMethod(methods, clazz, types);

        Object instance = null;
        if (!Modifier.isStatic(execMétodo.getModifiers()))
        {
            instance = clazz.newInstance();
        }

        execMétodo.setAccessible(true);
        return (execMétodo.invoke(instance, args));
    }

    /**
     * 
     * Método que invoca um método de uma outra classe
     * 
     * @param instancia O Objeto que contem o Método
     * @param methods O nome do Método (case sensitive!).
     * @param args Os argumentos do método no tipo aceito por ele
     * 
     * @return {@link Object} - O valor de retorno como um objeto caso o Método tenha valor de retorno, caso seja <code>void</code> sera retornado <code>null</code>
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public Object invokeUnaccessible(Object instancia, String methods, Object... args) throws Exception
    {
        args = arrayObjectsIsValid(args);
        Class<?>[] types = new Class<?>[args.length];

        for (int i = 0; i < args.length; i++)
        {
            types[i] = args[i].getClass();
        }

        Method execMethod = getCorrectDeclaratedMethod(methods, instancia.getClass(), types);
        execMethod.setAccessible(true);

        return (execMethod.invoke(instancia, args));
    }

    /**
     * Método que invoca um Método de uma outra classe, Métodos privados ou protegidos nao podem ser chamados
     * 
     * @param clazz A classe que contem o método
     * @param method O nome do Método (case sensitive!). O Método precisa ser marcado como <code>public</code>
     * @param args Os argumentos do método no tipo aceito por ele
     * 
     * @return {@link Object} - O valor de retorno como um objeto caso o Método tenha valor de retorno, caso seja <code>void</code> sera retornado <code>null</code>
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public Object invoke(Class<?> clazz, String method, Object... args) throws Exception
    {
        args = arrayObjectsIsValid(args);
        
        Class<?>[] types = new Class<?>[args.length];
        
        for (int i = 0; i < args.length; i++)
        {
            types[i] = args[i].getClass();
        }
        
        Method execMethod = getCorrectMethod(method, clazz, types);

        Object instance = null;
        if (!Modifier.isStatic(execMethod.getModifiers()))
        {
            instance = clazz.newInstance();
        }
        
        return (execMethod.invoke(instance, args));
    }

    /**
     * Método que invoca um Método de uma outra classe, Métodos privados ou protegidos nao podem ser chamados
     * 
     * @param instancia O Objeto que contem o Método
     * @param method O nome do Método (case sensitive!). O Método precisa ser marcado como <code>public</code>
     * @param args Os argumentos do Método no tipo aceito por ele
     * 
     * @return {@link Object} - O valor de retorno como um objeto caso o Método tenha valor de retorno, caso seja <code>void</code> sera retornado <code>null</code>
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public Object invoke(Object instancia, String method, Object... args) throws Exception
    {
        args = arrayObjectsIsValid(args);
        Class<?>[] tipos = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++)
        {
            tipos[i] = args[i].getClass();
        }
        Method execMethod = getCorrectMethod(method, instancia.getClass(), tipos);
        return execMethod.invoke(instancia, args);
    }

    /**
     * Método que retorna uma classe primitiva
     * 
     * <ul>
     *  <li>{@link Boolean} => <code>boolean</code></li>
     *  <li>{@link Byte} => <code>byte</code></li>
     *  <li>{@link Character} => <code>char</code></li>
     *  <li>{@link Double} => <code>double</code></li>
     *  <li>{@link Float} => <code>float</code></li>
     *  <li>{@link Integer} => <code>int</code></li>
     *  <li>{@link Long} => <code>long</code></li>
     *  <li>{@link Short} => <code>short</code></li>
     * </ul>
     * 
     * @param clazz A Classe primitiva
     * 
     * @return {@link Class} - A classe primitiva
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public Class<?> getClassePrimitiva(Class<?> clazz) throws Exception
    {
        if (clazz != null && isPrimitiveObject(clazz))
        {
            if (clazz.equals(Boolean.class))
            {
                return (boolean.class);
            }
            else if (clazz.equals(Byte.class))
            {
                return (byte.class);
            }
            else if (clazz.equals(Character.class))
            {
                return (char.class);
            }
            else if (clazz.equals(Double.class))
            {
                return (double.class);
            }
            else if (clazz.equals(Float.class))
            {
                return (float.class);
            }
            else if (clazz.equals(Integer.class))
            {
                return (int.class);
            }
            else if (clazz.equals(Long.class))
            {
                return (long.class);
            }
            else if (clazz.equals(Short.class))
            {
                return (short.class);
            }
        }
        
        return (null);
    }

    /**
     * Método que checa se a classe e uma classe primitiva em forma de objeto
     * 
     * @param clazz A classe a ser checada
     * 
     * @return Se True, É um objeto de classe primitiva
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public boolean isPrimitiveObject(Class<?> clazz) throws Exception
    {
        return (((clazz != null) && (clazz.equals(Boolean.class) || clazz.equals(Byte.class) || clazz.equals(Character.class) || clazz.equals(Double.class) || clazz.equals(Float.class) || clazz.equals(Integer.class) || clazz.equals(Long.class) || clazz.equals(Short.class))));
    }

    /**
     * Método que checa se a classe e uma classe de um tipo primitivo puro
     * 
     * @param clazz A classe a ser checada
     * 
     * @return Se True, e uma classe primitiva pura
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public boolean isPrimitivePure(Class<?> clazz) throws Exception
    {
        return (((clazz != null) && (clazz.equals(boolean.class) || clazz.equals(byte.class) || clazz.equals(char.class) || clazz.equals(double.class) || clazz.equals(float.class) || clazz.equals(int.class) || clazz.equals(long.class) || clazz.equals(short.class))));
    }

    /**
     * Método que cria uma instância para a classe
     * 
     * @param clazz O nome da classe que se deseja criar a instância
     * 
     * @return O Objeto com a instância da classe
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public Object createInstanceForClass(String clazz) throws Exception 
    {
        return find(clazz).newInstance();
    }
    
    /**
     * Método que retorna o método correto para execução
     * 
     * @param methodName O nome do Método
     * @param clazz A classe que contem o Método
     * @param type Os tipos dos argumentos
     * 
     * @return O método de execução equivalente ao nome passado
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    private Method getCorrectMethod(String methodName, Class<?> clazz, Class<?>[] type) throws Exception
    {
        List<Class<?>[]> realArgs = getPossibleArguments(type, new ArrayList<Class<?>[]>());
        
        for (Class<?>[] args : realArgs)
        {
            try
            {
                realArgs.clear();
                return (clazz.getMethod(methodName, args));
            }
            catch (NoSuchMethodException ex)
            {
                continue;
            }
        }
        
        throw new Exception("Nenhum Método encontrado com os argumentos disponiveis");
    }

    /**
     * Método que retorna o método declarado correto
     * 
     * @param methodName O nome do Método
     * @param clazz A classe que contem o Método
     * @param type Os tipos dos argumentos do Método
     * 
     * @return O Método correto
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    private Method getCorrectDeclaratedMethod(String methodName, Class<?> clazz, Class<?>[] type) throws Exception
    {
        List<Class<?>[]> realArgs = getPossibleArguments(type, new ArrayList<Class<?>[]>());
        
        for (Class<?>[] v : realArgs)
        {
            try
            {
                realArgs.clear();
                return (clazz.getDeclaredMethod(methodName, v));
            }
            catch (NoSuchMethodException nsme)
            {
                continue;
            }
        }
        
        throw new Exception("Não foi encontrado nenhum método com os tipos de argumentos passados");
    }

    /**
     * Método que verifica se a classe contem o array de tipos e argumentos
     * 
     * @param list A Lista de classes
     * @param types Os tipos disponiveis
     * 
     * @return Se True, a classe possui o método declarado
     * @throws Exception Caso ocorra algum erro uma excessao sera lancada
     */
    private boolean containsClassArray(List<Class<?>[]> list, Class<?>[] types) throws Exception
    {
        boolean contains = true;
        
        for (Class<?>[] clazz : list)
        {
            contains = true;

            if (clazz.length != types.length)
            {
                contains = false;
                continue;
            }

            for (int i = 0; i < clazz.length; i++)
            {
                if (!clazz[i].equals(types[i]))
                {
                    contains = false;
                    continue;
                }
            }
            
            if (contains)
            {
                return (Boolean.TRUE);
            }
        }
        
        return (Boolean.FALSE);
    }

    /**
     * Método que pega os argumentos possíveis na classe
     * 
     * @param types Os tipos
     * @param possibleTypes Os tipos possiveis
     * 
     * @return A lista de classes
     * @throws Exception Caso ocorra algum erro uma excessao sera lancada
     */
    private List<Class<?>[]> getPossibleArguments(Class<?>[] types, List<Class<?>[]> possibleTypes) throws Exception
    {
        if (!containsClassArray(possibleTypes, types))
        {
            possibleTypes.add(types);
        }

        for (int i = 0; i < types.length; i++)
        {
            if (isPrimitiveObject(types[i]))
            {
                Class<?>[] changedTypes = new Class<?>[types.length];
                
                for (int j = 0; j < types.length; j++)
                {
                    changedTypes[j] = types[j];
                }
                
                changedTypes[i] = getClassePrimitiva(types[i]);

                if (!containsClassArray(possibleTypes, changedTypes))
                {
                    possibleTypes.add(changedTypes);
                }

                possibleTypes = getPossibleArguments(changedTypes, possibleTypes);
            }
        }

        return (possibleTypes);
    }

    /**
     * Método que valida o array de argumentos passado
     * 
     * @param v O array de Objetos
     * 
     * @return Caso null, sera criado um novo array caso nao, entao sera retornado o proprio array
     */
    private Object[] arrayObjectsIsValid(Object[] v)
    {
        if (v == null)
        {
            v = new Object[] {};
        }

        return (v);
    }
    
    /**
     * Método que encontra uma determinada classe
     * 
     * @param name O nome da classe que se está procurando
     * 
     * @return A classe encontrada
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    private Class<?> find(String name) throws Exception 
    {
        Class<?> c = Class.forName(name);
        
        if (c.isInterface()) 
        {
            throw new Exception("Interfaces não podem ser injetadas no escopo de micro containers");
        }
        
        return (c);
    }
}

package jutil.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import jutil.abstracts.AbstractUtils;

/**
 * Classe utilitaria para trabalhar com serialização de objetos
 * 
 * @author Diego Steyner
 */
public class SerializableUtils extends AbstractUtils
{
    /**
     * Uma das possiveis extensões de arquivo serializados.
     */
    public static final String EXTENSAO_SERIALIZAR = ".ser";

    /**
     * Construtor privado
     */
    private SerializableUtils()
    {
        throw EXCEPTION_CONSTRUTOR;
    }

    /**
     * Método que serializa um objeto para o disco
     * 
     * @param object O objeto a ser serializado
     * @param dir O diretório onde o objeto deve ser salvo
     * @param fileName O nome do arquivo com extensão e tudo
     * 
     * @return O {@link File} do objeto serializado
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static File serializaObjeto(Object object, File dir, String fileName) throws Exception
    {
        if (dir.isFile())
        {
            throw new Exception("O local de salvamento precisa ser um diretório");
        }
        else if (!(object instanceof Serializable))
        {
            throw new Exception("O objeto passado não é uma instância serializável.");
        }

        File arqSer = new File(dir, fileName);

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(arqSer));
        out.writeObject(object);
        out.flush();
        out.close();

        return (arqSer);
    }

    /**
     * Método que deserializa um objeto serializado
     * 
     * @param file O caminho completo do arquivo a ser deserializado
     * 
     * @return Um objeto do arquivo serializado
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static Object deserializaObjeto(File file) throws Exception
    {
        if (file.isDirectory())
        {
            throw new Exception("O arquivo passado para deserialização é um diretório");
        }

        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        Object objeto = in.readObject();
        in.close();
        return (objeto);
    }

    /**
     * Método que retorna o nome da classe do Objeto
     * 
     * @param objeto O objeto
     * 
     * @return O nome da classe do objeto
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static String getNameClass(Object objeto) throws Exception
    {
        return (((Serializable) objeto).getClass().getSimpleName());
    }
}

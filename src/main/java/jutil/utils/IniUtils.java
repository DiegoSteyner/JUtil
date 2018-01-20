package jutil.utils;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import jutil.abstracts.AbstractUtils;

/**
 * Classe utilitaria para se trabalhar com arquivos .INI
 * 
 * @author Diego Steyner
 */
public class IniUtils extends AbstractUtils
{
    public static final String REGEX_REPLACE_BRACKETS = "(\\[|\\])";
    public static final String REGEX_SECTION          = "^\\[.*\\]";
    public static final String REGEX_PROPERTIES       = "^(?!;).*\\=.*";
    
    /**
     * Construtor privado
     */
    private IniUtils()
    {
        throw EXCEPTION_CONSTRUTOR;
    }
    
    /**
     * Método que cria um {@link Map} à partir de um arquivo .ini
     * 
     * @param iniFile O endereço do arquivo .ini
     * @param charset O charset no qual o arquivo foi escrito
     * @param removeBrackets Se True, Os colchetes serão removidos dos nomes das seções
     * 
     * @return Um {@link Map} com o conteúdo do arquivo .ini
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static Map<String, Properties> getIniAsMap(File iniFile, String charset, boolean removeBrackets) throws Exception
    {
        Map<String, Properties> retorno = new HashMap<>();
        LinkedList<String> lines = FileReaderUtils.getContentFile(iniFile, charset);
        
        Pattern section = Pattern.compile(REGEX_SECTION);
        Pattern props = Pattern.compile(REGEX_PROPERTIES);
        int lastSection = 0;
        String temp[];
        
        for (int i = 0; i < lines.size(); i++)
        {
            if(section.matcher(lines.get(i)).find())
            {
                lastSection = i;
                
                if(removeBrackets)
                {
                    retorno.put(lines.get(i).replaceAll(REGEX_REPLACE_BRACKETS, ""), new Properties());
                    continue;
                }
                
                retorno.put(lines.get(i), new Properties());
            }
            else if(props.matcher(lines.get(i)).find())
            {
                temp = lines.get(i).split("=");
                
                if(removeBrackets)
                {
                    retorno.get(lines.get(lastSection).replaceAll(REGEX_REPLACE_BRACKETS, "")).put(temp[0], temp[1]);
                    continue;
                }
                
                retorno.get(lines.get(lastSection)).put(temp[0], temp[1]);
            }
        }
        
        return(retorno);
    }
    
    /**
     * Método que formata um {@link Map} como conteúdo de um arquivo .ini</br>
     * Obs: Se os nomes das seções não contiverem os colchetes, eles serão adicionados automaticamente
     * 
     * @param ini O {@link Map} do arquivo .ini
     * 
     * @return Uma String formata como conteúdo de um arquivo .ini
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String iniMapToString(Map<String, Properties> ini) throws Exception
    {
        StringBuilder retorno = new StringBuilder();
        Pattern section = Pattern.compile(REGEX_SECTION);
        
        for(String a : ini.keySet())
        {
            if(!section.matcher(a).find())
            {
                retorno.append("[").append(a).append("]").append("\n");
            }
            else
            {
                retorno.append(a).append("\n");
            }
            
            for(Object b : ini.get(a).keySet())
            {
                retorno.append(b.toString()).append("=").append(ini.get(a).getProperty(b.toString())).append("\n");
            }
        }
        
        return(retorno.toString());
    }
}

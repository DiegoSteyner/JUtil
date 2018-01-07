package jutil.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import jutil.abstracts.AbstractUtils;

/**
 * Classe utilitaria para se trabalhar com Strings
 * 
 * @author Diego Steyner
 */
public class StringUtils extends AbstractUtils
{
    public static final String   REGEX_FIND_NON_DIGIT     = "\\D";
    public static final String[] DIGITOS_HEXADECIMAIS     = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    public static final String   REGEX_ONLY_NUMBERS       = "^\\d+$";
    public static final String   REGEX_TRIM_RIGHT         = "\\s+$";
    public static final String   REGEX_TRIM_LEFT          = "^\\s+";
    public static final String   REGEX_ESPACOS_DUPLICADOS = "\\s+";
    
    /**
     * Construtor privado
     */
    private StringUtils()
    {
        throw EXCEPTION_CONSTRUTOR;
    }

    /**
     * Metodo que testa se uma String tem letras acentuadas
     *
     * @param str A String que se deseja testar
     *
     * @return Se True, a String possui letras com acentos
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static boolean containsEspecials(String str) throws Exception
    {
        char s[] = getEspecialsLetters().toCharArray();
        
        for (char i : s)
        {
            for (int k = 0; k < str.length(); k++)
            {
                if (str.charAt(k) == i)
                {
                    return (Boolean.TRUE);
                }
            }
        }
        
        return (Boolean.FALSE);
    }

    /**
     * Método que testa se uma String contém algum das outras Strings
     * 
     * @param a A String que se deseja testar
     * @param b As String que se deseja verificar a existência
     * 
     * @return Se True, A String a contem alguma das String b
     */
    public static boolean containsAny(String a, String... b)
    {
        for (int i = 0; i < b.length; i++)
        {
            if (a.contains(b[i]))
            {
                return (Boolean.TRUE);
            }
        }

        return (Boolean.FALSE);
    }

    /**
     * Metodo que retorna as letras do alfabeto em maiúsculas do padrão UTF-16 do Java (http://www.oracle.com/us/technologies/java/supplementary-142654.html) 
     * das tabelas Latin (http://www.unicode.org/charts/)
     *
     * @return As letras maiúsculas dos unicodes 65 à 91
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String getAlphabetInUpperCase() throws Exception
    {
        StringBuilder abc = new StringBuilder();

        for (int i = 65; i < 91; i++)
        {
            abc.append(((char) i));
        }
        
        return (abc.toString());
    }

    /**
     * Metodo que retorna as letras do alfabeto em minúsculas do padrão UTF-16 do Java (http://www.oracle.com/us/technologies/java/supplementary-142654.html) 
     * das tabelas Latin (http://www.unicode.org/charts/)
     *
     * @return As letras minusculas dos unicodes 97 à 123
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String getAlphabetInLowerCase() throws Exception
    {
        StringBuilder abc = new StringBuilder();

        for (int i = 97; i < 123; i++)
        {
            abc.append(((char) i));
        }
        
        return (abc.toString());
    }

    /**
     * Metodo que retorna as letras especiais do padrão UTF-16 do Java (http://www.oracle.com/us/technologies/java/supplementary-142654.html) 
     * das tabelas Latin (http://www.unicode.org/charts/)
     *
     * @return As letras especiais dos unicodes 192 à 260
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String getEspecialsLetters() throws Exception
    {
        StringBuilder abc = new StringBuilder();

        for (int i = 192; i < 260; i++)
        {
            abc.append(((char) i));
        }
        return (abc.toString());
    }

    /**
     * Metodo que retorna os numeros do padrão UTF-16 do Java (http://www.oracle.com/us/technologies/java/supplementary-142654.html) 
     * das tabelas Latin (http://www.unicode.org/charts/)
     *
     * @return Os numeros dos unicodes 40 à 58
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String getNumbers() throws Exception
    {
        StringBuilder abc = new StringBuilder();

        for (int i = 48; i < 58; i++)
        {
            abc.append(((char) i));
        }
        return (abc.toString());
    }

    /**
     * Metodo que retorna as pontuaçães do padrão UTF-16 do Java (http://www.oracle.com/us/technologies/java/supplementary-142654.html) 
     * das tabelas Latin (http://www.unicode.org/charts/)
     *
     * @return As pontuaçães dos unicodes 33 à 48, 58 à 65, 91 à 97, 123 à 127 e 247 à 255
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String getScores() throws Exception
    {
        StringBuilder abc = new StringBuilder();

        for (int i = 33; i < 48; i++)
        {
            abc.append(((char) i));
        }
        for (int i = 58; i < 65; i++)
        {
            abc.append(((char) i));
        }
        for (int i = 91; i < 97; i++)
        {
            abc.append(((char) i));
        }
        for (int i = 123; i < 127; i++)
        {
            abc.append(((char) i));
        }
        for (int i = 247; i < 255; i++)
        {
            abc.append(((char) i));
        }

        return (abc.toString());
    }

    /**
     * Metodo que limpa um ou mais caracteres em uma String
     * 
     * @param str A String a ser limpa
     * @param strs Os Caracteres a serem removidos
     * 
     * @return A String sem os caracteres passados
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String clearChars(String str, String strs) throws Exception
    {
        String temp = str;
        for (int i = 0; i < strs.length(); i++)
        {
            temp = temp.replace(String.valueOf(strs.charAt(i)), "");
        }
        return (temp);
    }

    /**
     * Metodo que limpa todos os caracters especiais de uma String pelos seus equivalentes sem ascento
     *
     * @param str A String que se deseja limpar
     *
     * @return A String limpa sem caracter especiais
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String replaceSpecials(String str) throws Exception
    {
        String s = str;
        
        for (int i = 192; i < 199; i++)
        {
            if (s.contains(String.valueOf(((char) i))))
            {
                s = s.replace(((char) i), ((char) 65));
            }
        }
        for (int i = 200; i < 204; i++)
        {
            if (s.contains(String.valueOf(((char) i))))
            {
                s = s.replace(((char) i), ((char) 69));
            }
        }
        for (int i = 204; i < 208; i++)
        {
            if (s.contains(String.valueOf(((char) i))))
            {
                s = s.replace(((char) i), ((char) 73));
            }
        }
        for (int i = 210; i < 215; i++)
        {
            if (s.contains(String.valueOf(((char) i))))
            {
                s = s.replace(((char) i), ((char) 79));
            }
        }
        for (int i = 217; i < 221; i++)
        {
            if (s.contains(String.valueOf(((char) i))))
            {
                s = s.replace(((char) i), ((char) 85));
            }
        }
        for (int i = 224; i < 231; i++)
        {
            if (s.contains(String.valueOf(((char) i))))
            {
                s = s.replace(((char) i), ((char) 97));
            }
        }
        for (int i = 232; i < 236; i++)
        {
            if (s.contains(String.valueOf(((char) i))))
            {
                s = s.replace(((char) i), ((char) 101));
            }
        }
        for (int i = 236; i < 240; i++)
        {
            if (s.contains(String.valueOf(((char) i))))
            {
                s = s.replace(((char) i), ((char) 105));
            }
        }
        for (int i = 242; i < 247; i++)
        {
            if (s.contains(String.valueOf(((char) i))))
            {
                s = s.replace(((char) i), ((char) 111));
            }
        }
        for (int i = 249; i < 253; i++)
        {
            if (s.contains(String.valueOf(((char) i))))
            {
                s = s.replace(((char) i), ((char) 117));
            }
        }

        if (s.contains(String.valueOf(((char) 199))))
        {
            s = s.replace(((char) 199), ((char) 67));
        }
        if (s.contains(String.valueOf(((char) 208))))
        {
            s = s.replace(((char) 208), ((char) 68));
        }
        if (s.contains(String.valueOf(((char) 209))))
        {
            s = s.replace(((char) 209), ((char) 78));
        }
        if (s.contains(String.valueOf(((char) 216))))
        {
            s = s.replace(((char) 216), ((char) 48));
        }
        if (s.contains(String.valueOf(((char) 248))))
        {
            s = s.replace(((char) 248), ((char) 48));
        }
        if (s.contains(String.valueOf(((char) 221))))
        {
            s = s.replace(((char) 221), ((char) 89));
        }
        if (s.contains(String.valueOf(((char) 222))))
        {
            s = s.replace(((char) 222), ((char) 80));
        }
        if (s.contains(String.valueOf(((char) 223))))
        {
            s = s.replace(((char) 223), ((char) 66));
        }
        if (s.contains(String.valueOf(((char) 231))))
        {
            s = s.replace(((char) 231), ((char) 99));
        }
        if (s.contains(String.valueOf(((char) 240))))
        {
            s = s.replace(((char) 240), ((char) 100));
        }
        if (s.contains(String.valueOf(((char) 241))))
        {
            s = s.replace(((char) 241), ((char) 110));
        }
        if (s.contains(String.valueOf(((char) 253))))
        {
            s = s.replace(((char) 253), ((char) 121));
        }
        if (s.contains(String.valueOf(((char) 255))))
        {
            s = s.replace(((char) 255), ((char) 121));
        }
        if (s.contains(String.valueOf(((char) 254))))
        {
            s = s.replace(((char) 254), ((char) 98));
        }
        
        return (s);
    }

    /**
     * Metodo que verifica se uma String contem somente numeros
     *
     * @param str A String que se deseja verificar
     * 
     * @return Se True, a string possui somente números
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static boolean onlyNumbers(String str) throws Exception
    {
        return (str.matches(REGEX_ONLY_NUMBERS));
    }

    /**
     * Metodo que verifica se uma String é nula ou vazia
     *
     * @param str A String que se deseja verificar
     * @param trim Se True, Será chamado o método String.trim() antes da comparação
     *
     * @return Se True, A String é nula ou vazia
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static boolean isNullOrEmpty(boolean trim, String... str) throws Exception
    {
        if (str != null)
        {
            for (int i = 0; i < str.length; i++)
            {
                if (str[i] == null)
                {
                    return (Boolean.TRUE);
                }
                if (((trim) ? str[i].trim().length() : str[i].length()) == 0)
                {
                    return (Boolean.TRUE);
                }
            }

            return (Boolean.FALSE);
        }

        return (Boolean.TRUE);
    }
    
    /**
     * Método que verifica se uma String não é nula ou vazia
     * 
     * @param str A String que se deseja verificar
     * @param trim Se True, Será chamado o método String.trim() antes da comparação
     * 
     * @return Se True, A String não é vazia nem nula
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static boolean isNotNullOrEmpty(boolean trim, String... str) throws Exception
    {
        return (!isNullOrEmpty(trim, str));
    }

    /**
     * Método que testa se uma String é nula ou vazia e, caso seja, retorna um valor diferente.
     * 
     * @param str A String a ser testada
     * @param retorno O retorno caso a String seja nula ou vazia
     * @param trim Se True, Será chamado o método String.trim() antes da comparação
     * 
     * @return Se a String for nula ou vazia, será retornada a variável passada, caso não, será retornada a própria String
     * @throws Exception Caso algum erro ocorra uma excessao sera lancada
     */
    public static String ifNullOrEmptyGet(boolean trim, String str, String retorno) throws Exception
    {
        if (isNullOrEmpty(trim, str))
        {
            return (retorno);
        }

        return (str);
    }

    /**
     * Metodo que converte os caracteres de 0 à 1299 da tabela UTF-16 do Java (http://www.oracle.com/us/technologies/java/supplementary-142654.html) para um Array
     * 
     * @return Um vetor contendo em cada posicao um caracter
     * @throws Exception Caso ocorra algum erro uma excessao sera lancada
     */
    public static String[] getUTF16Table() throws Exception
    {
        ArrayList<String> ar = new ArrayList<String>();
        char ch = ' ';

        for (int i = 0; i <= 1299; i++)
        {
            ch = (char) i;
            ar.add(String.valueOf(ch));
        }

        return (((String[]) ar.toArray(new String[ar.size()])));
    }

    /**
     * Metodo que {@link InputStream} para String
     * 
     * @param stream O objeto {@link InputStream} que se deseja converter
     * @param autoCloseStream Se True, O método tentará fechar o {@link InputStream}
     * 
     * @return Uma String com o valor do Objeto
     * @throws Exception Caso ocorra algum erro uma excessao sera lancada
     */
    public String inputStreamToString(java.io.InputStream stream, boolean autoCloseStream) throws Exception
    {
        java.io.BufferedInputStream bre = new java.io.BufferedInputStream(stream);
        StringBuilder sb = new StringBuilder();

        int readInt = bre.read();

        while (readInt != -1)
        {
            sb.append((char) readInt);
            readInt = bre.read();
        }
        
        if(autoCloseStream)
        {
            stream.close();
        }
        
        bre.close();
        return (sb.toString());
    }

    /**
     * Metodo que remove todos os espacos duplicados de uma String
     *
     * @param str A String que e para ser verificada
     *
     * @return Uma string sem espacos Duplicados
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String removeDuplicateSpaces(String str) throws Exception
    {
        return(str.replaceAll(REGEX_ESPACOS_DUPLICADOS, " "));
    }

    /**
     * Método que captitaliza a String para UpperCase
     * 
     * @param str A String a ser captitalizada
     * 
     * @return A String capitalizada em UpperCase
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String capitalizeToUpperCase(String str) throws Exception
    {
        StringBuilder b = new StringBuilder();
        
        for (int i = 0; i < str.length(); i++)
        {
            b.append(Character.toUpperCase(str.charAt(i)));
        }
        
        return b.toString();
    }

    /**
     * Método que captitaliza a String para LowerCase
     * 
     * @param str A String a ser captitalizada
     * 
     * @return A String capitalizada em LowerCase
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String capitalizeToLowerCase(String str) throws Exception
    {
        StringBuilder b = new StringBuilder();
        
        for (int i = 0; i < str.length(); i++)
        {
            b.append(Character.toLowerCase(str.charAt(i)));
        }
        
        return b.toString();
    }

    /**
     * Método que captitaliza a String invertendo o case de cada letra
     * 
     * @param str A String a ser captitalizada
     * 
     * @return A String capitalizada com o case invertido
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String capitalizeToToggleCase(String str) throws Exception
    {
        StringBuilder b = new StringBuilder();
        
        for (int i = 0; i < str.length(); i++)
        {
            b.append((Character.isUpperCase(str.charAt(i))) ? Character.toLowerCase(str.charAt(i)) : Character.toUpperCase(str.charAt(i)));
        }
        
        return b.toString();
    }

    /**
     * Método que captitaliza a String para CamelCase
     * 
     * @param str A String a ser captitalizada
     * 
     * @return A String capitalizada em CamelCase
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String capitalizeToCamelCase(String str) throws Exception
    {
        StringBuilder b = new StringBuilder();
        int lastPosition = 0;
        
        for (int i = 0; i < str.length(); i++)
        {
            if(!Character.isUpperCase(str.charAt(i)) && !Character.isLowerCase(str.charAt(i)))
            {
                lastPosition = i;
                lastPosition++;
                b.append(str.charAt(i));
            }
            else if(lastPosition == i)
            {
                b.append(Character.toUpperCase(str.charAt(i)));
            }
            else
            {
                b.append(Character.toLowerCase(str.charAt(i)));
            }
        }
        return(b.toString());
    }

    /**
     * metodo que inverte todas as posicoes de uma String
     *
     * @param string A string que se deseja inverter
     *
     * @return Um string com todas as posicoes Invertidas
     * @throws Exception Caso ocorra algum erro uma excessao sera lancada
     */
    public static String inverseString(final String string) throws Exception
    {
        return (((new StringBuilder(string)).reverse()).toString());
    }

    /**
     * Metodo que testa se a String passada como parametro esta toda em Maiúscula
     *
     * @param str A String que e para ser testada
     *
     * @return Se True, A String esta toda em maiscula
     * @throws Exception Caso ocorra algum erro uma excessao sera lancada
     */
    public static boolean isAllCharInUpperCase(String str) throws Exception
    {
        for (int i = 0; i < str.length(); i++)
        {
            if (!Character.isUpperCase(str.charAt(i)) & !Character.isSpaceChar(str.charAt(i)))
            {
                return (Boolean.FALSE);
            }
        }

        return (Boolean.TRUE);
    }

    /**
     * Metodo que testa se a String passada como parâmetro esta toda em minúscula
     *
     * @param str A String que e para ser testada
     *
     * @return Se True, A String esta toda em minúscula
     * @throws Exception Caso ocorra algum erro uma excessao sera lancada
     */
    public static boolean isAllCharInLowerCase(String str) throws Exception
    {
        char verificar[] = str.toCharArray();

        for (int i = 0; i < verificar.length; i++)
        {
            if (!Character.isLowerCase(verificar[i]) & !Character.isSpaceChar(verificar[i]))
            {
                return (Boolean.FALSE);
            }
        }

        return (Boolean.TRUE);
    }

    /**
     * Metodo que conta a quantidade de vezes que uma determinada Substring aparece em uma String
     *
     * @param string A string que se deseja que seja verificada
     * @param subString A subString que se esta procurando
     *
     * @return Um inteiro que corresponde a quantidade de vezes que a subString aparece na string
     * @throws Exception Caso ocorra algum erro uma excessao sera lancada
     */
    public static int countSubstring(String string, String subString) throws Exception
    {
        int cont = 0;
        String res = "";

        for (int i = 0; i < (string.length() - subString.length() + 1); i++)
        {
            res = string.substring(i, (i + subString.length()));

            if (res.equals(subString))
            {
                cont++;
            }
        }

        res = null;
        return (cont);
    }

    /**
     * Método que transforma uma StackTrace em String
     * 
     * @param t A StackTrace que se deseja converter
     * 
     * @return A StackTrace em String
     */
    public static final String stackTraceToString(Exception t)
    {
        String s = null;

        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            t.printStackTrace(new PrintWriter(baos, true));
            s = baos.toString();

            baos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return s;
    }

    /**
     * Método que quebra uma String em várias substring de tamanho fixo
     * 
     * @param text A String que se deseja quebrar
     * @param width O tamanho de cada substring
     * 
     * @return Uma String com uma Substring em cada linha
     * @throws Exception Caso ocorra algum erro uma excessao sera lancada
     */
    public static String wrapText(String text, int width) throws Exception
    {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new StringReader(text));

        String s = "";

        while ((s = br.readLine()) != null)
        {
            if (s.length() == 0)
            {
                sb.append("\n");
            }
            else
            {
                while (true)
                {
                    int idx = s.lastIndexOf(' ', width);

                    if ((idx == -1) && (s.length() > width))
                    {
                        sb.append(s.substring(0, width));
                        sb.append("\n");

                        s = s.substring(width, s.length()).trim();
                    }
                    else if ((idx != -1) && (s.length() > width))
                    {
                        sb.append(s.substring(0, idx));
                        sb.append("\n");

                        s = s.substring(idx, s.length()).trim();
                    }
                    else
                    {
                        sb.append(s);
                        sb.append("\n");

                        break;
                    }
                }
            }
        }

        return sb.toString();
    }

    /**
     * Metodo que conta a quantidade de vezes que um determinado caracter aparece numa String
     *
     * @param str A String que e para ser verificada
     * @param caracter O caracter que se deseja que seja verificado
     *
     * @return Um inteiro que corresponde a quantidade de vezes que o caracter passado como parametro aparece na String
     * @throws Exception Caso ocorra algum erro uma excessao sera lancada
     */
    public static int characterCount(String str, char caracter) throws Exception
    {
        return ((str.length() - str.replaceAll(String.valueOf(caracter), "").length()));
    }

    /**
     * Método que faz um trim() somente a esqueda da String
     * 
     * @param str A String que se deseja fazer o Trim()
     * 
     * @return A String sem os espa�os a esquerda
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String trimRight(String str) throws Exception
    {
        return (Pattern.compile(REGEX_TRIM_RIGHT).matcher(str).replaceAll(""));
    }

    /**
     * Método que adiciona zeros a esquerda de um número
     * 
     * @param qtd A quantidade de zeros desejada
     * @param value O numero
     * 
     * @return A String do número com os zeros adicionados
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     * @see https://docs.oracle.com/javase/tutorial/java/data/numberformat.html
     */
    public static String addZeroToLeft(int qtd, int value) throws Exception
    {
        return (String.format(new StringBuilder().append("%0").append(qtd+String.valueOf(value).length()).append("d").toString(), value));
    }

    /**
     * Método que adiciona zeros a esquerda de um número para manter o tamanho fixo
     * 
     * @param size O tamanho que a String deve ter
     * @param value O numero
     * 
     * @return A String do número com os zeros adicionados
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     * 
     * @see https://docs.oracle.com/javase/tutorial/java/data/numberformat.html
     */
    public static String formatWithZeroLeft(int size, int value) throws Exception
    {
        return (String.format(new StringBuilder().append("%0").append(size).append("d").toString(), value));
    }
    
    /**
     * Método que faz um trim() somente a direita da String
     * 
     * @param str A String que se deseja fazer o Trim()
     * 
     * @return A String sem os espaços a direita
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String trimLeft(String str) throws Exception
    {
        return (Pattern.compile(REGEX_TRIM_LEFT).matcher(str).replaceAll(""));
    }

    /**
     * Metodo que retorna o valor do .toString() caso ele nao seja nulo, caso o Objeto seja nulo ele retorna uma String vazia;
     *
     * @param objeto O Objeto que se deseja converter
     * @return O valor String
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String getStringNotNull(Object objeto) throws Exception
    {
        return((objeto != null) ? objeto.toString() : "");
    }

    /**
     * Metodo que cria um Objeto
     *
     * @param str A String que se deseja transformar em Objeto
     *
     * @return Um objeto
     * @exception Exception Caso ocorra algum erro uma excessao sera lancada
     */
    public Object stringToObject(final String str) throws Exception
    {
        return new Object()
        {
            @Override
            public String toString()
            {
                return str;
            }
        };
    }

    /**
     * Metodo que extrai os numeros presentes em uma String
     * 
     * @param str A String a ser analisada
     * 
     * @return A String contendo os numeros
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String extractNumbers(String str) throws Exception
    {
        return(str.replaceAll(REGEX_FIND_NON_DIGIT, ""));
    }

    /**
     * Método que verifica se uma String começa com uma determinada String ignorando o case de ambas
     * 
     * @param str A String que se deseja verificar
     * @param start A String que se está procurando
     * 
     * @return Se True, A String verificada começa com a String procurada
     */
    public static boolean startsWithIgnoreCase(String str, String start)
    {
        return(Pattern.compile(new StringBuilder().append("^(").append(start).append(")").toString(), Pattern.CASE_INSENSITIVE).matcher(str).find());
    }

    /**
     * Método que verifica se uma String contém outra ignorando a case de ambas
     * 
     * @param a A String original
     * @param b A String que se deseja verificar
     * 
     * @return Se True, A String a contém a String b
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static boolean containsIgnoreCase(String a, String b) throws Exception
    {
        return(Pattern.compile(new StringBuilder().append("(").append(b).append(")").toString(), Pattern.CASE_INSENSITIVE).matcher(a).find());
    }
    
    /**
     * Método que verifica se uma String contém qualquer caracter de outra
     * 
     * @param a A String original
     * @param b A String que se deseja verificar
     * 
     * @return Se True, A String a contém algum caracter da String b
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static boolean containsAnyChar(String a, String b) throws Exception
    {
        for (int i = 0; i < b.length(); i++)
        {
            if (a.contains(String.valueOf(b.charAt(i))))
            {
                return (Boolean.TRUE);
            }
        }

        return (Boolean.FALSE);
    }

    /**
     * Método que converte um array de byte para String
     * 
     * @param b O Array de bytes que se deseja converter
     * 
     * @return A String convertida
     * @throws Exception Caso algum erro ocorra uma exceção será lançada
     */
    public static String byteArrayToHexString(byte[] b) throws Exception
    {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < b.length; ++i)
        {
            sb.append(byteToHexString(b[i]));
        }
        
        return sb.toString();
    }
    
    /**
     * Método que converte um byte para uma String Hexadecimal
     * 
     * @param b O {@link Byte} que se deseja converter
     * 
     * @return A String convertida
     * @throws Exception Caso algum erro ocorra uma exceção será lançada
     */
    public static String byteToHexString(byte b) throws Exception
    {
        int n = b;
        
        if (n < 0)
        {
            n = 256 + n;
        }
        
        int d1 = n / 16;
        int d2 = n % 16;
        
        return DIGITOS_HEXADECIMAIS[d1] + DIGITOS_HEXADECIMAIS[d2];
    }

    /**
     * Método que converte um Array de Bytes para uma String base 64
     * 
     * @param b O Array de Bytes
     * 
     * @return A String convertida
     * @throws Exception Caso algum erro ocorra uma exceção será lançada
     */
    public static String byteArrayToBase64String(byte[] b) throws Exception
    {
        StringBuilder s = new StringBuilder();
        
        // Organizando em grupos de 3 bytes para conversão
        int size = b.length;
        int colum = size / 3;
        int row = size % 3;
        
        for (int i = 0; i < colum; ++i)
        {
            int j = i * 3;
            s.append(toBase64(b[j], b[j + 1], b[j + 2]));
        }
        
        if (row == 1)
        {
            s.append(toBase64(b[size - 1]));
        }
        else if (row == 2)
        {
            s.append(toBase64(b[size - 2], b[size - 1]));
        }

        // Inserindo nova linha a cada 64 caracteres
        StringBuilder sb = new StringBuilder();
        
        size = s.length();
        colum = size / 64;

        for (int i = 0; i < colum; ++i)
        {
            sb.append(s.substring(i * 64, (i + 1) * 64)).append("\n");
        }

        if ((size % 64) > 0)
        {
            sb.append(s.substring(colum * 64, size) ).append("\n");
        }
        
        return sb.toString();
    }

    /**
     * Método que executa a transformação de Base64
     * 
     * @param b os Grupos de digitos
     * 
     * @return A String Base64 do grupo
     * @throws Exception Caso algum erro ocorra uma exceção será lançada
     */
    public static String toBase64(byte... b) throws Exception
    {
        int[] group = null;
        StringBuilder sb = new StringBuilder();
        String lastAppender = "";

        switch (b.length)
        {
            case 1:
            {
                group = new int[2];

                group[0] = (b[0] & 0xFC) >>> 2;
                group[1] = (b[0] & 0x03) << 4;
                lastAppender = "==";

                break;
            }

            case 2:
            {
                group = new int[3];

                group[0] = (b[0] & 0xFC) >>> 2;
                group[1] = (b[0] & 0x03) << 4;
                group[1] |= (b[1] & 0xF0) >> 4;
                group[2] = (b[1] & 0x0F) << 2;
                lastAppender = "=";

                break;
            }

            case 3:
            {
                group = new int[4];

                group[0] = (b[0] & 0xFC) >>> 2;
                group[1] = (b[0] & 0x03) << 4;
                group[1] |= (b[1] & 0xF0) >> 4;
                group[2] = (b[1] & 0x0F) << 2;
                group[2] |= (b[2] & 0xC0) >> 6;
                group[3] = (b[2] & 0x3F);
                lastAppender = "";

                break;
            }
            default:
            {
                return (null);
            }
        }

        for (int i = 0; i < group.length; ++i)
        {
            sb.append(base64Digit(group[i]));
        }

        if (lastAppender.isEmpty())
        {
            return (sb.toString());
        }
        else
        {
            return (sb.append(lastAppender).toString());
        }
    }
    
    /**
     * Método que calcula o digito Base64 do Inteiro
     * 
     * @param i O inteiro que se deseja calcular
     * @return O dígito calculado
     * 
     * @throws Exception Caso algum erro ocorra uma exceção será lançada
     */
    public static char base64Digit(int i) throws Exception
    {
        if (i < 26)
        {
            return (char) ('A' + i);
        }
        if (i < 52)
        {
            return (char) ('a' + (i - 26));
        }
        if (i < 62)
        {
            return (char) ('0' + (i - 52));
        }
        if (i == 62)
        {
            return '+';
        }
        else
        {
            return '/';
        }
    }

    /**
     * Método que transforma uma String Base 64 em um Byte Array pelo método de comparação
     * 
     * @param s A String que se deseja transformar
     * 
     * @return O Byte Array Convertido
     * @throws Exception Caso algum erro ocorra uma exceção será lançada
     */
    public static byte[] base64StringToByteArray(String s) throws Exception
    {
        StringBuilder str = new StringBuilder();
        
        for (int i = 0; i < str.length(); ++i)
        {
            char c = str.charAt(i);

            if (c == '\n')
            {
                continue;
            }

            else if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '+' || c == '/')
            {
                str.append(c);
            }
            else if (c == '=')
            {
                break;
            }
            else
            {
                throw new Exception();
            }
        }

        int len = str.length();
        int n = 3 * (len / 4);

        switch (len % 4)
        {
            case 1:
            {
                throw new Exception();
            }
            case 2:
            {
                len += 2;
                n += 1;
                str.append("==");
                
                break;
            }
            case 3:
            {
                ++len;
                n += 2;
                str.append("=");
                
                break;
            }
        }

        byte[] retorno = new byte[n];

        for (int i = 0; i < len / 4; ++i)
        {
            byte[] temp = base64ToBytes(str.substring(4 * i, 4 * (i + 1)));
            
            for (int j = 0; j < temp.length; ++j)
            {
                retorno[3 * i + j] = temp[j];
            }
        }
        
        return retorno;
    }

    /**
     * Método que transforma uma String Base 64 em um Byte Array pelo método de sobreposição
     * 
     * @param str A String que se deseja transformar
     * 
     * @return O Byte Array Convertido
     * @throws Exception Caso algum erro ocorra uma exceção será lançada
     */
    public static byte[] base64ToBytes(String str) throws Exception
    {
        int len = 0;
        for (int i = 0; i < str.length(); ++i)
        {
            if (str.charAt(i) != '=')
            {
                ++len;
            }
        }

        int[] group = new int[len];
        
        for (int i = 0; i < len; ++i)
        {
            char c = str.charAt(i);

            if (c >= 'A' && c <= 'Z')
            {
                group[i] = c - 'A';
            }
            else if (c >= 'a' && c <= 'z')
            {
                group[i] = c - 'a' + 26;
            }
            else if (c >= '0' && c <= '9')
            {
                group[i] = c - '0' + 52;
            }
            else if (c == '+')
            {
                group[i] = 62;
            }
            else if (c == '/')
            {
                group[i] = 63;
            }
        }

        byte[] retorno = new byte[len - 1];
        switch (len)
        {
            case 4:
            {
                retorno[2] = (byte) ((((group[2]) & 0x03) << 6) | group[3]);
            }
            case 3:
            {
                retorno[1] = (byte) ((((group[1]) & 0x0F) << 4) | ((group[2] & 0x3C) >>> 2));
            }
            case 2:
            {
                retorno[0] = (byte) ((group[0] << 2) | ((group[1] & 0x30) >>> 4));
            }
        }

        return retorno;
    }

    /**
     * Método que adiciona um espaço antes de cada letra maiuscula em uma String
     * 
     * @param str A String que se deseja separar
     * 
     * @return A String separada
     * @exception Exception Caso ocorra algum erro uma excessao sera lancada
     */
    public static String replaceUpperBySpace(String str) throws Exception
    {
        return (str.replaceAll("([A-Z])", " $1").trim());
    }
}

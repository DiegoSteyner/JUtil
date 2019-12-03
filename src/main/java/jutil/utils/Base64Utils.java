package jutil.utils;

import jutil.abstracts.AbstractUtils;

/**
 * Classe utilitaria para se trabalhar com Base64
 * 
 * @author Diego Steyner
 */
public class Base64Utils extends AbstractUtils
{
	
	private Base64Utils() 
	{
		throw EXCEPTION_CONSTRUTOR;
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

}

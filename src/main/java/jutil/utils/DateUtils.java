package jutil.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import jutil.abstracts.AbstractUtils;
import jutil.data.enums.DateEnum;

/**
 * Classe utilitária para se trabalhar com datas
 * 
 * @author Diego Steyner
 */
public final class DateUtils extends AbstractUtils
{
    /**
     * Construtor padrão privada
     */
    private DateUtils()
    {
        throw EXCEPTION_CONSTRUTOR;
    }

    /**
     * Método que transforma um {@link Date} em String
     * 
     * @param date A data que se deseja converter
     * 
     * @return A data em String
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String dateToString(Date date) throws Exception
    {
        return (new SimpleDateFormat().format(date));
    }

    /**
     * Método que transforma um {@link Date} em String
     * 
     * @param date A data que se deseja converter
     * @param pattern O formato que data deve ser retornada
     * 
     * @return A data em String
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String dateToString(Date date, String pattern) throws Exception
    {
        return (new SimpleDateFormat(pattern).format(date));
    }

    /**
     * Método que retorna a data atual como uma String baseado no padrão interno da VM
     * 
     * @param date A data que se deseja converter
     * @param format O {@link DateEnum} do formado desejado
     * 
     * @return A data em String formatada conforme o valor passado 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String dateToString(Date date, DateEnum format) throws Exception
    {
        return (DateFormat.getDateInstance(format.getIntValue()).format(new Date()));
    }

    /**
     * Método que faz o parse de uma String para um Date
     * 
     * @param date A String com a data que se deseja tranformar
     * @param pattern O formato da data
     * 
     * @return O objeto {@link Date}
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static Date stringToDate(String date, String pattern) throws Exception
    {
        return (new SimpleDateFormat(pattern).parse(date));
    }

    /**
     * Método que faz o parse de uma String para um Date
     * 
     * @param date A String com a data que se deseja tranformar
     * @param format O {@link DateEnum} do formato atual da String
     * 
     * @return O objeto {@link Date}
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static Date stringToDate(String date, DateEnum format) throws Exception
    {
        return (DateFormat.getDateInstance(format.getIntValue()).parse(date));
    }
    
    /**
     * Método que adiciona ou diminui DIAS, MESES, ANOS, HORAS, MINUTOS e SEGUNDOS a data atual ajustando os valores conforme necessário
     * 
     * @param date A data que se deseja manipular
     * @param operation O {@link DateEnum} da operação desejada
     * @param field O {@link DateEnum} do campo que se deseja manipular
     * @param value A quantidade que se deseja adicionar ou remover
     * 
     * @return Uma nova data com o campo desejado alterado de acordo com o valor passado
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static Date onDate(Date date, DateEnum operation, DateEnum field, int value) throws Exception
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        
        switch (operation)
        {
            case INCREMENT_ON_DATE:
            {
                cal.add(field.getIntValue(), Math.abs(value) * 1);
                break;
            }

            case DECREMENT_ON_DATE:
            {
                cal.add(field.getIntValue(), Math.abs(value) * -1);
                break;
            }

            default:
            {
                throw new Exception("A operação deve ser jutil.data.DateConstants.INCREMENT_ON_DATE ou jutil.data.DateConstants.DECREMENT_ON_DATE");
            }
        }
        
        return(cal.getTime());
    }

    /**
     * Método que adiciona ou diminui DIAS, MESES, ANOS, HORAS, MINUTOS e SEGUNDOS a data atual ajustando os valores conforme necessário
     * 
     * @param date A data que se deseja manipular
     * @param operation O {@link DateEnum} da operação desejada
     * @param values Um {@link Map} com as {@link DateEnum} dos campos que se deseja adicionar ou remover junto com a quantidades desejadas
     * 
     * @return Uma nova data com o campo desejado alterado de acordo com o valor passado
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static Date onDate(Date date, DateEnum operation, Map<DateEnum, Integer> values) throws Exception
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        
        for(DateEnum field : values.keySet())
        {
            switch (operation)
            {
                case INCREMENT_ON_DATE:
                {
                    cal.add(field.getIntValue(), Math.abs(values.get(field)) * 1);
                    break;
                }

                case DECREMENT_ON_DATE:
                {
                    cal.add(field.getIntValue(), Math.abs(values.get(field)) * -1);
                    break;
                }

                default:
                {
                    throw new Exception("A operação deve ser jutil.data.DateConstants.INCREMENT_ON_DATE ou jutil.data.DateConstants.DECREMENT_ON_DATE");
                }
            }
        }
        
        return(cal.getTime());
    }

    /**
     * Método que retorna um campo de uma determinada data
     * 
     * @param date  A data que se deseja extrair o campo
     * @param field O {@link DateEnum} do campo desejado
     * 
     * @return O valor da campo desejado
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static int getFieldInDate(Date date, DateEnum field) throws Exception
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        
        return (cal.get(field.getIntValue()));
    }

    /**
     * Método que retorna a quantidade de dias de um determinado mês
     * 
     * @param date A data que se deseja saber a quantidade de dias do mês
     * 
     * @return O número de dias que o mês possui
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static int getNumDaysInMonth(Date date)
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        return (cal.getActualMaximum(DateEnum.DIA_DO_MES.getIntValue()));
    }

    /**
     * Metodo que verifica se um ano e bissexto ou nao
     * 
     * @param ano O ano que se deseja verificar
     * 
     * @return Se True, o ano é bissexto
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static boolean isBissexto(int ano) throws Exception
    {
        if((ano % 400 == 0) || (ano % 4 == 0 && ano % 100 != 0))
        {
            return (Boolean.TRUE);
        }
        
        return (Boolean.FALSE);
    }

    /**
     * Método que retorna a diferença em dias entre duas datas
     * 
     * @param dataInicio A data inicial
     * @param dataFinal A data final
     * 
     * @return A diferença em dias
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static int getDiferenceInDaysBetweenDates(Date dataInicio, Date dataFinal) throws Exception
    {
        GregorianCalendar c0 = new GregorianCalendar();
        GregorianCalendar c1 = new GregorianCalendar();
        int retorno = -1;

        c0.setTime(dataInicio);
        c1.setTime(dataFinal);
        
        while(c0.before(c1))
        {
            retorno++;
            c0.add(DateEnum.DIA_DO_MES.getIntValue(), 1);
        }
        
        return (retorno);
    }
    
    /**
     * Metodo que retorna a quantidade de segundos que se passaram de meia noite até o momento atual
     * 
     * @return Um Inteiro equivalente a quantidade de segundos que se passaram
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static int secondsUntilNow() throws Exception
    {
        Calendar c = new GregorianCalendar();
        c.setTime(new Date());

        int hora = c.get(DateEnum.HORA_DO_DIA.getIntValue()) * 3600;
        int minuto = c.get(DateEnum.MINUTO.getIntValue()) * 60;
        int segundo = c.get(DateEnum.SEGUNDO.getIntValue()) * 1;

        return ((hora + minuto + segundo));
    }

    
    /**
     * Método que retorna o nome do mês de uma determinada data para uma determinada região
     * 
     * @param date A data que se deseja obter o nome dos mês
     * 
     * @return O nome do mês da data
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String getNameMonth(Date date, Locale locale) throws Exception
    {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        
        return (c.getDisplayName(DateEnum.MES.getIntValue(), DateEnum.FORMATO_PADRAO.getIntValue(), locale));
    }

    /**
     * Método que retorna os nomes de todos os meses do ano presente na JVM para o locale atual
     * 
     * @param locale O {@link Locale} da idioma em que os meses serão retornados
     * 
     * @return Os nomes dos meses no idioma configurado
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String[] getAllNamesMonths(Locale locale) throws Exception
    {
        GregorianCalendar c = new GregorianCalendar(1990, 0, 1);
        String r[] = new String[12];
        
        for (int i = 0; i < r.length; i++)
        {
            r[i] = c.getDisplayName(DateEnum.MES.getIntValue(), DateEnum.FORMATO_PADRAO.getIntValue(), locale);
            c.add(DateEnum.MES.getIntValue(), 1);
        }

        return (r);
    }

    /**
     * Método que retorna o nome do dia atual
     * 
     * @param date A data que se deseja oberter o nome
     * @param locale O {@link Locale} da idioma em que o nome do dia será retornado
     * 
     * @return O nome do dia atual
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String getNameDay(Date date, Locale locale) throws Exception
    {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        
        return (c.getDisplayName(DateEnum.DIA_DA_SEMANA.getIntValue(), DateEnum.FORMATO_PADRAO.getIntValue(), locale));
    }

    /**
     * Método que retorna os nomes dos dias da semana
     * 
     * @param locale O {@link Locale} da idioma em que os dias serão retornados
     * 
     * @return Um vetor contendo os nomes dos dias da semana
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String[] getAllNameOfDaysInWeek(Locale locale) throws Exception
    {
        Map<String, Integer> days = new GregorianCalendar(1990, 0, 1).getDisplayNames(DateEnum.DIA_DA_SEMANA.getIntValue(), DateEnum.FORMATO_PADRAO.getIntValue(), locale);
        
        try
        {
            return (days.keySet().toArray(new String[days.keySet().size()]));
        }
        finally
        {
            days.clear();
        }
    }
    
    /**
     * Método que retorna um {@link Locale} para o Brasil
     * 
     * @return O {@link Locale} do Brasil
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static Locale getLocaleBrasil() throws Exception
    {
        return (new Locale("pt", "BR"));
    }

    /**
     * Método que retorna um {@link Locale} para os Estados Unidos
     * 
     * @return O {@link Locale} dos Estados Unidos
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static Locale getLocaleUSA() throws Exception
    {
        return (new Locale("us", "US"));
    }

    /**
     * Metodo que cria um objeto {@link Calendar} para uma determinada data
     * 
     * @param date A data que se deseja que o Calendário seja criado
     * 
     * @return O Calendário para a data informada
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static GregorianCalendar getCalendar(Date date) throws Exception
    {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);

        return (c);
    }
    
    /**
     * Metodo que retorna todas as timeZones suportadas pela JVM
     *
     * @return Um vetor de String contendo em cada posicao uma ID de timeZone
     * @exception Exception Caso ocorra algum erro uma Excessao sera Lancada
     */
    public static String[] getTimeZones() throws Exception 
    {
        return(TimeZone.getAvailableIDs());
    }
    
    /**
     * Metodo que verifica se uma data e válida ou não
     *
     * @param formato O formato da data que se está verificando
     * @param data A data que se deseja verificar
     *
     * @return Se True, a Data é válida
     * @exception Exception Caso ocorra algum erro uma Excessao sera Lancada
     */
    public static boolean dateIsValid(String formato, String data) throws Exception 
    {
        SimpleDateFormat sd = new SimpleDateFormat(formato);

        try
        {
            sd.setLenient(false);
            sd.parse(data);
            sd = null;
            return(Boolean.TRUE);
        }
        catch (ParseException e)
        {
            return(Boolean.FALSE);
        }
        catch (Exception e) 
        {
            throw e;
        }
    }
}
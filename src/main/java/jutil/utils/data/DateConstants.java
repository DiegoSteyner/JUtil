package jutil.data;

import java.text.DateFormat;
import java.util.Calendar;

import jutil.utils.DateUtils;

/**
 * Classe de constantes usadas pela classe {@link DateUtils}
 * 
 * @author Diego Steyner
 */
public enum DateConstants
{
    FORMATO_PADRAO(DateFormat.DEFAULT),
    FORMATO_MEDIO(DateFormat.MEDIUM),
    FORMATO_LONGO(DateFormat.LONG),
    FORMATO_COMPLETO(DateFormat.FULL),
    
    DIA_DO_MES (Calendar.DAY_OF_MONTH),
    DIA_DA_SEMANA (Calendar.DAY_OF_WEEK),
    MES (Calendar.MONTH),
    ANO (Calendar.YEAR),
    HORA_DO_DIA (Calendar.HOUR_OF_DAY),
    MINUTO (Calendar.MINUTE),
    SEGUNDO (Calendar.SECOND),

    INCREMENT_ON_DATE(0),
    DECREMENT_ON_DATE(1),
    
    PATTERN_DIA_MES_ANO("dd/MM/yyyy"),
    PATTERN_HORA_MINUTO_SEGUNDO("HH:mm:ss"),
    PATTERN_DIA_MES_ANO_HORA_MINUTO_SEGUNDO("dd/MM/yyyy HH:mm:ss");

    private int intValue;
    private String stringValue;
    
    /**
     * Construtor padrão exigido pela classe
     * 
     * @param intValue O valor String com o qual a constante será inicializada
     */
    private DateConstants(String stringValue)
    {
        this.stringValue = stringValue;
    }

    /**
     * Construtor padrão exigido pela classe
     * 
     * @param intValue O valor inteiro com o qual a constante será inicializada
     */
    private DateConstants(int intValue)
    {
        this.intValue = intValue;
    }

    /**
     * Método que retorna o valor da variável intValue
     * 
     * @return O valor presente na variável intValue
     */
    public int getIntValue()
    {
        return intValue;
    }

    /**
     * Método que altera o valor da variável intValue
     * 
     * @param intValue O novo valor da variável intValue
     */
    public void setIntValue(int intValue)
    {
        this.intValue = intValue;
    }

    /**
     * Método que retorna o valor da variável stringValue
     * 
     * @return O valor presente na variável stringValue
     */
    public String getStringValue()
    {
        return stringValue;
    }

    /**
     * Método que altera o valor da variável stringValue
     * 
     * @param stringValue O novo valor da variável stringValue
     */
    public void setStringValue(String stringValue)
    {
        this.stringValue = stringValue;
    }
}

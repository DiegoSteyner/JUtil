package jutil.utils.jdbc;

import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Classe utilit�ria para se trabalhar com {@link PreparedStatement}
 * 
 * @author Diego Steyner
 */
public class PrepareStamentUtils 
{
	private HashMap<String, PreparedStatement> preparedStatementMap = new HashMap<String, PreparedStatement>();
	
	/**
	 * Adiciona um {@link PreparedStatement} ao map gerenciado pela classe
	 *  
	 * @param key A chave do {@link PreparedStatement} sendo adicionado
	 * @param pstament o {@link PreparedStatement} a ser adicionado
	 */
	public void putPreparedStatement(String key, PreparedStatement pstament)
	{
		preparedStatementMap.put(key, pstament);
	}
	
	/**
	 * M�todo que retorna um {@link PreparedStatement} do map gerenciado pela classe
	 * @param key a Chave do {@link PreparedStatement} adicionado
	 * @return O {@link PreparedStatement} na posi��o.
	 */
	public PreparedStatement getPreparedStatement(String key)
	{
		return(preparedStatementMap.get(key));
	}
	
	/**
	 * M�todo que configura faz o set dos parametros de um {@link PreparedStatement}
	 *  
	 * @param prstatement O {@link PreparedStatement} com os parametros
	 * @param parameters Os parametros a serem configurados
	 * 
	 * @return Um {@link PreparedStatement} com os parametros configurados
	 * @throws SQLException Caso algum erro, uma exce��o ser� lan�ada.
	 */
	public PreparedStatement configureStatements(PreparedStatement prstatement, Object... parameters) throws SQLException
	{
		for (int i = 0; i < parameters.length; i++) 
		{
			if(hasStringConfigured(prstatement, i, parameters))
			{
				continue;
			}
			else if(hasDateConfigured(prstatement, i, parameters))
			{
				continue;
			}
			else if(hasIntegerConfigured(prstatement, i, parameters))
			{
				continue;
			}
			else if(hasOtherConfigured(prstatement, i, parameters))
			{
				continue;
			}
		}
		
		return(prstatement);
	}
	
	/**
	 * M�todo que configura os parametros do tipo String no {@link PreparedStatement}
	 * 
	 * @param prstatement O {@link PreparedStatement}
	 * @param index O index da posi��o atual
	 * @param parameters Os parametros a serem configurados
	 * 
	 * @return Se True, houve parametros do tipo String configurado
	 * @throws SQLException Caso algum erro, uma exce��o ser� lan�ada.
	 */
	private boolean hasStringConfigured(PreparedStatement prstatement, int index, Object... parameters) throws SQLException 
	{
		if(parameters[index] == null)
		{
			prstatement.setNull(index+1, java.sql.Types.VARCHAR);
		}
		else if(parameters[index] instanceof java.lang.String)
		{
			final String str = parameters[index].toString();
			
			if(str.length() > 32766)
			{
				prstatement.setCharacterStream(index+1, new StringReader(str), str.length());
			}
			else
			{
				prstatement.setString(index+1, parameters[index].toString());
			}
		}
		else
		{
			return(false);
		}
		
		return(true);
	}

	/**
	 * M�todo que configura os parametros do tipo Date no {@link PreparedStatement}
	 * 
	 * @param prstatement O {@link PreparedStatement}
	 * @param index O index da posi��o atual
	 * @param parameters Os parametros a serem configurados
	 * 
	 * @return Se True, houve parametros do tipo Date configurado
	 * @throws SQLException Caso algum erro, uma exce��o ser� lan�ada.
	 */
	private boolean hasDateConfigured(PreparedStatement prstatement, int index, Object... parameters) throws SQLException 
	{
		if(parameters[index] instanceof java.util.Date)
		{
			prstatement.setDate(index+1, new java.sql.Date(((java.util.Date) parameters[index]).getTime()));
		}
		else if(parameters[index] instanceof java.sql.Time)
		{
			prstatement.setTime(index+1, ((java.sql.Time) parameters[index]));
		}
		else if(parameters[index] instanceof java.sql.Timestamp)
		{
			prstatement.setTimestamp(index+1, ((java.sql.Timestamp) parameters[index]));
		}
		else
		{
			return(false);
		}
		
		return(true);
	}
	
	/**
	 * M�todo que configura os parametros do tipo Integer no {@link PreparedStatement}
	 * 
	 * @param prstatement O {@link PreparedStatement}
	 * @param index O index da posi��o atual
	 * @param parameters Os parametros a serem configurados
	 * 
	 * @return Se True, houve parametros do tipo Integer configurado
	 * @throws SQLException Caso algum erro, uma exce��o ser� lan�ada.
	 */
	private boolean hasIntegerConfigured(PreparedStatement prstatement, int index, Object... parameters) throws SQLException
	{
		if(parameters[index] instanceof Long)
		{
			prstatement.setLong(index+1,((Long) parameters[index]).longValue());
		}
		else if(parameters[index] instanceof Integer)
		{
			prstatement.setInt(index+1,((Integer) parameters[index]).intValue());
		}
		else if(parameters[index] instanceof BigDecimal)
		{
			prstatement.setBigDecimal(index+1,(BigDecimal) parameters[index]);
		}
		else if(parameters[index] instanceof Double)
		{
			prstatement.setDouble(index+1,((Double) parameters[index]).doubleValue());
		}
		else if(parameters[index] instanceof Float)
		{
			prstatement.setFloat(index+1,((Float) parameters[index]).floatValue());
		}
		else if(parameters[index] instanceof Short)
		{
			prstatement.setShort(index+1,((Short) parameters[index]).shortValue());
		}
		else
		{
			return(false);
		}
		
		return(true);
	}
	
	/**
	 * M�todo que configura os parametros do tipo Boolean no {@link PreparedStatement}
	 * 
	 * @param prstatement O {@link PreparedStatement}
	 * @param index O index da posi��o atual
	 * @param parameters Os parametros a serem configurados
	 * 
	 * @return Se True, houve parametros do tipo Boolean configurado
	 * @throws SQLException Caso algum erro, uma exce��o ser� lan�ada.
	 */
	private boolean hasOtherConfigured(PreparedStatement prstatement, int index, Object... parameters) throws SQLException
	{
		if(parameters[index] instanceof java.sql.Array)
		{
			prstatement.setArray(index+1,(java.sql.Array) parameters[index]);
		}
		else if(parameters[index] instanceof Boolean)
		{
			prstatement.setBoolean(index+1,((Boolean) parameters[index]).booleanValue());
		}
		else if(parameters[index] instanceof java.net.URL)
		{
			prstatement.setURL(index+1,(java.net.URL) parameters[index]);
		}
		else if(parameters[index] instanceof Ref)
		{
			prstatement.setRef(index+1,(Ref) parameters[index]);
		}
		else if(parameters[index] instanceof byte[])
		{
			prstatement.setBytes(index+1,(byte[]) parameters[index]);
		}
		else if(parameters[index].getClass().isArray())
		{
			prstatement.setArray(index+1, (Array) parameters[index]);
		}
		else if(parameters[index] instanceof Blob)
		{
			prstatement.setBlob(index+1, (Blob)parameters[index]);
		}
		else if(parameters[index] instanceof Clob)
		{
			prstatement.setClob(index+1, (Clob)parameters[index]);
		}
		else if(parameters[index] instanceof InputStream)
		{
			throw new SQLException("Parametros do tipo INPUT_STREAM, UNICODE_STREAM, BINARY_STREAM e CHARACTER_STREAM ainda n\u00E3o foram implementados!");
		}
		else if(parameters[index] instanceof Object)
		{
			 prstatement.setObject(index+1, parameters[index]);
		}
		else 
		{
			return(false);
		}
		
		return(true);
	}
}

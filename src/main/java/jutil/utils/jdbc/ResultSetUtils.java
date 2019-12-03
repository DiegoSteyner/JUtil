package jutil.utils.jdbc;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeSet;

import jutil.annotation.JdbcColumnOf;

/**
 * Classe utilit�ria para se trabalhar com {@link ResultSet}
 * 
 * @author Diego Steyner
 */
public final class ResultSetUtils 
{
	/**
	 * M�todo que faz um {@link ResultSet} voltar ao seu estado inicial
	 * 
	 * @param result O {@link ResultSet} a ser resetado
	 * 
	 * @return Se True, O {@link ResultSet} foi resetado com sucesso.
	 * @throws SQLException Caso ocorra algum erro uma excess�o ser� lan�ada
	 */
	public static boolean resetResultSet(ResultSet result) throws SQLException
	{
		if(ResultSet.TYPE_FORWARD_ONLY != result.getType())
		{
			result.beforeFirst();
			return(true);
		}
		
		return(false);
	}

	/**
	 * M�todo que imprime um {@link ResultSet}
	 * 
	 * @param result O {@link ResultSet} a ser impresso
	 * @param rollResult Se True, O m�todo chamar� a fun��o "next()" do {@link ResultSet} automaticamente
	 * 
	 * @throws SQLException Caso ocorra algum erro uma excess�o ser� lan�ada
	 */
	public static void printResultSet(ResultSet result, boolean rollResult) throws SQLException
	{
		TreeSet<String> columns = getTreeSetWithBiggestComparator();
		
		if(rollResult)
		{
			while(result.next())
			{
				printResult(result, columns);
			}
		}
		else
		{
			printResult(result, columns);
		}
	}

	/**
	 * Método que imprime um {@link ResultSet}
	 * 
	 * @param result O {@link ResultSet}
	 * @param columns As colunas presentes no {@link ResultSet}
	 * 
	 * @throws SQLException Caso ocorra algum erro, uma exceção será lançada
	 */
	private static void printResult(ResultSet result, TreeSet<String> columns) throws SQLException
	{
		String biggest = "";
		String vcolumns[] = null;
		
		if(columns.isEmpty())
		{
			vcolumns = getColumnsNames(result);
			columns.addAll(Arrays.asList(vcolumns));
			biggest = columns.last();
		}
		else
		{
			vcolumns = columns.toArray(new String[columns.size()]);
			biggest = columns.last();
		}
		
		for (int i = 0; i < vcolumns.length; i++) 
		{
			if(vcolumns[i].length() == biggest.length())
			{
				System.out.print(vcolumns[i] + " : ");
				System.out.println(result.getObject(i+1));
			}
			else 
			{
				System.out.print(vcolumns[i] + (biggest.replaceAll(".", " ").substring(vcolumns[i].length()))+" : ");
				System.out.println(result.getObject(i+1));
			}
		}
		
		System.out.println("=================================================================");
	}
	
	/**
	 * Metodo que retorna um {@link TreeSet} configurado com ordenação do menor para o maior
	 * 
	 * @return o {@link TreeSet} com ordenação do menor para o maior
	 */
	private static TreeSet<String> getTreeSetWithBiggestComparator() 
	{
		return new TreeSet<>(new Comparator<String>() 
		{
			@Override
			public int compare(String o1, String o2) 
			{
				if(o2.length() > o1.length())
				{
					return (-1);
				}
				
				return(1);
			}
		});
	}
	
	/**
	 * M�todo que retorna os nomes das colunas no {@link ResultSet}
	 * 
	 * @param result O {@link ResultSet} que se deseja obter os nomes das colunas
	 * @param rollResult Se True, O m�todo chamar� a fun��o "next()" do {@link ResultSet} automaticamente
	 * 
	 * @return Um array com os nomes das colunas
	 * @throws SQLException Caso ocorra algum erro uma excess�o ser� lan�ada
	 */
	public static String[] getColumNames(ResultSet result, boolean rollResult) throws SQLException
	{
		String retorno[] = null;
		
		if(rollResult)
		{
			if(result.next())
			{
				retorno = getColumnsNames(result);
			}
		}
		else
		{
			return(getColumnsNames(result));
		}
		
		return(retorno);
	}
	
	/**
	 * Metodo que conta a quantidade de colunas presete no {@link ResultSet}
	 * 
	 * @param result O {@link ResultSet} a ser contado
	 * 
	 * @return A quantidade de linhas no {@link ResultSet}
	 * @throws SQLException Caso ocorra algum erro uma excess�o ser� lan�ada
	 */
	public static int getColumnCount(ResultSet result) throws SQLException
	{
		return(result.getMetaData().getColumnCount());
	}
	
	/**
     * Método que retorna o tipo de uma coluna presente no {@link ResultSet}
     * 
     * @param result O {@link ResultSet} da coluna.
     * @param columName O nome da coluna.
     * 
     * @return O {@link Types} da coluna
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static int getColumType(ResultSet result, String columName) throws Exception
    {
        return(result.getMetaData().getColumnType(result.findColumn(columName)));
    }

	/**
	 * M�todo que transforma um {@link ResultSet} em uma lista de objetos de um determinado tipo
	 * 
	 * @param result O {@link ResultSet} a ser transformado
	 * @param klass A classe para o qual {@link ResultSet} dever� ser convertido
	 * @param rollResult Se True, O m�todo chamar� a fun��o "next()" do {@link ResultSet} automaticamente
	 * @return Uma lista de objetos do tipo passado
	 * 
	 * @throws InstantiationException Caso ocorra algum erro de instancia��o, uma exe��o ser� lan�ada
	 * @throws IllegalAccessException Caso ocorra algum erro de viola��o de acesso, uma exe��o sera lan�ada
	 * @throws NoSuchFieldException Caso ocorr algum erro de ausencia de campos, uma exce��o sera lan�ada
	 * @throws SecurityException Caso ocorra algum erro de permiss�o, uma exe��o sera lan�ada
	 * @throws IllegalArgumentException Caso ocorra algum erro com a quantidade de parametros, uma exe��o sera lan�ada
	 * @throws InvocationTargetException Caso ocorra algum erro com a chamada a classe, uma exe��o sera lan�ada
	 * @throws SQLException Caso ocorra algum erro com uma excess�o ser� lan�ada
	 * @throws IntrospectionException Caso ocorra algum erro com uma excess�o ser� lan�ada
	 */
	public static <T> ArrayList<T> transformResultSetRows(ResultSet result, Class<T> klass, boolean rollResult) throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException, InvocationTargetException, SQLException, IntrospectionException
	{
		ArrayList<T> retorno = new ArrayList<>();
		
		if(rollResult)
		{
			while(result.next())
			{
				retorno.add(createInstance(result, klass));
			}
		}
		else
		{
			retorno.add(createInstance(result, klass));
		}
		
		return(retorno);
	}
	
	/**
	 * M�todo que transforma um {@link ResultSet} em uma objeto de um determinado tipo
	 * 
	 * @param result O {@link ResultSet} a ser transformado
	 * @param klass A classe para o qual {@link ResultSet} dever� ser convertido
	 * @param rollResult Se True, O m�todo chamar� a fun��o "next()" do {@link ResultSet} automaticamente
	 * @return Um objeto do tipo passado
	 * 
	 * @throws InstantiationException Caso ocorra algum erro de instancia��o, uma exe��o ser� lan�ada
	 * @throws IllegalAccessException Caso ocorra algum erro de viola��o de acesso, uma exe��o sera lan�ada
	 * @throws NoSuchFieldException Caso ocorr algum erro de ausencia de campos, uma exce��o sera lan�ada
	 * @throws SecurityException Caso ocorra algum erro de permiss�o, uma exe��o sera lan�ada
	 * @throws IllegalArgumentException Caso ocorra algum erro com a quantidade de parametros, uma exe��o sera lan�ada
	 * @throws InvocationTargetException Caso ocorra algum erro com a chamada a classe, uma exe��o sera lan�ada
	 * @throws SQLException Caso ocorra algum erro com uma excess�o ser� lan�ada
	 * @throws IntrospectionException Caso ocorra algum erro com uma excess�o ser� lan�ada
	 */
	public static <T> T transformResultSetRow(ResultSet result , Class<T> klass, boolean rollResult) throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException, InvocationTargetException, SQLException, IntrospectionException
	{
		if(rollResult)
		{
			if(result.next())
			{
				return(createInstance(result, klass));
			}
		}
		else
		{
			return(createInstance(result, klass));
		}
		
		return(null);
	}

	/**
	 * M�todo que cria uma inst�ncia de um objeto a partir de um {@link ResultSet}
	 * 
	 * @param result O {@link ResultSet} a ser transformado
	 * @param klass A classe para o qual {@link ResultSet} dever� ser convertido
	 * 
	 * @return Um objeto do tipo passado
	 * 
	 * @throws InstantiationException Caso ocorra algum erro de instancia��o, uma exe��o ser� lan�ada
	 * @throws IllegalAccessException Caso ocorra algum erro de viola��o de acesso, uma exe��o sera lan�ada
	 * @throws NoSuchFieldException Caso ocorr algum erro de ausencia de campos, uma exce��o sera lan�ada
	 * @throws InvocationTargetException Caso ocorra algum erro com a chamada a classe, uma exe��o sera lan�ada
	 * @throws SQLException Caso ocorra algum erro com uma excess�o ser� lan�ada
	 * @throws IntrospectionException Caso ocorra algum erro com uma excess�o ser� lan�ada
	 */
	private static <T> T createInstance(ResultSet result, Class<T> klass) throws IntrospectionException, NoSuchFieldException, SQLException, IllegalAccessException, InvocationTargetException, InstantiationException 
	{
		T instance = klass.newInstance();
		
		Field field;
		int colIndex;
		int colType;
		String colTypeName;
		String columnName;
		
		for(PropertyDescriptor i : Introspector.getBeanInfo(instance.getClass()).getPropertyDescriptors())
		{
			if(!i.getName().equalsIgnoreCase("class"))
			{
				if((field = instance.getClass().getDeclaredField(i.getName())).isAnnotationPresent(JdbcColumnOf.class))
				{
					columnName = field.getAnnotation(JdbcColumnOf.class).columnName();
					colIndex = result.findColumn(columnName);
					colType = result.getMetaData().getColumnType(colIndex);
					colTypeName = result.getMetaData().getColumnTypeName(colIndex);
					
					if(setStringFields(colType, i, instance, result, colIndex, columnName, field, colTypeName))
					{
						continue;
					}
					
					if(setIntFields(colType, i, instance, result, colIndex, columnName, field, colTypeName))
					{
						continue;
					}
					
					if(java.sql.Types.BIT == colType || java.sql.Types.BOOLEAN == colType)
					{
						fieldsOfType(field, boolean.class.getName(), columnName, colTypeName);
						i.getWriteMethod().invoke(instance, result.getBoolean(colIndex));
						
						continue;
					}

					if(java.sql.Types.BLOB == colType)
					{
						fieldsOfType(field, Blob.class.getName(), columnName, colTypeName);
						i.getWriteMethod().invoke(instance, result.getBlob(colIndex));
						
						continue;
					}

					if(java.sql.Types.CLOB == colType)
					{
						fieldsOfType(field, Clob.class.getName(), columnName, colTypeName);
						i.getWriteMethod().invoke(instance, result.getClob(colIndex));
						
						continue;
					}

					if(java.sql.Types.DATE == colType || java.sql.Types.TIME == colType || java.sql.Types.TIMESTAMP == colType || java.sql.Types.TIME_WITH_TIMEZONE == colType || java.sql.Types.TIMESTAMP_WITH_TIMEZONE == colType)
					{
						if(field.getType().getName().equalsIgnoreCase(Date.class.getName()))
						{
							fieldsOfType(field, Date.class.getName(), columnName, colTypeName);
							i.getWriteMethod().invoke(instance, new Date(result.getDate(colIndex).getTime()));
							
							continue;
						}
						else if(field.getType().getName().equalsIgnoreCase(java.sql.Date.class.getName()))
						{
							fieldsOfType(field, java.sql.Date.class.getName(), columnName, colTypeName);
							i.getWriteMethod().invoke(instance, result.getDate(colIndex));
							
							continue;
						}
						else
						{
							throw new NoSuchFieldException("O campo [ "+field.getName()+" ] foi declarado como [ "+field.getType().getName()+" ] que � incompativel com a coluna [ "+columnName+" ] que � do tipo [ "+colTypeName+" ].");
						}
					}

					if(java.sql.Types.BINARY == colType || java.sql.Types.LONGVARBINARY == colType || java.sql.Types.VARBINARY == colType)
					{
						fieldsOfType(field, byte.class.getName(), columnName, colTypeName);
						i.getWriteMethod().invoke(instance, result.getBytes(colIndex));
						continue;
					}
				}
			}
		}
		
		return(instance);
	}

	/**
	 * M�todo que coloca os valores do tipo String no objeto a ser criado 
	 * 
	 * @param colType O tipo da coluna retornado pelo SQL
	 * @param propertie O objeto que cont�m a inst�ncia do m�todo
	 * @param instance A inst�ncia que cont�m a vari�vel
	 * @param result O {@link ResultSet} com os valores
	 * @param colIndex O index da coluna
	 * @param columnName O nome da coluna
	 * @param field A vari�vel que receber� o valor
	 * @param colTypeName O nome da coluna retornado pelo SQL
	 * @return Se True, O valor foi configurado na inst�ncia.
	 * 
	 * @throws NoSuchFieldException Caso ocorr algum erro de ausencia de campos, uma exce��o sera lan�ada
	 * @throws IllegalAccessException Caso ocorra algum erro de viola��o de acesso, uma exe��o sera lan�ada
	 * @throws IllegalArgumentException Caso ocorra algum erro com a quantidade de parametros, uma exe��o sera lan�ada
	 * @throws InvocationTargetException Caso ocorra algum erro com a chamada a classe, uma exe��o sera lan�ada
	 * @throws SQLException Caso ocorra algum erro com uma excess�o ser� lan�ada
	 */
	private static boolean setStringFields(int colType, PropertyDescriptor propertie, Object instance, ResultSet result, int colIndex, String columnName, Field field, String colTypeName) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException 
	{
		if(java.sql.Types.CHAR == colType || java.sql.Types.LONGNVARCHAR == colType || java.sql.Types.LONGVARCHAR == colType || java.sql.Types.NCHAR == colType || java.sql.Types.NVARCHAR == colType || java.sql.Types.VARCHAR == colType)
		{
			fieldsOfType(field, String.class.getName(), columnName, colTypeName);
			propertie.getWriteMethod().invoke(instance, result.getString(colIndex));
			return(true);
		}
		
		return(false);
	}

	/**
	 * M�todo que coloca os valores do tipo int no objeto a ser criado 
	 * 
	 * @param colType O tipo da coluna retornado pelo SQL
	 * @param propertie O objeto que cont�m a inst�ncia do m�todo
	 * @param instance A inst�ncia que cont�m a vari�vel
	 * @param result O {@link ResultSet} com os valores
	 * @param colIndex O index da coluna
	 * @param columnName O nome da coluna
	 * @param field A vari�vel que receber� o valor
	 * @param colTypeName O nome da coluna retornado pelo SQL
	 * @return Se True, O valor foi configurado na inst�ncia.
	 * 
	 * @throws NoSuchFieldException Caso ocorr algum erro de ausencia de campos, uma exce��o sera lan�ada
	 * @throws IllegalAccessException Caso ocorra algum erro de viola��o de acesso, uma exe��o sera lan�ada
	 * @throws IllegalArgumentException Caso ocorra algum erro com a quantidade de parametros, uma exe��o sera lan�ada
	 * @throws InvocationTargetException Caso ocorra algum erro com a chamada a classe, uma exe��o sera lan�ada
	 * @throws SQLException Caso ocorra algum erro com uma excess�o ser� lan�ada
	 */
	private static boolean setIntFields(int colType, PropertyDescriptor propertie, Object instance, ResultSet result, int colIndex, String columnName, Field field, String colTypeName) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException 
	{
		if(java.sql.Types.NUMERIC == colType || java.sql.Types.DECIMAL == colType || java.sql.Types.DOUBLE == colType || java.sql.Types.FLOAT == colType || java.sql.Types.BIGINT == colType || java.sql.Types.REAL == colType || java.sql.Types.ROWID == colType || java.sql.Types.SMALLINT == colType || java.sql.Types.TINYINT == colType)
		{
			if(field.getType().getName().equalsIgnoreCase(int.class.getName()))
			{
				fieldsOfType(field, int.class.getName(), columnName, colTypeName);
				propertie.getWriteMethod().invoke(instance, result.getInt(colIndex));
			}
			else if(field.getType().getName().equalsIgnoreCase(long.class.getName()))
			{
				fieldsOfType(field, long.class.getName(), columnName, colTypeName);
				propertie.getWriteMethod().invoke(instance, result.getLong(colIndex));
			}
			else if(field.getType().getName().equalsIgnoreCase(BigDecimal.class.getName()))
			{
				fieldsOfType(field, BigDecimal.class.getName(), columnName, colTypeName);
				propertie.getWriteMethod().invoke(instance, result.getBigDecimal(colIndex));
			}
			else if(field.getType().getName().equalsIgnoreCase(double.class.getName()))
			{
				fieldsOfType(field, double.class.getName(), columnName, colTypeName);
				propertie.getWriteMethod().invoke(instance, result.getDouble(colIndex));
			}
			else if(field.getType().getName().equalsIgnoreCase(float.class.getName()))
			{
				fieldsOfType(field, float.class.getName(), columnName, colTypeName);
				propertie.getWriteMethod().invoke(instance, result.getFloat(colIndex));
			}
			else
			{
				throw new NoSuchFieldException("O campo [ "+field.getName()+" ] foi declarado como [ "+field.getType().getName()+" ] que � incompativel com a coluna [ "+columnName+" ] que � do tipo [ "+colTypeName+" ].");
			}
		}
		
		return(false);
	}

	/**
	 * M�todo que verifica se o campo da SQL � do mesmo tipo do campo a ser configurado
	 * 
	 * @param field A vari�vel que receber� o valor
	 * @param className O nome da classe que ser� o destino
	 * @param columnName O nome da coluna retornado pela SQL
	 * @param colTypeName O tipo da coluna retornado pelo SQL
	 * 
	 * @throws NoSuchFieldException Caso ocorr algum erro de ausencia de campos, uma exce��o sera lan�ada
	 */
	private static void fieldsOfType(Field field, String className, String columnName, String colTypeName) throws NoSuchFieldException
	{
		if(!field.getType().getName().equalsIgnoreCase(className))
		{
			throw new NoSuchFieldException("O campo [ "+field.getName()+" ] foi declarado como [ "+field.getType().getName()+" ] que � um tipo incompativel com a coluna [ "+columnName+" ] que � do tipo [ "+colTypeName+" ].");
		}
	}
	
	/**
	 * M�todo que retorna o nome das colunas no {@link ResultSet}
	 * 
	 * @param result O {@link ResultSet} que contem as colunas
	 * 
	 * @return Um array contendo os nomes das colunas
	 * @throws SQLException Caso ocorra algum erro com uma excess�o ser� lan�ada
	 */
	private static String[] getColumnsNames(ResultSet result) throws SQLException 
	{
		String[] retorno;
	
		retorno = new String[result.getMetaData().getColumnCount()];
		
		for (int i = 0; i < retorno.length; i++) 
		{
			retorno[i] = result.getMetaData().getColumnName(i+1);
		}
		return retorno;
	}
}

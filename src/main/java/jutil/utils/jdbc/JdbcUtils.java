package jutil.utils.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import jutil.data.dtos.JdbcConnectionDTO;
import jutil.data.enums.JdbcEnum;

/**
 * Classe utilit�ria para manipula��o de de conex�es JDBC
 * 
 * @author Diego Steyner
 */
public class JdbcUtils 
{
	private Connection connection;
	private PrepareStamentUtils psutils = new PrepareStamentUtils(); 
	
	/**
	 * Método que se conecta com o banco de dados usando usu�rio e senha
	 * 
	 * @param info O ENUM {@link JdbcEnum} do banco que se deseja conectar
	 * @param usuarioBd O usu�rio autorizado a fazer a conex�o
	 * @param senhaBd A senha do usu�rio
	 * 
	 * @return Um {@link Connection} contendo a conex�o criada
	 * @throws Exception Caso ocorra algum erro uma excess�o ser� lan�ada
	 */
	public Connection conectar(JdbcEnum info, String usuarioBd, String senhaBd) throws Exception
    {
        Class.forName(info.getDriver());

        setConnection((Connection) DriverManager.getConnection(info.getConectionString(), usuarioBd, senhaBd));

        return (getConnection());
    }

	/**
	 * Método que conecta com um banco de dados usando uma URL de conex�o
	 * 
	 * @param driver O ENUM {@link JdbcEnum} do banco que se deseja conectar
	 * @param info As informações de conexão
	 * @param autoCommit Se True, a conex�o far� o commit autom�tico.
	 * 
	 * @return Um {@link Connection} contendo a conex�o criada
	 * @throws Exception Caso ocorra algum erro uma excess�o ser� lan�ada
	 */
	public boolean conectar(JdbcEnum driver, JdbcConnectionDTO info, boolean autoCommit) throws Exception
	{
		Class.forName(driver.getDriver());
		
		setConnection((Connection) DriverManager.getConnection(driver.getConnectionUrl(info)));
		
		getConnection().setAutoCommit(autoCommit);
		
		return (getConnection() != null);
	}
	
	/**
	 * Método que desconecta do banco
	 * @throws SQLException Caso ocorra algum erro uma excess�o ser� lan�ada
	 */
	public void desconectar() throws SQLException
	{
		if(!getConnection().isClosed())
		{
			getConnection().close();
		}
	}
	
	/**
	 * Método que executa um SELECT sem parametros no banco
	 * 
	 * @param query O SELECT que se deseja executar
	 * 
	 * @return o {@link ResultSet} da execu��o
	 * @throws SQLException Caso ocorra algum erro uma excess�o ser� lan�ada
	 * @see ResultSetUtils Classe utilitaria para manipula��o de {@link ResultSet}
	 */
	public ResultSet executeQuery(String query) throws SQLException
	{
		return(getConnection().prepareStatement(query).executeQuery());
	}

	/**
	 * Método que executa um SELECT com parametros no banco
	 * 
	 * @param query O SELECT que se deseja executar
	 * @param parameters Os parametros a serem substituidos no SELECT
	 * 
	 * @return o {@link ResultSet} da execu��o
	 * @throws SQLException Caso ocorra algum erro uma excess�o ser� lan�ada
	 * @see ResultSetUtils Classe utilitaria para manipula��o de {@link ResultSet}
	 */
	public ResultSet executeQuery(String query, Object... parameters) throws SQLException
	{
		return(psutils.configureStatements(getConnection().prepareStatement(query), parameters).executeQuery());
	}

	/**
	 * Método que executa um SELECT com parametros no banco
	 * 
	 * @param query O SELECT que se deseja executar
	 * @param resultSetType O tipo do {@link ResultSet} a ser retornado
	 * @param resultSetConcurrecy O tipo de concorrencia do {@link ResultSet} a ser retornado
	 * @param parameters Os parametros a serem substituidos no SELECT
	 * 
	 * @return o {@link ResultSet} da execu��o
	 * @throws SQLException Caso ocorra algum erro uma excess�o ser� lan�ada
	 * @see ResultSetUtils Classe utilitaria para manipula��o de {@link ResultSet}
	 */
	public ResultSet executeQuery(String query, int resultSetType, int resultSetConcurrecy, Object... parameters) throws SQLException
	{
		return(psutils.configureStatements(getConnection().prepareStatement(query, resultSetType, resultSetConcurrecy), parameters).executeQuery());
	}

	/**
	 * Método que executa um UPDATE com parametros no banco
	 * 
	 * @param update O UPDATE que deseja executar
	 * @param parameters Os parametros a serem substituidos no UPDATE
	 * 
	 * @return A quantidade de linhas afetadas
	 * @throws SQLException Caso ocorra algum erro uma excess�o ser� lan�ada
	 */
	public int executeUpdate(String update, Object... parameters) throws SQLException
	{
		return(psutils.configureStatements(getConnection().prepareStatement(update), parameters).executeUpdate());
	}
	
	/**
	 * Método que executa um UPDATE com parametros no banco
	 * 
	 * @param update O UPDATE que deseja executar
	 * @param resultSetType O tipo do {@link ResultSet} a ser retornado
	 * @param resultSetConcurrecy O tipo de concorrencia do {@link ResultSet} a ser retornado
	 * @param parameters Os parametros a serem substituidos no UPDATE
	 * 
	 * @return A quantidade de linhas afetadas
	 * @throws SQLException Caso ocorra algum erro uma excess�o ser� lan�ada
	 */
	public int executeUpdate(String update, int resultSetType, int resultSetConcurrecy, Object... parameters) throws SQLException
	{
		return(psutils.configureStatements(getConnection().prepareStatement(update, resultSetType, resultSetConcurrecy), parameters).executeUpdate());
	}

	/**
	 * Método que chama uma Store Procedure no banco
	 * 
	 * @param spFunctionCall O chamada de execu��o da Store Procedure incluindo os parametros dela
	 * 
	 * @return Se True, A Store procedure foi chamada com sucesso.
	 * @throws SQLException Caso ocorra algum erro uma excess�o ser� lan�ada
	 */
	public boolean executeCallProcedure(String spFunctionCall) throws SQLException
	{
		return(getConnection().prepareCall("{call "+spFunctionCall+"}").execute());
	}

	/**
	 * Método que chama uma Store Procedure no banco
	 * 
	 * @param spFunctionCall O chamada de execu��o da Store Procedure incluindo os parametros dela
	 * @param parameters Os parametros a serem substituidos no comando
	 * 
	 * @return Se True, A Store procedure foi chamada com sucesso.
	 * @throws SQLException Caso ocorra algum erro uma excess�o ser� lan�ada
	 */
	public boolean executeCallProcedure(String spFunctionCall, Object... parameters) throws SQLException
	{
		return(psutils.configureStatements(getConnection().prepareCall("{call "+spFunctionCall+"}"), parameters).execute());
	}

	/**
	 * Método que chama uma Store Procedure no banco
	 * 
	 * @param spFunctionCall O chamada de execu��o da Store Procedure incluindo os parametros dela
	 * @param resultSetType O tipo do {@link ResultSet} a ser retornado
	 * @param resultSetConcurrecy O tipo de concorrencia do {@link ResultSet} a ser retornado
	 * @param parameters Os parametros a serem substituidos no comando
	 * 
	 * @return Se True, A Store procedure foi chamada com sucesso.
	 * @throws SQLException Caso ocorra algum erro uma excess�o ser� lan�ada
	 */
	public boolean executeCallProcedure(String spFunctionCall, int resultSetType, int resultSetConcurrecy, Object... parameters) throws SQLException
	{
		return(psutils.configureStatements(getConnection().prepareCall("{call "+spFunctionCall+"}", resultSetType, resultSetConcurrecy), parameters).execute());
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public PrepareStamentUtils getPsutils() {
		return psutils;
	}

	public void setPsutils(PrepareStamentUtils psutils) {
		this.psutils = psutils;
	}
}

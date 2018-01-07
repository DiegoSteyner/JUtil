package jutil.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jutil.abstracts.AbstractUtils;

/**
 * Classe utilitária para se trabalhar diretamente com conexões JDBC
 * 
 * @author Diego Steyner
 */
public class ConnectionUtils extends AbstractUtils
{
    private Connection connection;
    private Statement  statement;
    private ResultSet  resultSet;
    
    /**
     * Driver de conexão com o postgress usando postgres Driver
     */
    public static final String JDBC_DRIVER_POSTGRES    = "org.postgresql.Driver";

    /**
     * Driver de conexão Sybase usando JTDS Driver
     */
    public static final String JDBC_DRIVER_SYBASE_JTDS = "net.sourceforge.jtds.jdbc.Driver";

    /**
     * Driver de conexão HSQLDB usando o HSQLDB Driver
     */
    public static final String JDBC_DRIVER_HSQLDB      = "org.hsqldb.jdbcDriver";

    /**
     * Driver de conexão H2 usando o H2 Driver
     */
    public static final String JDBC_DRIVER_H2          = "org.h2.Driver";

    /**
     * Driver de conexão MYSQL usando o MYSQL Driver
     */
    public static final String JDBC_DRIVER_MYSQL       = "com.mysql.jdbc.Driver";
    

    /**
     * Construtor Padrao
     */
    public ConnectionUtils()
    {
    }

    /**
     * Metodo que cria uma conexão com o banco de dados
     *
     * @param driver O driver de conexao
     * @param url O endereco do banco
     * @param usuarioBd O login do banco
     * @param senhaBd A senha do banco
     *
     * @return A conexao criada com o banco de dados
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public Connection conectar(String driver, String url, String usuarioBd, String senhaBd) throws Exception
    {
        Class.forName(driver);

        connection = (Connection) DriverManager.getConnection(url, usuarioBd, senhaBd);
        statement = (Statement) connection.createStatement();

        return (connection);
    }

    /**
     * Metodo que encerra a conexao com o banco de dados executando um comando antes
     *
     * @param sqlStatment O Sql que se deseja executar
     * @param closeStatement Se True, será chamado o método {@link #closeStatement()}
     * @param closeResultSet Se True, será chamado o método {@link #closeResultSet()}
     *
     * @return Se True, A conexão foi desconectada com sucesso
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public boolean desconectar(String sqlStatment, boolean closeStatement, boolean closeResultSet) throws Exception
    {
        statement.execute(sqlStatment);

        return (desconectar(closeStatement, closeResultSet));
    }
    
    /**
     * Metodo que encerra a conexao com o banco de dados
     *
     * @param closeStatement Se True, será chamado o método {@link #closeStatement()}
     * @param closeResultSet Se True, será chamado o método {@link #closeResultSet()}
     *
     * @return Se True, A conexão foi desconectada com sucesso
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public boolean desconectar(boolean closeStatement, boolean closeResultSet) throws Exception
    {
        closeStatement();
        
        if (connection != null && !connectionIsClosed())
        {
            connection.close();
        }

        closeResultSet();
        
        return (connection.isClosed());
    }
    
    /**
     * Método que fecha o {@link ResultSet}
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada 
     */
    private void closeResultSet() throws Exception
    {
        if (resultSet != null && !resultSetIsClosed())
        {
            resultSet.close();
        }
    }

    /**
     * Método que fecha o {@link Statement}
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    private void closeStatement() throws Exception
    {
        if (statement != null && !statementIsClosed())
        {
            statement.close();
        }
    }

    /**
     * Método que testa se a conexão está fechada
     * 
     * @return Se True, a conexão está fechada
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public boolean connectionIsClosed() throws Exception
    {
        return (connection.isClosed());
    }

    /**
     * Método que testa se o {@link Statement} está fechado
     * 
     * @return Se True, o Statement está fechado
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public boolean statementIsClosed() throws Exception
    {
        return (statement.isClosed());
    }

    /**
     * Método que testa se o {@link ResultSet} está fechado
     * 
     * @return Se True, o ResultSet está fechado
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public boolean resultSetIsClosed() throws Exception
    {
        return (resultSet.isClosed());
    }

    /**
     * Metodo que executa um comando sql no banco
     *
     * @param sql O comando sql que se deseja executar
     *
     * @return Se True, O comando foi executado com sucesso
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public boolean execute(String sql) throws Exception
    {
        return statement.execute(sql);
    }

    /**
     * Metodo que executa um comando update/insert update no banco
     *
     * @param sqlInsert O comando sql update/insert que se deseja executar
     *
     * @return O codigo de retorno da execucao
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public int executeUpdate(String sqlInsert) throws Exception
    {
        return (statement.executeUpdate(sqlInsert));
    }

    /**
     * Metodo que executa um SQL Select no banco preenchendo a variavel {@link ResultSet} global para manipulação dos dados
     * 
     * @param sqlSelect O SQL select que se deseja executar
     * 
     * @return Se True, O comando foi executado com sucesso
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public boolean executeQuery(String sqlSelect) throws Exception
    {
        setResultSet(statement.executeQuery(sqlSelect));
        return (Boolean.TRUE);
    }

    /**
     * Método que retorna o tipo de uma coluna presente no {@link ResultSet}
     * 
     * @param columName O nome da coluna
     * 
     * @return O {@link Types} da coluna
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public int getColumType(String columName) throws Exception
    {
        return(getResultSet().getMetaData().getColumnType(getResultSet().findColumn(columName)));
    }
    
    /**
     * Metodo que executa um comando select no banco e retorna os valores de uma coluna usando o {@link ResultSet} global
     *
     * @param sqlSelect O comando SQL select que se deseja executar
     * @param nomeColuna O nome da coluna que se esta procurando
     *
     * @return Um {@link ArrayList} de Object contendo os valores da coluna
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public ArrayList<Object> getColumValues(String sqlSelect, String nomeColuna) throws Exception
    {
        ArrayList<Object> retorno = new ArrayList<Object>();
        
        executeQuery(sqlSelect);
        
        while(getResultSet().next())
        {
            retorno.add(getResultSet().getObject(nomeColuna));
        }

        return(retorno);
    }
    
    /**
     * Metodo que executa um comando select no banco e retorna os valores de cada linha usando o {@link ResultSet} global
     *
     * @param sqlSelect O comando SQL select que se deseja executar
     *
     * @return Um {@link Map} contendo como chave o número da linha e como valor os valores de cada linha
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     * 
     */
    public Map<Integer, ArrayList<Object>> getRowValues(String sqlSelect) throws Exception
    {
        Map<Integer, ArrayList<Object>> retorno = new HashMap<Integer, ArrayList<Object>>();
        
        executeQuery(sqlSelect);
        int rowNumber = 0;
        
        while(getResultSet().next())
        {
            rowNumber = getResultSet().getRow();
            retorno.put(rowNumber, new ArrayList<Object>());
            
            for (int i = 0; i < getResultSet().getMetaData().getColumnCount(); i++)
            {
                retorno.get(rowNumber).add(getResultSet().getObject(i+1));
            }
        }

        return(retorno);
    }
    
    /**
     * Metodo que executa um Select e retorna o nome de todas as colunas da tabela informada, os valores não serão armazenados no {@link ResultSet} global
     * 
     * @param table O nome da tabela
     * 
     * @return Um vetor de String contendo o nome de cada coluna
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public String[] getColumNames(String table) throws Exception
    {
        ArrayList<String> retorno = new ArrayList<String>();
        
        try
        {
            ResultSet result = (ResultSet) statement.executeQuery("select * from ".concat(table));
            
            for(int i = 0; i < result.getMetaData().getColumnCount(); i++)
            {
                retorno.add(result.getMetaData().getColumnName((i+1)));
            }
            
            try { result.close(); } catch (Exception e){}
                
            return(((String[]) retorno.toArray(new String[retorno.size()])));
        }
        finally
        {
            retorno.clear();
        }
    }

    /**
     * Metodo que executa um Select e retorna a quantidade de colunas que uma tabela possui
     * 
     * @param nomeTabela O nome da tabela que se deseja contar a quantidade de colunas
     * 
     * @return A quantidade de colunas da tabela
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public int countColumTables(String nomeTabela) throws Exception
    {
        return(((ResultSet) statement.executeQuery("select * from ".concat(nomeTabela))).getMetaData().getColumnCount());
    }
    
    /**
     * Metodo que executa a Insercao de Bytes (Imagens ou Binários) no banco
     * 
     * @param sqlInsert O Sql Insert contendo o WildCard '?' (interrogação)
     * @param posicao A posicao (Index) da '?'(interrogação) no insert
     * @param bytes Os Bytes que se deseja inserir
     * 
     * @return Se True, O comando foi executado com sucesso
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public boolean executeForByte(String sqlInsert, int posicao, byte[] bytes) throws Exception
    {
        PreparedStatement pstmt = getConnection().prepareStatement(sqlInsert);

        pstmt.setBytes(posicao, bytes);
        pstmt.executeUpdate();
        pstmt.close();

        return (pstmt.isClosed());
    }

    /**
     * Metodo que retorna a variavel de Connection usado na classe
     * 
     * @return A variavel Connection usada na classe
     */
    public Connection getConnection()
    {
        return connection;
    }

    /**
     * Metodo que configura a variavel de conexao usada na classe
     * 
     * @param connection A variavel do tipo Connection que deve ser usada
     */
    public void setConnection(Connection connection)
    {
        this.connection = connection;
    }

    /**
     * Metodo que retorna a variavel de Statement usado na classe
     * 
     * @return A variavel Statement usada na classe
     */
    public Statement getStatement()
    {
        return statement;
    }

    /**
     * Metodo que configura a variavel de Statement usada na classe
     * 
     * @param statement A variavel do tipo Statement que deve ser usada
     */
    public void setStatement(Statement statement)
    {
        this.statement = statement;
    }

    /**
     * Metodo que retorna a variavel de ResultSet usado na classe
     * 
     * @return A variavel ResultSet usada na classe
     */
    public ResultSet getResultSet()
    {
        return resultSet;
    }

    /**
     * Metodo que configura a variavel de ResultSet usada na classe
     * 
     * @param resultSet A variavel do tipo ResultSet que deve ser usada
     */
    public void setResultSet(ResultSet resultSet)
    {
        this.resultSet = resultSet;
    }

}

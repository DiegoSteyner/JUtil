package jutil.data.enums;

import jutil.data.dtos.JdbcConnectionDTO;

public enum JdbcEnum 
{
	MYSQL("com.mysql.jdbc.Driver", ""),
	SYBASE("net.sourceforge.jtds.jdbc.Driver", ""),
	HSQLDB("org.hsqldb.jdbcDriver", ""),
	H2("org.h2.Driver", ""),

	ORACLE("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:")
	{
		@Override
		public String getConnectionUrl(JdbcConnectionDTO info) 
		{
			if(info.isSchema())
			{
				return (getConectionString().concat(info.getUser()).concat("/")
											.concat(info.getPass()).concat("@")
											.concat(info.getHost()).concat(":")
											.concat(info.getPorta()).concat(":")
											.concat(info.getDbName()));
			}
			else
			{
				return (getConectionString().concat(info.getUser()).concat("/")
																   .concat(info.getPass()).concat("@")
																   .concat(info.getHost()).concat(":")
																   .concat(info.getPorta()).concat("/")
																   .concat(info.getDbName()));
			}
		}
	},
	POSTGRES("org.postgresql.Driver", "jdbc:postgresql://")
	{
		@Override
		public String getConnectionUrl(JdbcConnectionDTO info) 
		{
			String url = getConectionString().concat(info.getHost()).concat("/")
											 .concat(info.getDbName())
											 .concat("?user=").concat(info.getUser())
											 .concat("&password=").concat(info.getPass());
			
			if(info.getExtraInfoUrl() != null && !info.getExtraInfoUrl().isEmpty())
			{
				for(String key : info.getExtraInfoUrl().keySet())
				{
					url = url.concat("&").concat(key).concat("=").concat(info.getExtraInfoUrl().get(key));
				}
			}
			
			return url;
		}
	};
	
	private String driver;
	private String conectionString;
	
	private JdbcEnum(String driver, String conectionUrl) 
	{
		this.driver = driver;
		this.conectionString = conectionUrl;
	}

	public String getDriver() 
	{
		return driver;
	}

	public void setDriver(String driver) 
	{
		this.driver = driver;
	}
	
	public String getConectionString() {
		return conectionString;
	}

	public void setConectionString(String conectionString) {
		this.conectionString = conectionString;
	}

	/**
	 * 
	 * @param user
	 * @param pass
	 * @param host
	 * @param porta
	 * @param serviceOrSchema
	 * @param isSchema Se True, a conexão tentará se conectar pelo nome do banco ao invés do nome do servico
	 * @return
	 */
	public String getConnectionUrl(JdbcConnectionDTO info)
	{
		return conectionString;
		
	}
}

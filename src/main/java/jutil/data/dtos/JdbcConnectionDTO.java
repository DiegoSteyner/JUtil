package jutil.data.dtos;

import java.util.Map;

public class JdbcConnectionDTO 
{
	private String user;
	private String pass;
	private String host;
	private String porta;
	private String dbName;
	private Map<String, String> extraInfoUrl;
	private boolean isSchema;
	
	public JdbcConnectionDTO(String host, String porta, String user, String pass, String dbName, boolean isSchema) 
	{
		this.user = user;
		this.pass = pass;
		this.host = host;
		this.porta = porta;
		this.dbName = dbName;
		this.isSchema = isSchema;
	}

	public JdbcConnectionDTO(String host, String porta, String user, String pass) 
	{
		this.user = user;
		this.pass = pass;
		this.host = host;
		this.porta = porta;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPorta() {
		return porta;
	}

	public void setPorta(String porta) {
		this.porta = porta;
	}

	public boolean isSchema() {
		return isSchema;
	}

	public void setSchema(boolean isSchema) {
		this.isSchema = isSchema;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dbName == null) ? 0 : dbName.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + (isSchema ? 1231 : 1237);
		result = prime * result + ((pass == null) ? 0 : pass.hashCode());
		result = prime * result + ((porta == null) ? 0 : porta.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JdbcConnectionDTO other = (JdbcConnectionDTO) obj;
		if (dbName == null) {
			if (other.dbName != null)
				return false;
		} else if (!dbName.equals(other.dbName))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (isSchema != other.isSchema)
			return false;
		if (pass == null) {
			if (other.pass != null)
				return false;
		} else if (!pass.equals(other.pass))
			return false;
		if (porta == null) {
			if (other.porta != null)
				return false;
		} else if (!porta.equals(other.porta))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	public Map<String, String> getExtraInfoUrl() {
		return extraInfoUrl;
	}

	public void setExtraInfoUrl(Map<String, String> extraInfoUrl) {
		this.extraInfoUrl = extraInfoUrl;
	}
}

package jutil.utils.ldap;

import java.io.Serializable;

import javax.naming.directory.Attributes;

/**
 * Classe para armazenamento de valores (Value Object) dos resultados de pesquisa da classe {@link LDAPUtils}
 * 
 * @author Diego Steyner
 */
public class User implements Serializable
{
    private static final long serialVersionUID = 8840071268458620561L;
    
    private String distinguishedName;
	private String userPrincipal;
	private String commonName;
	private boolean blockChangePassword;

	/**
	 * Construtor parametrizado
	 * 
	 * @param attr O objeto com os atributos do usuário
	 * @throws Exception Caso algum erro ocorra, uma exceção será lançada.
	 */
	public User(Attributes attr) throws Exception 
	{
		userPrincipal = (String) attr.get(LDAPUtils.LDAP_ATTRIBUTE_USER_PRINCIPAL_NAME).get();
		commonName = (String) attr.get(LDAPUtils.LDAP_ATTRIBUTE_CN).get();
		distinguishedName = (String) attr.get(LDAPUtils.LDAP_ATTRIBUTE_DISTINGUISHED_NAME).get();
		blockChangePassword = true;
	}

    /**
     * Método que retorna o valor da variável distinguishedName
     * 
     * @return O valor presente na variável distinguishedName
     */
    public String getDistinguishedName()
    {
        return distinguishedName;
    }

    /**
     * Método que altera o valor da variável distinguishedName
     * 
     * @param distinguishedName O novo valor da variável distinguishedName
     */
    public void setDistinguishedName(String distinguishedName)
    {
        this.distinguishedName = distinguishedName;
    }

    /**
     * Método que retorna o valor da variável userPrincipal
     * 
     * @return O valor presente na variável userPrincipal
     */
    public String getUserPrincipal()
    {
        return userPrincipal;
    }

    /**
     * Método que altera o valor da variável userPrincipal
     * 
     * @param userPrincipal O novo valor da variável userPrincipal
     */
    public void setUserPrincipal(String userPrincipal)
    {
        this.userPrincipal = userPrincipal;
    }

    /**
     * Método que retorna o valor da variável commonName
     * 
     * @return O valor presente na variável commonName
     */
    public String getCommonName()
    {
        return commonName;
    }

    /**
     * Método que altera o valor da variável commonName
     * 
     * @param commonName O novo valor da variável commonName
     */
    public void setCommonName(String commonName)
    {
        this.commonName = commonName;
    }

    /**
     * Método que retorna o valor da variável blockChangePassword
     * 
     * @return O valor presente na variável blockChangePassword
     */
    public boolean isBlockChangePassword()
    {
        return blockChangePassword;
    }

    /**
     * Sem necessidade de Javadoc
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "User [distinguishedName=" + distinguishedName + ", userPrincipal=" + userPrincipal + ", commonName=" + commonName + ", blockChangePassword=" + blockChangePassword + "]";
    }
    
    /** Sem necessidade de Javadoc
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (blockChangePassword ? 1231 : 1237);
        result = prime * result + ((commonName == null) ? 0 : commonName.hashCode());
        result = prime * result + ((distinguishedName == null) ? 0 : distinguishedName.hashCode());
        result = prime * result + ((userPrincipal == null) ? 0 : userPrincipal.hashCode());
        return result;
    }

    /** Sem necessidade de Javadoc
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        User other = (User) obj;
        if (blockChangePassword != other.blockChangePassword)
        {
            return false;
        }
        if (commonName == null)
        {
            if (other.commonName != null)
            {
                return false;
            }
        }
        else if (!commonName.equals(other.commonName))
        {
            return false;
        }
        if (distinguishedName == null)
        {
            if (other.distinguishedName != null)
            {
                return false;
            }
        }
        else if (!distinguishedName.equals(other.distinguishedName))
        {
            return false;
        }
        if (userPrincipal == null)
        {
            if (other.userPrincipal != null)
            {
                return false;
            }
        }
        else if (!userPrincipal.equals(other.userPrincipal))
        {
            return false;
        }
        return true;
    }
}

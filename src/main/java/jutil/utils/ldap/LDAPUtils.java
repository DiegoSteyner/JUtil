package jutil.utils.ldap;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;
import javax.naming.ldap.StartTlsRequest;
import javax.naming.ldap.StartTlsResponse;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import jutil.abstracts.AbstractUtils;
import jutil.utils.StringUtils;

/**
 * Classe utilitária para trabalhar com autenticação LDAP
 * 
 * @author Diego Steyner
 */
public class LDAPUtils extends AbstractUtils 
{
    /**
     * URL inicial do protocolo
     */
    public static final String LDAP_PROTOCOL_URL                  = "ldap://";
    
    /**
     * Atributo onde normalmente é armazenado o password do usuário
     */
    public static final String LDAP_ATTRIBUTE_UNICODE_PWD         = "unicodePwd";
    
    /**
     * Atributo onde normalmente é armazenado o nome de login do usuário
     */
    public static final String LDAP_ATTRIBUTE_USER_PRINCIPAL_NAME = "userPrincipalName";
    
    /**
     * Atributo onde normalmente é armazenado o nome de login do usuário do usuário
     */
    public static final String LDAP_ATTRIBUTE_SAMACCOUNTNAME      = "samaccountname";
    
    /**
     * Atributo onde normalmente são armazenados os grupos do qual o usuário é membro
     */
    public static final String LDAP_ATTRIBUTE_MEMBER_OF           = "memberOf";
    
    /**
     * Atributo onde normalmente é armazenado o nome do usuário, pode ser apenas o primeiro nome ou ele todo
     */
    public static final String LDAP_ATTRIBUTE_GIVENNAME           = "givenname";
    
    /**
     * Atributo onde normalmente é armazenado o nome do usuário, pode ser apenas o último nome ou ele todo 
     */
    public static final String LDAP_ATTRIBUTE_SN                  = "sn";
    
    /**
     * Atributo onde normalmente é armazenado o ID do usuário
     */
    public static final String LDAP_ATTRIBUTE_UID                 = "uid";
    
    /**
     * Atributo onde normalmente é armazenado o nome do usuário, tem a mesma função do CN, mas para uso em sistemas legados e algumas diferenças de retorno
     */
    public static final String LDAP_ATTRIBUTE_NAME                = "name";
    
    /**
     * Atributo onde normalmente é armazenado o nome do usuário, geralmente representando a árvore completa do usuário
     */
    public static final String LDAP_ATTRIBUTE_CN                  = "cn";
    
    /**
     * Atributo onde normalmente é armazenado o nome de identificaçao do usuário, geralmente representando a árvore completa do usuário da mesma forma que o CN
     */
    public static final String LDAP_ATTRIBUTE_DISTINGUISHED_NAME  = "distinguishedName";
    
    /**
     * O provider de contexto LDAP do Java
     */
    public static final String LDAP_JNDI                          = "com.sun.jndi.ldap.LdapCtxFactory";
    
    /**
     * Atributos normalmente usados em LDAP Querys 
     */
    private static String[] LDAP_COMMONS_USER_ATTRIBUTES = { LDAP_ATTRIBUTE_DISTINGUISHED_NAME,
                                                             LDAP_ATTRIBUTE_CN, 
                                                             LDAP_ATTRIBUTE_NAME,
                                                             LDAP_ATTRIBUTE_UID, 
                                                             LDAP_ATTRIBUTE_SN,
                                                             LDAP_ATTRIBUTE_GIVENNAME,
                                                             LDAP_ATTRIBUTE_MEMBER_OF,
                                                             LDAP_ATTRIBUTE_SAMACCOUNTNAME, 
                                                             LDAP_ATTRIBUTE_USER_PRINCIPAL_NAME 
                                                            };
	
    /**
     * Construtor privado
     */
	private LDAPUtils()
    {
	    throw EXCEPTION_CONSTRUTOR;
    }
	
	/**
	 * Método que faz uma conexão com um servidor padrão LDAP
	 * 
	 * @param ldapHostName O IP ou nome do Host do servidor
	 * @param ldapHostRMIPort A porta de conexão com o servidor
	 * @param userDN O DN (Distinguished Name) do usuário com permissão de conexão no servidor
	 * @param userPass O Password do usuário com permissão de conexão no servidor
	 * 
	 * @return O objeto {@link LdapContext} contendo a conexão com o LDAP informado
	 * @throws Exception Caso algum erro ocorra uma excessão será lançada
	 */
	public static LdapContext connect(Object ldapHostName, Object ldapHostRMIPort, String userDN, String userPass) throws Exception
	{
		String providerURL = (new StringBuilder(LDAP_PROTOCOL_URL)).append(ldapHostName).append(":").append(ldapHostRMIPort).toString();
		return (new InitialLdapContext(getEnvHashTable(providerURL, userDN, userPass, null), null));
	}

	/**
     * Método que faz uma conexão com um servidor padrão LDAP
     * 
     * @param ldapHostName O IP ou nome do Host do servidor
     * @param ldapHostRMIPort A porta de conexão com o servidor
     * @param userLogin O login do usuário com permissão de conexão no servidor
     * @param userPass O Password do usuário com permissão de conexão
     * @param domain O dominio de autenticação do usuário
     * 
     * @return O objeto {@link LdapContext} contendo a conexão com o LDAP informado
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
	public static LdapContext connect(Object ldapHostName, Object ldapHostRMIPort, String userLogin, String userPass, String domain) throws Exception
	{
	    String providerURL = (new StringBuilder(LDAP_PROTOCOL_URL)).append(ldapHostName).append(":").append(ldapHostRMIPort).toString();
	    return new InitialLdapContext(getEnvHashTable(providerURL, userLogin, userPass, domain), null);
	}

	/**
	 * Método que gera o objeto com as informações de conexão
	 * 
	 * @param providerURL A URL de conexão
	 * @param userLogin O usuáiro
	 * @param userPass A senha
	 * @param domain O dominio de autenticação
	 * 
	 * @return O objeto {@link Hashtable} com as informações de conexão
	 * @throws Exception Caso algum erro ocorra uma excessão será lançada
	 */
	private synchronized static Hashtable<String, String> getEnvHashTable(String providerURL, String userLogin, String userPass, String domain) throws Exception
	{
	    Hashtable<String, String> env = new Hashtable<String, String>();
        
        env.put("java.naming.factory.initial", LDAP_JNDI);
        env.put("java.naming.provider.url", providerURL);
        env.put("java.naming.security.authentication", "simple");
        
        if(StringUtils.isNullOrEmpty(Boolean.TRUE, domain))
        {
            env.put("java.naming.security.principal", userLogin);
        }
        else
        {
            env.put("java.naming.security.principal", userLogin.concat("@").concat(domain));
        }
        
        env.put("java.naming.security.credentials", userPass);
        env.put("com.sun.jndi.ldap.connect.pool", "true");
        
        return(env);
	}
	
	/**
	 * Método que desconecta do servidor LDAP
	 * 
	 * @param context O objeto {@link LdapContext} contendo a conexão
	 * @throws Exception Caso algum erro ocorra uma excessão será lançada
	 */
	public static void disconnect(LdapContext context) throws Exception
	{
		context.close();
	}
	
	/**
	 * Método que retorna o valor de um atributo do usuário pesquisado
	 * 
	 * @param context O objeto {@link LdapContext} contendo a conexão com o servidor
	 * @param ldapSearchBase A base de busca
	 * @param accountName A conta que se está procurando
	 * @param atribbute O nome do atributo que se deseja recuperar
	 * 
	 * @return O valor presente no atributo, caso o usuário não exista, será retornado null
	 * @throws Exception Caso algum erro ocorra uma excessão será lançada
	 */
	public static String getSingleAtributeValue(LdapContext context, String ldapSearchBase, String accountName, String atribbute) throws Exception
	{
		SearchResult result = findAccountByAccountName(context, ldapSearchBase, accountName);
		
		if(result != null)
		{
			return(result.getAttributes().get(atribbute).get(0).toString());
		}
		
		return(null);
	}

	/**
	 * Método que retorna uma lista com os valores presente em um campo multivalorado
	 * 
	 * @param result O objeto {@link SearchResult} com os resultados da pesquisa
	 * @param attribute O atributo multivalorado
	 * 
	 * @return A lista com os valores presente no campo
	 * @throws Exception Caso algum erro ocorra uma excessão será lançada
	 */
	@SuppressWarnings("unchecked")
    public static List<String> getMultiAttributesVales(SearchResult result, String attribute) throws Exception
	{
	    NamingEnumeration<String> values = (NamingEnumeration<String>) result.getAttributes().get(attribute).getAll();
        List<String> retorno = new ArrayList<String>();
	    
        while( values.hasMoreElements() )
        {
            retorno.add(values.next());
        }
        
        return(retorno);
	}

	/**
	 * Método que retorna o valor presente em um campo singleValue
     * 
     * @param result O objeto {@link SearchResult} com os resultados da pesquisa
     * @param attribute O atributo singleValue
     * 
     * @return O valor presente no campo
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
	 */
	public static String getSingleAttributesVales(SearchResult result, String attribute) throws Exception
	{
	    if(result != null)
        {
            return(result.getAttributes().get(attribute).get(0).toString());
        }
        
        return(null);
	}
	
	/**
	 * Método que recupera o Distinguished Name (DN) do usuário pesquisado
	 * 
	 * @param context O objeto {@link LdapContext} contendo a conexão com o servidor
	 * @param ldapSearchBase A base de busca
     * @param accountName A conta que se está procurando
     * 
	 * @return O Distinguished Name do usuário
	 * @throws Exception Caso algum erro ocorra uma excessão será lançada
	 */
	public static String findAccountDnByAccountName(LdapContext context, String ldapSearchBase, String accountName) throws Exception
	{
		SearchResult result = findAccountByAccountName(context, ldapSearchBase, accountName);
		
		if(result != null)
		{
		    return(result.getAttributes().get(LDAP_ATTRIBUTE_DISTINGUISHED_NAME).get(0).toString());
		}
		
		return(null);
	}
	
	/**
	 * Método que retorna o objeto de resultado das buscas para um determinado usuário 
     * 
     * @param context O objeto {@link LdapContext} contendo a conexão com o servidor
     * @param ldapSearchBase A base de busca
     * @param accountName A conta que se está procurando
     * 
     * @return O {@link SearchResult} do usuário
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
	 */
	public static SearchResult findAccountByAccountName(LdapContext context, String ldapSearchBase, String accountName) throws Exception 
    {
        String searchFilter = "(&(objectClass=user)(sAMAccountName=" + accountName + "))";

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        NamingEnumeration<SearchResult> results = context.search(ldapSearchBase, searchFilter, searchControls);

        SearchResult searchResult = null;
        if(results.hasMoreElements()) 
        {
             searchResult = (SearchResult) results.nextElement();

            if(results.hasMoreElements()) 
            {
                throw new Exception("Multiplos usuários encontrados para a conta : " + accountName);
            }
        }
        
        return searchResult;
    }
	
	/**
	 * Método que executa uma search no LDAP e retorna uma lista com os resultados encontrados
	 * 
	 * @param context O objeto {@link LdapContext} contendo a conexão com o servidor
	 * @param search A procura que se deseja executar
	 * @param baseSearch A base de busca
	 * 
	 * @return Uma lista com os resultados dentro
	 * @throws Exception Caso algum erro ocorra uma excessão será lançada
	 */
    public static List<SearchResult> getAllResultsForSearch(LdapContext context, String search, String baseSearch) throws Exception
    {
        Control[] ctrl = new Control[] { new PagedResultsControl(3000, true) };
        LdapContext con = context.newInstance(ctrl);
        
        con.setRequestControls(ctrl);

        byte[] cookie = null;
        NamingEnumeration<SearchResult> resultados = null;
        Control[] resControl = null;
        PagedResultsResponseControl prrc = null;

        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        List<SearchResult> retorno = new ArrayList<SearchResult>();
        
        do
        {
            resultados = con.search(baseSearch, search, controls);

            while (resultados.hasMoreElements())
            {
                retorno.add((SearchResult) resultados.next());
            }

            resControl = con.getResponseControls();
            
            if (controls != null)
            {
                for (int i = 0; i < resControl.length; i++)
                {
                    if (resControl[i] instanceof PagedResultsResponseControl)
                    {
                        prrc = (PagedResultsResponseControl) resControl[i];
                        cookie = prrc.getCookie();
                    }
                }
            }

            con.setRequestControls(new Control[] { new PagedResultsControl(3000, cookie, Control.CRITICAL) });
        }
        while (cookie != null);

        return retorno;
    }
	
	/**
	 * Método que encontra um grupo baseado no SID do grupo
	 * 
	 * @param context O objeto {@link LdapContext} contendo a conexão com o servidor
     * @param ldapSearchBase A base de busca
	 * @param sid O SID do grupo
	 * 
	 * @return O nome do grupo pertecente ao SID
	 * @throws Exception Caso algum erro ocorra uma excessão será lançada
	 */
	public static String findGroupBySID(LdapContext context, String ldapSearchBase, String sid) throws Exception 
	{
		String searchFilter = "(&(objectClass=group)(objectSid=" + sid + "))";

		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		NamingEnumeration<SearchResult> results = context.search(ldapSearchBase, searchFilter, searchControls);

		if (results.hasMoreElements()) 
		{
			SearchResult searchResult = (SearchResult) results.nextElement();

			if (results.hasMoreElements()) 
			{
				throw new Exception("Multiplos grupos encontrados para o SID : " + sid);
			} 
			else 
			{
				return (String) searchResult.getAttributes().get("sAMAccountName").get();
			}
		}
		
		return null;
	}

	/**
	 * Método que retorna todos os usuário dentro de uma determinada base de busca
	 * 
	 * @param context O objeto {@link LdapContext} contendo a conexão com o servidor
     * @param ldapSearchBase A base de busca
     * 
	 * @return Um vetor com todos os usuários encontrados abstraídos para o objeto {@link User}
	 * @throws Exception Caso algum erro ocorra uma excessão será lançada
	 */
	public static User[] getAllUsers(LdapContext context, String searchBase) throws Exception 
	{
		ArrayList<User> users = new ArrayList<User>();

		SearchControls controls = new SearchControls();
		controls.setSearchScope(javax.naming.directory.SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(LDAP_COMMONS_USER_ATTRIBUTES);
		NamingEnumeration<SearchResult> answer = context.search(searchBase, "(objectClass=user)", controls);

		while (answer.hasMore()) 
        {
            Attributes attr = ((SearchResult) answer.next()).getAttributes();
            Attribute user = attr.get(LDAP_ATTRIBUTE_USER_PRINCIPAL_NAME);
            if (user != null) 
            {
                users.add(new User(attr));
            }
        }
		
		return users.toArray(new User[users.size()]);
	}
	
	/**
	 * Método que pesquisa por um usuário no LDAP
     * 
     * @param context O objeto {@link LdapContext} contendo a conexão com o servidor
     * @param searchBase A base de busca
     * @param username A conta que se está procurando
     * 
     * @return O usuário encontrado abstraído para o objeto {@link User}
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
	 */
	public static User getUser(LdapContext context, String searchBase, String username) throws Exception 
	{
        String principalName = username + "@" + searchBase.substring(searchBase.indexOf("DC"), searchBase.length()).replace("DC=", "").replace(",", ".");
        
        SearchControls controls = new SearchControls();
        controls.setSearchScope(javax.naming.directory.SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(LDAP_COMMONS_USER_ATTRIBUTES);
        NamingEnumeration<SearchResult> answer = context.search( searchBase, "(& (userPrincipalName="+principalName+")(objectClass=user))", controls);
        
        if (answer.hasMore()) 
        {
        	Attributes attr = answer.next().getAttributes();
        	Attribute user = attr.get(LDAP_ATTRIBUTE_USER_PRINCIPAL_NAME);
        	if (user!=null) 
        	{
        	    return new User(attr);
        	}
        }
        
        return null;
    }
	
	/**
     * Método que altera o password do usuário se o controlador de domínio do LDAP estiver
     * habilitado.
     * 
     * @param user O objeto {@link User} recuperado do servidor
     * @param oldPass O password antigo do usuário
     * @param newPass O novo password que se deseja atribuir ao usuário
     * 
     * @param trustAllCerts Se True, Pula o Hostname e a validação de certificados. 
     *                      Se False, é necessário importar todos os certificados e trust store e fornecê-los antes de chamar o método.
     *                       
     *                      Exemplo: String keystore = "/<path_java>/jdk1.5.0_01/jre/lib/security/cacerts";
     *                               System.setProperty("javax.net.ssl.trustStore",keystore);
     * @param context O objeto {@link LdapContext} contendo a conexão com o servidor
     * 
     * @exception Exception Caso algum erro ocorra, uma exceção será lançada.
     */
    public void changePassword(User user, String oldPass, String newPass, boolean trustAllCerts, LdapContext context) throws Exception 
    {
        if(user.isBlockChangePassword())
        {
            throw new Exception("A alteração de senha de usuários é desabilitada por padrão pois pode haver diferenças no método que negociação de credenciais com o servidor, o que pode ocasionar bloqueios indevidos, caso deseje alterar, altere o valor de bloqueio do usuário para false");
        }
        
        final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier()
        {
            public boolean verify(String hostname, SSLSession session)
            {
                return (Boolean.TRUE);
            }
        };

        final TrustManager[] TRUST_ALL_CERTS = new TrustManager[] { new X509TrustManager()
        {
            public java.security.cert.X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
            {
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
            {
            }
        } };
        
        //Negociando a conexão SSL/TSL
        StartTlsResponse tls = null;

        try 
        {
            tls = (StartTlsResponse) context.extendedOperation(new StartTlsRequest());
        } 
        catch (Exception e) 
        {
            throw new java.io.IOException("Falha ao estabelecer conexão SSL/TLS para o controlador do Dominio; O servidor LDAP está Habilitado e as negociações de configuradas?");
        }

        // Certificados Exchange
        if (trustAllCerts) 
        {
            tls.setHostnameVerifier(DO_NOT_VERIFY);
            
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, TRUST_ALL_CERTS, null);
            
            tls.negotiate(sc.getSocketFactory());
        } 
        else 
        {
            tls.negotiate();
        }

        // Alterando a senha
        try 
        {
            ModificationItem[] modificationItems = new ModificationItem[2];
            modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute(LDAP_ATTRIBUTE_UNICODE_PWD, getPassword(oldPass)));
            modificationItems[1] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute(LDAP_ATTRIBUTE_UNICODE_PWD, getPassword(newPass)));
            context.modifyAttributes(user.getDistinguishedName(), modificationItems);
        } 
        catch (javax.naming.directory.InvalidAttributeValueException e) 
        {
            String error = StringUtils.stackTraceToString(e);
            if (error.startsWith("[") && error.endsWith("]")) 
            {
                error = error.substring(1, error.length() - 1);
            }
            tls.close();
            
            throw new NamingException("Password não atende os requisitos de complexidade exigidos, erro retornado: "+error);
        }
        catch (NamingException e) 
        {
            tls.close();
            throw e;
        }

        // Fechando a conexão TLS/SSL
        tls.close();
    }

    /**
     * Método que converte a String da senha para um array de bytes aceito pelo servidor
     * 
     * @param pass A senha que se deseja converter
     * 
     * @return O array de byte convertido
     */
    private synchronized byte[] getPassword(String pass) 
    {
        char pwdUnicode[] = ("\"" + pass + "\"").toCharArray();
        byte pwd[] = new byte[pwdUnicode.length * 2];
        
        for (int i = 0; i < pwdUnicode.length; i++) 
        {
            pwd[i * 2 + 1] = (byte) (pwdUnicode[i] >>> 8);
            pwd[i * 2 + 0] = (byte) (pwdUnicode[i] & 0xff);
        }
        
        return pwd;
    }
}

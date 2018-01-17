package jutil.implementation;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Classe de implentação para manipulação de certificados
 * 
 * @author Diego Steyner
 */
public class TrustManagerX509Imp implements X509TrustManager
{
    private final X509TrustManager manager;
    private X509Certificate[] chain;

    /**
     * Construtor parametrizado
     * 
     * @param manager O {@link X509TrustManager}
     */
    public TrustManagerX509Imp(X509TrustManager manager)
    {
        this.manager = manager;
    }

    /**
     * Veja {@link javax.net.ssl.X509TrustManager#getAcceptedIssuers()}
     */
    public X509Certificate[] getAcceptedIssuers()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Veja {@link javax.net.ssl.X509TrustManager#checkClientTrusted(X509Certificate[], String)}
     */
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Veja {@link javax.net.ssl.X509TrustManager#checkServerTrusted(X509Certificate[], String)}
     */
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
    {
        this.chain = chain;
        manager.checkServerTrusted(chain, authType);
    }

    /**
     * Método que retorna o valor da variável chain
     * 
     * @return O valor presente na variável chain
     */
    public X509Certificate[] getChain()
    {
        return chain;
    }

    /**
     * Método que altera o valor da variável chain
     * 
     * @param chain O novo valor da variável chain
     */
    public void setChain(X509Certificate[] chain)
    {
        this.chain = chain;
    }

    /**
     * Método que retorna o valor da variável manager
     * 
     * @return O valor presente na variável manager
     */
    public X509TrustManager getManager()
    {
        return manager;
    }
}

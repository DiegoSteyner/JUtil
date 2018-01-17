package jutil.utils;

import java.rmi.server.UID;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import jutil.abstracts.AbstractUtils;

/**
 * Classe utilitária para se trabalhar com questões de segurança
 * 
 * @author Diego Steyner
 */
public class SecurityUtils extends AbstractUtils
{
    public static final String ALGORITHM_MD5             = "MD5";
    public static final String ALGORITHM_SHA_1           = "SHA-1";
    public static final String ALGORITHM_SHA1            = "SHA1";
    public static final String ALGORITHM_SHA1PRNG        = "SHA1PRNG";
    public static final String ALGORITMO_SHA256_WITH_RSA = "SHA256withRSA";
    public static final String ALGORITMO_RSA             = "RSA";
    public static final String ALGORITMO_BLOWFISH        = "Blowfish";
    public static final String PASS_FRASE                = "achavedeveter16";
    
    /**
     * Construtor padrão privado
     */
    private SecurityUtils()
    {
        throw EXCEPTION_CONSTRUTOR;
    }

    /**
     * Método que gera um UUID (GUID) randomicamente
     * 
     * @return A UUID gerada randomicamente
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static String getRandomUUID() throws Exception
    {
        return(UUID.randomUUID().toString());
    }
    
    /**
     * Método que gera uma UUID (GUID) randomicamente usando um método seguro
     * 
     * @param secureAlgorithm O algoritmo a ser usado pelo {@link SecureRandom}
     * @param digestAlgorithm O algoritmo a ser usado pelo {@link MessageDigest}
     * 
     * @return A UUID gerada randomicamente
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static String getSecutiryUUID(String secureAlgorithm, String digestAlgorithm) throws Exception
    {
        final String randomNum = new Integer(SecureRandom.getInstance(secureAlgorithm).nextInt()).toString();
        return(getHexEncode(MessageDigest.getInstance(digestAlgorithm).digest(randomNum.getBytes())));
    }
    
    /**
     * Método que gera uma String randômica
     * 
     * @param lenght O tamanho da String
     * @param allowEspecial Se True, Serão incluídos caracteres especiais
     * @param allowUpper Se True, Serão incluídos caracteres maiúsculos
     * @param allowScore Se True, Serão incluídos símbolos
     * @param allowLower Se True, Serão incluídos caracteres minúsculos
     * @param allowNumber Se True, Serão incluídos números
     * 
     * @return A String randomicamente gerada
     * @throws Exception Caso algum erro ocorra uma excessao sera lancada
     */
    public static String getRandomString(int lenght, boolean allowEspecial, boolean allowUpper, boolean allowScore, boolean allowLower, boolean allowNumber) throws Exception
    {
        ArrayList<String> ar = new ArrayList<String>();
        
        try
        {
            if(!allowEspecial && !allowUpper && !allowScore && !allowNumber && !allowLower)
            {
                throw new Exception("Ao menos um tipo de caracter deve ser permitido");
            }
            
            String upperLetter = StringUtils.getAlphabetInUpperCase();
            String lowerLetter = StringUtils.getAlphabetInLowerCase();
            String especials = StringUtils.getEspecialsLetters();
            String scores = StringUtils.getScores();
            String number = StringUtils.getNumbers();
            
            StringBuilder r = new StringBuilder();
            
            if(allowEspecial)
            {
                ar.add(especials);
            }
            if(allowLower)
            {
                ar.add(lowerLetter);
            }
            if(allowUpper)
            {
                ar.add(upperLetter);
            }
            if(allowNumber)
            {
                ar.add(number);
            }
            if(allowScore)
            {
                ar.add(scores);
            }
            
            int index = 0;
            Random random = new Random();
            
            while(r.length() < lenght)
            {
                index = random.nextInt((ar.size() - 0));
                r.append(ar.get(index).charAt(random.nextInt((ar.get(index).length() - 0))));
            }
            
            return(r.toString());
        }
        finally
        {
            ar.clear();
        }
    }
    
    /**
     * Método que gera uma String randomica de 0-9 e de a-z misturados
     *
     * @param length O tamanho que a String deve ter
     * 
     * @return Uma String randomizada contendo caracteres de 0-9 e de a-z
     * @throws Exception Caso ocorra algum erro uma excessao sera lancada
     */
    public static String getRandomString(int length) throws Exception
    {
        StringBuilder sb = new StringBuilder();

        while (sb.length() < length) 
        {
            sb.append(Integer.toHexString(((int)(Math.random() * (length)))));
        }

        return sb.toString();
    }
    
    /**
     * Metodo que randomiza uma Letra do alfabeto
     *
     * @param allowUpper Se True, A letra poderá ser randomizada também em maíuscula
     *
     * @return Uma letra qualquer
     * @throws Exception Caso ocorra algum erro uma excessao sera lancada
     */
    public static String getRandomChar(boolean allowUpper) throws Exception
    {
        ArrayList<String> r = new ArrayList<String>();

        try
        {
            for (char ch = 'a'; ch <= 'z'; ch++)
            {
                r.add(String.valueOf(ch));
            }
            
            if (allowUpper)
            {
                for (char ch = 'A'; ch <= 'Z'; ch++)
                {
                    r.add(String.valueOf(ch));
                }
            }
            
            return (r.get((int) (Math.random() * (r.size() + 1))).toString());
        }
        finally
        {
            r.clear();
        }
    }
    
    /**
     * Método que gera uma UID (UserID)
     * 
     * @return A UID gerada
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static String getUID() throws Exception
    {
        return(new UID().toString());
    }
    
    /**
     * Método que gera uma chave hexadecimal para uma String
     * 
     * @param str A String que se deseja gerar o Hexadecimal
     * 
     * @return A String convertida para Hexadecimal
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static String getHexEncode(byte[] str) throws Exception
    {
        StringBuilder result = new StringBuilder();
        
        char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        
        for (int idx = 0; idx < str.length; ++idx)
        {
            byte b = str[idx];
            result.append(digits[(b & 0xf0) >> 4]);
            result.append(digits[b & 0x0f]);
        }
        
        return (result.toString());
    }
    
    /**
     * Metodo que gera um numero aleatório dentro da casa especificada
     *
     * @param maxNumber O maior valor para o qual o número será gerado, por exemplo caso seja passado o valor 10, sera gerado um numero entre 0 e 10
     * @param canReturnLast Se True, O último número poderá ser retornado
     *
     * @return Um numero randomizado
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static String getRandomNumber(int maxNumber, boolean canReturnLast) throws Exception
    {
        if(canReturnLast)
        {
            return (String.valueOf((int)(Math.random() * (maxNumber+1))));
        }
        
        return (String.valueOf((int)(Math.random() * maxNumber)));
    }

    /**
     * Método que gera um número aleatorio entre dois números
     * 
     * @param min O menor número a ser gerado
     * @param max O maior número a ser gerado
     * @param canReturnLast Se True, Será permitido incluir o maior número
     * 
     * @return Uma String contendo o número randomico
     * @throws Exception Caso algum erro ocorra uma excessao sera lancada
     */
    public static String getRandomNumber(int min, int max, boolean canReturnLast) throws Exception
    {
        if(canReturnLast)
        {
            return String.valueOf(new Random().nextInt((max - min) + 1) + min);
        }
        
        return(String.valueOf(new Random().nextInt((max - min)) + min));
    }
    
    /**
     * Método que criptografa uma String
     * 
     * @param str A String que se deseja criptografar
     * @param algorithm O algoritmo a ser usado, o default é {@link #ALGORITMO_BLOWFISH}
     * @param password A palavra passe, o default é {@link #PASS_FRASE}
     * 
     * @return A String criptografada
     * @throws Exception Caso algum erro ocorra uma exceção será lançada
     */
    public static String cript(String str, String algorithm, String password) throws Exception
    {
        Cipher c = Cipher.getInstance(StringUtils.ifNullOrEmptyGet(Boolean.FALSE, algorithm, ALGORITMO_BLOWFISH));
        SecretKey k = new SecretKeySpec(StringUtils.ifNullOrEmptyGet(Boolean.FALSE, password, PASS_FRASE).getBytes(), StringUtils.ifNullOrEmptyGet(Boolean.FALSE, algorithm, ALGORITMO_BLOWFISH));
        
        c.init(Cipher.ENCRYPT_MODE, k);

        return (StringUtils.byteArrayToBase64String(c.doFinal(str.getBytes())).trim());
    }

    /**
     * Método que descriptografa uma String
     * 
     * @param str A String criptografada que se deseja descriptografar
     * @param algorithm O algoritmo a ser usado, o default é {@link #ALGORITMO_BLOWFISH}
     * @param password A palavra passe, o default é {@link #PASS_FRASE}
     * 
     * @return A String descriptografada
     * @throws Exception Caso algum erro ocorra uma exceção será lançada
     */
    public static String decript(String str, String algorithm, String password) throws Exception
    {
        Cipher c = Cipher.getInstance(StringUtils.ifNullOrEmptyGet(Boolean.FALSE, algorithm, ALGORITMO_BLOWFISH));
        SecretKey k = new SecretKeySpec(StringUtils.ifNullOrEmptyGet(Boolean.FALSE, password, PASS_FRASE).getBytes(), StringUtils.ifNullOrEmptyGet(Boolean.FALSE, algorithm, ALGORITMO_BLOWFISH));

        c.init(Cipher.DECRYPT_MODE, k);
        byte b[] = StringUtils.base64StringToByteArray(str);
        byte b1[] = c.doFinal(b);
        
        return (new String(b1));
    }
    
    /**
     * Metodo que criptografa a String passada por parametro em MD5
     *
     * @param str A String que se deseja Criptografar
     * @param algorithm O algoritmo que se deseja que a String seja criptografada
     * 
     * @return Uma String criptografada
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static String criptStringIn(String str, String algorithm) throws Exception
    {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(str.getBytes());

        byte[] hash = md.digest();

        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < hash.length; i++)
        {
            if ((0xff & hash[i]) < 0x10)
            {
                hexString.append("0").append(Integer.toHexString((0xFF & hash[i])));
            }
            else
            {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }

        return (hexString.toString());
    }

    /**
     * Método que gera uma chave publica e uma privada para autenticação
     * 
     * @param algorithm O algoritmo que se deseja usar, se nenhum for passa, será usado por padrão o {@link #ALGORITMO_RSA}
     * 
     * @return O {@link KeyPair} com as chave publica e privada
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static KeyPair generatePrivatePublicKey(String algorithm) throws Exception
    {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(StringUtils.ifNullOrEmptyGet(Boolean.FALSE, algorithm, ALGORITMO_RSA));
        keyGen.initialize(1024);
        
        return(keyGen.genKeyPair());
    }
    
    /**
     * Método que codifica um array de bytes em base64
     * 
     * @param str O Array de bytes
     * 
     * @return A String codificada em base64
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static String encodeBase64(byte[] str) throws Exception
    {
        return(DatatypeConverter.printBase64Binary(str));
    }

    /**
     * Método que decodifica um array de bytes em base64
     * 
     * @param str O Array de bytes
     * 
     * @return A String decodificada
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static String decodeBase64(byte[] str) throws Exception
    {
        return(DatatypeConverter.printBase64Binary(str));
    }
    
    /**
     * Método que gera um {@link PrivateKey} à partir de uma String Base64
     * 
     * @param str A String Base64
     * @param algorithm O algoritmo que se deseja que seja usado, caso não seja passado nenhum, será usado o algoritmo RSA
     * 
     * @return O Objeto {@link PrivateKey} gerado
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static PrivateKey generatePrivateKeyFromBase64(String str, String algorithm) throws Exception
    {
        KeyFactory kf = KeyFactory.getInstance(StringUtils.ifNullOrEmptyGet(Boolean.FALSE, algorithm, ALGORITMO_RSA));
        
        return(kf.generatePrivate(new PKCS8EncodedKeySpec(DatatypeConverter.parseBase64Binary(str))));
    }

    /**
     * Método que gera um {@link PublicKey} à partir de uma String Base64
     * 
     * @param str A String Base64
     * @param algorithm O algoritmo que se deseja que seja usado, caso não seja passado nenhum, será usado o algoritmo RSA
     * 
     * @return O Objeto {@link PublicKey} gerado
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static PublicKey generatePublicKeyFromBase64(String str, String algorithm) throws Exception
    {
        KeyFactory pk = KeyFactory.getInstance(StringUtils.ifNullOrEmptyGet(Boolean.FALSE, algorithm, ALGORITMO_RSA));
        
        return(pk.generatePublic(new X509EncodedKeySpec(DatatypeConverter.parseBase64Binary(str))));
    }
    
    /**
     * Método que testa se a combinação de chaves Publica e Privada são válidas
     * 
     * @param privateKey A chave privada
     * @param publicKey A chave publica
     * 
     * @return Se True, a combinação é válida
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static boolean privatePublicKeyIsValid(PrivateKey privateKey, PublicKey publicKey) throws Exception
    {
        String data = "somedatatest";

        Signature privateSigner = Signature.getInstance(ALGORITMO_SHA256_WITH_RSA);
        privateSigner.initSign(privateKey);
        privateSigner.update(data.getBytes());
        
        Signature publicSigner = Signature.getInstance(ALGORITMO_SHA256_WITH_RSA);
        publicSigner.initVerify(publicKey);
        publicSigner.update(data.getBytes());
        
        return (publicSigner.verify(privateSigner.sign()));
    }
    
    /**
     * Método que assinada um array de bytes com uma chave privada
     * 
     * @param data O dado a ser assinado
     * @param key A chave privada
     * 
     * @return O array de bytes assinado
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static byte[] signData(byte[] data, PrivateKey key) throws Exception
    {
        Signature signer = Signature.getInstance(ALGORITMO_SHA256_WITH_RSA);
        signer.initSign(key);
        signer.update(data);
        return (signer.sign());
    }

    /**
     * Método que verificar se uma chave publica pode ser usada para a validação de um array de bytes assinado com uma chave privada
     * 
     * @param data O dado original
     * @param key A chave publica
     * @param sig O objeto assinado com a chave privada
     * 
     * @return Se True, A chave publica é válida
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static boolean verifySig(byte[] data, PublicKey key, byte[] sig) throws Exception
    {
        Signature signer = Signature.getInstance(ALGORITMO_SHA256_WITH_RSA);
        signer.initVerify(key);
        signer.update(data);
        return (signer.verify(sig));
    }
    
    /**
     * Método que adiciona um ou mais certificados no repositório local ou remoto
     * 
     * @param ks O repositório de certificados
     * @param algorithmsForUpdate Os algoritmos de criptografia local para serem atualizados
     * @param certName O nome dos certificados na cadeia certificadora</br></br> Obs: Cada certificado terá o seu index anexado ao nome, certName-index
     * @param certs Os certificados que se deseja adicionar
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public void addCertificatesInRepository(KeyStore ks, String algorithmsForUpdate[], String certName, X509Certificate[] certs) throws Exception
    {
        for (int i = 0; i < certs.length; i++)
        {
            for (int j = 0; j < algorithmsForUpdate.length; j++)
            {
                MessageDigest.getInstance(algorithmsForUpdate[j]).update(certs[i].getEncoded());
            }
            
            ks.setCertificateEntry(new StringBuilder().append(certName).append("-").append(i).toString(), certs[i]);
        }
    }
}

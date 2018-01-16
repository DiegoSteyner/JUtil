package jutil.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import jutil.abstracts.AbstractUtils;

/**
 * Classe que contem Método para se trabalhar com a rede local da máquina ou com links de internet
 * 
 * @author Diego Steyner
 */
public class NetworkUtils extends AbstractUtils
{

    /**
     * Regex para validação de endereços IP
     */
    public static final String  REGEX_IP    = "(([0-1]?[0-9]{1,2}\\.)|(2[0-4][0-9]\\.)|(25[0-5]\\.)){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))";

    /**
     * Prefixo do protocolo HTTP
     */
    private static final String PREFIX_HTTP = "http://";

    /**
     * Construtor privado
     */
    private NetworkUtils()
    {
        throw EXCEPTION_CONSTRUTOR;
    }

    /**
     * Método que executa um ping em um site qualquer
     *
     * @param site O site que se deseja pingar
     * 
     * @return Se True, O site foi pingado e a resposta foi recebida com suscesso
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static boolean pingSite(String site) throws Exception
    {
        URL url = new URL((site.startsWith(PREFIX_HTTP)) ? site : PREFIX_HTTP.concat(site));
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

        httpConn.setInstanceFollowRedirects(true);
        httpConn.setConnectTimeout(3000);
        httpConn.connect();

        int respCode = httpConn.getResponseCode();

        if (respCode == HttpURLConnection.HTTP_OK)
        {
            return (Boolean.TRUE);
        }
        else
        {
            return (Boolean.FALSE);
        }
    }

    /**
     * Método que abre um link no navegador padrao do sistema operacional
     *
     * @param link O link que deve ser aberto no navegador
     *
     * @return Se True, O link foi aberto com suscesso
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static boolean openLinkInDefaultBrowser(String link) throws Exception
    {
        Desktop.getDesktop().browse((new URI(link)));

        return (Boolean.TRUE);
    }

    /**
     * Método que verifica se a String passada é um IP
     *
     * @param str A String que se deseja verificar
     * 
     * @return Se True, A String é um ip
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static boolean validarIp(String str) throws Exception
    {
        return (Pattern.compile(REGEX_IP).matcher(str).matches());
    }

    /**
     * Método que retorna o nome de um host baseado no seu IP
     * 
     * @param ip O IP que se deseja verificar
     * 
     * @return O nome do host
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static String getHostName(String ip) throws Exception
    {
        if (StringUtils.isNullOrEmpty(Boolean.TRUE, ip))
        {
            throw new Exception("O IP informado para verificação está nulo ou vazio");
        }
        else
        {
            return (InetAddress.getByName(ip).getHostName());
        }
    }

    /**
     * Método que retorna o endereço IP local
     * 
     * @param multiplesInterfaces Se True, será utilizado uma lógica de Gateway para recuperar a interface correta no qual o host responde
     * 
     * @return O IP local
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static String getIpLocal(boolean multiplesInterfaces) throws Exception
    {
        if (multiplesInterfaces)
        {
            Enumeration<NetworkInterface> netList = NetworkInterface.getNetworkInterfaces();
            Enumeration<InetAddress> addressList = null;
            InetAddress address = null;
            NetworkInterface net = null;

            while (netList.hasMoreElements())
            {
                if ((net = netList.nextElement()) != null)
                {
                    addressList = net.getInetAddresses();

                    while (addressList.hasMoreElements())
                    {
                        if ((address = addressList.nextElement()) != null)
                        {
                            if (!address.getCanonicalHostName().equals(InetAddress.getLocalHost().getHostName()) && !address.isLoopbackAddress())
                            {
                                return (address.getHostAddress());
                            }
                        }
                    }
                }
            }

            return (null);
        }
        else
        {
            return (InetAddress.getLocalHost().getHostAddress());
        }
    }

    /**
     * Método que recupera e inprime as informações recuperáveis de todas as placas de rede da máquina
     * 
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static void printInformationAboutAllNetworkInterfaces() throws Exception
    {
        InetAddress localhost = InetAddress.getLocalHost();
        out.println(" Localhost");

        if (localhost != null)
        {
            out.println("             Nome : " + localhost.getHostName());
            out.println("               ip : " + localhost.getHostAddress());
            out.println("          Gateway : " + localhost.getCanonicalHostName());
            out.println("       Mac-adress : " + rawIpToMacString(localhost.getAddress()));
            out.println("-----------------------------------------------------------------");
        }
        else
        {
            out.println("Nenhuma interface local foi encontrada");
        }

        Enumeration<NetworkInterface> netList = NetworkInterface.getNetworkInterfaces();
        List<InterfaceAddress> adrressList = null;
        NetworkInterface net = null;

        while (netList.hasMoreElements())
        {
            net = netList.nextElement();

            out.println("--------------------");
            out.println("                     Nome : " + net.getName());
            out.println("             Dysplay Name : " + net.getDisplayName());

            if (net.getHardwareAddress() != null)
            {
                if (net.getHardwareAddress().length != 0)
                {
                    out.println("      Mac-address : " + rawIpToMacString(net.getHardwareAddress()));
                }
            }

            out.println("                      MTU : " + net.getMTU());
            out.println("        Suporta Multicast : " + net.supportsMulticast());
            out.println("               É Loopback : " + net.isLoopback());
            out.println("                      PTP : " + net.isPointToPoint());
            out.println("                É Virtual : " + net.isVirtual());
            out.println("              Está ligada : " + net.isUp());

            adrressList = net.getInterfaceAddresses();
            out.println("         Ips configurados : " + adrressList.size());
            int count = 0;

            for (InterfaceAddress intAddr : adrressList)
            {
                count++;
                out.println("                               Index : " + count);
                out.println("                                Nome : " + intAddr.getAddress().getHostName());
                out.println("                                  IP : " + intAddr.getAddress().getHostAddress() + "/" + intAddr.getNetworkPrefixLength());

                if (intAddr.getBroadcast() != null)
                {
                    out.println("           Endereço de broadcast : " + intAddr.getBroadcast().getHostAddress());
                }

                out.println("                             Máscara : " + bitMaskToAddress((Integer.MIN_VALUE >> (intAddr.getNetworkPrefixLength() - 1))));
                out.println("                             Gateway : " + intAddr.getAddress().getCanonicalHostName());
                out.println("                         Mac-address : " + rawIpToMacString(intAddr.getAddress().getAddress()));
                out.println("                             É Local : " + intAddr.getAddress().isSiteLocalAddress());
                out.println("");
            }
        }
    }

    /**
     * Método que converte o array retornada pela Interface em uma String
     * 
     * @param address O array retornado pela interface
     * 
     * @return A String equivalente ao array no formato MAC Adrres
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static synchronized String rawIpToMacString(byte[] address) throws Exception
    {
        final int end = (address.length - 1);

        if (address == null || end == -1)
        {
            throw new Exception("O array está nulo ou vazio, por favor, passe um array válido");
        }

        StringBuilder str = new StringBuilder();

        for (int i = 0;; i++)
        {
            str.append(String.format("%1$02x", address[i]));

            if (i == end)
            {
                return (str.toString());
            }

            str.append(":");
        }
    }

    /**
     * Método que verifica o "BIT" da máscara de rede e cria a máscara correta
     * 
     * @param bitMask O "BIT" configurado para a máscara
     * 
     * @return A String no formato MAC-Address
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static synchronized String bitMaskToAddress(int bitMask) throws Exception
    {
        StringBuilder ip = new StringBuilder();
        ip.append(Integer.toString(0x000000ff & (bitMask >> 24)));

        ip.append(".");
        ip.append(Integer.toString(0x000000ff & (bitMask >> 16)));

        ip.append(".");
        ip.append(Integer.toString(0x000000ff & (bitMask >> 8)));

        ip.append(".");
        ip.append(Integer.toString(0x000000ff & (bitMask)));

        return (ip.toString());
    }

    /**
     * Método que baixa um arquivo da internet
     * 
     * @param fileURL A URL do arquivo
     * @param saveToDir O diretório onde o arquivo deve ser salvo
     * 
     * @return Se True, O Arquivo foi baixado com sucesso
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static boolean downloadFile(String fileURL, String saveToDir) throws Exception
    {
        if (StringUtils.isNullOrEmpty(Boolean.TRUE, fileURL))
        {
            throw new Exception("A URL passada está vazia ou nula, por favor, passe uma URL válida");
        }

        if (fileURL.startsWith(PREFIX_HTTP) || fileURL.startsWith("www"))
        {
            HttpURLConnection httpConn = (HttpURLConnection) new URL(fileURL).openConnection();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                String fileName = "";
                String hasNameOnHeader = httpConn.getHeaderField("Content-Disposition");

                if (hasNameOnHeader != null)
                {
                    int index = hasNameOnHeader.indexOf("filename=");
                    if (index > 0)
                    {
                        fileName = hasNameOnHeader.substring(index + 10, hasNameOnHeader.length() - 1);
                    }
                }
                else
                {
                    fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
                }

                if (StringUtils.containsAnyChar(fileName, StringUtils.getScores()))
                {
                    fileName = StringUtils.clearChars(fileName, StringUtils.getScores());
                }

                InputStream inputStream = httpConn.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(new File(new File(saveToDir), fileName).getAbsolutePath());

                int bytesRead = -1;
                byte[] buffer = new byte[4096];
                while ((bytesRead = inputStream.read(buffer)) != -1)
                {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();
                httpConn.disconnect();

                return (Boolean.TRUE);
            }
            else
            {
                System.err.println("Não foi possível efetuar uma conexão com a URL informada.");
                httpConn.disconnect();
                return (Boolean.FALSE);
            }
        }
        else
        {
            throw new Exception("A URL deve iniciar com o protocolo desejado, por exemplo, HTTP ou WWW, por favor, passe uma URL válida");
        }
    }

    /**
     * Método que lê o conteúdo de um arquivo TXT ou JSON retornado por um link
     * 
     * @param url A URL que se pretende lê
     * 
     * @return As linhas do TXT ou JSON da forma que foi retornado
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String getTextContentFromURL(String url) throws Exception
    {
        Scanner scanner = new Scanner(new URL(url).openStream());
        Scanner s = scanner.useDelimiter("\\Z");
        String content = s.next();

        s.close();
        scanner.close();

        return (content);
    }
}

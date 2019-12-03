package jutil;

import static java.lang.System.out;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import jutil.data.dtos.JdbcConnectionDTO;
import jutil.data.enums.JdbcEnum;
import jutil.utils.LDAPUtils;
import jutil.utils.NetworkUtils;
import jutil.utils.SecurityUtils;
import jutil.utils.StringUtils;
import jutil.utils.SystemUtils;
import jutil.utils.TimerTaskUtil;
import jutil.utils.barcode.Code128;
import jutil.utils.jdbc.JdbcUtils;
import jutil.utils.jdbc.ResultSetUtils;

@SuppressWarnings({ "unused" })
public class PrincipalJUtilUtilitarios
{
    public static void main(String[] args)
    {
        String str = "";

        try
        {
        
        }
        catch (Exception e)
        {
            System.out.println("Erro " + StringUtils.stackTraceToString(e));
        }
    }
}



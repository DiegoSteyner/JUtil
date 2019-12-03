package jutil.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import jutil.abstracts.AbstractUtils;

/**
 * Classe que possui metodos para se trabalhar com arquivos ZIP
 * 
 * @author Diego Steyner
 */
public class ZipUtils extends AbstractUtils
{
    public static final int TAMANHO_BUFFER = 2048;

    public ZipUtils() 
    {
    }
    
    /**
     * Método que lista todos os arquivos e pastas dentro de um Arquivo ZIP
     * 
     * @param zipFile O arquivo ZIP que se deseja listar
     * 
     * @return Um {@link List} contendo o endereco de todos os arquivos e pastas presente dentro do arquivo ZIP
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public List<String> getFilesInZip(File zipFile) throws Exception
    {
        List<String> retorno = new ArrayList<String>();
        ZipFile zip = new ZipFile(zipFile);

        try 
        {
            Enumeration<? extends ZipEntry> entries = zip.entries();

            while (entries.hasMoreElements()) 
            {
                retorno.add(((ZipEntry) entries.nextElement()).toString());
            }
            
            return retorno;
        }
        finally 
        {
            if (zip != null) 
            {
                zip.close();
            }
            
            retorno.clear();
        }
    }

    /**
     * Método que lista todos os arquivos e pastas dentro de um Arquivo ZIP
     * 
     * @param zipFile O arquivo ZIP que se deseja listar
     * 
     * @return Um {@link List} contendo {@link ZipEntry} do arquivo
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public List<ZipEntry> getEntrysInZip(File zipFile) throws Exception
    {
        List<ZipEntry> retorno = new ArrayList<ZipEntry>();
        ZipFile zip = new ZipFile(zipFile);
        
        try 
        {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            
            while (entries.hasMoreElements()) 
            {
                retorno.add(((ZipEntry) entries.nextElement()));
            }
            
            return (retorno);
        }
        finally 
        {
            if (zip != null) 
            {
                zip.close();
            }
            
            retorno.clear();
        }
    }

    /**
     * Método que extrai um arquivo ZIP em um determinado diretório
     * 
     * @param zipFile O arquivo ZIP a ser extraido
     * @param dir O diretório onde o arquivo deve ser extraido
     * @param createDir Se True, Caso o diretório de destino não exista, ele será criado
     * 
     * @return Se True, O arquivo foi extraido com sucesso
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public boolean extractZipToDir(File zipFile, File dir, boolean createDir) throws Exception
    {
        ZipFile zip = null;
        File file = null;
        InputStream is = null;
        OutputStream os = null;
        byte[] buffer = new byte[TAMANHO_BUFFER];

        try 
        {
            if(createDir)
            {
                if (!dir.exists()) 
                {
                    dir.mkdirs();
                }
            }
            
            if (!dir.exists() || !dir.isDirectory()) 
            {
                throw new Exception("O diretório "+dir+" Não existe ou não é um diretório.");
            }
            
            zip = new ZipFile(zipFile);
            Enumeration<? extends ZipEntry> entries = zip.entries();
            ZipEntry entry = null;
            
            while (entries.hasMoreElements()) 
            {
                entry = (ZipEntry) entries.nextElement();
                file = new File(dir, entry.getName());
                
                //se for diretório inexistente, cria a estrutura e pula pra próxima entrada
                if (entry.isDirectory() && !file.exists()) 
                {
                    file.mkdirs();
                    continue;
                }
                
                //se a estrutura de diretórios não existe, cria
                if (!file.getParentFile().exists()) 
                {
                    file.getParentFile().mkdirs();
                }
                
                try 
                {
                    //lê o arquivo do zip e grava em disco
                    is = zip.getInputStream(entry);
                    os = new FileOutputStream(file);
                    int bytesLidos = 0;
                    
                    if (is == null) 
                    {
                        throw new ZipException("Erro ao ler a entrada do zip: " + entry.getName());
                    }
                    
                    while ((bytesLidos = is.read(buffer)) > 0) 
                    {
                        os.write(buffer, 0, bytesLidos);
                    }
                }
                finally 
                {
                    if (is != null) 
                    {
                        is.close();
                    }
                    
                    if (os != null) 
                    {
                        os.close();
                    }
                }
            }
        }
        finally 
        {
            if (zip != null) 
            {
                zip.close();
            }
        }
        
        return(Boolean.TRUE);
    }

    /**
     * Método que cria um arquivo ZIP à partir do diretório informado
     * 
     * @param arqZip O endereço completo onde o arquivo ZIP deve ser salvo
     * @param addRootFolder Se True, A Pasta informada será adicionada ao ZIP como RootFolder
     * @param folder A Pasta que se deseja Zipar
     * 
     * @return Um {@link List} com todos os arquivos internos do ZIP
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public List<ZipEntry> createZipFileFromFolder(File arqZip, boolean addRootFolder, File folder) throws Exception
    {
        if (!folder.isDirectory()) 
        {
            throw new ZipException("O "+folder+" Não é um diretório ou não existe");
        }
        
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ZipOutputStream zos = null;
        File arqs[] = folder.listFiles();
        
        try 
        {
            fos = new FileOutputStream(arqZip);
            bos = new BufferedOutputStream(fos, TAMANHO_BUFFER);
            zos = new ZipOutputStream(bos);
            
            if(addRootFolder)
            {
                ArrayList<File> ar = new ArrayList<File>();
                ar.add(folder);
                ar.addAll(Arrays.asList(arqs));
                
                arqs = ar.toArray(new File[ar.size()]);
                ar.clear();
            }
            
            if(addRootFolder)
            {
                return(addFileInZip(zos, folder, folder.getParent()));
            }
            else
            {
                String root = "";
                
                if(folder.getAbsolutePath().endsWith("\\") || folder.getAbsolutePath().endsWith("/"))
                {
                    root = folder.getAbsolutePath();
                }
                else
                {
                    root = folder.getAbsolutePath().concat(File.separator);
                }
                
                return(addFileInZip(zos, folder, root));
            }
        } 
        finally 
        {
            if (zos != null) 
            {
                zos.close();
            }
            
            if (bos != null) 
            {
                bos.close();
            }
            
            if (fos != null) 
            {
                fos.close();
            }
        }
    }

    /**
     * Método que cria um arquivo ZIP à partir de um arquivo
     * 
     * @param arqZip O endereço completo onde o arquivo ZIP deve ser salvo
     * @param file O arquivo que se deseja Zipar
     * @param dir O diretório que deve ser criado no arquivo ZIP, se passado vazio ou Nulo, o arquivo será adicionado na raiz do ZIP
     * 
     * @return Se True, O arquivo foi criado com sucesso
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public boolean createZipFileFromFile(File arqZip, File file, String dir) throws Exception
    {
        if (!file.isFile()) 
        {
            throw new ZipException("O "+file+" Não é um arquivo ou não existe");
        }
        
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ZipOutputStream zos = null;
        
        try 
        {
            fos = new FileOutputStream(arqZip);
            bos = new BufferedOutputStream(fos, TAMANHO_BUFFER);
            zos = new ZipOutputStream(bos);
            String root = "";
            
            if(StringUtils.isNullOrEmptyTrim(dir))
            {
                if(file.getParent().endsWith("\\") || file.getParent().endsWith("/"))
                {
                    root = file.getParent();
                }
                else
                {
                    root = file.getParent().concat(File.separator);
                }
                
                return(addFileInZip(zos, file, root) != null);
            }
            else
            {
                if(!dir.endsWith("\\") || !dir.endsWith("/"))
                {
                    root = dir.concat("/");
                }
                
                return(addFileInZip(zos, file, root) != null);
            }
        } 
        finally 
        {
            if (zos != null) 
            {
                zos.close();
            }
            
            if (bos != null) 
            {
                bos.close();
            }
            
            if (fos != null) 
            {
                fos.close();
            }
        }
    }
    
    /**
     * Método que adiciona arquivos dentro do arquivo ZIP
     * 
     * @param zos O {@link ZipOutputStream} do arquivo ZIP
     * @param file O arquivo que se deseja adicionar
     * @param root O Root do arquivo
     * 
     * @return Um {@link List} contendo o endereco de todos os arquivos e pastas presente dentro do arquivo ZIP
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    private List<ZipEntry> addFileInZip(ZipOutputStream zos, File file, String root) throws Exception 
    {
        List<ZipEntry> zipEntrys = new ArrayList<ZipEntry>();
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        byte buffer[] = new byte[TAMANHO_BUFFER];

        try 
        {
            if (file.isDirectory()) 
            {
                zos.putNextEntry(new ZipEntry(createEntryName(root, file).concat("/")));
                
                //recursivamente adiciona os arquivos dos diretórios abaixo
                File[] files = file.listFiles();
                
                for (int i = 0; i < files.length; i++) 
                {
                    List<ZipEntry> entrys = addFileInZip(zos, files[i], root);
                    
                    if (entrys != null) 
                    {
                        zipEntrys.addAll(entrys);
                    }
                }
                
                return (zipEntrys);
            }
            
            ZipEntry entry = new ZipEntry(createEntryName(root, file));
            zos.putNextEntry(entry);
            zos.setMethod(ZipOutputStream.DEFLATED);

            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis, TAMANHO_BUFFER);
            int bytesLidos = 0;
            
            while ((bytesLidos = bis.read(buffer, 0, TAMANHO_BUFFER)) != -1) 
            {
                zos.write(buffer, 0, bytesLidos);
            }
            
            zipEntrys.add(entry);
        } 
        finally 
        {
            if (bis != null) 
            {
                bis.close();
            }
            
            if (fis != null) 
            {
                fis.close();
            }
        }
        
        return (zipEntrys);
    }

    /**
     * Método que cria o endereço relativo de um arquivo a ser criado dentro de um arquivo ZIP
     * 
     * @param root O endereço root do arquivo a ser zipado
     * @param file O arquivo a ser ziado
     * 
     * @return O endereço relativo do arquivo
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    private String createEntryName(String root, File file) throws Exception
    {
        int idx = file.getAbsolutePath().indexOf(root);
        
        if (idx >= 0)
        {
            return(file.getAbsolutePath().substring(idx + root.length()));
        }
        else
        {
            return(root.concat(file.getName()));
        }
    }
}

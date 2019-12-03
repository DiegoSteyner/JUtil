package jutil.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.filechooser.FileSystemView;

import jutil.abstracts.AbstractUtils;
import jutil.data.enums.FileEnum;
import jutil.data.enums.RegexEnum;

/**
 * Classe utilitária para se trabalhar com arquivos
 * 
 * @author Diego Steyner
 */
public final class FileUtils extends AbstractUtils
{
    /**
     * Construtor Privado
     */
    private FileUtils()
    {
        throw EXCEPTION_CONSTRUTOR;
    }

    /**
     * Método que cria um arquivo em um diretório se ele não existir
     * 
     * @param dir O diretório onde o arquivo deve ser criado
     * @param name O nome do arquivo
     * 
     * @return O {@link File} do arquivo criado ou do arquivo já existente no diretório
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static File createFileIfNotExist(String dir, String name) throws Exception
    {
        File temp = new File(dir, name);

        if (!new File(dir).isDirectory())
        {
            throw new Exception("O arquivo " + dir + " não é um diretório.");
        }

        if (!temp.exists())
        {
            if (!temp.createNewFile())
            {
                throw new Exception("Não foi possível criar o arquivo no diretório " + dir);
            }
        }

        return (temp);
    }

    /**
     * Método que cria uma pasta no diretório
     * 
     * @param path O diretório onde a pasta será criada
     * @param name O nome da pasta
     * 
     * @return O {@link File} da pasta criada ou Null caso a pasta não possa ser criada
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static File createFolder(String path, String name) throws Exception
    {
        File file = new File(path, name);
        if (!file.mkdir())
        {
            return (null);
        }

        return (file);
    }

    /**
     * Método que cria a estrutura de pasta do endereço passado
     * 
     * @param path A Estrutura de pasta que se deseja criar
     * 
     * @return O {@link File} criado
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static File createFolderStruture(String path) throws Exception
    {
        File file = new File(path);
        if (!file.mkdirs())
        {
            return (null);
        }

        return (file);
    }

    /**
     * Método que cria um arquivo temporario no diretório temporário do sistema
     * 
     * @param name O nome do arquivo
     * @param extension A extensao do arquivo
     * 
     * @return O {@link File} criado
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static File createTempFile(String name, String extension) throws Exception
    {
        return (File.createTempFile(name, extension));
    }

    /**
     * Método que lê todos os arquivos de um diretório recursivamente
     * 
     * @param caminho O endereço da pasta
     * @param addArquivosEmSubPastas Se True, O método irá ler os diretório recursivamente e adicioná-los na lista
     * @param addSubPastasNaLista Se True, As subpastas que forem encontradas serão adicionadas na lista
     * 
     * @return Um {@link List} com todos os arquivos encontrados
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static List<File> getFilesInFolder(String caminho, boolean addArquivosEmSubPastas, boolean addSubPastasNaLista) throws Exception
    {
        List<File> fileList = new ArrayList<File>();
        File path = new File(caminho);

        if (path.exists())
        {
            if (path.isDirectory())
            {
                String[] children = path.list();

                if (children != null)
                {
                    for (int i = 0; i < children.length; i++)
                    {
                        File childFile = new File(caminho, children[i]);
                        if (childFile.isDirectory())
                        {
                            if (addSubPastasNaLista)
                            {
                                fileList.add(childFile);
                            }

                            if (addArquivosEmSubPastas)
                            {
                                fileList.addAll(getFilesInFolder(new File(caminho, children[i]).getAbsolutePath(), addArquivosEmSubPastas, addSubPastasNaLista));
                            }
                        }
                        else
                        {
                            fileList.add(childFile);
                        }
                    }
                }
            }
        }

        return (fileList);
    }

    /**
     * Método que lê todos os arquivos de um diretório recursivamente testando cada um deles contra uma expressão regular
     * 
     * @param caminho O endereço da pasta
     * @param regex A expressão regular para testar a validade do arquivo
     * @param fileOrDir Se True, A expressão será testada somente em arquivo, do contrário, ela será testada somente em pastas
     * @param addArquivosEmSubPastas Se True, O método irá ler os diretório recursivamente e adicioná-los na lista
     * @param addSubPastasNaLista Se True, As subpastas que forem encontradas serão adicionadas na lista
     * 
     * @return Um {@link List} com todos os arquivos encontrados
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static List<File> getFilesInFolderByExtension(String caminho, String regex, final boolean fileOrDir, boolean addArquivosEmSubPastas, boolean addSubPastasNaLista) throws Exception
    {
        List<File> fileList = new ArrayList<File>();
        File path = new File(caminho);
        final Pattern pattern = Pattern.compile(regex);

        if (path.exists())
        {
            if (path.isDirectory())
            {
                String[] children = path.list(new FilenameFilter()
                {
                    @Override
                    public boolean accept(File dir, String name)
                    {
                        if(fileOrDir && new File(dir, name).isFile())
                        {
                            return (pattern.matcher(name).find());
                        }
                        else if(!fileOrDir && new File(dir, name).isDirectory()) 
                        {
                            return (pattern.matcher(dir.getAbsolutePath()).find());
                        }
                        else
                        {
                            return(Boolean.TRUE);
                        }
                    }
                });

                if (children != null)
                {
                    for (int i = 0; i < children.length; i++)
                    {
                        File childFile = new File(caminho, children[i]);
                        if (childFile.isDirectory())
                        {
                            if (addSubPastasNaLista)
                            {
                                fileList.add(childFile);
                            }

                            if (addArquivosEmSubPastas)
                            {
                                fileList.addAll(getFilesInFolderByExtension(new File(caminho, children[i]).getAbsolutePath(), regex, fileOrDir, addArquivosEmSubPastas, addSubPastasNaLista));
                            }
                        }
                        else
                        {
                            fileList.add(childFile);
                        }
                    }
                }
            }
        }

        return (fileList);
    }

    /**
     * Método que retorna o MymeType de um arquivo
     * 
     * @param file O arquivo que se deseja conseguir o MymeType
     * 
     * @return O nome do MymeType do arquivo
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String getMymeType(String file) throws Exception
    {
        return (new URL("file:////" + new File(file).getAbsolutePath()).openConnection().getContentType());
    }

    /**
     * Método que retorna o Label de uma determinada unidade
     * 
     * @param driverLetter A letra da unidade que se deseja testar o LABEL
     * 
     * @return O nome do disco
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String getDriverLabelName(char driverLetter) throws Exception
    {
        File[] drivers = File.listRoots();

        for (int i = 0; i < drivers.length; i++)
        {
            if (drivers[i].getAbsolutePath().toLowerCase().startsWith(Character.toString(driverLetter).toLowerCase()))
            {
                return (FileSystemView.getFileSystemView().getSystemDisplayName(drivers[i]));
            }
        }

        return (null);
    }

    /**
     * Método que lê um arquivo usando o método de leitura sequencial
     * 
     * @param file O arquivo que se deseja ler
     * 
     * @return Um array de byte do arquivo
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static byte[] getBytesFromFileByReadFile(String file) throws Exception
    {
        InputStream is = new FileInputStream(file);
        byte[] bytes = null;

        try
        {
            long length = new File(file).length();

            if (length > Integer.MAX_VALUE)
            {
                throw new Exception("O Arquivo é muito Grande! O Máximo aceito é " + Integer.MAX_VALUE + " em bytes!");
            }

            bytes = new byte[(int) length];
            int offset = 0;
            int numRead = 0;

            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
            {
                offset += numRead;
            }

            if (offset < bytes.length)
            {
                throw new Exception("Ocorreu um erro ao ler os bytes, nem todos os bytes foram lidos!");
            }
        }
        finally
        {
            is.close();
        }

        return (bytes);
    }

    /**
     * Método que ler um arquivo usando o {@link DataInputStream}
     * 
     * @param file O arquivo que se deseja ler
     * 
     * @return Um array de byte do arquivo
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static byte[] getBytesFromFileByInputStream(String file) throws Exception
    {
        byte[] fileArray = new byte[(int) new File(file).length()];
        DataInputStream inputStream = new DataInputStream(new FileInputStream(file));

        try
        {
            inputStream.readFully(fileArray);
        }
        finally
        {
            inputStream.close();
        }

        return (fileArray);
    }

    /**
     * Método que copia um arquivo usando o modo sequencial
     * 
     * @param srcFile O arquivo de origem com extensão e tudo
     * @param destFile O arquivo de destino com extensão e tudo
     * 
     * @return Se True, O arquivo foi copiado com sucesso
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static boolean copyFileByRead(File srcFile, File destFile) throws Exception
    {
        InputStream in = null;
        OutputStream out = null;

        in = new FileInputStream(srcFile);
        out = new FileOutputStream(destFile);
        byte[] buf = new byte[1024];
        int len;

        try
        {
            while ((len = in.read(buf)) > 0)
            {
                out.write(buf, 0, len);
            }
        }
        finally
        {
            if (in != null)
            {
                in.close();
            }
            if (out != null)
            {
                out.close();
            }
        }

        if (destFile.exists())
        {
            return (destFile.length() == srcFile.length());
        }
        else
        {
            return (Boolean.FALSE);
        }
    }
    
    /**
     * Método que copia um arquivo usando {@link FileChannel}
     * 
     * @param source O arquivo de origem com extensão e tudo
     * @param target O arquivo de destino com extensão e tudo
     * 
     * @return Se True, O arquivo foi copiado com sucesso
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    @SuppressWarnings("resource")
    public static boolean copyFileByFileChannel(File source, File target) throws Exception
    {
        if (target.isDirectory())
        {
            target = new File(target.getAbsolutePath(), source.getName());
        }
        
        FileChannel sourceChan = null;
        FileChannel targetChan = null;

        try
        {
            sourceChan = new FileInputStream(source).getChannel();
            targetChan = new FileOutputStream(target).getChannel();
            sourceChan.transferTo(0, sourceChan.size(), targetChan);
        }
        finally
        {
            if (sourceChan != null && sourceChan.isOpen())
            {
                sourceChan.close();
            }
            if (targetChan != null && targetChan.isOpen())
            {
                targetChan.close();
            }
        }

        if (target.exists())
        {
            return (target.length() == source.length());
        }
        else
        {
            return (Boolean.FALSE);
        }
    }

    /**
     * Método que copia todo o conteúdo de uma pasta para outra pasta
     * 
     * @param source A pasta de origem
     * @param target A pasta de destino, caso ela não exista no outro diretório, a mesma será criada.
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    @SuppressWarnings("resource")
    public static void copyFolder(File source, File target) throws Exception
    {
        if (source.isDirectory())
        {
            if (!target.exists())
            {
                target.mkdirs();
            }

            String files[] = source.list();

            for (String file : files)
            {
                copyFolder(new File(source, file), new File(target, file));
            }
        }
        else
        {
            FileChannel sourceChan = null;
            FileChannel targetChan = null;

            try
            {
                sourceChan = new FileInputStream(source).getChannel();
                targetChan = new FileOutputStream(target).getChannel();
                sourceChan.transferTo(0, sourceChan.size(), targetChan);
            }
            finally
            {
                if (sourceChan != null && sourceChan.isOpen())
                {
                    sourceChan.close();
                }
                if (targetChan != null && targetChan.isOpen())
                {
                    targetChan.close();
                }
            }
            
            if (target.exists())
            {
                if(target.length() != source.length())
                {
                    throw new Exception("O arquivo "+source.getPath()+"Não pode ser corretamente copiado");
                }
            }
        }
    }

    /**
     * Método que retorna a extensão de um arquivo
     * 
     * @param file O arquivo
     * 
     * @return A Extensao do arquivo
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String getExtension(String file) throws Exception
    {
        Matcher mat = Pattern.compile(RegexEnum.FIND_FILE_EXTENSION.getStringValue()).matcher(file);
        
        if(mat.find())
        {
            return(mat.group());
        }
        
        return ("");
    }

    /**
     * Método que ordena uma lista de Files
     * 
     * @param list A Lista de Files
     * 
     * @param tipo O {@link FileEnum} com o tipo de ordenação
     * 
     * @return A Lista ordenada de acordo o modo escolhido
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static List<File> orderListFileBy(List<File> list, FileEnum tipo) throws Exception
    {
        int index = 0;

        for (int i = 0; i < list.size(); i++)
        {
            switch (tipo)
            {
                case ORDER_BY_FOLDER:
                {
                    if (list.get(i).isDirectory())
                    {
                        Collections.swap(list, i, index);
                        index++;
                    }
                    break;
                }
                case ORDER_BY_FILES:
                {
                    if (list.get(i).isFile())
                    {
                        Collections.swap(list, i, index);
                        index++;
                    }
                    break;
                }
                default:
                    break;
            }
        }
        
        return (list);
    }

    /**
     * Método que retorna o nome de um arquivo sem extensão
     * 
     * @param arquivo O arquivo
     * 
     * @return O Nome do arquivo sem extensão
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String getFileNameWithoutExtension(String arquivo) throws Exception
    {
        return (arquivo.replaceAll(RegexEnum.FIND_FILE_EXTENSION.getStringValue(), ""));
    }

    /**
     * Método que renomeia um arquivo
     * 
     * @param file O arquivo a ser renomeado
     * @param newName O novo nome do arquivo
     * 
     * @return Um {@link File} do arquivo com o novo nome, caso não seja possível renomear o arquivo, será retornado null
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static File rename(File file, String newName) throws Exception
    {
        File newfileName = new File(file.getParentFile(), newName);

        if (file.renameTo(newfileName))
        {
            return (new File(newfileName.getPath()));
        }

        return (null);
    }

    /**
     * Método que deleta um diretório
     * 
     * @param dir O diretório a ser deletado
     * 
     * @return Se True, o diretorio foi inteiramente deletado com sucesso
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static boolean deleteDir(File dir) throws Exception
    {
        if (dir.isDirectory())
        {
            String[] files = dir.list();
            for (int i = 0; i < files.length; i++)
            {
                if (!deleteDir(new File(dir, files[i])))
                {
                    return (Boolean.FALSE);
                }
            }
        }

        return (dir.delete());
    }

    /**
     * Método que retorna o tamanho total do disco
     * 
     * @param rootDisk A letra do disco
     * 
     * @return O tamanho total do disco
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static long getSizeDisk(char rootDisk) throws Exception
    {
        return (new File(Character.toString(rootDisk)).getTotalSpace());
    }

    /**
     * Método que retorna o espaço livre de um disco
     * 
     * @param rootDisk A letra do disco
     * 
     * @return O espaço livre do disco
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static long getFreeSizeDisk(char rootDisk) throws Exception
    {
        return (new File(Character.toString(rootDisk)).getFreeSpace());
    }

    /**
     * Método que retorna o espaço usado de um disco
     * 
     * @param disk A letra do disco
     * 
     * @return O espaço usado do disco
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static long getUsedSizeDisk(char disk) throws Exception
    {
        String rootDisk = Character.toString(disk);
        return (new File(rootDisk).getTotalSpace() - new File(rootDisk).getUsableSpace());
    }

    /**
     * Metodo que transforma o tamanho long retornado pelo Java em um tamamho mais útil a leitura humana
     * 
     * @param bytes O long que se deseja transformar
     * @param qtdDigitos A quantidade de digitos depois da virgula
     * 
     * @return Uma String com o tamanho
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String sizeToHumanReadable(Long bytes, int qtdDigitos) throws Exception
    {
        StringBuilder str = new StringBuilder();
        
        if (bytes < FileEnum.UM_BYTE.getLongValue())
        {
            return (str.append(new BigDecimal(((double) bytes / FileEnum.UM_BYTE.getLongValue())).setScale(qtdDigitos, RoundingMode.HALF_UP).doubleValue()).append(FileEnum.SULFIX_BYTE.getStringValue()).toString());
        }
        else
        {
            if (bytes < FileEnum.UM_KILOBYTE.getLongValue())
            {
                if (bytes < 800)
                {
                    return (str.append(new BigDecimal((double) bytes).setScale(qtdDigitos, RoundingMode.HALF_UP).doubleValue()).append(FileEnum.SULFIX_BYTE.getStringValue()).toString());
                }
                
                return (str.append(((double) bytes / FileEnum.UM_BYTE.getLongValue())).append(FileEnum.SULFIX_BYTE.getStringValue()).toString());
            }
            else
            {
                if (bytes < FileEnum.UM_MEGABYTE.getLongValue())
                {
                    return (str.append(new BigDecimal(((double) bytes / FileEnum.UM_KILOBYTE.getLongValue())).setScale(qtdDigitos, RoundingMode.HALF_UP).doubleValue()).append(FileEnum.SULFIX_KILOBYTE.getStringValue()).toString());
                }
                else
                {
                    if (bytes < FileEnum.UM_GIGABYTE.getLongValue())
                    {
                        return (str.append(new BigDecimal(((double) bytes / FileEnum.UM_MEGABYTE.getLongValue())).setScale(qtdDigitos, RoundingMode.HALF_UP).doubleValue()).append(FileEnum.SULFIX_MEGABYTE.getStringValue()).toString());
                    }
                    else
                    {
                        if (bytes < FileEnum.UM_TERABYTE.getLongValue())
                        {
                            return (str.append(new BigDecimal(((double) bytes / FileEnum.UM_GIGABYTE.getLongValue())).setScale(qtdDigitos, RoundingMode.HALF_UP).doubleValue()).append(FileEnum.SULFIX_GIGABYTE.getStringValue()).toString());
                        }
                        else if (bytes < FileEnum.UM_PETABYTE.getLongValue())
                        {
                        	return (str.append(new BigDecimal(((double) bytes / FileEnum.UM_TERABYTE.getLongValue())).setScale(qtdDigitos, RoundingMode.HALF_UP).doubleValue()).append(FileEnum.SULFIX_TERABYTE.getStringValue()).toString());
                        }
                    }
                }
            }
        }
        
        throw new Exception("Tamanho do arquivo não suportado");
    }

    /**
     * Método que retorna a data de modificação de um arquivo
     * 
     * @param file O arquivo
     * @param formato O formato de retorno da data
     * 
     * @return A data de modificação no formato especificado
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String getModifyDateFile(String file, String formato) throws Exception
    {
        return ((new java.text.SimpleDateFormat(formato)).format(new File(file).lastModified()));
    }

    /**
     * Método que altera a data de modificação de um arquivo
     * 
     * @param file O arquivo
     * @param data A nova data de modificação
     * 
     * @return Se True, A data de modificação foi alterada com suscesso
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static boolean setDataModificacaoDoArquivo(String file, Date data) throws Exception
    {
        return (new File(file).setLastModified(data.getTime()));
    }

    /**
     * Método que retorna um {@link DataInputStream} de um arquivo
     * 
     * @param file O arquivo
     * 
     * @return O {@link DataInputStream} do arquivo
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static DataInputStream getDataInputStream(String file) throws Exception
    {
        byte[] fileArray = new byte[(int) new File(file).length()];
        DataInputStream inputStream = new DataInputStream(new FileInputStream(file));
        inputStream.readFully(fileArray);
        
        return (inputStream);
    }

    /**
     * Método que testa se dois arquivos são iguais
     * 
     * @param arquivo1 O Primeiro arquivo
     * @param arquivo2 O Segundo arquivo
     * 
     * @return Se True, Os dois arquivos são iguais
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public boolean equals(String arquivo1, String arquivo2) throws Exception
    {
        FileInputStream stream1 = new FileInputStream(new File(arquivo1));
        FileInputStream stream2 = new FileInputStream(new File(arquivo2));
        DataInputStream in1 = new DataInputStream(stream1);
        DataInputStream in2 = new DataInputStream(stream2);

        try
        {
            return (equals(in1, in2));
        }
        finally
        {
            stream1.close();
            stream2.close();
            in1.close();
            in2.close();
        }
    }

    /**
     * Método que testa se dois {@link InputStream} são iguais
     * 
     * @param input1 O Primeiro Input
     * @param input2 O Segundo Input
     * 
     * @return Se True, Os dois {@link InputStream} são iguais
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public boolean equals(InputStream input1, InputStream input2) throws Exception
    {
        byte[] buffer1 = new byte[1024 * 1024];
        byte[] buffer2 = new byte[1024 * 1024];

        int numRead1 = 0;
        int numRead2 = 0;

        while (true)
        {
            numRead1 = input1.read(buffer1);
            numRead2 = input2.read(buffer2);
            
            if (numRead1 > -1)
            {
                if (numRead2 != numRead1)
                {
                    return (Boolean.FALSE);
                }
                if (!Arrays.equals(buffer1, buffer2))
                {
                    return (Boolean.TRUE);
                }
            }
            else
            {
                return (numRead2 < 0);
            }
        }
    }

    /**
     * Metodo que retorna um {@link ByteArrayOutputStream} de um arquivo
     * 
     * @param file O arquivo que se deseja conseguir o {@link ByteArrayOutputStream}
     * 
     * @return O {@link ByteArrayOutputStream} do arquivo
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static ByteArrayOutputStream getByteArrayOutputStream(String file) throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream in = new FileInputStream(file);

        try
        {
            byte[] buffer = new byte[4096];
            int len;

            while ((len = in.read(buffer)) >= 0)
            {
                baos.write(buffer, 0, len);
            }
        }
        finally
        {
            in.close();
        }
        
        baos.flush();

        return (baos);
    }

    /**
     * Método que escreve um {@link ByteArrayOutputStream} para o disco
     * 
     * @param bais O {@link ByteArrayOutputStream} que se deseja escrever
     * @param dirToSave O diretório onde o {@link ByteArrayOutputStream} deve ser salvo
     * @param objetName O nome que o arquivo deve ter
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static void writeInFileSystem(ByteArrayInputStream bais, String dirToSave, String objetName) throws Exception
    {
        InputStream in = (InputStream) bais;
        FileOutputStream out = new FileOutputStream(new File(new File(dirToSave), objetName));

        try
        {
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, bytesRead);
            }
        }
        finally
        {
            in.close();
            out.close();
        }
    }

    /**
     * Metodo que escreve um array de bytes no disco
     * 
     * @param b O Array de bytes que se deseja escrever
     * @param path O local onde ele deve ser escrito
     * @param name O nome do arquivo
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public void writeInFileSystem(byte b[], String path, String name) throws Exception
    {
        FileOutputStream fos = new FileOutputStream(new File(path, name));
        
        try
        {
            fos.write(b);
        }
        finally
        {
            fos.close();
        }
    }

    /**
     * Método que transforma um array de bytes de um CheckSum em String
     * @param b O array de bytes do CheckSum
     * 
     * @return A String do array
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String checkSumToString(byte[] b) throws Exception
    {
        StringBuilder retorno = new StringBuilder();
        
        for (int i = 0; i < b.length; i++)
        {
            retorno.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        return retorno.toString();
    }
    
    /**
     * Método que testa de um arquivo de checkSum corresponde ao CheckSum do arquivo para o qual ele foi gerado
     * 
     * @param actualChecksumFile O CheckSum atual do arquivo
     * @param checksumFile O arquivo de CheckSum gerado para o arquivo
     * 
     * @return Se True, O CheckSum atual corresponde ao CheckSum gerado
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static boolean verifyChecksumByFile(byte[] actualChecksumFile, String checksumFile) throws Exception
    {
        InputStream is = null;
        try
        {
            byte[] chk2 = new byte[actualChecksumFile.length];
            is = new FileInputStream(checksumFile);
            is.read(chk2);

            return(new String(chk2).equals(new String(actualChecksumFile)));
        }
        finally
        {
            is.close();
        }

    }

    /**
     * Método que cria um CheckSum para um arquivo
     * 
     * @param file O arquivo que se deseja criar o CheckSum
     * @param mode O algoritmo de geração a ser usado aceito pela classe {@link MessageDigest}
     * 
     * @return O array de bytes do checkSum do arquivo
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static byte[] createChecksum(File file, String mode) throws Exception
    {
        InputStream fis = new FileInputStream(file);
        MessageDigest digest = null;
        
        try
        {
            byte[] buffer = new byte[1024];
            digest = MessageDigest.getInstance(mode);
            int numRead;
            
            do
            {
                numRead = fis.read(buffer);
                if (numRead > 0)
                {
                    digest.update(buffer, 0, numRead);
                }
            }
            while (numRead != -1);
        }
        finally
        {
            fis.close();
        }
        
        return digest.digest();
    }
    
    /**
     * Método que testa se um arquivo está em Lock por algum outro programa através da abertura de escrita assincrona.
     * 
     * @param file O arquivo que se deseja testar
     * 
     * @return Se True, O arquivo está travado por algum outro programa
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static boolean isLocked(File file) throws Exception
    {
        RandomAccessFile rdn = new RandomAccessFile(file, "rw");
        FileChannel channel = rdn.getChannel();
        
        try
        {
            if (channel != null)
            {
                rdn.close();
                channel.close();
                return (Boolean.FALSE);
            }
            else
            {
                rdn.close();
                return (Boolean.TRUE);
            }
        }
        catch (Exception ex)
        {
            if (channel != null)
            {
                channel.close();
            }
            
            rdn.close();
            return (Boolean.TRUE);
        }
    }
}

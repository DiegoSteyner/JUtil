package jutil.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.Arrays;

import jutil.abstracts.AbstractUtils;

/**
 * Classe utilitaria para interagir tanto com o SO atual quanto com a JVM
 * 
 * @author Diego Steyner
 */
public class SystemUtils extends AbstractUtils
{
    public static final int    LEFT_CLICK                   = InputEvent.BUTTON1_MASK;
    public static final int    RIGHT_CLICK                  = InputEvent.BUTTON3_MASK;
    public static final int    SCROLL_CLICK                 = InputEvent.BUTTON2_MASK;

    public static final String PROGRAMFILES                 = "programfiles";
    public static final String VM_PROPERTIES_USER_DIR       = "user.dir";
    public static final String VM_PROPERTIES_TEMP_DIR       = "java.io.tmpdir";
    public static final String VM_PROPERTIES_USER_NAME      = "user.name";
    public static final String VM_PROPERTIES_USER_HOME_DIR  = "user.home";
    public static final String VM_PROPERTIES_SO_NAME        = "os.name";
    public static final String VM_PROPERTIES_OS_VERSION     = "os.version";
    public static final String VM_PROPERTIES_OS_ARQUITETURA = "os.arch";
    public static final String VM_PROPERTIES_LINE_SEPARATOR = "line.separator";
    public static final String VM_PROPERTIES_FILE_SEPARATOR = "file.separator";
    public static final String VM_PROPERTIES_JAVA_VERSION   = "java.version";
    
    /**
     * Construtor privado
     */
    private SystemUtils()
    {
        throw EXCEPTION_CONSTRUTOR;
    }

    /**
     * Método que retorna o local de execução do programa
     * 
     * @return O endereço onde o programa foi executado
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static String getDirExecution() throws Exception
    {
        return (System.getProperty(VM_PROPERTIES_USER_DIR));
    }

    /**
     * Método que retorna o diretório onde uma determinada classe foi executada.
     * 
     * @param clazz A classe que se deseja saber onde foi executada
     * 
     * @return O local onde a classe foi executado ou <code>null</code> caso não seja possível recuperar o local
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static String getDirExecutionClass(Class<?> clazz) throws Exception
    {
        String caminho = clazz.getName().replace('.', '/').concat(".class");
        URL url = SystemUtils.class.getClassLoader().getResource(caminho);
        
        String local = url.getFile().replace(caminho, "").replace("%20", " ");
        return(local.substring(local.indexOf("/") + 1, local.length() - 1));
    }

    
    /**
     * Método que imprime todas as propriedades atualmente configuradas para a JVM
     * 
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static void printJVMProperties() throws Exception
    {
        System.getProperties().list(out);
    }
    
    /**
     * Método que muda o valor de uma propriedade da JVM
     * 
     * @param propertie O nome da propriedade que se deseja mudar o valor
     * @param value O novo valor da propriedade
     * 
     * @return Se True, o valor da propriedade foi alterado com sucesso
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static boolean setJVMPropertie(String propertie, String value) throws Exception
    {
        if (System.getProperties().containsKey(propertie))
        {
            System.setProperty(propertie, value);
            return (Boolean.TRUE);
        }

        return(Boolean.FALSE);
    }
    
    /**
     * Método que adiciona uma propriedade a JVM
     * 
     * @param propertie O nome da propriedade que se deseja adicionar
     * @param value O valor da propriedade
     * 
     * @return Se True, a propriedade foi adicionada com sucesso
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static boolean addVmPropertie(String propertie, String value) throws Exception
    {
        if (!System.getProperties().containsKey(propertie))
        {
            System.setProperty(propertie, value);
            return (Boolean.TRUE);
        }

        return(Boolean.FALSE);
    }
    
    /**
     * Método que pega o diretorio de instalacao padrao dos programas Ex(C:\Arquivos de Programas)
     *
     * @return retorna uma string com o endereco do diretorio de instalacao padrao dos programas
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static String getWindowsDirProgramFile() throws Exception
    {
        return (System.getenv(PROGRAMFILES));
    }
    
    /**
     * Método que retorna o estado do Capslook
     *
     * @return Se True, Está ligada
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static boolean getCapsLookStatus() throws Exception
    {
        return (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK));
    }

    /**
     * Método que retorna o estado da tecla Num Lock
     *
     * @return Se True, Está ligada 
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static boolean getNumLookStatus() throws Exception
    {
        return (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK));
    }

    /**
     * Método que configura um novo estado para a tecla Capslook
     *
     * @param state Se True, irá ligar a tecla 
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static void setEstadoCapslook(boolean state) throws Exception
    {
        Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_CAPS_LOCK, state);
    }

    /**
     * Método que configura o estado da tecla Numlook
     *
     * @param state Se True, irá ligar a tecla 
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static void setEstadoNumlook(boolean state) throws Exception
    {
        Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, state);
    }

    /**
     * Método que retorna o nome de todas as fontes disponíveis no sistema
     *
     * @return O nome das fontes
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static String[] getAvailableFonts() throws Exception
    {
        return (GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
    }

    /**
     * Método que retorna o nome de todas as unidades instaladas no computador Ex(C:, D:, Etc)
     *
     * @return Retorna um vetor com as letras de todas as unidades instaladas no computador
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static String[] getLetrasUnidades() throws Exception
    {
        File discos[] = File.listRoots();
        String unidades[] = new String[discos.length];

        for (int i = 0; i < discos.length; i++) 
        {
            unidades[i] = discos[i].toString();
        }

        discos = null;
        return (unidades);
    }
    
    /**
     * Método que retorna o texto que for encontrado na área de transferência do sistema operacional
     *
     * @return Uma string contendo o que foi encontrado na area de transferencia do sistema operacional</br></br> 
     * Caso algum arquivo seja encontrado na area de transferencia, sera retornada uma String contendo o endereco entre colchetes Ex([C:\endereco\arquivo.txt])</br></br>
     * Se for encontrado um tipo diferente de String na área de transferência ou não houver nada, será retornado <code>null</code>
     * 
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static String getStringFromClipboard() throws Exception
    {
        Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

        if ((contents != null) & contents.isDataFlavorSupported(DataFlavor.stringFlavor))
        {
            return(contents.getTransferData(DataFlavor.stringFlavor).toString());
        }
        if ((contents != null) & contents.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
        {
            return Arrays.asList(contents.getTransferData(DataFlavor.javaFileListFlavor)).toString();
        }
        else
        {
            return(null);
        }
    }

    /**
     * Método que coloca uma String na área de transferência do sistema Operacional
     *
     * @param str A string que se deseja que seja mandada para a área de transferência do sistema
     *
     * @return Se True, A String foi inserida com sucesso
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static boolean setStringInClipboard(String str) throws Exception
    {
        if(StringUtils.isNotNullOrEmpty(Boolean.FALSE, str))
        {
            StringSelection stringSelection = new StringSelection(str);
            ClipboardOwner text = stringSelection;
            
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, text);
            
            return (Boolean.TRUE);
        }
        else
        {
            return (Boolean.FALSE);
        }
    }
    
    /**
     * Metodo que pega a resolução aplicada ao monitor
     *
     * @return Um vetor sendo: [0] = width; [1] = height
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static int[] getResolucaoMonitor() throws Exception
    {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        return(new int[]{dim.width, dim.height});
    }
    
    /**
     * Método que move o mouse para uma determinada posicao na tela
     *
     * @param x A posicao x que se deseja mover o mouse
     * @param y A posicao y que se deseja mover o mouse
     * @param click Se True, Será chamado o click do mouse quando ele chegar na posição indicada
     * @param clickType Pode ser {@link #RIGHT_CLICK}, {@link #LEFT_CLICK} ou {@link #SCROLL_CLICK}
     *
     * @return Se True, Todas as operações foram concluídas
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static boolean moveMouse(int x, int y, boolean click, int clickType) throws Exception
    {
        Robot robot = new Robot();
        robot.mouseMove(x, y);

        if (click)
        {
            robot.mousePress(clickType);
            robot.mouseRelease(clickType);
        }

        return (Boolean.TRUE);
    }
    
    /**
     * Método que move a rodinha do mouse
     *
     * @param upDown Se True, Move a rodinha para baixo
     * @param qtd A quantidade de "voltas" que a rodinha deve ser movida
     *
     * @return Se True, A rodinha foi movida com suscesso
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static boolean moveScrollMouse(boolean upDown, int qtd) throws Exception
    {
        Robot robot = new Robot();

        if (upDown)
        {
            robot.mouseWheel(qtd);
        }
        else
        {
            robot.mouseWheel((qtd = -qtd));
        }

        return (Boolean.TRUE);
    }
    
    /**
     * Método que aciona a tecla
     *
     * @param key O valor da tecla baseada nos valores de {@link KeyEvent} (Ex: java.awt.event.KeyEvent.VK_A)
     * @param times A quantidade de vezes que ela deve ser  pressionada
     * 
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static void clickKey(int key, int times) throws Exception
    {
        Robot robot = new Robot();

        for(int i = 0; i < times; i++)
        {
            robot.keyPress(key);
            robot.keyRelease(key);
        }
    }
    
    /**
     * Método que retorna a cor presente em uma determinada posicao da tela
     *
     * @param x A posicao x da tela
     * @param y A posicao y da tela
     *
     * @return A cor presente naquela posicao
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static Color getPositionolor(int x, int y) throws Exception
    {
        return((new Robot()).getPixelColor(x, y));
    }

    /**
     * Método que faz o sistema emitir um bip </br></br> 
     * Em alguns sistemas mais antigos, é necessário que o speaker esteja instalado e configurado na placa mãe do computador
     * 
     * @throws Exception Caso algum erro ocorra uma excessão será lançada
     */
    public static void bip() throws Exception
    {
        Toolkit.getDefaultToolkit().beep();
    }
}

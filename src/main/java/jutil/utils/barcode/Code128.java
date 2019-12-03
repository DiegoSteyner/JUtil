package jutil.utils.barcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import jutil.abstracts.AbstractUtils;
import jutil.utils.MathUtils;

/**
 * Classe utilitária para trabalhar com o código de barra Code128B
 * 
 * @author Diego Steyner
 */
public final class Code128 extends AbstractUtils
{
    private String                        data                  = "";
    private Hashtable<Character, Integer> barcodeTable     = new Hashtable<Character, Integer>();
    private BufferedImage                 barcode               = null;
    
    public static final int               ALTURA_LINHA_PADRAO   = 10;
    public static final int               LARGURA_IMAGEM_PADRAO = 500;
    public static final int               ESPACO_INICIAL_PADRAO = 50;
    public static final int               ESPACO_FINAL_PADRAO   = 100;
    public static final Color             COR_BARRA_PADRAO      = Color.BLACK;
    public static final Color             COR_FUNDO_PADAO       = Color.WHITE;
    
    /**
     * Construtor parametrizado
     * 
     * @param data A tabela de códigos do Code128B
     */
    public Code128(String data)
    {
        this.data = data;
        loadBarcodeData();
    }
    
    /**
     * Método que gera o objeto {@link BufferedImage} com um código de barras
     * 
     * @param initialSpace O espaco inicial na imagem
     * @param finalSpace O espaco final na imagem
     * @param barColor O objeto {@link Color} com a cor das barras
     * @param backgroundColor O objeto {@link Color} com a cor do fundo
     * @param hightBars A altura das barras
     * @param widthImage A largura da imagem
     * 
     * @return O objeto {@link BufferedImage} com o código de barras gerado
     */
    public BufferedImage generateBarcode(int initialSpace, int finalSpace, Color barColor, Color backgroundColor, int hightBars, int widthImage) throws Exception
    {
        final String completData = (char) 136 + data + getChecksum() + (char) 138;
        final int width = (hightBars * (20 + 2 + completData.length() * 11));
        final MathUtils util = new MathUtils();

        barcode = new BufferedImage(width, widthImage, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = barcode.createGraphics();
        g2d.setColor(Color.black);

        int xPos = 10 * hightBars;

        for (int i = 0; i < completData.length(); i++)
        {
            // Calculando o tamanho das barras
            int widthBar = barcodeTable.get(completData.charAt(i));
            
            // Iterando sobre os dígitos
            for (int j = 0; j < util.numberOfDigits(widthBar); j++)
            {
                // Desenhando as barras
                g2d.fill(new Rectangle2D.Double(xPos, 0, util.getDigitInNumber(widthBar, j) * hightBars, widthImage));

                // Movendo o eixo X do gráfico
                xPos += util.getDigitInNumber(widthBar, j) * hightBars;

                // Alternando entre as cores
                if (g2d.getColor() == Color.BLACK)
                {
                    g2d.setColor(Color.WHITE);
                }
                else
                {
                    g2d.setColor(Color.BLACK);
                }
            }
        }
        
        // Redesenhando as barras nas cores escolhidas
        for (int x = 0; x < barcode.getWidth(); x++)
        {
            for (int y = 0; y < barcode.getHeight(); y++)
            {
                int rgba = barcode.getRGB(x, y);

                if (rgba == 0 || rgba == -1)
                {
                	barcode.setRGB(x, y, backgroundColor.getRGB());
                }
                else if (rgba == -16777216)
                {
                	barcode.setRGB(x, y, barColor.getRGB());
                }
            }
        }

        barcode.flush();
    
        return(barcode);
    }

    /**
     * Método que calcula o checksum dos dados a serem códificados no código de barras
     * 
     * @return O Checksum dos dados
     */
    private char getChecksum()
    {
        int result = 104;
        for (int i = 0; i < data.length(); i++)
        {
            result += ((int) data.charAt(i) - 32) * (i + 1);
        }
        
        return (char) (result % 103 + 32);
    }
    
    /**
     * Códigos da especificação Code 128B.
     */
    private void loadBarcodeData()
    {
        // Limpando a tabela
        barcodeTable.clear();

        // Caracteres normais.
        barcodeTable.put(' ', 212222);
        barcodeTable.put('!', 222122);
        barcodeTable.put('"', 222221);
        barcodeTable.put('#', 121223);
        barcodeTable.put('$', 121322);
        barcodeTable.put('%', 131222);
        barcodeTable.put('&', 122213);
        barcodeTable.put('\'', 122312);
        barcodeTable.put('(', 132212);
        barcodeTable.put(')', 221213);
        barcodeTable.put('*', 221312);
        barcodeTable.put('+', 231212);
        barcodeTable.put(',', 112232);
        barcodeTable.put('-', 122132);
        barcodeTable.put('.', 122231);
        barcodeTable.put('/', 113222);
        barcodeTable.put('0', 123122);
        barcodeTable.put('1', 123221);
        barcodeTable.put('2', 223211);
        barcodeTable.put('3', 221132);
        barcodeTable.put('4', 221231);
        barcodeTable.put('5', 213212);
        barcodeTable.put('6', 223112);
        barcodeTable.put('7', 312131);
        barcodeTable.put('8', 311222);
        barcodeTable.put('9', 321122);
        barcodeTable.put(':', 321221);
        barcodeTable.put(';', 312212);
        barcodeTable.put('<', 322112);
        barcodeTable.put('=', 322211);
        barcodeTable.put('>', 212123);
        barcodeTable.put('?', 212321);
        barcodeTable.put('@', 232121);
        barcodeTable.put('A', 111323);
        barcodeTable.put('B', 131123);
        barcodeTable.put('C', 131321);
        barcodeTable.put('D', 112313);
        barcodeTable.put('E', 132113);
        barcodeTable.put('F', 132311);
        barcodeTable.put('G', 211313);
        barcodeTable.put('H', 231113);
        barcodeTable.put('I', 231311);
        barcodeTable.put('J', 112133);
        barcodeTable.put('K', 112331);
        barcodeTable.put('L', 132131);
        barcodeTable.put('M', 113123);
        barcodeTable.put('N', 113321);
        barcodeTable.put('O', 133121);
        barcodeTable.put('P', 313121);
        barcodeTable.put('Q', 211331);
        barcodeTable.put('R', 231131);
        barcodeTable.put('S', 213113);
        barcodeTable.put('T', 213311);
        barcodeTable.put('U', 213131);
        barcodeTable.put('V', 311123);
        barcodeTable.put('W', 311321);
        barcodeTable.put('X', 331121);
        barcodeTable.put('Y', 312113);
        barcodeTable.put('Z', 312311);
        barcodeTable.put('[', 332111);
        barcodeTable.put('\\', 314111);
        barcodeTable.put(']', 221411);
        barcodeTable.put('^', 431111);
        barcodeTable.put('_', 111224);
        barcodeTable.put('`', 111422);
        barcodeTable.put('a', 121124);
        barcodeTable.put('b', 121421);
        barcodeTable.put('c', 141122);
        barcodeTable.put('d', 141221);
        barcodeTable.put('e', 112214);
        barcodeTable.put('f', 112412);
        barcodeTable.put('g', 122114);
        barcodeTable.put('h', 122411);
        barcodeTable.put('i', 142112);
        barcodeTable.put('j', 142211);
        barcodeTable.put('k', 241211);
        barcodeTable.put('l', 221114);
        barcodeTable.put('m', 413111);
        barcodeTable.put('n', 241112);
        barcodeTable.put('o', 134111);
        barcodeTable.put('p', 111242);
        barcodeTable.put('q', 121142);
        barcodeTable.put('r', 121241);
        barcodeTable.put('s', 114212);
        barcodeTable.put('t', 124112);
        barcodeTable.put('u', 124211);
        barcodeTable.put('v', 411212);
        barcodeTable.put('w', 421112);
        barcodeTable.put('x', 421211);
        barcodeTable.put('y', 212141);
        barcodeTable.put('z', 214121);
        barcodeTable.put('{', 412121);
        barcodeTable.put('|', 111143);
        barcodeTable.put('}', 111341);
        barcodeTable.put('~', 131141);

        // Caracteres especiais
        barcodeTable.put((char) 127, 114113);
        barcodeTable.put((char) 128, 114311);
        barcodeTable.put((char) 129, 411113);
        barcodeTable.put((char) 130, 411311);
        barcodeTable.put((char) 131, 113141);
        barcodeTable.put((char) 132, 114131);
        barcodeTable.put((char) 133, 311141);
        barcodeTable.put((char) 134, 411131);
        barcodeTable.put((char) 135, 211412);
        barcodeTable.put((char) 136, 211214);
        barcodeTable.put((char) 137, 211232);

        // Caracter de inicio
        barcodeTable.put((char) 136, 211214);

        // Caracter de parada
        barcodeTable.put((char) 138, 2331112);
    }

    /**
     * Método que retorna o valor da variável data
     * 
     * @return O valor presente na variável data
     */
    public String getData()
    {
        return data;
    }

    /**
     * Método que altera o valor da variável data
     * 
     * @param data O novo valor da variável data
     */
    public void setData(String data)
    {
        this.data = data;
    }

    /**
     * Método que retorna o valor da variável barcodeTable
     * 
     * @return O valor presente na variável barcodeTable
     */
    public Hashtable<Character, Integer> getBarcodeTable()
    {
        return barcodeTable;
    }

    /**
     * Método que altera o valor da variável barcodeTable
     * 
     * @param barcodeTable O novo valor da variável barcodeTable
     */
    public void setBarcodeTable(Hashtable<Character, Integer> barcodeTable)
    {
        this.barcodeTable = barcodeTable;
    }

    /**
     * Método que retorna o valor da variável barcode
     * 
     * @return O valor presente na variável barcode
     */
    public BufferedImage getBarcode()
    {
        return barcode;
    }

    /**
     * Método que altera o valor da variável barcode
     * 
     * @param barcode O novo valor da variável barcode
     */
    public void setBarcode(BufferedImage barcode)
    {
        this.barcode = barcode;
    }
}
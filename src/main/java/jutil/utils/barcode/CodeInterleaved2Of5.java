package jutil.utils.barcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import jutil.abstracts.AbstractUtils;

/**
 * Classe utilitária para trabalhar com o código de barra Interleaved2Of5
 * 
 * @author Diego Steyner
 */
public final class CodeInterleaved2Of5 extends AbstractUtils
{
    public static final int    LARGURA_IMG_PADRAO     = 200;
    public static final int    ALTURA_IMG_PADRAO      = 150;
    public static final Color  COR_FUNDO_IMG_PADRAO   = Color.WHITE;
    public static final Color  COR_BARRAS_IMG_PADRAO  = Color.BLACK;
    public static final int    MARGEM_DIREITA_PADRAO  = 5;
    public static final int    MARGEM_ESQUERDA_PADRAO = 40;
    public static final int    MARGEM_INFERIOR_PADRAO = 5;
    public static final int    MARGEM_SUPERIO_PADRAO  = 5;

    public static final int    LARGURA_N              = 1;
    public static final int    LARGURA_W              = 3;

    public static final char[] PATTERN_INITIAL        = new char[] { 'n', 'n', 'n', 'n' };
    public static final char[] PATTERN_FINAL          = new char[] { 'w', 'n', 'n' };
    
    private int                 tamanhoImagem          = 0;
    private String              data                   = "";
    
    private static char[][]     pattern       = new char[][] { 
                                                                { 'n', 'n', 'w', 'w', 'n' },
                                                                { 'w', 'n', 'n', 'n', 'w' },
                                                                { 'n', 'w', 'n', 'n', 'w' },
                                                                { 'w', 'w', 'n', 'n', 'n' },
                                                                { 'n', 'n', 'w', 'n', 'w' },
                                                                { 'w', 'n', 'w', 'n', 'n' },
                                                                { 'n', 'w', 'w', 'n', 'n' },
                                                                { 'n', 'n', 'n', 'w', 'w' },
                                                                { 'w', 'n', 'n', 'w', 'n' },
                                                                { 'n', 'w', 'n', 'w', 'n' },
                                                             };
    /**
     * Construtor parametrizado
     * 
     * @param data O dados que se deseja codificar no código de barras
     */
    public CodeInterleaved2Of5(String data)
    {
        this.data = data;
        validateData();
    }
    
    /**
     * Método que testa se os dados passados são válidos
     */
    protected void validateData()
    {
        if (data == null)
        {
            throw new IllegalArgumentException("Valor do código é null");
        }
        if (data.replaceAll("\\D", "").length() % 2 != 0)
        {
            throw new IllegalArgumentException("O tamanho do código deve consistir de tamanho par e somente números.");
        }
    }
    
    /**
     * Método que cria e desenha o objeto {@link Graphics2D}
     * 
     * @param widthImage A largura da imagem final
     * @param heightImage A altura da imagem final
     * @param backgroundColor A cor do fundo da imagem final
     * @param barColor A cor das barras da imagem final
     * @param paddingRight O tamanho da margem direita da imagem
     * @param paddingLeft O tamanho da margem esquerda da imagem
     * @param paddingBottom O tamanho da margem inferior da imagem
     * @param paddingTop O tamanho da margem superior da imagem
     * 
     * @return O objeto {@link Graphics2D} com o código de barras desenhado
     * @throws Exception Se algum erro ocorrer, uma exceção será lançada
     */
    public BufferedImage generateBarCode(int widthImage, int heightImage, Color backgroundColor, Color barColor, int paddingRight, int paddingLeft, int paddingBottom, int paddingTop) throws Exception
    {
        BufferedImage bufferedImage = new BufferedImage(widthImage, heightImage, java.awt.image.BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();

        graphics.setPaint(backgroundColor);
        graphics.fillRect(0, 0, widthImage, heightImage);

        graphics.setColor(barColor);
        
        int posicaoFinal = renderize(graphics, widthImage, heightImage, backgroundColor, barColor, paddingRight, paddingLeft, paddingBottom, paddingTop);

        graphics.setPaint(backgroundColor);
        graphics.fillRect(posicaoFinal, 0, paddingRight, heightImage);

        tamanhoImagem = (widthImage + (posicaoFinal - widthImage));

        if (posicaoFinal > widthImage)
        {
            throw new Exception("As barras ultrapassam o tamanho da figura em " + (posicaoFinal - widthImage));
        }

        return(bufferedImage);
    }
    
    /**
     * Método que desenha o objeto {@link Graphics2D}
     * 
     * @param graphics O Objeto {@link Graphics2D} que será desenhado
     * @param widthImage A largura da imagem final
     * @param heightImage A altura da imagem final
     * @param backgroundColor A cor do fundo da imagem final
     * @param barColor A cor das barras da imagem final
     * @param paddingRight O tamanho da margem direita da imagem
     * @param paddingLeft O tamanho da margem esquerda da imagem
     * @param paddingBottom O tamanho da margem inferior da imagem
     * @param paddingTop O tamanho da margem superior da imagem
     * 
     * @return A última posição do padrão
     * @throws Exception Se algum erro ocorrer, uma exceção será lançada
     */
    private int renderize(Graphics2D graphics, int widthImage, int heightImage, Color backgroundColor, Color barColor, int paddingRight, int paddingLeft, int paddingBottom, int paddingTop) throws Exception
    {
        int index = paddingLeft;
        
        graphics.setPaint(backgroundColor);
        graphics.fillRect(0, 0, widthImage, heightImage);
        graphics.setColor(barColor);
        
        int heightBars = heightImage - (paddingTop + paddingBottom);
        String model = parseData();
        boolean swap = true;

        for (int i = 0; i < model.length(); i++)
        {
            index = writeBars(model.charAt(i), graphics, index, heightBars, swap, paddingTop);
            swap = !swap;
        }
        
        graphics.setPaint(backgroundColor);
        graphics.fillRect(index, 0, paddingRight, heightImage);

        return (index);
    }

    /**
     * Método que desenha as barras no objeto {@link Graphics2D}
     * 
     * @param str Qual é a String do padrão a ser desenhada 
     * @param graphics O Objeto {@link Graphics2D} onde as barras serão desenhadas
     * @param index A próxima posição no padrão
     * @param heightBars A altura das barras
     * @param fill Se True, A barra será desenhada no objeto {@link Graphics2D}
     * @param paddingTop O tamanho da margem superior da imagem final
     * 
     * @return A última posição usada no padrão
     * @throws Exception Se algum erro ocorrer, uma exceção será lançada
     */
    private int writeBars(char str, Graphics2D graphics, int index, int heightBars, boolean fill, int paddingTop) throws Exception
    {
        Rectangle dim = new Rectangle();
        dim.setLocation(index, paddingTop);
        
        switch (str)
        {
            case 'n':
            {
                dim.setSize(LARGURA_N, heightBars);
                index += LARGURA_N;
                break;
            }

            case 'w':
            {
                dim.setSize(LARGURA_W, heightBars);
                index += LARGURA_W;
                break;
            }
        }
        
        if (fill)
        {
            graphics.fill(dim);
        }
        
        return (index);
    }
    
    /**
     * Método que faz o parse dos dados a serem transformados em código de barras
     * 
     * @return Uma String equivalente ao parse dos dados
     * @throws Exception Se algum erro ocorrer, uma exceção será lançada
     */
    private String parseData() throws Exception
    {
        StringBuilder sb = new StringBuilder();
        sb.append(PATTERN_INITIAL);

        for (int i = 0; i < data.length(); i += 2)
        {
            sb.append(concat(pattern[Integer.parseInt(data.substring(i, i + 1))], pattern[Integer.parseInt(data.substring(i + 1, i + 2))]));
        }

        return (sb.append(PATTERN_FINAL).toString());
    }

    /**
     * Método que faz o parse dos valores passados para os códigos interleaved
     * 
     * @param firts O primeiro caracter
     * @param second O segundo caracter
     * 
     * @return A String com os valores concatenados
     */
    private StringBuilder concat(char[] firts, char[] second)
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < second.length; i++)
        {
            sb.append(firts[i]).append(second[i]);
        }

        return (sb);
    }

    /**
     * Método que retorna o valor da variável tamanhoImagem
     * 
     * @return O valor presente na variável tamanhoImagem
     */
    public int getTamanhoImagem()
    {
        return tamanhoImagem;
    }

    /**
     * Método que altera o valor da variável tamanhoImagem
     * 
     * @param tamanhoImagem O novo valor da variável tamanhoImagem
     */
    public void setTamanhoImagem(int tamanhoImagem)
    {
        this.tamanhoImagem = tamanhoImagem;
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
     * Método que retorna o valor da variável pattern
     * 
     * @return O valor presente na variável pattern
     */
    public static char[][] getPattern()
    {
        return pattern;
    }

    /**
     * Método que altera o valor da variável pattern
     * 
     * @param pattern O novo valor da variável pattern
     */
    public static void setPattern(char[][] pattern)
    {
        CodeInterleaved2Of5.pattern = pattern;
    }
}

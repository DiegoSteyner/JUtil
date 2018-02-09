package jutil.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.GrayFilter;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import jutil.abstracts.AbstractUtils;
import jutil.implementation.TransferableImp;

/**
 * Classe utilitária para trabalhar com imagens
 * 
 * @author Diego Steyner
 */
@SuppressWarnings("restriction")
public class ImageUtils extends AbstractUtils
{
    public static final double ROTATE_IMAGE_90  = 90.0;
    public static final double ROTATE_IMAGE_180 = 180.0;
    public static final double ROTATE_IMAGE_270 = 270.0;
    public static final double ROTATE_IMAGE_360 = 360.0;
    
    /**
     * Construtor Padrao
     */
    private ImageUtils()
    {
        throw EXCEPTION_CONSTRUTOR;
    }    
    
    /**
     * Método que salva um objeto do tipo Image como imagem em disco
     *
     * @param imagem O objeto do tipo Image que se deseja salvar
     * @param file O endereço completo com extensão e tudo de onde a imagem deve ser salva 
     *
     * @return Se True, A imagem foi salva com sucesso
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static boolean imageToFile(Image imagem, File file) throws Exception
    {
        MediaTracker mediaTracker = new MediaTracker(new Container());
        
        mediaTracker.addImage(imagem, 0);
        mediaTracker.waitForID(0);
        
        int largura = imagem.getWidth(null);
        int altura = imagem.getHeight(null);
        
        // define a  qualidade da imagem
        int quality = 100;                // 100%
        
        BufferedImage imagemRedimensionada = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = imagemRedimensionada.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(imagem, 0, 0, largura, altura, null);
        
        // Salva a nova imagem
        BufferedOutputStream saida = new BufferedOutputStream(new FileOutputStream(file));
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(saida);
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(imagemRedimensionada);
        param.setQuality((float) quality / 100.0f, false);
        encoder.setJPEGEncodeParam(param);
        encoder.encode(imagemRedimensionada);
        saida.close();
        
        return (true);
    }
    
    /**
     * Método que tira um printScreem de toda a tela visível
     *
     * @return Um {@link BufferedImage} com a imagem da tela
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static BufferedImage printScreenTela() throws Exception
    {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        return (new Robot()).createScreenCapture(new Rectangle(0, 0, dim.width, dim.height));
    }

    /**
     * Método que tira um printScreem de toda a tela
     * 
     * @param x A posição 'x' da tela que se deseja capturar
     * @param y A posição 'y' da tela que se deseja capturar
     * @param width O tamanho da imagem capturada
     * @param height A altura da imagem capturada
     *
     * @return Um {@link BufferedImage} com a imagem da tela
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static BufferedImage printScreenTela(int x, int y, int width, int height) throws Exception
    {
        return (new Robot()).createScreenCapture(new Rectangle(x, y, width, height));
    }

    /**
     * Método que retorna o objeto de desenho padrão da JVM em relação ao SO
     * 
     * @return O Objeto de {@link GraphicsConfiguration}
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static GraphicsConfiguration getDefaultConfiguration() throws Exception
    {
        return (GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
    }
    
    /**
     * Método salva uma imagem redimensionada no disco
     *
     * @param imagem A imagem que se deseja redimensionar
     * @param file O endereço completo com extensão e tudo onde a imagem deve ser salva
     * @param with A nova largura da imagem da imagem
     * @param height A nova altura da imagem
     *
     * @return Se True, A imagem foi salva com suscesso
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static boolean imageToFile(Image imagem, File file, int with, int height) throws Exception
    {
        MediaTracker mediaTracker = new MediaTracker(new Container());
        mediaTracker.addImage(imagem, 0);
        mediaTracker.waitForID(0);

        // define a qualidade da imagem
        int quality = 100; // 100%
        
        double thumbRatio = (double) with / (double) height;
        double imageRatio = (double) imagem.getWidth(null) / (double) imagem.getHeight(null);

        if (thumbRatio < imageRatio)
        {
            height = (int) (with / imageRatio);
        }
        else
        {
            with = (int) (height * imageRatio);
        }

        // Desenha a imagem original para o thumbnail e redimensiona para o novo tamanho
        BufferedImage imagemRedimensionada = new BufferedImage(with, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = imagemRedimensionada.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(imagem, 0, 0, with, height, null);

        // Salva a nova imagem
        BufferedOutputStream saida = new BufferedOutputStream(new FileOutputStream(file));
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(saida);
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(imagemRedimensionada);
        param.setQuality((float) quality / 100.0f, false);
        
        encoder.setJPEGEncodeParam(param);
        encoder.encode(imagemRedimensionada);
        saida.close();

        return (Boolean.TRUE);
    }
    
    /**
     * Método que calcula o tamanho baseado em um fator multiplicador.
     *  
     * @param widthOrHeight O Width ou o Height que se deseja obter à partir do fatos
     * @param factor O fator desejado
     * 
     * @return O novo tamanho baseado no fator
     */
    public static int getScaleByFactor(int widthOrHeight, double factor)
    {
    	return((int) (factor * widthOrHeight));
    }
    
    /**
     * Método que recebe um {@link BufferedImage} e cria um novo com um novo tamanho redimensionado.
     *
     * @param image a imagem a ser redimensionada
     * @param width O novo tamanho da imagem
     * @param height A nova altura da imagem
     * @param gc O {@link GraphicsConfiguration} do SO
     *
     * @return O novo {@link BufferedImage} com a imagem redimensionada
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static BufferedImage getScaledInstance(BufferedImage image, int width, int height, GraphicsConfiguration gc) throws Exception
    {
        if (gc == null)
        {
            gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        }
        
        return (gc.createCompatibleImage(width, height, image.getColorModel().getTransparency()));
    }
    
    /**
     * Método que retorna uma imagem com suporte a transparência compatível com a maioria dos componentes de desenho do Java
     * 
     * @param image O {@link BufferedImage}
     * @param gc O {@link GraphicsConfiguration} do SO
     * 
     * @return O {@link BufferedImage} compatível
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static BufferedImage getCompatibleImage(BufferedImage image, GraphicsConfiguration gc) throws Exception
    {
        if (gc == null)
        {
            gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        }
        
        BufferedImage result = gc.createCompatibleImage(image.getWidth(), image.getHeight(), image.getColorModel().getTransparency());
        Graphics2D g2 = result.createGraphics();
        g2.drawRenderedImage(image, null);
        g2.dispose();
        
        return (result);
    }
    
    /**
     * Método que transforma um {@link File} em {@link BufferedImage}
     * 
     * @param file O {@link File} que se deseja transformar
     * 
     * @return O {@link BufferedImage} criado a partir do {@link File}
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static BufferedImage fileToBufferedImage(File file) throws Exception
    {
    	return(ImageIO.read(file));
    }
    
    /**
     * Método que recortar uma parte de uma imagem sem alterar a imagem original
     * 
     * @param file O arquivo de imagem que se deseja recortar
     * @param x A posição X dentro da imagem
     * @param y A posição Y dentro da imagem
     * @param width A largura que o corte deve ter
     * @param height A altura que o corte deve ter
     * 
     * @return Um {@link BufferedImage} com o pedaço equivalente ao corte
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static BufferedImage getCroppedImage(File file, int x, int y, int width, int height) throws Exception
    {
    	return(ImageIO.read(file).getSubimage(x, y, width, height));
    }
    
    /**
     * Método que faz o rotacionamento de uma imagem
     * 
     * @param img O {@link BufferedImage} da imagem que se deseja rotacionar
     * @param angle O angulo de rotacionamento, valores padrões {@link #ROTATE_IMAGE_90}, {@link #ROTATE_IMAGE_180}, {@link #ROTATE_IMAGE_270}, {@link #ROTATE_IMAGE_360}
     * 
     * @return o {@link BufferedImage} com a imagem rotacionada
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static BufferedImage rotate(BufferedImage img, double angle) throws Exception
    {
        double sin = Math.abs(Math.sin(Math.toRadians(angle)));
        double cos = Math.abs(Math.cos(Math.toRadians(angle)));

        int width = img.getWidth(null);
        int height = img.getHeight(null);

        int neww = (int) Math.floor(width*cos + height*sin);
        int newh = (int) Math.floor(height*cos + width*sin);

        BufferedImage bimg = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bimg.createGraphics();

        g.translate((neww-width)/2, (newh-height)/2);
        g.rotate(Math.toRadians(angle), width/2, height/2);
        g.drawRenderedImage(img, null);
        g.dispose();

        return (bimg);
    }
    
    /**
     * Método que inverte um imagem horizontalmente todos os pixels de uma imagem (Flip)
     * 
     * @param file O arquivo de imagem que se quer inverter
     * 
     * @return o {@link BufferedImage} com a imagem invertida
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static BufferedImage flipHorizontal(File file) throws Exception
    {
    	BufferedImage img = ImageIO.read(file);
    	
        int w = img.getWidth(); 
        int h = img.getHeight();
        
        BufferedImage copy = new BufferedImage(w, h, img.getType());
        Graphics2D g = copy.createGraphics();
        g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
        g.dispose();
        
        return (copy);
    }
    
    /**
     * Ao redimensionar uma imagem ela pode perder as proporcoes devido a uma discordancia do ratio original com o novo tamanho da imagem, 
     * fazendo com que a imagem redimensionada fique ruim, esse método calcula o melhor LxA para manter a proporcao do ratio original da imagem
     *
     * @param actualWidth A largura antiga da imagem
     * @param actualHeight A altura antiga da imagem
     * @param larguraNova A largura nova da imagem
     * @param alturaNova A altura nova da imagem
     *
     * @return [0] = Width recomendado; [1] = Height recomendado
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static int[] calcularRatioLxA(int actualWidth, int actualHeight, int larguraNova, int alturaNova) throws Exception
    {
        double thumbRatio = (double) larguraNova / (double) alturaNova;
        double imageRatio = (double) actualWidth / (double) actualHeight;

        if (thumbRatio < imageRatio)
        {
            alturaNova = (int) (larguraNova / imageRatio);
        }
        else
        {
            larguraNova = (int) (alturaNova * imageRatio);
        }

        return (new int[] { larguraNova, alturaNova });
    }

    /**
     * Método que le um arquivo de imagem usando o Default Toolkit da VM
     * 
     * @param file O arquivo de imagem
     * 
     * @return A {@link Image} lida
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static Image fileToImageByNativeMode(String file) throws Exception
    {
        return (Toolkit.getDefaultToolkit().getImage(file));
    }
    
    /**
     * Método que escreve um objeto {@link BufferedImage} no disco
     * 
     * @param file O endereço completo com extensão e tudo onde o arquivo será gravado
     * @param buffer O {@link BufferedImage} com a imagem
     * @param extension A extensão do arquivo
     * 
     * @return Se True, A imagem foi salva com sucesso.
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static boolean bufferedImageToFile(BufferedImage buffer, File file, String extension) throws Exception
    {
        return(ImageIO.write(buffer, extension, file));
    }
    
    /**
     * Método que transforma um {@link BufferedImage} em {@link Image}
     * 
     * @param image O {@link Image} da imagem que se deseja converter 
     * @param extension A extensão (tipo) da imagem presente no {@link BufferedImage}
     * 
     * @return O {@link Image} criado à partir do {@link BufferedImage}
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static Image bufferedImageToImage(BufferedImage image, String extension) throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(32768);
        ImageOutputStream ios = null;
        
        try
        {
            // Recuperando a image,
            Iterator<javax.imageio.ImageWriter> writers = ImageIO.getImageWritersBySuffix(extension);
            
            if (!writers.hasNext())
            {
                throw new IllegalStateException("Não foi encontrado nenhum escritor para a extensão informada");
            }
            
            ImageWriter writer = (ImageWriter) writers.next();
            
            // Colocando a imagem em memória
            ios = ImageIO.createImageOutputStream(bos);
            writer.setOutput(ios);
            writer.write(null, new javax.imageio.IIOImage(image, null, null), null);
            ios.flush();
            
            // Criando o retorno
            BufferedImage buf = ImageIO.read(new ByteArrayInputStream(bos.toByteArray()));
            
            return(buf.getScaledInstance(buf.getWidth(), buf.getHeight(), buf.getType()));
        }
        finally
        {
            bos.close();
            ios.close();
        }
    }

    /**
     * Método que lê um arquivo de imagem usando o Default Toolkit da VM
     *
     * @param file O endereco da imagem
     * @param toGrayScale Se True, A imagem retornada será em escala de cinza
     * 
     * @return o {@link Image} do {@link File}
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static Image fileToImage(File file, boolean toGrayScale) throws Exception
    {
        if (toGrayScale)
        {
            return (GrayFilter.createDisabledImage(Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath())));
        }
        else
        {
            return (Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath()));
        }
    }

    /**
     * Método que lê e redimensiona um arquivo de imagem usando o Default Toolkit da VM
     *
     * @param file O endereco da imagem
     * @param width O tamanho que se deseja que a imagem seja retornada
     * @param height A altura que se deseja que a imagem seja retornada
     * 
     * @return o {@link Image} do {@link File}
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static Image fileToImage(File file, int width, int height) throws Exception
    {
    	return (Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath()).getScaledInstance(width, height, (Image.SCALE_AREA_AVERAGING + Image.SCALE_SMOOTH)));
    }

    /**
     * Método que lê, redimensiona e transforma um arquivo de imagem usando o Default Toolkit da VM
     *
     * @param file O endereco da imagem
     * @param width O tamanho que se deseja que a imagem seja retornada
     * @param height A altura que se deseja que a imagem seja retornada
     * @param toGrayScale Se True, A imagem retornada será em escala de cinza
     * 
     * @return o {@link Image} do {@link File}
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static Image fileToImage(File file, int width, int height, boolean toGrayScale) throws Exception
    {
        if (toGrayScale)
        {
            return (GrayFilter.createDisabledImage(Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath()).getScaledInstance(width, height, (Image.SCALE_AREA_AVERAGING + Image.SCALE_SMOOTH))));
        }
        else
        {
            return (Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath()).getScaledInstance(width, height, (Image.SCALE_AREA_AVERAGING + Image.SCALE_SMOOTH)));
        }
    }

    /**
     * Método que lê, redimensiona e transforma um arquivo de imagem usando o Default Toolkit da VM
     *
     * @param file O endereco da imagem
     * @param width O tamanho que se deseja que a imagem seja retornada
     * @param height A altura que se deseja que a imagem seja retornada
     * @param toGrayScale Se True, O ícone será retornado em escala de cinza
     * 
     * @return O {@link Icon} do {@link File}
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static Icon fileToIcon(String file, int width, int height, boolean toGrayScale) throws Exception
    {
        if (toGrayScale)
        {
            return ((new ImageIcon(GrayFilter.createDisabledImage(Toolkit.getDefaultToolkit().getImage(file).getScaledInstance(width, height, (Image.SCALE_AREA_AVERAGING + Image.SCALE_SMOOTH))))));
        }
        else
        {
            return ((new ImageIcon(Toolkit.getDefaultToolkit().getImage(file).getScaledInstance(width, height, (Image.SCALE_AREA_AVERAGING + Image.SCALE_SMOOTH)))));
        }
    }

    /**
     * Método que lê, redimensiona e transforma um arquivo de imagem em icone
     *
     * @param imagem A imagem que se deseja transformar
     * @param width O tamanho que se deseja que o icone seja retornado
     * @param height A altura que se deseja que o icone seja retornado
     * @param toGrayScale Se True, O ícone será retornado em escala de cinza
     * 
     * @return O {@link Icon} do {@link File}
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static Icon imageToIcon(Image imagem, int width, int height, boolean toGrayScale) throws Exception
    {
        if (toGrayScale)
        {
            return ((new ImageIcon(GrayFilter.createDisabledImage(imagem.getScaledInstance(width, height, (Image.SCALE_AREA_AVERAGING + Image.SCALE_SMOOTH))))));
        }
        else
        {
            return ((new ImageIcon(imagem.getScaledInstance(width, height, (Image.SCALE_AREA_AVERAGING + Image.SCALE_SMOOTH)))));
        }
    }
    
    /**
     * Método que retorna um {@link Image} apartir de um Array de bytes
     * 
     * @param b O Array de bytes que se deseja converter
     * 
     * @return O {@link Image} gerado do array
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static Image getImage(byte b[]) throws Exception
    {
        return(new ImageIcon(b).getImage());
    }
    
    /**
     * Método que retorna um Objeto {@link Icon} apartir de um array de bytes
     * 
     * @param b O vetor de bytes
     * 
     * @return O {@link Icon} gerado do array
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static Icon getIcon(byte b[]) throws Exception
    {
        return(new ImageIcon(b));
    }
    
    /**
     * Método que copia uma imagem de um diretorio para outro
     *
     * @param source O endereco da imagem a ser copiada com extensão e tudo
     * @param extension A extensão da imagem
     * @param target O endereco completo de onde a imagem sera salva com extensão e tudo
     *
     * @return Se True, A imagem foi copiada com suscesso
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static boolean copyImageToDir(File source, String extension, File target) throws Exception
    {
    	BufferedImage imagem = ImageIO.read(source);
    	boolean retorno = ImageIO.write(imagem, extension, target);
    	
    	imagem.flush();
    	return (retorno);
    }
    
    /**
     * Método que faz a copia do conteúdo de um {@link BufferedImage} para outro.
     * 
     * @param source O {@link BufferedImage} com a imagem
     * @param target O {@link BufferedImage} para onde a imagem será copiada
     * 
     * @return O {@link BufferedImage} com a imagem copiada
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static BufferedImage clone(BufferedImage source, BufferedImage target) throws Exception 
    {
		Graphics2D g2 = target.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2.drawRenderedImage(source, AffineTransform.getScaleInstance(((double) target.getWidth() / source.getWidth()), ((double) target.getHeight() / source.getHeight())));
		g2.dispose();
		
		return (target);
	}

    /**
     * Método que converte um {@link Icon} para um {@link Image}
     *
     * @param icon O {@link Icon} a ser convertido
     *
     * @return O {@link Image} gerado do {@link Icon}
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static Image iconToImage(Icon icon) throws Exception
    {
        if (icon instanceof ImageIcon)
        {
            return ((ImageIcon) icon).getImage();
        }
        else
        {
            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(icon.getIconWidth(), icon.getIconHeight());
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);

            g.dispose();
            return (image);
        }
    }

    /**
     * Método que desenha um {@link Image} em um {@link Component}
     *
     * @param image A imagem a ser desenhada no componente
     * @param componente O componente onde a imagem ser[a desenhada
     * @param x A posicao x da imagem no componente
     * @param y A posicao y da imagem no componente
     * @param width A largura da imagem
     * @param height A altura da imagem
     * 
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static void writeImageOnComponent(Image image, Component componente, int x, int y, int width, int height) throws Exception
    {
        componente.getGraphics().drawImage(image, x, y,width ,height ,componente);
    }

    /**
     * Método que coloca uma imagem na area de transferëncia do sistema, de modo que ela possa ser utilizada por outros programas como Paint, Gimp, Navegadores, etc. 
     *
     * @param image A imagem que se deseja colocar na área de transferência
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static void copiarImagemParaAreaDeTransferencia(Image image) throws Exception
    {
    	new ImageIcon(image); // Forçando a carga da imagem para previnir problemas de compatibilidade.
    	
    	BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    	newImage.createGraphics().drawImage(image, 0, 0, null);
    	image = newImage;
    	
    	TransferableImp imageSelection = new TransferableImp(image);
    	Toolkit toolkit = Toolkit.getDefaultToolkit();
    	toolkit.getSystemClipboard().setContents(imageSelection, null);
    }

    /**
     * Método que comprime uma imagem para um tamanho menor
     *
     * @param image A imagem que se deseja comprimir
     * @param quality Um valor de 0 - 1 definindo a qualidade da compressão
     *
     * @return O {@link Image} comprimido
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static Image getImagemComprimida(BufferedImage image, String extension, float quality) throws Exception
    {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix(extension);
        
        if (!writers.hasNext())
        {
            throw new IllegalStateException("Não foram encontrados escritos para a extensão informada");
        }
        
        ImageWriter writer = (ImageWriter) writers.next();
        
        // Comprimindo a imagem.
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);
        
        // Colocando a imagem em memória
        ByteArrayOutputStream bos = new ByteArrayOutputStream(32768);
        ImageOutputStream ios = ImageIO.createImageOutputStream(bos);
        writer.setOutput(ios);
        writer.write(null, new IIOImage(image, null, null), param);
        ios.flush(); 
        
        // Criando o retorno
        BufferedImage buf = ImageIO.read(new ByteArrayInputStream(bos.toByteArray()));
        return(buf.getScaledInstance(buf.getWidth(), buf.getHeight(), buf.getType()));
    }

    /**
     * Método que transforma um {@link Image} em um {@link BufferedImage}
     *  
     * @param image O {@link Image} que se deseja transforma
     * @param hasAlpha Se True, será utilizado o modo de transparência que leva em conta o ALPHA da imagem
     * 
     * @return O {@link BufferedImage} criado
     */
    public static BufferedImage imageToBufferedImage(Image image, boolean hasAlpha)
    {
        if (image instanceof BufferedImage)
        {
            return (BufferedImage) image;
        }

        // Explicitamente carregando a imagem
        image = new ImageIcon(image).getImage();


        // Criando o objeto compativel com o monitor
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        
        // determimando o tipo da transparência
        int transparency = Transparency.OPAQUE;
        if (hasAlpha)
        {
            transparency = Transparency.BITMASK;
        }

        // Criando o objeto
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);

        if (bimage == null)
        {
            // Se o objeto não pode ser criado na forma compativel, criamos ele explicitamente
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha)
            {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        Graphics g = bimage.createGraphics();

        // Desenhando a imagem no Buffer
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return (bimage);
    }
    
    /**
     * Método que cria um {@link BufferedImage} de duas imagem unidas em uma só
     *
     * @param background O {@link BufferedImage} que será o fundo da imagem
     * @param foreground O {@link BufferedImage} que será colocado em cima do background
     * @param x A posicao X de onde a segunda imagem deve ser colada na primeira
     * @param y A posicao Y de onde a segunda imagem deve ser colada na segunda
     *
     * @return O {@link BufferedImage} com as imagens unidas
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static BufferedImage joinImagesWithModeTransparencyByColors(BufferedImage background, BufferedImage foreground, int x, int y) throws Exception
    {
        int width = foreground.getWidth();
        int height = foreground.getHeight();

        int pixels, red, green, blue;

        for (int w = 0; w < width; w++)
        {
            for (int h = 0; h < height; h++)
            {
                pixels = foreground.getRGB(w, h);

                //Excluindo tipos de verdes incompatíveis com determinados formatos de imagens
                if ((pixels & 0x00FFFFFF) != 0x00FF00)
                {
                    red = (int) ((pixels & 0x00FF0000) >>> 16);
                    green = (int) ((pixels & 0x0000FF00) >>> 8);
                    blue = (int) (pixels & 0x000000FF);

                    background.getRaster().setPixel(w + x, h + y, new int[] { red, green, blue, 255 });
                }
            }
        }
        
        return(background);
    }

    
    /**
     * Método que cria um {@link BufferedImage} de duas imagem unidas em uma só
     *
     * @param background O {@link BufferedImage} que será o fundo da imagem
     * @param foreground O {@link BufferedImage} que será colocado em cima do background
     * @param x A posicao X de onde a segunda imagem deve ser colada na primeira
     * @param y A posicao Y de onde a segunda imagem deve ser colada na segunda
     *
     * @return O {@link BufferedImage} com as imagens unidas
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static BufferedImage joinImagesWithModeTranslucencyByColors(BufferedImage background, BufferedImage foreground, int x, int y) throws Exception
    {
        int width = foreground.getWidth();
        int height = foreground.getHeight();
        
        int pixels = 0;
        int fPixels = 0;
        
        int refRed = 0;
        int refGreen = 255;
        int refBlue = 0;
        
        int iRed = 0;
        int iGreen = 0;
        int iBlue = 0;
        
        int lRed = 0;
        int lGreen = 0;
        int lBlue = 0;
        
        int oRed = 0;
        int oGreen = 0;
        int oBlue = 0;
        
        for (int w = 0; w < width; w++)
        {
            for (int h = 0; h < height; h++)
            {
                pixels = background.getRGB(w + x, h + y);
                iRed = (int) ((pixels & 0x00FF0000) >>> 16);
                iGreen = (int) ((pixels & 0x0000FF00) >>> 8);
                iBlue = (int) (pixels & 0x000000FF);
                fPixels = foreground.getRGB(w, h);
                lRed = (int) ((fPixels & 0x00FF0000) >>> 16);
                lGreen = (int) ((fPixels & 0x0000FF00) >>> 8);
                lBlue = (int) (fPixels & 0x000000FF);

                // Calculando a transparência. que são os valores entre 0 (não visível) and 1 (opaco).
                double distance = Math.sqrt((refRed - lRed) * (refRed - lRed) + (refGreen - lGreen) * (refGreen - lGreen) + (refBlue - lBlue) * (refBlue - lBlue));

                // Achando a distância entre as duas transparências
                distance = Math.min(distance, 100f);
                float translucency = ((float) distance / 100f);

                // Corrigindo os RGB
                oRed = (int) (translucency * lRed + (1 - translucency) * iRed);
                oGreen = (int) (translucency * lGreen + (1 - translucency) * iGreen);
                oBlue = (int) (translucency * lBlue + (1 - translucency) * iBlue);

                background.getRaster().setPixel(w + x, h + y, new int[] { oRed, oGreen, oBlue, 255 });
            }
        }
        
        return(background);
    }
    
    /**
     * Método que cria um {@link BufferedImage} de duas imagem unidas em uma só
     *
     * @param background O {@link BufferedImage} que será o fundo da imagem
     * @param foreground O {@link BufferedImage} que será colocado em cima do background
     * @param x A posicao X de onde a segunda imagem deve ser colada na primeira
     * @param y A posicao Y de onde a segunda imagem deve ser colada na segunda
     * @param transparencia O nível de transparência da imagem em foreground
     *
     * @return O {@link BufferedImage} com as imagens unidas
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static BufferedImage joinImagesWithModeTranslucencyByColorsHSB(BufferedImage background, BufferedImage foreground, int x, int y, float transparencia) throws Exception
    {
        int width = foreground.getWidth();
        int height = foreground.getHeight();
        
        //Calcular todos os Pixels exceto os verdes na primeira camada
        int iPixel = 0;
        int lPixel = 0;
        float targetHue = 1f / 3f;
        
        int iRed = 0;
        int iGreen = 0;
        int iBlue = 0;
        
        int lRed = 0;
        int lGreen = 0;
        int lBlue = 0;
        
        int oRed = 0;
        int oGreen = 0;
        int oBlue = 0;
        
        for (int w = 0; w < width; w++) 
        {
            for (int h = 0; h < height; h++) 
            {
                // Pixels do fundo
                iPixel = background.getRGB(w + x, h + y);
                iRed = (int) ((iPixel & 0x00FF0000) >>> 16);
                iGreen = (int) ((iPixel & 0x0000FF00) >>> 8);
                iBlue = (int) (iPixel & 0x000000FF);
                
                // Pixels da frente
                lPixel = foreground.getRGB(w, h);
                lRed = (int) ((lPixel & 0x00FF0000) >>> 16);
                lGreen = (int) ((lPixel & 0x0000FF00) >>> 8);
                lBlue = (int) (lPixel & 0x000000FF);
                float[] lHSB = Color.RGBtoHSB(lRed, lGreen, lBlue, null);
                
                // Calculando a transparência
                float deltaHue = Math.abs(lHSB[0] - targetHue);
                float translucency = (deltaHue / transparencia);
                translucency = Math.min(translucency, 1f);
                
                // Recalculando o RGB
                oRed = (int) (translucency * lRed + (1 - translucency) * iRed);
                oGreen = (int) (translucency * lGreen + (1 - translucency) * iGreen);
                oBlue = (int) (translucency * lBlue + (1 - translucency) * iBlue);
                
                background.getRaster().setPixel(w + x, h + y, new int[]{oRed, oGreen, oBlue, 255});
            }
        }
        
        return(background);
    }
    
    /**
     * Método que escreve um objeto {@link BufferedImage} no disco
     * 
     * @param image O {@link BufferedImage} com a imagem
     * @param type O tipo de salvamento sendo, BufferedImage.TYPE_BYTE_GRAY para escala de cinza e BufferedImage.TYPE_BYTE_BINARY para preto e branco
     * @param file O endereço completo com extensão e tudo onde o arquivo será gravado
     * @param extension A extensão do arquivo
     * 
     * @return Se True, A imagem foi salva com sucesso.
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static boolean bufferedImageToFile(BufferedImage image, int type, File file, String extension) throws Exception
    {
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = im.createGraphics();
        g2d.drawImage(image,0,0,null);
        
        return(ImageIO.write(im, extension, file));
    }
    
    /**
     * Método que compara duas imagens e verifica se ambas sao iguais
     *
     * @param image1 A primeira imagem
     * @param image2 A segunda imagem
     *
     * @return Se True, Ambas as imagens são iguais
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static boolean compareImagens(BufferedImage image1, BufferedImage image2) throws Exception
    {
        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight())
        {
            return (Boolean.FALSE);
        }

        for (int x = 0; x < image1.getWidth(); x++)
        {
            for (int y = 0; y < image1.getHeight(); y++)
            {
                if (image1.getRGB(x, y) == image2.getRGB(x, y))
                {
                    return (Boolean.TRUE);
                }
            }
        }
        
        return (Boolean.FALSE);
    }

    /**
     * Método que corta as bordas de uma imagem para deixa-la mais arredondada
     * 
     * @param imagem A imagem que se deseja arredondar
     * @param curvatura O Grau de arredondamento da imagem
     * @param corBorda A cor da borda da imagem
     * @param larguraBorda A largura da borda da Imagem
     *
     * @return Uma imagem com as bordas arredondadas
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public static Image getRoundedImage(Image imagem, int curvatura, Color corBorda, float larguraBorda) throws Exception
    {
    	int width = imagem.getWidth(null);
    	int height = imagem.getHeight(null);
    	
    	BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    	RoundRectangle2D rect = new RoundRectangle2D.Double(0, 0, width, height, curvatura, curvatura);
    	
    	Graphics2D g2 = (Graphics2D) image.getGraphics();
    	
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	
    	g2.clip(rect);
    	g2.drawImage(imagem, 0, 0, null);
    	
    	g2.setStroke(new BasicStroke(larguraBorda));
    	g2.setColor(corBorda);
    	g2.draw(rect);
    	
    	return (image);
    }
}

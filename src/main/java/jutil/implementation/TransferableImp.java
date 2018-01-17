package jutil.implementation;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 * Classe que implementa um {@link Transferable} para o objetos {@link Image} 
 * para manipulação da área de transferência do sistema para objetos do tipo imagem
 * 
 * @author Diego Steyner
 */
public class TransferableImp implements Transferable
{
    /**
     * A imagem a ser colocada na área de transferência
     */
    private Image image;

    /**
     * O contrutor da classe
     * 
     * @param image A imagem
     * 
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public TransferableImp(Image image) throws Exception
    {
        this.image = image;
    }

    /**
     * Método que copia uma Imagem para a área de transferência do sistema
     * 
     * @param image A imagem que se deseja colocar na area de transferência
     * 
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public void copyImageToClipboard(Image image) throws Exception
    {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new TransferableImp(image), null);
    }

    /**
     * Método que verifica se o {@link DataFlavor} é do tipo imagem
     * 
     * @param flavor O DataFlavor (tipo) da área de transferência
     * 
     * @return A imagem a ser colocada na área de transferência
     * @throws java.awt.datatransfer.UnsupportedFlavorException Caso ocorra algum erro uma excessão será lançada
     */
    @Override
    public Object getTransferData(DataFlavor flavor) throws java.awt.datatransfer.UnsupportedFlavorException
    {
        if (flavor.equals(DataFlavor.imageFlavor) == false)
        {
            throw new UnsupportedFlavorException(flavor);
        }
        
        return (image);
    }

    /**
     * Método que verifica se o {@link DataFlavor} atual é do tipo imagem
     * 
     * @param tipo O DataFlavor (tipo) da área de transferência
     * @return Se True, O tipo passado é do tipo imagem
     */
    @Override
    public boolean isDataFlavorSupported(DataFlavor tipo)
    {
        return tipo.equals(DataFlavor.imageFlavor);
    }

    /**
     * Método que retorna um {@link DataFlavor} de imagem
     * 
     * @return Um {@link DataFlavor} do tipo imagem
     */
    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return new DataFlavor[] { DataFlavor.imageFlavor };
    }
}

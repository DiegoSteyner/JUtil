package jutil.data;

import java.io.PrintStream;
import java.io.Serializable;

import javax.swing.text.JTextComponent;

/**
 * Classe utilizada para armazenar objetos de impressão que podem ser chamados onde tais objetos se façam necessários
 * 
 * @author Diego Steyner
 *
 */
public class PrintOutput implements Serializable
{
    private static final long serialVersionUID = -2424180574783622742L;
    
    private PrintStream printOut;
    private JTextComponent componentOut;
    
    /**
     * Construtor padrão parametrizado
     * 
     * @param printOut O {@link PrintStream} que deve ser chamado em caso de impressão
     * @param componentOut o {@link JTextComponent} que deve ser chamado em caso de impressão
     */
    public PrintOutput(PrintStream printOut, JTextComponent componentOut)
    {
        super();
        this.printOut = printOut;
        this.componentOut = componentOut;
    }

    /**
     * Construtor padrão parametrizado
     * 
     * @param printOut O {@link PrintStream} que deve ser chamado em caso de impressão
     */
    public PrintOutput(PrintStream printOut)
    {
        super();
        this.printOut = printOut;
    }

    /**
     * Construtor padrão parametrizado
     * 
     * @param componentOut o {@link JTextComponent} que deve ser chamado em caso de impressão
     */
    public PrintOutput(JTextComponent componentOut)
    {
        super();
        this.componentOut = componentOut;
    }

    /**
     * Método que retorna o valor da variável printOut
     * 
     * @return O valor presente na variável printOut
     */
    public PrintStream getPrintOut()
    {
        return printOut;
    }

    /**
     * Método que altera o valor da variável printOut
     * 
     * @param printOut O novo valor da variável printOut
     */
    public void setPrintOut(PrintStream printOut)
    {
        this.printOut = printOut;
    }

    /**
     * Método que retorna o valor da variável componentOut
     * 
     * @return O valor presente na variável componentOut
     */
    public JTextComponent getComponentOut()
    {
        return componentOut;
    }

    /**
     * Método que altera o valor da variável componentOut
     * 
     * @param componentOut O novo valor da variável componentOut
     */
    public void setComponentOut(JTextComponent componentOut)
    {
        this.componentOut = componentOut;
    }

    /** 
     * Sem necessidade de Javadoc
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((componentOut == null) ? 0 : componentOut.hashCode());
        result = prime * result + ((printOut == null) ? 0 : printOut.hashCode());
        return result;
    }

    /** 
     * Sem necessidade de Javadoc
     * 
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        PrintOutput other = (PrintOutput) obj;
        if (componentOut == null)
        {
            if (other.componentOut != null)
            {
                return false;
            }
        }
        else if (!componentOut.equals(other.componentOut))
        {
            return false;
        }
        if (printOut == null)
        {
            if (other.printOut != null)
            {
                return false;
            }
        }
        else if (!printOut.equals(other.printOut))
        {
            return false;
        }
        return true;
    }

    /** 
     * Sem necessidade de Javadoc
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "PrintOutput [printOut=" + printOut + ", componentOut=" + componentOut + "]";
    }
    
    
}

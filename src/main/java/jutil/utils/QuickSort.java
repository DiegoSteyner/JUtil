package jutil.utils;

import java.util.Random;

/**
 * Classe que implementa a versão pública do algoritmo de ordenação QuickSort
 *
 * @author Diego Steyner
 * @param <E> O Tipo de dados que se esta ordenando
 */
public class QuickSort<E extends Comparable<? super E>>
{
    private Random random = new Random();
    
    /**
     * Método que faz a troca de posições no vetor
     * 
     * @param array O vetor
     * @param start A primeira posição a ser trocada
     * @param end A segunda posição a ser trocada
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     **/
    private void swap(E[] array, int start, int end) throws Exception
    {
        E tmp = array[start];
        array[start] = array[end];
        array[end] = tmp;
    }

    /**
     * Método que calcula o meio do vetor
     * 
     * @param array O vetor
     * @param start O Inicio do vetor
     * @param end O fim do vetor
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     **/
    private int partition(E[] array, int start, int end) throws Exception
    {
        int index = start + random.nextInt(end - start + 1);
        E pivot = array[index];
        swap(array, index, end);

        for (int i = index = start; i < end; ++i)
        {
            if (array[i].compareTo(pivot) <= 0)
            {
                swap(array, index++, i);
            }
        }

        swap(array, index, end);
        return (index);
    }

    /**
     * Método de ordenação baseado no QuickSort
     * 
     * @param array O vetor
     * @param inicio O Inicio do vetor
     * @param fim O fim do vetor
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     **/
    private void qsort(E[] array, int inicio, int fim) throws Exception
    {
        if (fim > inicio)
        {
            int index = partition(array, inicio, fim);
            qsort(array, inicio, index - 1);
            qsort(array, index + 1, fim);
        }
    }

    /**
     * Método de ordenação generico baseado no QuickSort
     * 
     * @param array O vetor
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     **/
    public void sort(E[] array) throws Exception
    {
        qsort(array, 0, array.length - 1);
    }

    /**
     * Método que retorna o valor da variável random
     * 
     * @return O valor presente na variável random
     */
    public Random getRandom()
    {
        return random;
    }

    /**
     * Método que altera o valor da variável random
     * 
     * @param random O novo valor da variável random
     */
    public void setRandom(Random random)
    {
        this.random = random;
    }
}
package jutil.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import jutil.abstracts.AbstractUtils;

/**
 * Classe utilitária para ordenação onde foram implementados versões públicas dos algoritmos de ordenações.
 * Obs: Todas as descrições foram traduzidas das descrições originais dos algoritmos com a alteração de alguns termos para facilitar o entendimento
 * 
 * @author Diego Steyner
 */
public class SortUtils extends AbstractUtils
{
    private int[] mergerSortVetor;

    /**
     * Construtor padrão
     */
    public SortUtils()
    {
    }

    /**
     * O bubble sort, ou ordenação por flutuação (literalmente "por bolha"), é um algoritmo de ordenação dos mais simples. A ideia é percorrer o vetor diversas vezes, 
     * a cada passagem fazendo flutuar para o topo o maior elemento da sequência. Essa movimentação lembra a forma como as bolhas em um tanque de água procuram seu próprio nível, 
     * e disso vem o nome do algoritmo. No melhor caso, o algoritmo executa n2 / 2 operações relevantes, onde n representa o número de elementos do vector. No pior caso, são feitas 
     * 2n2 operações. No caso médio, são feitas 5n2 / 2 operações. A complexidade desse algoritmo é de Ordem quadrática. Por isso, ele não é recomendado para programas que precisem 
     * de velocidade e operem com quantidade elevada de dados.
     *
     * @param vetor O vetor de inteiros que se deseja Ordenar
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public void bubbleSort(int[] vetor) throws Exception
    {
        for (int i = 0; i < vetor.length - 1; i++)
        {
            for (int j = 0; j < vetor.length - 1; j++)
            {
                if (vetor[j] > vetor[j + 1])
                {
                    swapBubbleSort(vetor, j, j + 1);
                }
            }
        }
    }
    
    /**
     * O bubble sort, ou ordenação por flutuação (literalmente "por bolha"), é um algoritmo de ordenação dos mais simples. A ideia é percorrer o vetor diversas vezes, 
     * a cada passagem fazendo flutuar para o topo o maior elemento da sequência. Essa movimentação lembra a forma como as bolhas em um tanque de água procuram seu próprio nível, 
     * e disso vem o nome do algoritmo. No melhor caso, o algoritmo executa n2 / 2 operações relevantes, onde n representa o número de elementos do vector. No pior caso, são feitas 
     * 2n2 operações. No caso médio, são feitas 5n2 / 2 operações. A complexidade desse algoritmo é de Ordem quadrática. Por isso, ele não é recomendado para programas que precisem 
     * de velocidade e operem com quantidade elevada de dados.
     *
     * @param vetor O vetor de String que se deseja Ordenar
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public static String [] stringBubbleSort(String [] vetor) {
        Vector<String> v = new Vector<>();
        
        for(int i=0; i<vetor.length; i++)
        {
        	v.addElement(vetor[i]);
        }
        
        int n = v.size();
        boolean swap = true;
        String tmp = null;
        
        while ( swap ) 
        {
           swap = false;
        
           for(int i=0; i<(n-1); i++) 
           {
              if ( ((String)v.elementAt(i)).compareTo(((String)v.elementAt(i+1)) ) > 0 ) 
              {
                 tmp = (String)v.elementAt(i+1);
                 v.removeElementAt( i+1 );
                 v.insertElementAt( tmp, i );
                 swap = true;
              }
           }
        }
        
        String [] out = new String [ n ];
        v.copyInto(out);
        
        return out;
     }
    
    /**
     * O bubble sort, ou ordenação por flutuação (literalmente "por bolha"), é um algoritmo de ordenação dos mais simples. A ideia é percorrer o vector diversas vezes, 
     * a cada passagem fazendo flutuar para o topo o maior elemento da sequência. Essa movimentação lembra a forma como as bolhas em um tanque de água procuram seu próprio nível, 
     * e disso vem o nome do algoritmo. No melhor caso, o algoritmo executa n2 / 2 operações relevantes, onde n representa o número de elementos do vector. No pior caso, são feitas 
     * 2n2 operações. No caso médio, são feitas 5n2 / 2 operações. A complexidade desse algoritmo é de Ordem quadrática. Por isso, ele não é recomendado para programas que precisem 
     * de velocidade e operem com quantidade elevada de dados.
     *
     * @param vetor O vetor de String que se deseja Ordenar
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public String[] bubbleSort(String[] vetor) throws Exception
    {
        Vector<String> v = new Vector<String>();

        for (int i = 0; i < vetor.length; i++)
        {
            v.addElement(vetor[i]);
        }
        
        int size = v.size();
        boolean swap = true;

        while (swap)
        {
            swap = false;

            for (int i = 0; i < (size - 1); i++)
            {
                if (((String) v.elementAt(i)).compareTo(((String) v.elementAt(i + 1))) > 0)
                {
                    v.removeElementAt(i + 1);
                    v.insertElementAt(((String) v.elementAt(i + 1)), i);
                    swap = true;
                }
            }
        }

        String[] retorno = new String[size];
        v.copyInto(retorno);
        v.clear();

        return (retorno);
    }

    /**
     * Swap da ordenacao Bolha, o que esta na segunda posicao ficara na primeira, e o que esta na primeira ficara na segunda
     *
     * @param a vetor passado como parametro
     * @param i primeira Posicao
     * @param j segunda Posicao
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    private void swapBubbleSort(int[] a, int i, int j) throws Exception
    {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    /**
     * Metodo que inverte todo o vetor de inteiro passado como parametro, o que estiver em primeiro vai pra ultimo e vice versa
     *
     * @param vetor O vetor que se deseja inverter
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public void flipHorizontalInt(int vetor[]) throws Exception
    {
        ArrayList<Integer> flip = new ArrayList<Integer>();

        for (int i = 0; i < vetor.length; i++)
        {
            flip.add(vetor[i]);
        }

        Collections.reverse(flip);

        for (int i = 0; i < flip.size(); i++)
        {
            vetor[i] = Integer.parseInt(flip.get(i).toString());
        }
    }

    /**
     * Metod que inverter todo o vetor de String passado como parametro, o que estiver em primeiro vai pra ultimo e vice versa
     *
     * @param vetor O vetor que se deseja inverter
     * 
     * @return O vetor de String invertido
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public String[] flipHorizontalString(String vetor[]) throws Exception
    {
        ArrayList<String> flip = new ArrayList<String>();

        for (int i = 0; i < vetor.length; i++)
        {
            flip.add(vetor[i]);
        }

        Collections.reverse(flip);

        for (int i = 0; i < flip.size(); i++)
        {
            vetor[i] = flip.get(i).toString();
        }

        return vetor;
    }

    /**
     * O algoritmo heapsort é um algoritmo de ordenação generalista, Tem um desempenho em tempo de execucao muito bom em conjuntos ordenados aleatoriamente, tem um uso de memoria 
     * bem comportado e o seu desempenho em pior cenario é praticamente igual ao desempenho em cenario medio. Alguns algoritmos de ordenacao rapidos tem desempenhos 
     * espectacularmente ruins no pior cenario, quer em tempo de execucao, quer no uso da memoria. O Heapsort trabalha no lugar e o tempo de execucao em pior cenario para 
     * ordenar n elementos e de O (n lg n). Le-se logaritmo (ou log) de "n" na base 2. Para valores de n, razoavelmente grande, o termo lg n e quase constante, de modo que o 
     * tempo de ordenacao e quase linear com o numero de itens a ordenar.
     *
     * @param v O vetor que se deseja Ordenar
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public void heapSort(int v[]) throws Exception
    {
        createMaxHeap(v);
        int n = v.length;

        for (int i = v.length - 1; i > 0; i--)
        {
            swapHeapSort(v, i, 0);
            createMinHeap(v, 0, --n);
        }
    }

    /**
     * Construindo um Heap Maximo
     *
     * @param v o Vetor para se contruir o heap Maximo
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    private void createMaxHeap(int v[]) throws Exception
    {
        for (int i = v.length / 2 - 1; i >= 0; i--)
        {
            createMinHeap(v, i, v.length);
        }
    }

    /**
     * Construindo um Hep Mínimo
     *
     * @param v Os Valores
     * @param pos A posição atual
     * @param n A maior posição
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    private void createMinHeap(int v[], int pos, int n) throws Exception
    {
        int max = 2 * pos + 1, right = max + 1;

        if (max < n)
        {
            if (right < n && v[max] < v[right])
            {
                max = right;
            }
            if (v[max] > v[pos])
            {
                swapHeapSort(v, max, pos);
                createMinHeap(v, max, n);
            }
        }
    }

    /**
     * Swap da heap Sort, o que esta na segunda posicao ficara na primeira, e o que esta na primeira ficara na segunda
     *
     * @param v Os valores
     * @param firsPosition A primeira posição
     * @param secondPosition A última posição
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    private void swapHeapSort(int[] v, int firsPosition, int secondPosition) throws Exception
    {
        int aux = 0;
        aux = v[firsPosition];
        v[firsPosition] = v[secondPosition];
        v[secondPosition] = aux;
    }

    /**
     * Insertion sort, ou ordenação por insercao, e um simples algoritmo de ordenacao, eficiente quando aplicado a um pequeno numero de elementos. Em termos gerais, ele percorre 
     * um vetor de elementos da esquerda para a direita e à medida que avanca vai deixando os elementos mais a esquerda ordenados. O algoritmo de insercao funciona da mesma maneira 
     * com que muitas pessoas ordenam cartas em um jogo de baralho como o poquer. Ele tem um Menor numero de trocas e comparacoes entre os algoritmos de ordenação Ω(n) 
     * quando o vetor está ordenado. porem no Pior caso O(n²)
     *
     * @param vetor o vetor que se deseja Ordenar
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public void insertionSort(int[] vetor) throws Exception
    {
        for (int i = 0; i < vetor.length; i++)
        {
            int copyNumber = vetor[i];
            int j = i;

            while (j > 0 && copyNumber < vetor[j - 1])
            {
                vetor[j] = vetor[j - 1];
                j--;
            }
            vetor[j] = copyNumber;
        }
    }

    /**
     * O merge sort, ou ordenacao por mistura, e um exemplo de algoritmo de ordenacao do tipo dividir-para-conquistar. Sua ideia basica e que e muito facil criar uma sequencia 
     * ordenada a partir de duas outras tambem ordenadas. Para isso, ele divide a sequencia original em pares de dados, ordena-as; depois as agrupa em sequencias de quatro elementos, 
     * e assim por diante, ate ter toda a sequencia dividida em apenas duas partes. Os três passos uteis dos algoritmos dividir-para-conquistar, ou divide and conquer, 
     * que se aplicam ao merge sort sao:
     *  
     *  <blockquote>1) Dividir: Dividir os dados em subsequencias pequenas;</blockquote> 
     *  <blockquote>2) Conquistar: Classificar as duas metades recursivamente aplicando o merge sort;</blockquote> 
     *  <blockquote>3) Combinar: Juntar as duas metades em um unico conjunto ja classificado.</blockquote>
     * 
     * @param vetor O vetor de inteiros que se deseja ordenar
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public void mergeSort(int vetor[]) throws Exception
    {
        this.mergerSortVetor = vetor;
        startMergeSort(0, (vetor.length - 1));
    }

    /**
     * Metodo recursivo que divide o vetor em dois e depois os mescla e ordena
     *
     * @param start deve Iniciar em 0
     * @param end e o tamanho total do (vetor -1)
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    private void startMergeSort(int start, int end) throws Exception
    {
        if (start < end)
        {
            int meio = (start + end) / 2;
            startMergeSort(start, meio);
            startMergeSort(meio + 1, end);
            join(start, meio, end);
        }
    }

    /**
     * Ordena dois trechos ordenados e adjacente de vetores e ordena-os conjuntamente
     *
     * @param start O inicio dos dados
     * @param middle O meio dos dados
     * @param end O fim dos dados
     * 
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    private void join(int start, int middle, int end) throws Exception
    {
        int tamanho = end - start + 1;

        /*
         * Inicialização de um vetor temporario para auxiliar na ordenação,  O vetor temporário é uma cópia do trecho que será ordenado
         */
        int[] temp = new int[tamanho];

        for (int posicao = 0; posicao < tamanho; posicao++)
        {
            temp[posicao] = mergerSortVetor[start + posicao];
        }

        /*
         * Laço para ordenação do vetor, utilizando o vetor temporário, usando índices i e j para cada trecho de vetor da mesclagem
         */
        int i = 0;
        int j = middle - start + 1;

        // A depender das condições, recebe um elemento de um trecho ou outro
        for (int posicao = 0; posicao < tamanho; posicao++)
        {
            mergerSortVetor[start + posicao] = (j <= tamanho - 1) ? ((i <= middle - start) ? (temp[i] < temp[j]) ? temp[i++] : temp[j++] : temp[j++]) : temp[i++];
        }
    }

    /**
     * O algoritmo Quicksort e um metodo de ordenacao muito rapido e eficiente, inventado por C.A.R. Hoare em 1960, quando visitou a Universidade de Moscovo como estudante. Ele 
     * criou o 'Quicksort ao tentar traduzir um dicionario de ingles para russo, ordenando as palavras, tendo como objetivo reduzir o problema original em subproblemas que possam 
     * ser resolvidos mais facil e rapidamente. Foi publicado em 1962 apos uma serie de refinamentos.
     *
     * @param vetor O vetor de inteiros que se deseja Ordenar
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public void QuickSortInt(int[] vetor) throws Exception
    {
        Integer objetoInteger[] = new Integer[vetor.length];

        for (int i = 0; i < vetor.length; i++)
        {
            objetoInteger[i] = vetor[i];
        }

        QuickSort<Integer> qs = new QuickSort<Integer>();
        qs.sort(objetoInteger);

        for (int i = 0; i < objetoInteger.length; i++)
        {
            vetor[i] = objetoInteger[i];
        }
    }

    /**
     * O algoritmo Quicksort e um metodo de ordenacao muito rapido e eficiente, inventado por C.A.R. Hoare em 1960, quando visitou a Universidade de Moscovo como estudante. Ele criou 
     * o 'Quicksort ao tentar traduzir um dicionario de ingles para russo, ordenando as palavras, tendo como objetivo reduzir o problema original em subproblemas que possam ser 
     * resolvidos mais facil e rapidamente. Foi publicado em 1962 apos uma serie de refinamentos.
     *
     * @param vetor O vetor de Strings que se deseja Ordenar
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public void QuickSortString(String vetor[]) throws Exception
    {
        QuickSort<String> qs = new QuickSort<String>();
        qs.sort(vetor);
    }

    /**
     * O selection sort (do ingles, ordenacao por selecao) e um algoritmo de ordenacao baseado em se passar sempre o menor valor do vetor para a primeira posicao 
     * (ou o maior dependendo da ordem requerida), depois o de segundo menor valor para a segunda posicao, e assim e feito sucessivamente com os (n-1) elementos restantes, ate os 
     * últimos dois elementos.
     *
     * @param v O vetor que se deseja Ordenadar
     * @throws Exception Caso ocorra algum erro uma exceção será lançada
     */
    public void SelectionSort(int[] v) throws Exception
    {
        int min = 0; 
        int aux = 0;

        for (int i = 0; i < v.length; i++)
        {
            min = i;

            for (int j = i + 1; j < v.length; j++)
            {
                if (v[j] < v[min])
                {
                    min = j;
                }
            }

            if (min != i)
            {
                aux = v[min];
                v[min] = v[i];
                v[i] = aux;
            }
        }
    }
}

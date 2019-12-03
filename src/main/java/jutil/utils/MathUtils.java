package jutil.utils;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;

import jutil.abstracts.AbstractUtils;

/**
 * Classe utilitaria para se trabalhar com operações matemáticas
 * 
 * @author Diego Steyner
 */
public final class MathUtils extends AbstractUtils
{
    public static final int ARREDONDAR_PARA_BAIXO = 0;
    public static final int ARREDONDAR_PARA_CIMA = 1;
    public static final int NUMERO_MAXIMO_CASAS = 2;
    
    /**
     * O peso número do CPF
     */
    public static final int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    
    /**
     * O peso numérico do CNPJ
     */
    public static final int[] pesoCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    
    /**
     * Construtor privado
     */
    public MathUtils()
    {
    }
    
    /**
     * Método que gera todas as combinações de um dado conjunto em todas as posições possíveis
     * 
     * @param qtdElementsPerGroup A quantidade de elementos que cada conjunto de combinações deve ter
     * @param values O valores que se deseja gerar as combinações
     * @param sizeReturn Quantos grupos de combinações devem ser gerados para o retorno
     * 
     * @return O {@link ArrayList} com todas as combinações geradas com repetição
     * @throws Exception Caso algum erro ocorra uma excessao será lançada
     */
    public ArrayList<String> generateAllCombinationsWithRepetition(int qtdElementsPerGroup, ArrayList<String> values, int sizeReturn) throws Exception
    {
        ArrayList<String> retorno = new ArrayList<>();
        
        try
        {
            int next;
            int[] indices = new int[qtdElementsPerGroup];
            StringBuilder sequence = new StringBuilder();

            do
            {
                for (int index : indices)
                {
                    sequence.append(values.get(index)).append(",");
                }

                if (sequence.length() > 0)
                {
                    retorno.add(sequence.toString());
                    sequence = new StringBuilder();
                    retorno.set(retorno.size()-1, retorno.get(retorno.size()-1).substring(0, retorno.get(retorno.size()-1).length()-1));
                    
                    if (retorno.size() == sizeReturn)
                    {
                        break;
                    }
                }

                next = 1;
                for (int i = indices.length - 1; i >= 0; i--)
                {
                    if (next == 0)
                    {
                        break;
                    }

                    indices[i] += next;
                    next = 0;

                    if (indices[i] == values.size())
                    {
                        next = 1;
                        indices[i] = 0;
                    }
                }
            }
            while (next != 1);
            
            return(retorno);
        }
        finally
        {
            retorno.clear();
        }
    }
    
    /**
     * Método que gera todas as combinações de um dado conjunto sem repetições, ou seja, a posição do valor não será levado em consideração
     * 
     * @param values O valores que se deseja gerar as combinações
     * @param qtdElementsPerGroup A quantidade de elementos que cada conjunto de combinações deve ter
     * @param sizeReturn Quantos grupos de combinações devem ser gerados para o retorno
     * 
     * @return O {@link ArrayList} com todas as combinações geradas sem repetição
     * @throws Exception Caso algum erro ocorra uma excessao será lançada
     */
    public ArrayList<String> generateAllCombinationsWithoutRepetition(ArrayList<String> values, int qtdElementsPerGroup, int sizeReturn) throws Exception
    {
        ArrayList<String> retorno = new ArrayList<String>();
        
        try
        {
            combinations(values, qtdElementsPerGroup, 0, new String[qtdElementsPerGroup], retorno, sizeReturn);
            return(retorno);
        }
        finally
        {
            retorno.clear();
        }
    }
    
    /**
     * Método que calcula a quantidade de digitos de um número.
     * 
     * @param number O número que se quer caluclar (Obs: Maior que Zero)
     * 
     * @return O número de digitos presente no número, Ex: 300 = 3; 15 = 2; 1000 = 4;
     * @throws Exception Caso algum erro ocorra uma excessao será lançada
     */
    public int numberOfDigits(int number) throws Exception
    {
        return (1 + (int) (Math.log(number) / Math.log(10)));
    }
    
    /**
     * Método que pega o dígito na posição do número
     * 
     * @param number O número
     * @param position A posição
     * 
     * @return O ditito na localização
     * @throws Exception Caso algum erro ocorra uma excessao será lançada
     */
    public int getDigitInNumber(int number, int position)
    {
        return Integer.parseInt(Character.toString(Integer.toString(number).charAt(position)));
    }
    
    /**
     * Método que gera todas as combinações de um dado conjunto sem repetições, ou seja, a posição do valor não será levado em consideração
     * 
     * @param values O valores que se deseja gerar as combinações
     * @param qtdElementsPerGroup A quantidade de elementos que cada conjunto de combinações deve ter
     * @param startPosition A posição que o grupo de valores se inicia
     * @param groups O vetor onde o grupo de valores serão armazenados
     * @param retorno A variável onde os valores serão armazenados
     * @param sizeReturn Quantos grupos de combinações devem ser gerados para o retorno
     * 
     * @throws Exception Caso algum erro ocorra uma excessao será lançada
     */
    private void combinations(ArrayList<String> values, int qtdElementsPerGroup, int startPosition, String[] groups, ArrayList<String> retorno, int sizeReturn) throws Exception
    {
        if (qtdElementsPerGroup == 0)
        {
            StringBuilder sb = new StringBuilder();
            for (String string : groups)
            {
                sb.append(string).append(",");
            }
            
            if(sb.length() != 0)
            {
                retorno.add(sb.toString());
                retorno.set(retorno.size()-1, retorno.get(retorno.size()-1).substring(0, retorno.get(retorno.size()-1).length()-1));
                
                if (retorno.size() == sizeReturn)
                {
                    return;
                }
            }
            
            return;
        }
        
        for (int i = startPosition; i <= values.size() - qtdElementsPerGroup; i++)
        {
            groups[groups.length - qtdElementsPerGroup] = values.get(i);
            combinations(values, qtdElementsPerGroup - 1, i + 1, groups, retorno, sizeReturn);
            
            if (retorno.size() == sizeReturn)
            {
                break;
            }
        }
    }
    
    /**
     * Método que faz o fatorial de um número
     * 
     * @param number O número que se deseja tirar o fatorial
     * 
     * @return Um {@link BigInteger} com o valor do fatorial
     * @throws Exception Caso algum erro ocorra uma excessao será lançada
     */
    public BigInteger factorial(final int number) throws Exception
    {
        BigInteger result = BigInteger.valueOf(number);
        for (int i = number - 1; i > 0; i--)
        {
            result = result.multiply(BigInteger.valueOf(i));
        }
        
        return (result);
    }

    /**
     * Método que retorna a média aritmetica entre vários números
     * 
     * @param numero Os numeros que se deseja obter a media Aritmetica
     * 
     * @return A media Aritmetica entre os numeros
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public int getAverageArithmetic(int... numero) throws Exception
    {
        int k = 0;

        for (int i = 0; i < numero.length; i++)
        {
            k = (numero[i] + k);
        }

        return (k / numero.length);
    }
    
    /**
     * Método que soma a Diagonal principal de um vetor
     * 
     * @param v O vetor que se deseja somar a Diagnal Principal
     * 
     * @return A soma da Diagonal principal do vetor
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public int sumMainDiagonal(int v[][]) throws Exception
    {
        int r = 0;

        for (int i = 0; i < v.length; i++)
        {
            r += v[i][i];
        }

        return (r);
    }
    
    /**
     * Método que converte um numero na base 10 para um numero na Base 2
     * 
     * @param numero O numero que se deseja converter
     * 
     * @return O numero convertido
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public String decimalToBinary(int numero) throws Exception
    {
        StringBuilder b = new StringBuilder();

        int resto = 0;
        int n1 = numero;

        do
        {
            resto = n1 % 2;
            n1 = (n1 / 2);

            b.append(String.valueOf(resto));

            if (n1 == 0)
            {
                break;
            }

        }
        while (true);

        return (b.reverse().toString());
    }
    
    /**
     * Método que testa se um numero e par ou nao
     * 
     * @param numero O numero que se deseja testar
     * 
     * @return Se True, O número é par
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public boolean numberIsPar(int numero) throws Exception
    {
        return((numero%2) == 0);
    }
    
    /**
     * Método que calcula quantas combinações sem repetição são possíveis dentro de um dado conjunto  
     * 
     * @param qtdElements Quantos elementos o conjunto inteiro tem (Ex: 60 números)
     * @param qtdElementPerGroup Quantos elementos por grupo as combinações terão (Ex: 6 números cada)
     * 
     * @return A quantidade de combinações possíveis com os conjuntos
     * @throws Exception Caso algum erro ocorra uma excessao será lançada
     */
    public int possibitiesWithoutRepetition(int qtdElements, int qtdElementPerGroup) throws Exception
    {
        if (qtdElements == qtdElementPerGroup)
        {
            return 1;
        }
        
        BigInteger nFat = factorial(qtdElements);
        BigInteger pFat = factorial(qtdElementPerGroup);
        BigInteger nMinusPFat = factorial(qtdElements - qtdElementPerGroup);
        BigInteger result = nFat.divide(pFat.multiply(nMinusPFat));
        
        return (result.intValue());
    }

    /**
     * Método que calcula quantas combinações com repetição são possíveis dentro de um dado conjunto  
     * 
     * @param qtdElements Quantos elementos o conjunto inteiro tem (Ex: 60 números)
     * @param qtdPerGroup Quantos elementos por grupo as combinações terão (Ex: 6 números cada)
     * 
     * @return A quantidade de combinações possíveis com os conjuntos
     * @throws Exception Caso algum erro ocorra uma excessao será lançada
     */
    public int possibitiesWithRepetition(int qtdElements, int qtdPerGroup) throws Exception
    {
        BigInteger result = new BigInteger("1");
        
        for(int i = 0; i < qtdPerGroup; i++)
        {
            result = result.multiply(new BigInteger(String.valueOf(qtdElements)));
        }
        
        return result.intValue();
    }
    
    /**
     * Método que verifica se um número CPF é válido
     *
     * @param cpf O CPF que se deseja verificar
     *
     * @return Se True, O CPF é válido
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public boolean cpfIsValid(String cpf) throws Exception
    {
        if ((cpf == null) || (cpf.length() != 11)) 
        {
            new Exception("CPF nulo ou inferior a 11 dígitos");
        }
        
        Integer digito1 = getLastDigitCPForCNPJ(cpf.substring(0, 9), pesoCPF);
        Integer digito2 = getLastDigitCPForCNPJ(cpf.substring(0, 9) + digito1, pesoCPF);
        
        return cpf.equals(cpf.substring(0, 9) + digito1.toString() + digito2.toString());
    }

    /**
     * Método que verifica se o CNPJ passado por parametro e valido ou nao
     *
     * @param cnpj O CNPJ que se deseja verificar
     *
     * @return Se True, O CNPJ é válido
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public boolean cnpjIsValid(String cnpj) throws Exception{

        if ((cnpj == null) || (cnpj.length() != 14)) 
        {
            new Exception("CNPJ nulo ou inferior a 14 dígitos");
        }
        
        Integer digito1 = getLastDigitCPForCNPJ(cnpj.substring(0, 12), pesoCNPJ);
        Integer digito2 = getLastDigitCPForCNPJ(cnpj.substring(0, 12) + digito1, pesoCNPJ);
        
        return cnpj.equals(cnpj.substring(0, 12) + digito1.toString() + digito2.toString());
    }

    /**
     * Método que calcula o último dígito de um CPF ou CNPJ
     *
     * @param str O CPF ou CNPJ que se deseja calcular
     * @param peso O peso de cada numero do CPF ou CNPJ
     * 
     * @return O ultimo digito do CPF
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public int getLastDigitCPForCNPJ(String str, int[] peso) throws Exception{

        int soma = 0;
        
        for (int indice = str.length() - 1, digito; indice >= 0; indice--) 
        {
            digito = Integer.parseInt(str.substring(indice, indice + 1));
            soma += digito * peso[peso.length - str.length() + indice];
        }
        soma = 11 - soma % 11;
        
        return (soma > 9 ? 0 : soma);
    }
    
    /**
     * Método que arredonda um numero double
     *
     * @param numero O numero que se deseja Arredondar
     * @param numeroMaximoDeCasas O numero maximo de casas aceitas depois da virgula
     *
     * @param tipo O tipo de arredondamento sendo {@link #ARREDONDAR_PARA_CIMA}, {@link #ARREDONDAR_PARA_BAIXO}, {@link #NUMERO_MAXIMO_CASAS}
     *
     * @return Uma String que equivale ao numero arredondado
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public String RoudingNumber(double numero, int numeroMaximoDeCasas, int tipo) throws Exception
    {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(numeroMaximoDeCasas);

        switch (tipo)
        {
            case NUMERO_MAXIMO_CASAS:
            {
                nf.setRoundingMode(java.math.RoundingMode.FLOOR);
                break;
            }
            case ARREDONDAR_PARA_CIMA:
            {
                nf.setRoundingMode(java.math.RoundingMode.CEILING);
                break;
            }
            case ARREDONDAR_PARA_BAIXO:
            {
                nf.setRoundingMode(java.math.RoundingMode.DOWN);
                break;
            }

            default:
            {
                nf.setRoundingMode(java.math.RoundingMode.FLOOR);
                break;
            }
        }

        return (nf.format(numero));
    }

    /**
     * Método que calcula o máximo divisor comum de dois números
     * 
     * @param numero1 O primeiro numero
     * @param numero2 O segundo numero
     * 
     * @return O maximo divisor comum dos dois numeros
     * @exception Exception Caso ocorra algum erro uma excessão será lançada
     */
    public int getMaximoDivisorComum(int numero1, int numero2) throws Exception
    {
        int n1 = numero1;
        int n2 = numero2;

        if (numero1 < numero2)
        {
            int aux = n1;
            n1 = n2;
            n2 = aux;
        }

        int div = n1 % n2;

        do
        {
            if (div != 0)
            {
                div = n2 % div;

                if (div != 0)
                {
                    n2 = div;
                }
                else
                {
                    break;
                }
            }
            else
            {
                break;
            }
        }
        while (true);

        return (n2);
    }
}

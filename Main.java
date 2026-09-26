import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    private static Aluno[] alunos = new Aluno[10];
    private static int totalAlunos = 0;

    private static final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        int opcao;

        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    cadastrarAluno();
                    break;
                case 2:
                    relatorioPorNomeCrescente();
                    break;
                case 3:
                    relatorioPorRaDecrescente();
                    break;
                case 4:
                    relatorioAprovados();
                    break;
                case 5:
                    buscarPorRaSequencial();
                    break;
                case 6:
                    buscarPorRaBinaria();
                    break;
                case 0:
                    System.out.println("Encerrando o programa. Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }

        } while (opcao != 0);

        scanner.close();
    }

    // ==================================================================
    // MENU
    // ==================================================================

    private static void exibirMenu() {
        System.out.println("============================================");
        System.out.println("      SISTEMA DE CADASTRO DE ALUNOS");
        System.out.println("============================================");
        System.out.println("1 - Cadastrar Aluno");
        System.out.println("2 - Relatório por Nome (Crescente)  [Bubble Sort]");
        System.out.println("3 - Relatório por RA (Decrescente)  [Selection Sort]");
        System.out.println("4 - Relatório de Aprovados (Crescente por Nome) [Merge Sort]");
        System.out.println("5 - Buscar Aluno por RA (Busca Sequencial)");
        System.out.println("6 - Buscar Aluno por RA (Busca Binária)");
        System.out.println("0 - Sair");
        System.out.println("============================================");
    }

    // ==================================================================
    // 1) CADASTRO
    // ==================================================================

    private static void cadastrarAluno() {
        if (totalAlunos == alunos.length) {
            aumentarCapacidadeArray();
        }

        System.out.println("--- Cadastro de Aluno ---");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        int ra = lerInteiro("RA: ");
        int idade = lerInteiro("Idade: ");

        System.out.print("Sexo (M/F): ");
        String sexo = scanner.nextLine();

        double media = lerDouble("Média final: ");

        Aluno aluno = new Aluno(nome, ra, idade, sexo, media);
        alunos[totalAlunos] = aluno;
        totalAlunos++;

        System.out.println("Aluno cadastrado com sucesso! Resultado: " + aluno.getResultado());
    }

    /**
     * Como o Array possui tamanho fixo, quando ele lota, criar um novo
     * Array com o dobro da capacidade e copiar os dados (técnica
     * de redimensionamento de Array).
     */
    private static void aumentarCapacidadeArray() {
        Aluno[] novoArray = new Aluno[alunos.length * 2];
        for (int i = 0; i < alunos.length; i++) {
            novoArray[i] = alunos[i];
        }
        alunos = novoArray;
    }

    // ==================================================================
    // 2) RELATÓRIO POR NOME CRESCENTE - BUBBLE SORT
    // ==================================================================

    private static void relatorioPorNomeCrescente() {
        if (!existemAlunos()) return;

        Aluno[] copia = copiarArrayValido();
        bubbleSortPorNome(copia);

        System.out.println("--- Relatório de Alunos por Nome (Crescente) ---");
        imprimirLista(copia);
    }

    /**
     * Bubble Sort: a cada passagem, compara pares adjacentes e "borbulha"
     * o maior valor (em ordem alfabética) para o final do array.
     */
    private static void bubbleSortPorNome(Aluno[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (array[j].getNome().compareToIgnoreCase(array[j + 1].getNome()) > 0) {
                    trocar(array, j, j + 1);
                }
            }
        }
    }

    // ==================================================================
    // 3) RELATÓRIO POR RA DECRESCENTE - SELECTION SORT
    // ==================================================================

    private static void relatorioPorRaDecrescente() {
        if (!existemAlunos()) return;

        Aluno[] copia = copiarArrayValido();
        selectionSortPorRaDecrescente(copia);

        System.out.println("--- Relatório de Alunos por RA (Decrescente) ---");
        imprimirLista(copia);
    }

    /**
     * Selection Sort: a cada passagem, seleciona o maior RA restante
     * e o posiciona na posição correta (ordem decrescente).
     */
    private static void selectionSortPorRaDecrescente(Aluno[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int indiceMaior = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j].getRa() > array[indiceMaior].getRa()) {
                    indiceMaior = j;
                }
            }
            if (indiceMaior != i) {
                trocar(array, i, indiceMaior);
            }
        }
    }

    // ==================================================================
    // 4) RELATÓRIO DE APROVADOS - MERGE SORT (RECURSIVO)
    // ==================================================================

    private static void relatorioAprovados() {
        if (!existemAlunos()) return;

        // Filtra apenas os aprovados
        Aluno[] validos = copiarArrayValido();
        int qtdAprovados = 0;
        for (Aluno a : validos) {
            if (a.getResultado().equals("Aprovado")) {
                qtdAprovados++;
            }
        }

        if (qtdAprovados == 0) {
            System.out.println("Nenhum aluno aprovado até o momento.");
            return;
        }

        Aluno[] aprovados = new Aluno[qtdAprovados];
        int indice = 0;
        for (Aluno a : validos) {
            if (a.getResultado().equals("Aprovado")) {
                aprovados[indice++] = a;
            }
        }

        mergeSortPorNome(aprovados, 0, aprovados.length - 1);

        System.out.println("--- Relatório de Alunos Aprovados (Crescente por Nome) ---");
        imprimirLista(aprovados);
    }

    /**
     * Merge Sort: algoritmo recursivo "dividir para conquistar".
     * Divide o array ao meio recursivamente até restar 1 elemento,
     * depois intercala (merge) os subarrays já ordenados.
     */
    private static void mergeSortPorNome(Aluno[] array, int inicio, int fim) {
        if (inicio < fim) {
            int meio = (inicio + fim) / 2;

            mergeSortPorNome(array, inicio, meio);       // ordena metade esquerda
            mergeSortPorNome(array, meio + 1, fim);      // ordena metade direita
            intercalar(array, inicio, meio, fim);        // combina as duas metades
        }
    }

    private static void intercalar(Aluno[] array, int inicio, int meio, int fim) {
        int tamanhoEsquerda = meio - inicio + 1;
        int tamanhoDireita = fim - meio;

        Aluno[] esquerda = new Aluno[tamanhoEsquerda];
        Aluno[] direita = new Aluno[tamanhoDireita];

        for (int i = 0; i < tamanhoEsquerda; i++) {
            esquerda[i] = array[inicio + i];
        }
        for (int j = 0; j < tamanhoDireita; j++) {
            direita[j] = array[meio + 1 + j];
        }

        int i = 0, j = 0, k = inicio;

        while (i < tamanhoEsquerda && j < tamanhoDireita) {
            if (esquerda[i].getNome().compareToIgnoreCase(direita[j].getNome()) <= 0) {
                array[k] = esquerda[i];
                i++;
            } else {
                array[k] = direita[j];
                j++;
            }
            k++;
        }

        while (i < tamanhoEsquerda) {
            array[k] = esquerda[i];
            i++;
            k++;
        }

        while (j < tamanhoDireita) {
            array[k] = direita[j];
            j++;
            k++;
        }
    }

    // ==================================================================
    // 5) BUSCA SEQUENCIAL POR RA
    // ==================================================================

    private static void buscarPorRaSequencial() {
        if (!existemAlunos()) return;

        int ra = lerInteiro("Digite o RA a ser buscado: ");
        long inicio = System.nanoTime();

        int posicao = -1;
        for (int i = 0; i < totalAlunos; i++) {
            if (alunos[i].getRa() == ra) {
                posicao = i;
                break;
            }
        }

        long fim = System.nanoTime();

        if (posicao != -1) {
            System.out.println("Aluno encontrado (Busca Sequencial): " + alunos[posicao]);
        } else {
            System.out.println("Aluno com RA " + ra + " não encontrado.");
        }
        System.out.println("Tempo de busca: " + (fim - inicio) + " ns");
    }

    // ==================================================================
    // 6) BUSCA BINÁRIA POR RA (exige lista ordenada por RA)
    // ==================================================================

    private static void buscarPorRaBinaria() {
        if (!existemAlunos()) return;

        // A busca binária exige que o array esteja ordenado pelo campo de busca (RA)
        Aluno[] ordenadoPorRa = copiarArrayValido();
        selectionSortPorRaCrescenteAuxiliar(ordenadoPorRa);

        int ra = lerInteiro("Digite o RA a ser buscado: ");

        int posicao = buscaBinariaRecursiva(ordenadoPorRa, ra, 0, ordenadoPorRa.length - 1);

        if (posicao != -1) {
            System.out.println("Aluno encontrado (Busca Binária): " + ordenadoPorRa[posicao]);
        } else {
            System.out.println("Aluno com RA " + ra + " não encontrado.");
        }
    }

    /**
     * Busca binária implementada de forma recursiva.
     * Compara o RA buscado com o elemento do meio e descarta metade do array
     * a cada chamada, reduzindo o espaço de busca (log2 n).
     */
    private static int buscaBinariaRecursiva(Aluno[] array, int raProcurado, int inicio, int fim) {
        if (inicio > fim) {
            return -1; // não encontrado
        }

        int meio = (inicio + fim) / 2;

        if (array[meio].getRa() == raProcurado) {
            return meio;
        } else if (raProcurado < array[meio].getRa()) {
            return buscaBinariaRecursiva(array, raProcurado, inicio, meio - 1);
        } else {
            return buscaBinariaRecursiva(array, raProcurado, meio + 1, fim);
        }
    }

    /**
     * Selection sort auxiliar (crescente por RA), usado apenas para preparar
     * o array antes da busca binária.
     */
    private static void selectionSortPorRaCrescenteAuxiliar(Aluno[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int indiceMenor = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j].getRa() < array[indiceMenor].getRa()) {
                    indiceMenor = j;
                }
            }
            if (indiceMenor != i) {
                trocar(array, i, indiceMenor);
            }
        }
    }

    // ==================================================================
    // MÉTODOS AUXILIARES
    // ==================================================================

    private static boolean existemAlunos() {
        if (totalAlunos == 0) {
            System.out.println("Nenhum aluno cadastrado ainda.");
            return false;
        }
        return true;
    }

    /**
     * Retorna uma cópia apenas das posições realmente preenchidas do array
     * (evita nulos, já que o array interno pode ter posições vazias/capacidade extra).
     */
    private static Aluno[] copiarArrayValido() {
        Aluno[] copia = new Aluno[totalAlunos];
        for (int i = 0; i < totalAlunos; i++) {
            copia[i] = alunos[i];
        }
        return copia;
    }

    private static void trocar(Aluno[] array, int i, int j) {
        Aluno temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private static void imprimirLista(Aluno[] array) {
        for (Aluno a : array) {
            System.out.println(a);
        }
    }

    private static int lerInteiro(String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextInt()) {
            System.out.print("Digite um número inteiro válido: ");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine(); // limpa o buffer (quebra de linha)
        return valor;
    }

    private static double lerDouble(String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextDouble()) {
            System.out.print("Digite um número válido (use ponto para decimais): ");
            scanner.next();
        }
        double valor = scanner.nextDouble();
        scanner.nextLine();
        return valor;
    }
}
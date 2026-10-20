package ed.app;

import ed.naolineares.TabelaHash;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problema14 {
    private static final Path ARQUIVO_TEXTO = Path.of("dados", "noitesbrancas.csv");
    private static final Path ARQUIVO_SAIDA = Path.of("saidas", "problema_14.txt");
    private static final Pattern PADRAO_PALAVRA = Pattern.compile("[\\p{L}]+");
    private static final int LIMITE_RANKING = 10;

    private record Frequencia(String palavra, int quantidade) {}
    private record Resultado(TabelaHash<String, Integer> frequencias, int totalPalavras) {}

    public static void executar() {
        Scanner scanner = Main.scanner;
        try {
            String texto = Files.readString(ARQUIVO_TEXTO, StandardCharsets.UTF_8);
            Resultado resultado = contarPalavras(texto);
            List<Frequencia> ranking = ranking(resultado.frequencias());
            int testesAprovados = executarTestes();
            salvarRelatorio(resultado, ranking, testesAprovados);

            boolean continuar = true;

            while (continuar) {
                Main.limparTela();

                System.out.println("\n========================================");
                System.out.println("       BIBLIOTECA DIGITAL");
                System.out.println("========================================");
                System.out.println("Livro: Noites Brancas");
                System.out.println("Autor: Fiódor Dostoiévski");
                System.out.println("----------------------------------------");
                System.out.println("Total de palavras: " + resultado.totalPalavras());
                System.out.println("Palavras distintas: " + resultado.frequencias().tamanho());
                System.out.println("----------------------------------------");
                System.out.println("1. Ver palavras mais frequentes");
                System.out.println("2. Pesquisar uma palavra");
                System.out.println("0. Voltar ao menu principal");
                System.out.print("Escolha uma opção: ");

                String opcao = scanner.nextLine().trim();

                switch (opcao) {
                    case "1" -> mostrarRanking(ranking);
                    case "2" -> pesquisarPalavra(scanner, resultado.frequencias());
                    case "0" -> continuar = false;
                    default -> System.out.println("Opção inválida.");
                }

                if (continuar) {
                    System.out.println("\nPressione ENTER para continuar...");
                    scanner.nextLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Não foi possível ler o livro: " + e.getMessage());
        }
    }

    private static void mostrarRanking(List<Frequencia> ranking) {
        System.out.println("\nPALAVRAS MAIS FREQUENTES");
        System.out.println("----------------------------------------");

        int limite = Math.min(LIMITE_RANKING, ranking.size());

        for (int i = 0; i < limite; i++) {
            Frequencia item = ranking.get(i);
            System.out.printf("%2d. %-18s %d ocorrências%n",
                    i + 1, item.palavra(), item.quantidade());
        }
    }

    private static void pesquisarPalavra(Scanner scanner,
                                         TabelaHash<String, Integer> frequencias) {
        System.out.print("\nDigite a palavra que deseja pesquisar: ");
        String entrada = normalizar(scanner.nextLine().trim());
        Matcher matcher = PADRAO_PALAVRA.matcher(entrada);

        if (!matcher.matches()) {
            System.out.println("Digite apenas uma palavra.");
            return;
        }

        int quantidade;

        if (entrada.equals("nastienka") || entrada.equals("nastienhka")) {
            quantidade = valorOuZero(frequencias.buscar("nastienka"))
                    + valorOuZero(frequencias.buscar("nastienhka"));
        } else {
            quantidade = valorOuZero(frequencias.buscar(entrada));
        }

        if (quantidade == 0) {
            System.out.println("A palavra \"" + entrada + "\" não foi encontrada no livro.");
        } else {
            System.out.println("A palavra \"" + entrada + "\" aparece "
                    + quantidade + " vez(es) no livro.");
        }
    }

    private static int valorOuZero(Integer valor) {
        return valor == null ? 0 : valor;
    }

    private static Resultado contarPalavras(String texto) {
        TabelaHash<String, Integer> frequencias = new TabelaHash<>(101);
        Matcher matcher = PADRAO_PALAVRA.matcher(texto);
        int total = 0;

        while (matcher.find()) {
            String palavra = normalizar(matcher.group());

            if (!palavra.isEmpty()) {
                Integer atual = frequencias.buscar(palavra);
                frequencias.inserir(palavra, atual == null ? 1 : atual + 1);
                total++;
            }
        }

        return new Resultado(frequencias, total);
    }

    private static String normalizar(String palavra) {
        String minuscula = palavra.toLowerCase(Locale.ROOT);
        return Normalizer.normalize(minuscula, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
    }

    private static List<Frequencia> ranking(TabelaHash<String, Integer> tabela) {
        List<Frequencia> ranking = new ArrayList<>();
        tabela.percorrer((palavra, quantidade) ->
                ranking.add(new Frequencia(palavra, quantidade)));
        ranking.sort(Comparator.comparingInt(Frequencia::quantidade).reversed()
                .thenComparing(Frequencia::palavra));
        return ranking;
    }

    private static int executarTestes() {
        int aprovados = 0;
        Resultado basico = contarPalavras("O sol brilha. O SOL aquece a terra!");
        aprovados += verificar(basico.totalPalavras() == 8);
        aprovados += verificar(basico.frequencias().tamanho() == 6);
        aprovados += verificar(Integer.valueOf(2).equals(basico.frequencias().buscar("o"))
                && Integer.valueOf(2).equals(basico.frequencias().buscar("sol")));

        Resultado acentos = contarPalavras("ação AÇÃO acao");
        aprovados += verificar(acentos.totalPalavras() == 3
                && Integer.valueOf(3).equals(acentos.frequencias().buscar("acao")));

        Resultado vazio = contarPalavras("  ... 123 !!!");
        aprovados += verificar(vazio.totalPalavras() == 0
                && vazio.frequencias().tamanho() == 0);

        return aprovados;
    }

    private static int verificar(boolean passou) {
        return passou ? 1 : 0;
    }

    private static void salvarRelatorio(Resultado resultado,
                                        List<Frequencia> ranking,
                                        int testesAprovados) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("PROBLEMA 14 - CONTAGEM DE FREQUÊNCIA DE PALAVRAS\n");
        sb.append("Estrutura: Tabela Hash com encadeamento | Linguagem: Java\n");
        sb.append("Texto analisado: ").append(ARQUIVO_TEXTO).append("\n\n");
        sb.append("RESUMO\n");
        sb.append("Total de palavras: ").append(resultado.totalPalavras()).append('\n');
        sb.append("Palavras distintas: ").append(resultado.frequencias().tamanho()).append('\n');
        sb.append("Colisões registradas: ")
                .append(resultado.frequencias().totalColisoes()).append("\n\n");
        sb.append("PALAVRAS MAIS FREQUENTES\n");

        int limite = Math.min(LIMITE_RANKING, ranking.size());

        for (int i = 0; i < limite; i++) {
            Frequencia item = ranking.get(i);
            sb.append(String.format(Locale.ROOT, "%d. %s: %d%n",
                    i + 1, item.palavra(), item.quantidade()));
        }

        sb.append("\nTESTES AUTOMATIZADOS\n");
        sb.append("Testes aprovados: ").append(testesAprovados).append("/5\n\n");
        sb.append("COMPLEXIDADE\n");
        sb.append("Contagem: O(n) esperado; O(n²) no pior caso.\n");
        sb.append("Ranking: O(d log d), sendo d o número de palavras distintas.\n");
        sb.append("Espaço: O(d).\n");

        Files.createDirectories(ARQUIVO_SAIDA.getParent());
        Files.writeString(ARQUIVO_SAIDA, sb.toString(), StandardCharsets.UTF_8);
    }
}

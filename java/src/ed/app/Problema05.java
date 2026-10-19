
package ed.app;

import ed.lineares.Lista;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;


public class Problema05 {
    private static final Lista playlist = new Lista();
    private static final StringBuilder saida = new StringBuilder();
    private static int quantidadeInicial;
    private static int adicionadas;
    private static int removidas;

    public static void executar() {
        Scanner scanner = Main.scanner;
        saida.setLength(0);
        adicionadas = 0;
        removidas = 0;
        carregarPlaylist();
        quantidadeInicial = playlist.getTamanho();

        registrar("GERENCIAMENTO DE PLAYLIST DE MÚSICAS");
        registrar("Playlist inicializada com " + playlist.getTamanho() + " músicas.");

        int opcao;

        do {
            System.out.println("\n=== MENU DA PLAYLIST ===");
            System.out.println("1. Listar músicas");
            System.out.println("2. Inserir música no início");
            System.out.println("3. Inserir música no fim");
            System.out.println("4. Inserir música em uma posição");
            System.out.println("5. Buscar música");
            System.out.println("6. Remover música");
            System.out.println("7. Mostrar duração total");
            System.out.println("0. Encerrar");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                registrar("Opção inválida.");
                opcao = -1;
                continue;
            }

            switch (opcao) {
                case 1 -> registrar("\nPLAYLIST ATUAL:\n" + playlist.listar());
                case 2 -> inserir(scanner, 1);
                case 3 -> inserir(scanner, 2);
                case 4 -> inserir(scanner, 3);
                case 5 -> buscar(scanner);
                case 6 -> remover(scanner);
                case 7 -> {
                    int total = playlist.calcularDuracaoTotal();
                    registrar(total == 0
                            ? "Duração total indisponível: as músicas estão sem duração informada."
                            : "Duração total: " + formatarDuracao(total));
                }
                case 0 -> registrar("Programa encerrado.");
                default -> registrar("Opção inválida.");
            }
        } while (opcao != 0);

        salvarArquivo();
    }


    private static void carregarPlaylist() {
        playlist.limpar();
        Path caminho = Path.of("dados", "playlist.csv");

        try {
            for (String linha : Files.readAllLines(caminho, StandardCharsets.UTF_8)) {
                if (linha.isBlank() || linha.startsWith("titulo;")) {
                    continue;
                }

                String[] campos = linha.split(";", -1);

                if (campos.length < 3) {
                    throw new IllegalArgumentException("Linha inválida no CSV: " + linha);
                }

                String titulo;
                String artista;
                int duracao;

                try {
                    duracao = Integer.parseInt(campos[campos.length - 1].trim());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Duração inválida no CSV: " + linha);
                }

                if (campos.length == 3) {
                    titulo = campos[0].trim();
                    artista = campos[1].trim();
                } else {
                    titulo = String.join(";", java.util.Arrays.copyOfRange(campos, 0, campos.length - 2)).trim();
                    artista = campos[campos.length - 2].trim();
                }

                playlist.inserirFim(new Musica(titulo, artista, duracao));

            }

            if (playlist.getTamanho() < 12) {
                throw new IllegalStateException("A playlist precisa ter pelo menos 12 músicas.");
            }
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível ler dados/playlist.csv.", e);
        }
    }


    private static void inserir(Scanner scanner, int tipo) {
        try {
            System.out.print("Título: ");
            String titulo = scanner.nextLine().trim();

            System.out.print("Artista: ");
            String artista = scanner.nextLine().trim();

            System.out.print("Duração em segundos: ");
            int duracao = Integer.parseInt(scanner.nextLine().trim());

            Musica musica = new Musica(titulo, artista, duracao);

            if (tipo == 1) {
                playlist.inserirInicio(musica);
            } else if (tipo == 2) {
                playlist.inserirFim(musica);
            } else {
                System.out.print("Posição (começando em 0): ");
                int posicao = Integer.parseInt(scanner.nextLine().trim());
                playlist.inserirNaPosicao(musica, posicao);
            }

            adicionadas++;
            registrar("Música inserida: " + musica);
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            registrar("Não foi possível inserir a música: " + e.getMessage());
        }
    }

    private static void buscar(Scanner scanner) {
        System.out.print("Título da música: ");
        String titulo = scanner.nextLine().trim();
        Musica musica = playlist.buscarPorTitulo(titulo);

        if (musica == null) {
            registrar("Música não encontrada: " + titulo);
        } else {
            registrar("Música encontrada: " + musica);
        }
    }

    private static void remover(Scanner scanner) {
        System.out.print("Título da música que deseja remover: ");
        String titulo = scanner.nextLine().trim();

        if (playlist.removerPorTitulo(titulo)) {
            removidas++;
            registrar("Música removida: " + titulo);
        } else {
            registrar("Música não encontrada: " + titulo);
        }
    }

    private static String formatarDuracao(int segundos) {
        return (segundos / 60) + " min " + (segundos % 60) + " s";
    }

    private static void registrar(String texto) {
        System.out.println(texto);
        saida.append(texto).append(System.lineSeparator());
    }

    private static void salvarArquivo() {
        try {
            registrar("");
            registrar("========================================");
            registrar("RELATÓRIO FINAL — PROBLEMA 05");
            registrar("========================================");
            registrar("Quantidade inicial: " + quantidadeInicial);
            registrar("Músicas adicionadas: " + adicionadas);
            registrar("Músicas removidas: " + removidas);
            registrar("Quantidade final: " + playlist.getTamanho());
            registrar("");
            registrar("PLAYLIST FINAL:");
            registrar(playlist.listar());
            int total = playlist.calcularDuracaoTotal();
            registrar("Duração total: " + formatarDuracao(total));
            registrar("");
            registrar("COMPLEXIDADE:");
            registrar("Busca por título: O(n)");
            registrar("Inserção no início: O(1)");
            registrar("Inserção no fim: O(n) na implementação atual, se não houver referência ao último nó.");
            registrar("Inserção em posição: O(n)");
            registrar("Remoção por título: O(n)");
            registrar("Listagem completa: O(n)");

            Path caminho = Path.of("saidas", "problema_05.txt");
            Files.createDirectories(caminho.getParent());
            Files.writeString(caminho, saida.toString(), StandardCharsets.UTF_8);
            System.out.println("Relatório atualizado em " + caminho);
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }
}

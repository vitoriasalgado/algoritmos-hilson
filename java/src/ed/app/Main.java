
package ed.app;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao;

        do {
            limparTela();
            exibirMenu();
            System.out.print("Escolha o problema: ");
            String entrada = scanner.nextLine().trim();

            try {
                opcao = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            limparTela();

            switch (opcao) {
                case 5 -> Problema05.executar();
                case 6 -> Problema06.executar();
                case 7 -> Problema07.executar();
                case 8 -> Problema08.executar();
                case 9 -> Problema09.executar();
                case 10 -> Problema10.executar();
                case 13 -> Problema13.executar();
                case 14 -> Problema14.executar();
                case 0 -> System.out.println("Encerrando o programa...");
                default -> System.out.println(
                        "Opção inválida. Escolha um dos problemas listados."
                );
            }

            if (opcao != 0) {
                System.out.println("\nPressione ENTER para voltar ao menu...");
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                } else {
                    opcao = 0;
                }
            }

        } while (opcao != 0);
    }

    public static void limparTela() {
        try {
            new ProcessBuilder("clear")
                    .inheritIO()
                    .start()
                    .waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            System.out.print("\033[H\033[2J\033[3J");
            System.out.flush();
        }
    }

    private static void exibirMenu() {
        System.out.println("==============================================");
        System.out.println("       ALGORITMOS E ESTRUTURAS DE DADOS I");
        System.out.println("==============================================");
        System.out.println(" 5  - Gerenciamento de Playlist de Músicas");
        System.out.println(" 6  - Controle de Estoque de uma Pequena Loja");
        System.out.println(" 7  - Cadastro e Busca de Alunos por Matrícula");
        System.out.println(" 8  - Sistema de Autocomplete de Produtos");
        System.out.println(" 9  - Índice de CPF para Sistema de RH");
        System.out.println("10  - Ranking de Jogadores em um Jogo Online");
        System.out.println("13  - Cadastro de Usuários com Login Rápido");
        System.out.println("14  - Contagem de Frequência de Palavras");
        System.out.println("----------------------------------------------");
        System.out.println(" 0  - Sair");
        System.out.println("==============================================");
    }
}

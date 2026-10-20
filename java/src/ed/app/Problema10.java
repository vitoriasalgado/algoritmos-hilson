package ed.app;

import ed.naolineares.ArvoreAVLJogadores;
import ed.naolineares.ArvoreAVLJogadores.Jogador;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Problema10{
    private static final Scanner scanner = new Scanner(System.in);
    private static final ArvoreAVLJogadores arvore = new ArvoreAVLJogadores();

    public static void executar() {
        carregarJogadores();

        while (true) {
            Main.limparTela();

            System.out.println("\n=== RANKING DE JOGADORES ===");
            System.out.println("Jogadores cadastrados: " + arvore.quantidade());
            System.out.println("1 - Cadastrar jogador");
            System.out.println("2 - Atualizar pontuação");
            System.out.println("3 - Buscar jogador");
            System.out.println("4 - Exibir ranking completo");
            System.out.println("5 - Consultar Top-K");
            System.out.println("6 - Exibir informações da AVL");
            System.out.println("7 - Executar testes e salvar relatório");
            System.out.println("0 - Voltar ao menu principal");
            System.out.print("Escolha: ");

            int opcao = lerInteiro();

            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> atualizar();
                case 3 -> buscar();
                case 4 -> exibirRanking(arvore.listarRanking());
                case 5 -> consultarTopK();
                case 6 -> mostrarInformacoes();
                case 7 -> executarTestes();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void carregarJogadores() {
        if (arvore.quantidade() > 0) return;

        String[] nomes = {
            "Ana", "Bruno", "Carla", "Daniel", "Eduarda",
            "Felipe", "Gabriela", "Henrique", "Isabela", "João",
            "Karen", "Lucas", "Mariana", "Nicolas", "Olivia",
            "Pedro", "Rafaela", "Samuel", "Tatiane", "Vitor",
            "Amanda", "Beatriz", "Caio", "Diego", "Elisa",
            "Fernando", "Gustavo", "Helena", "Igor", "Julia"
        };

        for (int i = 0; i < nomes.length; i++) {
            arvore.inserir(nomes[i], 500 + ((i * 137) % 1501));
        }

        System.out.println("Foram carregados 30 jogadores para a simulação.");
    }

    private static void cadastrar() {
        System.out.print("Nome do jogador: ");
        String nome = scanner.nextLine().trim();

        if (nome.isBlank()) {
            System.out.println("O nome não pode ficar vazio.");
            return;
        }

        System.out.print("Pontuação inicial: ");
        int pontos = lerInteiro();

        if (arvore.inserir(nome, pontos)) {
            System.out.println("Jogador cadastrado.");
        } else {
            System.out.println("Cadastro inválido: verifique a pontuação ou se o nome já existe.");
        }
    }

    private static void atualizar() {
        System.out.print("Nome do jogador: ");
        String nome = scanner.nextLine().trim();

        if (arvore.buscar(nome) == null) {
            System.out.println("Jogador não encontrado.");
            return;
        }

        System.out.print("Nova pontuação: ");
        int pontos = lerInteiro();

        if (arvore.atualizarPontuacao(nome, pontos)) {
            System.out.println("Pontuação atualizada.");
        } else {
            System.out.println("Pontuação inválida.");
        }
    }

    private static void buscar() {
        System.out.print("Nome do jogador: ");
        String nome = scanner.nextLine().trim();
        Jogador jogador = arvore.buscar(nome);

        if (jogador == null) {
            System.out.println("Jogador não encontrado.");
        } else {
            System.out.println(jogador);
        }
    }

    private static void consultarTopK() {
        System.out.print("Quantos jogadores deseja consultar? ");
        int k = lerInteiro();

        if (k <= 0) {
            System.out.println("Informe um valor maior que zero.");
            return;
        }

        exibirRanking(arvore.topK(k));
    }

    private static void exibirRanking(List<Jogador> jogadores) {
        if (jogadores.isEmpty()) {
            System.out.println("Nenhum jogador cadastrado.");
            return;
        }

        System.out.println("\n--- RANKING ---");
        for (int i = 0; i < jogadores.size(); i++) {
            System.out.println((i + 1) + "º - " + jogadores.get(i));
        }
    }

    private static void mostrarInformacoes() {
        System.out.println("Quantidade: " + arvore.quantidade());
        System.out.println("Altura da árvore: " + arvore.altura());
        System.out.println("AVL balanceada: " + arvore.estaBalanceada());
    }

    private static void executarTestes() {
        ArvoreAVLJogadores teste = new ArvoreAVLJogadores();

        for (int i = 1; i <= 30; i++) {
            teste.inserir("Teste" + i, i * 100);
        }

        boolean insercao = teste.quantidade() == 30;
        boolean atualizacao = teste.atualizarPontuacao("Teste1", 5000)
                && teste.quantidade() == 30
                && teste.buscar("Teste1").getPontuacao() == 5000;

        List<Jogador> ranking = teste.listarRanking();
        boolean ordenacao = true;
        for (int i = 1; i < ranking.size(); i++) {
            if (ranking.get(i - 1).getPontuacao() < ranking.get(i).getPontuacao()) {
                ordenacao = false;
                break;
            }
        }

        List<Jogador> top5 = teste.topK(5);
        boolean topK = top5.size() == 5
                && top5.get(0).getPontuacao() == 5000;

        boolean balanceamento = teste.estaBalanceada();

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("RELATÓRIO - PROBLEMA 10: RANKING DE JOGADORES\n");
        relatorio.append("============================================\n");
        relatorio.append("Estrutura: Árvore AVL ordenada por pontuação e nome\n");
        relatorio.append("Jogadores na simulação principal: ").append(arvore.quantidade()).append('\n');
        relatorio.append("Altura da AVL principal: ").append(arvore.altura()).append('\n');
        relatorio.append("AVL principal balanceada: ").append(arvore.estaBalanceada()).append("\n\n");
        relatorio.append("TESTES AUTOMATIZADOS\n");
        relatorio.append("Inserção de 30 jogadores: ").append(resultado(insercao)).append('\n');
        relatorio.append("Atualização sem duplicar jogador: ").append(resultado(atualizacao)).append('\n');
        relatorio.append("Ordenação decrescente do ranking: ").append(resultado(ordenacao)).append('\n');
        relatorio.append("Consulta Top-5: ").append(resultado(topK)).append('\n');
        relatorio.append("Balanceamento AVL: ").append(resultado(balanceamento)).append("\n\n");
        relatorio.append("TOP 10 DA SIMULAÇÃO PRINCIPAL\n");

        List<Jogador> top10 = arvore.topK(10);
        for (int i = 0; i < top10.size(); i++) {
            relatorio.append(i + 1).append("º - ").append(top10.get(i)).append('\n');
        }

        relatorio.append("\nCOMPLEXIDADE\n");
        relatorio.append("Busca: O(log n) em árvore AVL balanceada\n");
        relatorio.append("Inserção: O(log n) em árvore AVL balanceada\n");
        relatorio.append("Remoção/atualização: O(log n) para as operações na árvore\n");
        relatorio.append("Ranking completo: O(n)\n");
        relatorio.append("Top-K: O(n + k) nesta implementação\n");

        try {
            Path diretorio = Path.of("saidas");
            Files.createDirectories(diretorio);
            Path arquivo = diretorio.resolve("problema_10.txt");
            Files.writeString(arquivo, relatorio.toString(), StandardCharsets.UTF_8);
            System.out.println(relatorio);
            System.out.println("Relatório salvo em: " + arquivo);
        } catch (IOException e) {
            System.out.println("Erro ao salvar relatório: " + e.getMessage());
        }
    }

    private static String resultado(boolean passou) {
        return passou ? "APROVADO" : "FALHOU";
    }

    private static int lerInteiro() {
        while (true) {
            String entrada = scanner.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.print("Digite um número inteiro válido: ");
            }
        }
    }
}

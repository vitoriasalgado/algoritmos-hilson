package ed.app;

import java.util.Scanner;

public class Problema06 {
    private static final Scanner scanner = Main.scanner;
    private static final ListaProdutos estoque = new ListaProdutos();

    public static void executar() {
        int opcao;

        do {
            Main.limparTela();

            System.out.println("\n===== CONTROLE DE ESTOQUE =====");
            System.out.println("1. Cadastrar produto");
            System.out.println("2. Listar produtos");
            System.out.println("3. Buscar produto por codigo");
            System.out.println("4. Buscar produto por nome");
            System.out.println("5. Atualizar quantidade");
            System.out.println("6. Remover produto");
            System.out.println("7. Listar produtos abaixo do estoque minimo");
            System.out.println("8. Simular movimentacoes de uma semana");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opcao: ");

            opcao = lerInteiro();

            switch (opcao) {
                case 1 -> cadastrarProduto();
                case 2 -> System.out.println(estoque.listar());
                case 3 -> buscarPorCodigo();
                case 4 -> buscarPorNome();
                case 5 -> atualizarQuantidade();
                case 6 -> removerProduto();
                case 7 -> listarEstoqueMinimo();
                case 8 -> simularSemana();
                case 0 -> System.out.println("Voltando ao menu principal.");
                default -> System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private static void cadastrarProduto() {
        System.out.print("Codigo: ");
        String codigo = scanner.nextLine().trim();

        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();

        System.out.print("Quantidade inicial: ");
        int quantidade = lerInteiro();

        System.out.print("Preco unitario: ");
        double preco = lerDouble();

        if (codigo.isEmpty() || nome.isEmpty() || quantidade < 0 || preco < 0) {
            System.out.println("Dados invalidos. Confira os valores informados.");
            return;
        }

        Produto produto = new Produto(codigo, nome, quantidade, preco);

        if (estoque.cadastrar(produto)) {
            System.out.println("Produto cadastrado com sucesso.");
        } else {
            System.out.println("Ja existe um produto com esse codigo.");
        }
    }

    private static void buscarPorCodigo() {
        System.out.print("Informe o codigo: ");
        String codigo = scanner.nextLine().trim();

        Produto produto = estoque.buscarPorCodigo(codigo);

        if (produto == null) {
            System.out.println("Produto nao encontrado.");
        } else {
            System.out.println(produto);
        }
    }

    private static void buscarPorNome() {
        System.out.print("Informe o nome: ");
        String nome = scanner.nextLine().trim();

        Produto produto = estoque.buscarPorNome(nome);

        if (produto == null) {
            System.out.println("Produto nao encontrado.");
        } else {
            System.out.println(produto);
        }
    }

    private static void atualizarQuantidade() {
        System.out.print("Codigo do produto: ");
        String codigo = scanner.nextLine().trim();

        System.out.print("Nova quantidade em estoque: ");
        int quantidade = lerInteiro();

        if (estoque.atualizarQuantidade(codigo, quantidade)) {
            System.out.println("Quantidade atualizada com sucesso.");
        } else {
            System.out.println("Produto nao encontrado ou quantidade invalida.");
        }
    }

    private static void removerProduto() {
        System.out.print("Codigo do produto a remover: ");
        String codigo = scanner.nextLine().trim();

        if (estoque.remover(codigo)) {
            System.out.println("Produto removido com sucesso.");
        } else {
            System.out.println("Produto nao encontrado.");
        }
    }

    private static void listarEstoqueMinimo() {
        System.out.print("Informe a quantidade minima: ");
        int minimo = lerInteiro();

        if (minimo < 0) {
            System.out.println("O estoque minimo nao pode ser negativo.");
            return;
        }

        System.out.println(estoque.listarAbaixoDoEstoqueMinimo(minimo));
    }

    private static void simularSemana() {
        if (estoque.estaVazia()) {
            System.out.println("Cadastre produtos antes de iniciar a simulacao.");
            return;
        }

        System.out.println("\n===== SIMULACAO SEMANAL =====");
        System.out.println("A simulacao tem sete dias.");
        System.out.println("Informe 0 no codigo para encerrar as movimentacoes do dia.");

        for (int dia = 1; dia <= 7; dia++) {
            System.out.println("\n--- Dia " + dia + " ---");
            System.out.println(estoque.listar());

            while (true) {
                System.out.print("Codigo do produto (0 para proximo dia): ");
                String codigo = scanner.nextLine().trim();

                if (codigo.equals("0")) {
                    break;
                }

                Produto produto = estoque.buscarPorCodigo(codigo);

                if (produto == null) {
                    System.out.println("Produto nao encontrado.");
                    continue;
                }

                System.out.print("Tipo de movimentacao (E = entrada, S = saida): ");
                String tipo = scanner.nextLine().trim().toUpperCase();

                if (!tipo.equals("E") && !tipo.equals("S")) {
                    System.out.println("Tipo invalido.");
                    continue;
                }

                System.out.print("Quantidade movimentada: ");
                int quantidade = lerInteiro();

                if (quantidade <= 0) {
                    System.out.println("A quantidade deve ser maior que zero.");
                    continue;
                }

                int atual = produto.getQuantidade();
                int novaQuantidade;

                if (tipo.equals("E")) {
                    novaQuantidade = atual + quantidade;

                    if (novaQuantidade < atual) {
                        System.out.println("Quantidade excede o limite permitido.");
                        continue;
                    }
                } else {
                    if (quantidade > atual) {
                        System.out.println("Estoque insuficiente. Disponivel: " + atual);
                        continue;
                    }

                    novaQuantidade = atual - quantidade;
                }

                estoque.atualizarQuantidade(codigo, novaQuantidade);

                if (tipo.equals("E")) {
                    System.out.println("Entrada registrada.");
                } else {
                    System.out.println("Saida registrada.");
                }

                System.out.println("Estoque atual de " + produto.getNome()
                        + ": " + produto.getQuantidade());
            }
        }

        System.out.println("\n===== ESTOQUE AO FINAL DA SEMANA =====");
        System.out.println(estoque.listar());
        System.out.println("Simulacao concluida.");
    }

    private static int lerInteiro() {
        while (true) {
            String entrada = scanner.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.print("Informe um numero inteiro valido: ");
            }
        }
    }

    private static double lerDouble() {
        while (true) {
            String entrada = scanner.nextLine().trim().replace(",", ".");

            try {
                double valor = Double.parseDouble(entrada);

                if (Double.isFinite(valor)) {
                    return valor;
                }
            } catch (NumberFormatException e) {
            }

            System.out.print("Informe um preco valido: ");
        }
    }
}

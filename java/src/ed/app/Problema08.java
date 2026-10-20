package ed.app;

import ed.naolineares.ArvoreProdutos;
import ed.naolineares.ArvoreProdutos.Produto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Problema08 {
    private static final String ENTRADA = "dados/produtos.csv";
    private static final String SAIDA = "saidas/problema_08.txt";
    private static final String NL = System.lineSeparator();
    private static final StringBuilder registro = new StringBuilder();

    public static void executar() {
        try {
            List<Produto> produtos = carregarProdutos();

            if (produtos.size() < 30) {
                throw new IllegalStateException(
                        "O arquivo precisa conter pelo menos 30 produtos; encontrados: "
                                + produtos.size()
                );
            }

            ArvoreProdutos arvore = new ArvoreProdutos();
            int inseridos = 0;

            for (Produto produto : produtos) {
                if (arvore.inserir(produto)) {
                    inseridos++;
                }
            }

            if (inseridos < 30) {
                throw new IllegalStateException(
                        "A árvore precisa conter pelo menos 30 nomes distintos."
                );
            }

            java.util.Scanner scanner = new java.util.Scanner(System.in);
            int opcao;

            do {
            Main.limparTela();

                System.out.println("\n===== AUTOCOMPLETE DE PRODUTOS =====");
                System.out.println("Produtos carregados: " + produtos.size());
                System.out.println("Nomes distintos na BST: " + arvore.getTamanho());
                System.out.println("1. Cadastrar produto");
                System.out.println("2. Listar produtos");
                System.out.println("3. Autocomplete por prefixo");
                System.out.println("4. Executar testes e salvar relatório");
                System.out.println("0. Voltar ao menu principal");
                System.out.print("Escolha uma opção: ");

                String entrada = scanner.nextLine().trim();

                try {
                    opcao = Integer.parseInt(entrada);
                } catch (NumberFormatException e) {
                    opcao = -1;
                }

                switch (opcao) {
                    case 1 -> {
                        System.out.print("Código: ");
                        String codigo = scanner.nextLine().trim();

                        boolean codigoExiste = produtos.stream()
                                .anyMatch(p -> p.codigo.equalsIgnoreCase(codigo));

                        if (codigo.isBlank() || codigoExiste) {
                            System.out.println("Código vazio ou já cadastrado.");
                            break;
                        }

                        System.out.print("Nome do produto: ");
                        String nome = scanner.nextLine().trim();

                        if (nome.isBlank()) {
                            System.out.println("O nome não pode ficar vazio.");
                            break;
                        }

                        System.out.print("Quantidade: ");
                        int quantidade;
                        try {
                            quantidade = Integer.parseInt(scanner.nextLine().trim());
                            if (quantidade < 0) {
                                System.out.println("A quantidade não pode ser negativa.");
                                break;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Quantidade inválida.");
                            break;
                        }

                        System.out.print("Preço: ");
                        double preco;
                        try {
                            preco = Double.parseDouble(
                                    scanner.nextLine().trim().replace(',', '.'));
                            if (!Double.isFinite(preco) || preco < 0) {
                                System.out.println("Preço inválido.");
                                break;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Preço inválido.");
                            break;
                        }

                        Produto produto = new Produto(codigo, nome, quantidade, preco);

                        if (arvore.inserir(produto)) {
                            produtos.add(produto);
                            System.out.println("Produto cadastrado na árvore.");
                        } else {
                            System.out.println(
                                    "Já existe um produto com esse nome normalizado.");
                        }
                    }
                    case 2 -> {
                        System.out.println("\n===== PRODUTOS EM ORDEM ALFABÉTICA =====");
                        List<Produto> ordenados = new ArrayList<>(produtos);
                        ordenados.sort((a, b) ->
                                ArvoreProdutos.normalizar(a.nome)
                                        .compareTo(ArvoreProdutos.normalizar(b.nome)));

                        for (Produto produto : ordenados) {
                            System.out.printf(
                                    "%s | %s | Quantidade: %d | Preço: R$ %.2f%n",
                                    produto.codigo, produto.nome,
                                    produto.quantidade, produto.preco);
                        }
                        System.out.println("Total: " + produtos.size());
                    }
                    case 3 -> {
                        System.out.println(
                                "Digite um prefixo (linha vazia para voltar).");
                        while (true) {
                            System.out.print("Prefixo: ");
                            String prefixo = scanner.nextLine().trim();

                            if (prefixo.isEmpty()) {
                                break;
                            }

                            List<Produto> resultados = arvore.autocomplete(prefixo);
                            System.out.println(
                                    "Sugestões encontradas: " + resultados.size());

                            if (resultados.isEmpty()) {
                                System.out.println("Nenhum produto encontrado.");
                            } else {
                                for (Produto produto : resultados) {
                                    System.out.println("  " + produto);
                                }
                            }
                        }
                    }
                    case 4 -> {
                        registro.setLength(0);
                        linha("VERIFICAÇÃO DO ALGORITMO — AUTOCOMPLETE DE PRODUTOS");
                        linha("Estrutura: Árvore Binária de Busca (BST) | Linguagem: Java");
                        linha("Arquivo: " + ENTRADA);
                        linha("Produtos carregados: " + produtos.size());
                        linha("Nomes distintos na árvore: " + arvore.getTamanho());
                        linha("");
                        testarPrefixo(arvore, "Paraf");
                        testarPrefixo(arvore, "Silicone");
                        testarPrefixo(arvore, "Furadeira");
                        testarPrefixo(arvore, "xyz-inexistente");
                        linha("RESUMO:");
                        linha("Base mínima de 30 nomes distintos: atendida");
                        linha("Autocomplete com prefixos existentes e inexistentes: testado");
                        linha("");
                        linha("COMPLEXIDADE:");
                        linha("Inserção: O(h) por produto.");
                        linha("Autocomplete: O(v + k), sem contar comparações de strings.");
                        linha("Espaço: O(n) para a árvore e O(k) para os resultados.");
                        linha("h = altura; v = nós visitados; k = resultados encontrados.");
                        linha("No pior caso, a BST pode visitar O(n) nós.");
                        salvar();
                        System.out.println(registro);
                        System.out.println("Relatório salvo em " + SAIDA);
                    }
                    case 0 -> System.out.println("Voltando ao menu principal.");
                    case -1 -> System.out.println("Digite uma opção numérica válida.");
                    default -> System.out.println("Opção inválida.");
                }
            } while (opcao != 0);

        } catch (IOException | RuntimeException e) {
            System.err.println("Erro no Problema 08: " + e.getMessage());
        }
    }

    private static void testarPrefixo(ArvoreProdutos arvore, String prefixo) {
        List<Produto> resultados = arvore.autocomplete(prefixo);

        linha("AUTOCOMPLETE: \"" + prefixo + "\"");
        linha("Quantidade de sugestões: " + resultados.size());

        if (resultados.isEmpty()) {
            linha("Nenhum produto encontrado.");
        } else {
            for (Produto produto : resultados) {
                linha("  " + produto);
            }
        }

        linha("");
    }

    private static List<Produto> carregarProdutos() throws IOException {
        List<String> linhas = Files.readAllLines(
                Path.of(ENTRADA), StandardCharsets.UTF_8
        );
        List<Produto> produtos = new ArrayList<>();

        for (String linha : linhas) {
            if (linha.isBlank()
                    || linha.toLowerCase(Locale.ROOT).startsWith("codigo,")) {
                continue;
            }

            String[] campos = linha.split(",", 4);

            if (campos.length != 4) {
                throw new IOException("Registro inválido: " + linha);
            }

            try {
                produtos.add(new Produto(
                        campos[0].trim(),
                        campos[1].trim(),
                        Integer.parseInt(campos[2].trim()),
                        Double.parseDouble(campos[3].trim())
                ));
            } catch (NumberFormatException e) {
                throw new IOException("Dados numéricos inválidos: " + linha);
            }
        }

        return produtos;
    }

    private static void linha(String texto) {
        registro.append(texto).append(NL);
    }

    private static void salvar() throws IOException {
        Path caminho = Path.of(SAIDA);
        Files.createDirectories(caminho.getParent());
        Files.writeString(caminho, registro.toString(), StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        executar();
    }
}

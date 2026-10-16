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
        registro.setLength(0);

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

            linha("VERIFICAÇÃO DO ALGORITMO — AUTOCOMPLETE DE PRODUTOS");
            linha("Estrutura: Árvore Binária de Busca (BST) | Linguagem: Java");
            linha("==============================================================");
            linha("");
            linha("ENTRADA:");
            linha("Arquivo: " + ENTRADA);
            linha("Produtos lidos: " + produtos.size());
            linha("Nomes inseridos na árvore: " + inseridos);
            linha("");

            testarPrefixo(arvore, "Paraf");
            testarPrefixo(arvore, "Silicone");
            testarPrefixo(arvore, "Furadeira");
            testarPrefixo(arvore, "xyz-inexistente");

            linha("RESUMO:");
            linha("Base mínima de 30 produtos: atendida");
            linha("Inserção dos nomes na BST: executada");
            linha("Busca por prefixo: testada com resultados e sem resultados");
            linha("Sugestões apresentadas em ordem alfabética.");
            linha("Busca limitada aos ramos que podem conter o prefixo.");
            linha("");
            linha("COMPLEXIDADE:");
            linha("Inserção: O(h) por produto.");
            linha("Autocomplete: O(v + k), desconsiderando o custo das comparações de strings.");
            linha("Espaço: O(n) para a árvore e O(k) para as sugestões.");
            linha("h = altura da árvore; v = nós visitados; k = resultados encontrados.");
            linha("No pior caso, a BST pode ficar degenerada e visitar O(n) nós.");

            salvar();
            System.out.println(registro);
            System.out.println("Resultado salvo em " + SAIDA);
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

package ed.naolineares;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ArvoreProdutos {
    public static class Produto {
        public final String codigo;
        public final String nome;
        public final int quantidade;
        public final double preco;

        public Produto(String codigo, String nome, int quantidade, double preco) {
            this.codigo = codigo;
            this.nome = nome;
            this.quantidade = quantidade;
            this.preco = preco;
        }

        @Override
        public String toString() {
            return codigo + " | " + nome;
        }
    }

    private static class No {
        Produto produto;
        String chave;
        No esquerda;
        No direita;

        No(Produto produto) {
            this.produto = produto;
            this.chave = normalizar(produto.nome);
        }
    }

    private No raiz;
    private int tamanho;

    public static String normalizar(String texto) {
        String semAcentos = Normalizer.normalize(
                texto, Normalizer.Form.NFD
        ).replaceAll("\\p{M}+", "");
        return semAcentos.toLowerCase(Locale.ROOT).trim();
    }

    public boolean inserir(Produto produto) {
        if (produto == null || produto.nome == null || produto.nome.isBlank()) {
            throw new IllegalArgumentException("Produto inválido.");
        }

        int antes = tamanho;
        raiz = inserir(raiz, produto);
        return tamanho > antes;
    }

    private No inserir(No atual, Produto produto) {
        if (atual == null) {
            tamanho++;
            return new No(produto);
        }

        String chave = normalizar(produto.nome);
        int comparacao = chave.compareTo(atual.chave);

        if (comparacao < 0) {
            atual.esquerda = inserir(atual.esquerda, produto);
        } else if (comparacao > 0) {
            atual.direita = inserir(atual.direita, produto);
        }

        return atual;
    }

    public List<Produto> autocomplete(String prefixo) {
        List<Produto> resultado = new ArrayList<>();

        if (prefixo == null || prefixo.isBlank()) {
            return resultado;
        }

        String prefixoNormalizado = normalizar(prefixo);
        String limiteSuperior = prefixoNormalizado + Character.MAX_VALUE;

        buscarPrefixos(
                raiz, prefixoNormalizado, limiteSuperior, resultado
        );

        return resultado;
    }

    private void buscarPrefixos(
            No atual,
            String prefixo,
            String limiteSuperior,
            List<Produto> resultado) {

        if (atual == null) {
            return;
        }

        if (atual.chave.compareTo(prefixo) >= 0) {
            buscarPrefixos(
                    atual.esquerda, prefixo, limiteSuperior, resultado
            );
        }

        if (atual.chave.startsWith(prefixo)) {
            resultado.add(atual.produto);
        }

        if (atual.chave.compareTo(limiteSuperior) <= 0) {
            buscarPrefixos(
                    atual.direita, prefixo, limiteSuperior, resultado
            );
        }
    }

    public int getTamanho() {
        return tamanho;
    }
}


package ed.lineares;

import ed.app.Musica;

public class Lista {
    private No inicio;
    private int tamanho;

    public Lista() {
        inicio = null;
        tamanho = 0;
    }

    public boolean estaVazia() {
        return inicio == null;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void inserirInicio(Musica musica) {
        if (musica == null) {
            throw new IllegalArgumentException("A música não pode ser nula.");
        }

        No novo = new No(musica);
        novo.setProximo(inicio);
        inicio = novo;
        tamanho++;
    }

    public void inserirFim(Musica musica) {
        if (musica == null) {
            throw new IllegalArgumentException("A música não pode ser nula.");
        }

        No novo = new No(musica);

        if (inicio == null) {
            inicio = novo;
        } else {
            No atual = inicio;

            while (atual.getProximo() != null) {
                atual = atual.getProximo();
            }

            atual.setProximo(novo);
        }

        tamanho++;
    }

    public void inserirNaPosicao(Musica musica, int posicao) {
        if (musica == null) {
            throw new IllegalArgumentException("A música não pode ser nula.");
        }

        if (posicao < 0 || posicao > tamanho) {
            throw new IndexOutOfBoundsException("Posição inválida: " + posicao);
        }

        if (posicao == 0) {
            inserirInicio(musica);
            return;
        }

        if (posicao == tamanho) {
            inserirFim(musica);
            return;
        }

        No anterior = inicio;

        for (int i = 0; i < posicao - 1; i++) {
            anterior = anterior.getProximo();
        }

        No novo = new No(musica);
        novo.setProximo(anterior.getProximo());
        anterior.setProximo(novo);
        tamanho++;
    }

    public Musica buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            return null;
        }

        No atual = inicio;

        while (atual != null) {
            if (atual.getMusica().getTitulo().equalsIgnoreCase(titulo)) {
                return atual.getMusica();
            }

            atual = atual.getProximo();
        }

        return null;
    }

    public boolean removerPorTitulo(String titulo) {
        if (titulo == null || titulo.isBlank() || inicio == null) {
            return false;
        }

        if (inicio.getMusica().getTitulo().equalsIgnoreCase(titulo)) {
            inicio = inicio.getProximo();
            tamanho--;
            return true;
        }

        No anterior = inicio;
        No atual = inicio.getProximo();

        while (atual != null) {
            if (atual.getMusica().getTitulo().equalsIgnoreCase(titulo)) {
                anterior.setProximo(atual.getProximo());
                tamanho--;
                return true;
            }

            anterior = atual;
            atual = atual.getProximo();
        }

        return false;
    }

    public String listar() {
        if (estaVazia()) {
            return "Playlist vazia.";
        }

        StringBuilder resultado = new StringBuilder();
        No atual = inicio;
        int posicao = 0;

        while (atual != null) {
            resultado.append(posicao)
                    .append(". ")
                    .append(atual.getMusica())
                    .append(System.lineSeparator());

            atual = atual.getProximo();
            posicao++;
        }

        return resultado.toString().stripTrailing();
    }

    public int calcularDuracaoTotal() {
        int total = 0;
        No atual = inicio;

        while (atual != null) {
            total += atual.getMusica().getDuracao();
            atual = atual.getProximo();
        }

        return total;
    }
}

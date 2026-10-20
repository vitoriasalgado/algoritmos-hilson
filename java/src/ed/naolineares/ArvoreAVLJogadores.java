package ed.naolineares;

import java.util.ArrayList;
import java.util.List;

public class ArvoreAVLJogadores {
    public static class Jogador {
        private final String nome;
        private final int pontuacao;

        public Jogador(String nome, int pontuacao) {
            this.nome = nome;
            this.pontuacao = pontuacao;
        }

        public String getNome() {
            return nome;
        }

        public int getPontuacao() {
            return pontuacao;
        }

        @Override
        public String toString() {
            return nome + " - " + pontuacao + " pontos";
        }
    }

    private static class No {
        Jogador jogador;
        No esquerda;
        No direita;
        int altura = 1;

        No(Jogador jogador) {
            this.jogador = jogador;
        }
    }

    private No raiz;
    private int quantidade;

    private int altura(No no) {
        return no == null ? 0 : no.altura;
    }

    private int fator(No no) {
        return no == null ? 0 : altura(no.esquerda) - altura(no.direita);
    }

    private void atualizarAltura(No no) {
        no.altura = 1 + Math.max(altura(no.esquerda), altura(no.direita));
    }

    private No rotacaoDireita(No y) {
        No x = y.esquerda;
        No t = x.direita;
        x.direita = y;
        y.esquerda = t;
        atualizarAltura(y);
        atualizarAltura(x);
        return x;
    }

    private No rotacaoEsquerda(No x) {
        No y = x.direita;
        No t = y.esquerda;
        y.esquerda = x;
        x.direita = t;
        atualizarAltura(x);
        atualizarAltura(y);
        return y;
    }

    private No balancear(No no) {
        if (no == null) return null;

        atualizarAltura(no);
        int f = fator(no);

        if (f > 1) {
            if (fator(no.esquerda) < 0) {
                no.esquerda = rotacaoEsquerda(no.esquerda);
            }
            return rotacaoDireita(no);
        }

        if (f < -1) {
            if (fator(no.direita) > 0) {
                no.direita = rotacaoDireita(no.direita);
            }
            return rotacaoEsquerda(no);
        }

        return no;
    }

    private int comparar(Jogador a, Jogador b) {
        int resultado = Integer.compare(a.getPontuacao(), b.getPontuacao());
        if (resultado != 0) return resultado;
        return a.getNome().compareToIgnoreCase(b.getNome());
    }

    public boolean inserir(String nome, int pontuacao) {
        if (nome == null || nome.isBlank() || pontuacao < 0) return false;
        if (buscar(nome) != null) return false;

        raiz = inserir(raiz, new Jogador(nome.trim(), pontuacao));
        quantidade++;
        return true;
    }

    private No inserir(No no, Jogador jogador) {
        if (no == null) return new No(jogador);

        if (comparar(jogador, no.jogador) < 0) {
            no.esquerda = inserir(no.esquerda, jogador);
        } else {
            no.direita = inserir(no.direita, jogador);
        }

        return balancear(no);
    }

    public Jogador buscar(String nome) {
        if (nome == null) return null;
        return buscarNome(raiz, nome.trim());
    }

    private Jogador buscarNome(No no, String nome) {
        if (no == null) return null;
        if (no.jogador.getNome().equalsIgnoreCase(nome)) return no.jogador;

        Jogador encontrado = buscarNome(no.esquerda, nome);
        return encontrado != null ? encontrado : buscarNome(no.direita, nome);
    }

    public boolean atualizarPontuacao(String nome, int novaPontuacao) {
        if (nome == null || novaPontuacao < 0) return false;

        Jogador atual = buscar(nome);
        if (atual == null) return false;

        raiz = remover(raiz, atual);
        quantidade--;
        raiz = inserir(raiz, new Jogador(atual.getNome(), novaPontuacao));
        quantidade++;
        return true;
    }

    public boolean remover(String nome) {
        Jogador jogador = buscar(nome);
        if (jogador == null) return false;

        raiz = remover(raiz, jogador);
        quantidade--;
        return true;
    }

    private No remover(No no, Jogador jogador) {
        if (no == null) return null;

        int cmp = comparar(jogador, no.jogador);

        if (cmp < 0) {
            no.esquerda = remover(no.esquerda, jogador);
        } else if (cmp > 0) {
            no.direita = remover(no.direita, jogador);
        } else {
            if (no.esquerda == null) return no.direita;
            if (no.direita == null) return no.esquerda;

            No sucessor = menor(no.direita);
            no.jogador = sucessor.jogador;
            no.direita = remover(no.direita, sucessor.jogador);
        }

        return balancear(no);
    }

    private No menor(No no) {
        while (no.esquerda != null) no = no.esquerda;
        return no;
    }

    public List<Jogador> listarRanking() {
        List<Jogador> lista = new ArrayList<>();
        listarDecrescente(raiz, lista);
        return lista;
    }

    private void listarDecrescente(No no, List<Jogador> lista) {
        if (no == null) return;
        listarDecrescente(no.direita, lista);
        lista.add(no.jogador);
        listarDecrescente(no.esquerda, lista);
    }

    public List<Jogador> topK(int k) {
        List<Jogador> ranking = listarRanking();
        return new ArrayList<>(ranking.subList(0, Math.min(Math.max(k, 0), ranking.size())));
    }

    public int altura() {
        return altura(raiz);
    }

    public int quantidade() {
        return quantidade;
    }

    public boolean estaBalanceada() {
        return verificarBalanceamento(raiz) != -1;
    }

    private int verificarBalanceamento(No no) {
        if (no == null) return 0;

        int esquerda = verificarBalanceamento(no.esquerda);
        int direita = verificarBalanceamento(no.direita);

        if (esquerda == -1 || direita == -1 || Math.abs(esquerda - direita) > 1) {
            return -1;
        }

        return 1 + Math.max(esquerda, direita);
    }
}

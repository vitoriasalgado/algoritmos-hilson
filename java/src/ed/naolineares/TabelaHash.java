package ed.naolineares;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class TabelaHash<K, V> {

    private static class Entrada<K, V> {
        private final K chave;
        private V valor;

        private Entrada(K chave, V valor) {
            this.chave = chave;
            this.valor = valor;
        }
    }

    private final List<Entrada<K, V>>[] tabela;
    private int tamanho;
    private int colisoes;

    @SuppressWarnings("unchecked")
    public TabelaHash(int capacidade) {
        if (capacidade <= 0) {
            throw new IllegalArgumentException("A capacidade deve ser positiva.");
        }

        tabela = (List<Entrada<K, V>>[]) new List<?>[capacidade];

        for (int i = 0; i < capacidade; i++) {
            tabela[i] = new ArrayList<>();
        }
    }

    private int indice(K chave) {
        if (chave == null) {
            throw new IllegalArgumentException("A chave não pode ser nula.");
        }

        return Math.floorMod(chave.hashCode(), tabela.length);
    }

    public void inserir(K chave, V valor) {
        if (valor == null) {
            throw new IllegalArgumentException("O valor não pode ser nulo.");
        }

        int indice = indice(chave);
        List<Entrada<K, V>> lista = tabela[indice];

        for (Entrada<K, V> entrada : lista) {
            if (entrada.chave.equals(chave)) {
                entrada.valor = valor;
                return;
            }
        }

        if (!lista.isEmpty()) {
            colisoes++;
        }

        lista.add(new Entrada<>(chave, valor));
        tamanho++;
    }

    public V buscar(K chave) {
        int indice = indice(chave);

        for (Entrada<K, V> entrada : tabela[indice]) {
            if (entrada.chave.equals(chave)) {
                return entrada.valor;
            }
        }

        return null;
    }

    public boolean contemChave(K chave) {
        return buscar(chave) != null;
    }

    public V remover(K chave) {
        int indice = indice(chave);
        List<Entrada<K, V>> lista = tabela[indice];

        for (int i = 0; i < lista.size(); i++) {
            Entrada<K, V> entrada = lista.get(i);

            if (entrada.chave.equals(chave)) {
                lista.remove(i);
                tamanho--;
                return entrada.valor;
            }
        }

        return null;
    }

    public int tamanho() {
        return tamanho;
    }

    public int capacidade() {
        return tabela.length;
    }

    public int totalColisoes() {
        return colisoes;
    }

    public void percorrer(BiConsumer<K, V> acao) {
        if (acao == null) {
            throw new IllegalArgumentException("A ação não pode ser nula.");
        }

        for (List<Entrada<K, V>> lista : tabela) {
            for (Entrada<K, V> entrada : lista) {
                acao.accept(entrada.chave, entrada.valor);
            }
        }
    }

    public void listar() {
        for (List<Entrada<K, V>> lista : tabela) {
            for (Entrada<K, V> entrada : lista) {
                System.out.println(entrada.chave + " -> " + entrada.valor);
            }
        }
    }
}
package ed.app;

public class ListaProdutos {
    private class No {
        private Produto produto;
        private No proximo;

        public No(Produto produto) {
            this.produto = produto;
            this.proximo = null;
        }
    }

    private No inicio;
    private int tamanho;

    public ListaProdutos() {
        inicio = null;
        tamanho = 0;
    }

    public boolean estaVazia() {
        return inicio == null;
    }

    public int getTamanho() {
        return tamanho;
    }

    public boolean cadastrar(Produto produto) {
        if (produto == null || buscarPorCodigo(produto.getCodigo()) != null) {
            return false;
        }

        No novo = new No(produto);

        if (inicio == null) {
            inicio = novo;
        } else {
            No atual = inicio;

            while (atual.proximo != null) {
                atual = atual.proximo;
            }

            atual.proximo = novo;
        }

        tamanho++;
        return true;
    }

    public Produto buscarPorCodigo(String codigo) {
        No atual = inicio;

        while (atual != null) {
            if (atual.produto.getCodigo().equalsIgnoreCase(codigo)) {
                return atual.produto;
            }

            atual = atual.proximo;
        }

        return null;
    }

    public Produto buscarPorNome(String nome) {
        No atual = inicio;

        while (atual != null) {
            if (atual.produto.getNome().equalsIgnoreCase(nome)) {
                return atual.produto;
            }

            atual = atual.proximo;
        }

        return null;
    }

    public boolean remover(String codigo) {
        if (inicio == null) {
            return false;
        }

        if (inicio.produto.getCodigo().equalsIgnoreCase(codigo)) {
            inicio = inicio.proximo;
            tamanho--;
            return true;
        }

        No atual = inicio;

        while (atual.proximo != null) {
            if (atual.proximo.produto.getCodigo().equalsIgnoreCase(codigo)) {
                atual.proximo = atual.proximo.proximo;
                tamanho--;
                return true;
            }

            atual = atual.proximo;
        }

        return false;
    }

    public boolean atualizarQuantidade(String codigo, int quantidade) {
        Produto produto = buscarPorCodigo(codigo);

        if (produto == null || quantidade < 0) {
            return false;
        }

        produto.setQuantidade(quantidade);
        return true;
    }

    public String listar() {
        StringBuilder resultado = new StringBuilder();
        No atual = inicio;

        while (atual != null) {
            resultado.append(atual.produto).append("\n");
            atual = atual.proximo;
        }

        if (resultado.length() == 0) {
            return "Nenhum produto cadastrado.";
        }

        return resultado.toString();
    }

    public String listarAbaixoDoEstoqueMinimo(int minimo) {
        StringBuilder resultado = new StringBuilder();
        No atual = inicio;

        while (atual != null) {
            if (atual.produto.getQuantidade() < minimo) {
                resultado.append(atual.produto).append("\n");
            }

            atual = atual.proximo;
        }

        if (resultado.length() == 0) {
            return "Nenhum produto abaixo do estoque minimo.";
        }

        return resultado.toString();
    }
}

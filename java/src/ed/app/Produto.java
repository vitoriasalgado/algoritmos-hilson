package ed.app;

public class Produto {
    private String codigo;
    private String nome;
    private int quantidade;
    private double preco;

    public Produto(String codigo, String nome, int quantidade, double preco) {
        this.codigo = codigo;
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    @Override
    public String toString() {
        return "Código: " + codigo
                + " | Nome: " + nome
                + " | Quantidade: " + quantidade
                + " | Preço unitário: R$ " + String.format("%.2f", preco);
    }
}

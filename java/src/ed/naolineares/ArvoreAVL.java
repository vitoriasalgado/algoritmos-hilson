package ed.naolineares;

import java.util.ArrayList;
import java.util.List;

public class ArvoreAVL {
    public static class Funcionario {
        private final String cpf;
        private final String nome;
        private final String cargo;

        public Funcionario(String cpf, String nome, String cargo) {
            this.cpf = normalizarCpf(cpf);
            this.nome = nome;
            this.cargo = cargo;
        }

        private static String normalizarCpf(String cpf) {
            if (cpf == null) {
                throw new IllegalArgumentException("CPF não pode ser nulo.");
            }
            String valor = cpf.replaceAll("\\D", "");
            if (!valor.matches("\\d{11}")) {
                throw new IllegalArgumentException("CPF deve conter 11 dígitos.");
            }
            return valor;
        }

        public String getCpf() {
            return cpf;
        }

        public String getNome() {
            return nome;
        }

        public String getCargo() {
            return cargo;
        }

        @Override
        public String toString() {
            return "CPF: " + cpf + " | Nome: " + nome + " | Cargo: " + cargo;
        }
    }

    private static class No {
        Funcionario funcionario;
        No esquerda;
        No direita;
        int altura = 1;

        No(Funcionario funcionario) {
            this.funcionario = funcionario;
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
        No subarvore = x.direita;
        x.direita = y;
        y.esquerda = subarvore;
        atualizarAltura(y);
        atualizarAltura(x);
        return x;
    }

    private No rotacaoEsquerda(No x) {
        No y = x.direita;
        No subarvore = y.esquerda;
        y.esquerda = x;
        x.direita = subarvore;
        atualizarAltura(x);
        atualizarAltura(y);
        return y;
    }

    private No balancear(No no) {
        if (no == null) {
            return null;
        }

        atualizarAltura(no);
        int fator = fator(no);

        if (fator > 1) {
            if (fator(no.esquerda) < 0) {
                no.esquerda = rotacaoEsquerda(no.esquerda);
            }
            return rotacaoDireita(no);
        }

        if (fator < -1) {
            if (fator(no.direita) > 0) {
                no.direita = rotacaoDireita(no.direita);
            }
            return rotacaoEsquerda(no);
        }

        return no;
    }

    public void inserir(String cpf, String nome, String cargo) {
        Funcionario funcionario = new Funcionario(cpf, nome, cargo);
        if (buscar(funcionario.getCpf()) != null) {
            throw new IllegalArgumentException("CPF já cadastrado: " + funcionario.getCpf());
        }
        raiz = inserir(raiz, funcionario);
        quantidade++;
    }

    private No inserir(No no, Funcionario funcionario) {
        if (no == null) {
            return new No(funcionario);
        }

        if (funcionario.getCpf().compareTo(no.funcionario.getCpf()) < 0) {
            no.esquerda = inserir(no.esquerda, funcionario);
        } else {
            no.direita = inserir(no.direita, funcionario);
        }

        return balancear(no);
    }

    public Funcionario buscar(String cpf) {
        String chave = normalizarCpf(cpf);
        No atual = raiz;

        while (atual != null) {
            int comparacao = chave.compareTo(atual.funcionario.getCpf());
            if (comparacao == 0) {
                return atual.funcionario;
            }
            atual = comparacao < 0 ? atual.esquerda : atual.direita;
        }

        return null;
    }

    private String normalizarCpf(String cpf) {
        return new Funcionario(cpf, "", "").getCpf();
    }

    public boolean remover(String cpf) {
        String chave = normalizarCpf(cpf);
        if (buscar(chave) == null) {
            return false;
        }
        raiz = remover(raiz, chave);
        quantidade--;
        return true;
    }

    private No remover(No no, String cpf) {
        if (no == null) {
            return null;
        }

        int comparacao = cpf.compareTo(no.funcionario.getCpf());

        if (comparacao < 0) {
            no.esquerda = remover(no.esquerda, cpf);
        } else if (comparacao > 0) {
            no.direita = remover(no.direita, cpf);
        } else {
            if (no.esquerda == null) {
                return no.direita;
            }
            if (no.direita == null) {
                return no.esquerda;
            }

            No sucessor = menorNo(no.direita);
            no.funcionario = sucessor.funcionario;
            no.direita = remover(no.direita, sucessor.funcionario.getCpf());
        }

        return balancear(no);
    }

    private No menorNo(No no) {
        while (no.esquerda != null) {
            no = no.esquerda;
        }
        return no;
    }

    public int altura() {
        return altura(raiz);
    }

    public int fatorBalanceamentoRaiz() {
        return fator(raiz);
    }

    public int quantidade() {
        return quantidade;
    }

    public boolean estaBalanceada() {
        return verificarBalanceamento(raiz);
    }

    private boolean verificarBalanceamento(No no) {
        if (no == null) {
            return true;
        }
        return Math.abs(fator(no)) <= 1
                && verificarBalanceamento(no.esquerda)
                && verificarBalanceamento(no.direita);
    }

    public List<Funcionario> listarEmOrdem() {
        List<Funcionario> lista = new ArrayList<>();
        listarEmOrdem(raiz, lista);
        return lista;
    }

    private void listarEmOrdem(No no, List<Funcionario> lista) {
        if (no == null) {
            return;
        }
        listarEmOrdem(no.esquerda, lista);
        lista.add(no.funcionario);
        listarEmOrdem(no.direita, lista);
    }
}

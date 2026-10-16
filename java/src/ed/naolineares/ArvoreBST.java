package ed.naolineares;

public class ArvoreBST {
    public static class Aluno {
        public final int matricula;
        public final String nome;
        public final String curso;

        public Aluno(int matricula, String nome, String curso) {
            this.matricula = matricula;
            this.nome = nome;
            this.curso = curso;
        }

        @Override
        public String toString() {
            return matricula + " | " + nome + " | " + curso;
        }
    }

    private static class No {
        Aluno aluno;
        No esquerda;
        No direita;

        No(Aluno aluno) {
            this.aluno = aluno;
        }
    }

    private No raiz;
    private int tamanho;

    public boolean inserir(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno inválido.");
        }
        int antes = tamanho;
        raiz = inserir(raiz, aluno);
        return tamanho > antes;
    }

    private No inserir(No atual, Aluno aluno) {
        if (atual == null) {
            tamanho++;
            return new No(aluno);
        }
        if (aluno.matricula < atual.aluno.matricula) {
            atual.esquerda = inserir(atual.esquerda, aluno);
        } else if (aluno.matricula > atual.aluno.matricula) {
            atual.direita = inserir(atual.direita, aluno);
        }
        return atual;
    }

    public Aluno buscar(int matricula) {
        No atual = raiz;
        while (atual != null) {
            if (matricula == atual.aluno.matricula) {
                return atual.aluno;
            }
            atual = matricula < atual.aluno.matricula
                    ? atual.esquerda : atual.direita;
        }
        return null;
    }

    public boolean remover(int matricula) {
        if (buscar(matricula) == null) {
            return false;
        }
        raiz = remover(raiz, matricula);
        tamanho--;
        return true;
    }

    private No remover(No atual, int matricula) {
        if (atual == null) {
            return null;
        }
        if (matricula < atual.aluno.matricula) {
            atual.esquerda = remover(atual.esquerda, matricula);
        } else if (matricula > atual.aluno.matricula) {
            atual.direita = remover(atual.direita, matricula);
        } else {
            if (atual.esquerda == null) {
                return atual.direita;
            }
            if (atual.direita == null) {
                return atual.esquerda;
            }
            No sucessor = menor(atual.direita);
            atual.aluno = sucessor.aluno;
            atual.direita = remover(atual.direita, sucessor.aluno.matricula);
        }
        return atual;
    }

    private No menor(No atual) {
        while (atual.esquerda != null) {
            atual = atual.esquerda;
        }
        return atual;
    }

    public String percursoEmOrdem() {
        StringBuilder resultado = new StringBuilder();
        percursoEmOrdem(raiz, resultado);
        return resultado.toString();
    }

    private void percursoEmOrdem(No atual, StringBuilder resultado) {
        if (atual == null) {
            return;
        }
        percursoEmOrdem(atual.esquerda, resultado);
        resultado.append(atual.aluno).append(System.lineSeparator());
        percursoEmOrdem(atual.direita, resultado);
    }

    public int getTamanho() {
        return tamanho;
    }
}

package ed.app;

import ed.naolineares.ArvoreBST;
import ed.naolineares.ArvoreBST.Aluno;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Problema07 {
    private static final String ARQUIVO = "dados/alunos.csv";
    private static final String SAIDA = "saidas/problema_07.txt";
    private static final String NL = System.lineSeparator();
    private static final StringBuilder registro = new StringBuilder();

    public static void executar() {
        registro.setLength(0);
        ArvoreBST arvore = new ArvoreBST();

        try {
            List<Aluno> alunos = carregarAlunos();
            if (alunos.size() < 20) {
                throw new IllegalStateException("O CSV precisa conter pelo menos 20 alunos.");
            }

            int inseridos = 0;
            for (Aluno aluno : alunos) {
                if (arvore.inserir(aluno)) {
                    inseridos++;
                }
            }

            linha("VERIFICAÇÃO DO ALGORITMO — CADASTRO E BUSCA DE ALUNOS");
            linha("Estrutura: Árvore Binária de Busca (BST) | Linguagem: Java");
            linha("========================================================================");
            linha("");
            linha("ENTRADA:");
            linha("Arquivo: " + ARQUIVO);
            linha("Registros lidos: " + alunos.size());
            linha("Alunos cadastrados na árvore: " + inseridos);
            linha("");

            Aluno existente = alunos.get(0);
            Aluno encontrado = arvore.buscar(existente.matricula);
            linha("BUSCA:");
            linha("Matrícula " + existente.matricula + ": "
                    + (encontrado == null ? "não encontrada" : "aluno encontrado — " + encontrado.nome));
            int matriculaInexistente = Integer.MIN_VALUE;
            while (arvore.buscar(matriculaInexistente) != null) {
                matriculaInexistente++;
            }
            linha("Matrícula inexistente " + matriculaInexistente + ": "
                    + (arvore.buscar(matriculaInexistente) == null ? "não encontrada, conforme esperado" : "encontrada"));
            linha("");

            linha("REMOÇÃO:");
            boolean removido = arvore.remover(existente.matricula);
            linha("Matrícula " + existente.matricula + ": "
                    + (removido ? "removida com sucesso" : "não removida"));
            linha("Busca após remoção: "
                    + (arvore.buscar(existente.matricula) == null ? "matrícula não encontrada, conforme esperado" : "matrícula ainda encontrada"));
            linha("Total após remoção: " + arvore.getTamanho() + " alunos");
            linha("");

            linha("PERCURSO EM ORDEM — MATRÍCULAS CRESCENTES:");
            linha(arvore.percursoEmOrdem().stripTrailing());
            linha("");
            linha("RESUMO:");
            linha("Cadastro de pelo menos 20 alunos: " + (inseridos >= 20 ? "atendido" : "não atendido"));
            linha("Busca de matrícula existente e inexistente: testadas");
            linha("Remoção e verificação após remoção: testadas");
            linha("Percurso em ordem: executado");
            linha("A listagem é apresentada em ordem crescente de matrícula.");
            linha("");
            linha("COMPLEXIDADE:");
            linha("Inserção: O(h).");
            linha("Busca: O(h).");
            linha("Remoção: O(h).");
            linha("Percurso em ordem: O(n).");
            linha("Espaço: O(n), para n alunos.");
            linha("h representa a altura da árvore; no pior caso, h = n.");

            salvar();
            System.out.println(registro);
            System.out.println("Resultado salvo em " + SAIDA);
        } catch (IOException | RuntimeException e) {
            System.err.println("Erro no Problema 07: " + e.getMessage());
        }
    }

    private static List<Aluno> carregarAlunos() throws IOException {
        Path caminho = Path.of(ARQUIVO);
        List<Aluno> alunos = new ArrayList<>();
        List<String> linhas = Files.readAllLines(caminho, StandardCharsets.UTF_8);

        for (String linha : linhas) {
            if (linha.isBlank() || linha.toLowerCase().startsWith("matricula,")) {
                continue;
            }
            String[] campos = linha.split(",", 3);
            if (campos.length != 3) {
                throw new IOException("Registro inválido: " + linha);
            }
            try {
                alunos.add(new Aluno(
                        Integer.parseInt(campos[0].trim()),
                        campos[1].trim(),
                        campos[2].trim()));
            } catch (NumberFormatException e) {
                throw new IOException("Matrícula inválida: " + linha);
            }
        }
        return alunos;
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

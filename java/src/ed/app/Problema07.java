package ed.app;

import ed.naolineares.ArvoreBST;
import ed.naolineares.ArvoreBST.Aluno;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Problema07 {
    private static final String ENTRADA = "dados/alunos.csv";
    private static final String SAIDA = "saidas/problema_07.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static ArvoreBST arvore;
    private static int opcao;

    public static void executar() {
        try {
            arvore = new ArvoreBST();
            List<Aluno> alunos = carregarAlunos();

            for (Aluno aluno : alunos) {
                arvore.inserir(aluno);
            }

            System.out.println("\n===== CADASTRO E BUSCA DE ALUNOS =====");
            System.out.println("Alunos carregados do CSV: " + arvore.getTamanho());

            do {
            Main.limparTela();

                System.out.println("\n1. Cadastrar aluno");
                System.out.println("2. Listar alunos por matrícula");
                System.out.println("3. Buscar aluno por matrícula");
                System.out.println("4. Remover aluno por matrícula");
                System.out.println("5. Demonstrar testes do algoritmo");
                System.out.println("0. Voltar ao menu principal");
                System.out.print("Escolha uma opção: ");

                opcao = lerInteiro();

                switch (opcao) {
                    case 1 -> cadastrarAluno();
                    case 2 -> listarAlunos();
                    case 3 -> buscarAluno();
                    case 4 -> removerAluno();
                    case 5 -> demonstrarTestes();
                    case 0 -> System.out.println("Voltando ao menu principal.");
                    default -> System.out.println("Opção inválida.");
                }
            } while (opcao != 0);

        } catch (IOException | RuntimeException e) {
            System.err.println("Erro no Problema 07: " + e.getMessage());
        }
    }

    private static void cadastrarAluno() {
        System.out.println("\n===== CADASTRAR ALUNO =====");
        System.out.print("Matrícula: ");
        int matricula = lerInteiro();

        if (arvore.buscar(matricula) != null) {
            System.out.println("Já existe um aluno com essa matrícula.");
            return;
        }

        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();

        System.out.print("Curso: ");
        String curso = scanner.nextLine().trim();

        if (nome.isBlank() || curso.isBlank()) {
            System.out.println("Nome e curso são obrigatórios.");
            return;
        }

        arvore.inserir(new Aluno(matricula, nome, curso));
        System.out.println("Aluno cadastrado com sucesso.");
    }

    private static void listarAlunos() {
        System.out.println("\n===== ALUNOS POR MATRÍCULA =====");
        String alunos = arvore.percursoEmOrdem();

        if (alunos.isBlank()) {
            System.out.println("Nenhum aluno cadastrado.");
        } else {
            System.out.print(alunos);
        }

        System.out.println("Total de alunos: " + arvore.getTamanho());
    }

    private static void buscarAluno() {
        System.out.println("\n===== BUSCAR ALUNO =====");
        System.out.print("Digite a matrícula: ");
        int matricula = lerInteiro();

        Aluno aluno = arvore.buscar(matricula);

        if (aluno == null) {
            System.out.println("Aluno não encontrado.");
        } else {
            System.out.println("Aluno encontrado: " + aluno);
        }
    }

    private static void removerAluno() {
        System.out.println("\n===== REMOVER ALUNO =====");
        System.out.print("Digite a matrícula: ");
        int matricula = lerInteiro();

        if (arvore.remover(matricula)) {
            System.out.println("Aluno removido com sucesso.");
        } else {
            System.out.println("Matrícula não encontrada.");
        }
    }

    private static void demonstrarTestes() throws IOException {
        StringBuilder relatorio = new StringBuilder();
        int matriculaExistente = arvore.getTamanho() > 0
                ? obterPrimeiraMatricula()
                : Integer.MIN_VALUE;

        relatorio.append("VERIFICAÇÃO DO ALGORITMO — CADASTRO E BUSCA DE ALUNOS")
                .append(System.lineSeparator());
        relatorio.append("Estrutura: Árvore Binária de Busca (BST) | Linguagem: Java")
                .append(System.lineSeparator());
        relatorio.append("Arquivo inicial: ").append(ENTRADA)
                .append(System.lineSeparator());
        relatorio.append("Alunos atualmente cadastrados: ")
                .append(arvore.getTamanho()).append(System.lineSeparator());

        boolean buscaExistente = arvore.buscar(matriculaExistente) != null;
        boolean buscaInexistente = arvore.buscar(Integer.MIN_VALUE) == null;

        relatorio.append("Busca de matrícula existente: ")
                .append(buscaExistente ? "aprovada" : "falhou")
                .append(System.lineSeparator());
        relatorio.append("Busca de matrícula inexistente: ")
                .append(buscaInexistente ? "aprovada" : "falhou")
                .append(System.lineSeparator());
        relatorio.append("Listagem em ordem crescente:").append(System.lineSeparator());
        relatorio.append(arvore.percursoEmOrdem());

        if (matriculaExistente != Integer.MIN_VALUE) {
            Aluno removido = arvore.buscar(matriculaExistente);
            boolean removeu = arvore.remover(matriculaExistente);
            boolean naoExisteMais = arvore.buscar(matriculaExistente) == null;

            if (removeu) {
                arvore.inserir(removido);
            }

            relatorio.append("Remoção de matrícula existente: ")
                    .append(removeu ? "aprovada" : "falhou")
                    .append(System.lineSeparator());
            relatorio.append("Busca após remoção: ")
                    .append(naoExisteMais ? "aprovada" : "falhou")
                    .append(System.lineSeparator());
            relatorio.append("Aluno restaurado após o teste: ")
                    .append(removido).append(System.lineSeparator());
        } else {
            relatorio.append("Remoção: não testada, árvore vazia.")
                    .append(System.lineSeparator());
        }

        relatorio.append("Inserção: O(h) por operação.")
                .append(System.lineSeparator());
        relatorio.append("Busca: O(h).").append(System.lineSeparator());
        relatorio.append("Remoção: O(h).").append(System.lineSeparator());
        relatorio.append("Percurso em ordem: O(n).").append(System.lineSeparator());
        relatorio.append("Espaço: O(n).").append(System.lineSeparator());
        relatorio.append("h = altura da árvore; n = número de alunos.")
                .append(System.lineSeparator());
        relatorio.append("No pior caso, h = n.")
                .append(System.lineSeparator());

        Path caminho = Path.of(SAIDA);
        Files.createDirectories(caminho.getParent());
        Files.writeString(caminho, relatorio.toString(), StandardCharsets.UTF_8);

        System.out.println("\n===== DEMONSTRAÇÃO DOS TESTES =====");
        System.out.print(relatorio);
        System.out.println("Relatório salvo em " + SAIDA);
    }

    private static int obterPrimeiraMatricula() {
        String percurso = arvore.percursoEmOrdem();
        String primeiraLinha = percurso.lines().findFirst().orElse("");
        int separador = primeiraLinha.indexOf(" | ");

        if (separador < 0) {
            return Integer.MIN_VALUE;
        }

        return Integer.parseInt(primeiraLinha.substring(0, separador).trim());
    }

    private static List<Aluno> carregarAlunos() throws IOException {
        List<String> linhas = Files.readAllLines(
                Path.of(ENTRADA), StandardCharsets.UTF_8
        );
        List<Aluno> alunos = new ArrayList<>();

        for (String linha : linhas) {
            if (linha.isBlank()
                    || linha.toLowerCase().startsWith("matricula,")) {
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
                        campos[2].trim()
                ));
            } catch (NumberFormatException e) {
                throw new IOException("Matrícula inválida: " + linha);
            }
        }

        if (alunos.size() < 20) {
            throw new IOException(
                    "O arquivo precisa conter pelo menos 20 alunos. Encontrados: "
                            + alunos.size()
            );
        }

        return alunos;
    }

    private static int lerInteiro() {
        while (true) {
            String entrada = scanner.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.print("Digite um número inteiro válido: ");
            }
        }
    }

    public static void main(String[] args) {
        executar();
    }
}

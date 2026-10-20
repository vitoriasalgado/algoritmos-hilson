package ed.app;

import ed.naolineares.ArvoreAVL;
import ed.naolineares.ArvoreAVL.Funcionario;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Problema09 {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String SAIDA = "saidas/problema_09.txt";

    public static void executar() {
        ArvoreAVL arvore = new ArvoreAVL();

        String[][] iniciais = {
            {"52998224725", "Ana Souza", "Analista de RH"},
            {"11144477735", "Bruno Lima", "Desenvolvedor"},
            {"98765432100", "Carla Mendes", "Gerente"},
            {"12345678909", "Daniel Costa", "Assistente Administrativo"},
            {"45678912300", "Eduarda Alves", "Analista Financeira"},
            {"32165498700", "Felipe Santos", "Técnico de Suporte"},
            {"65432198700", "Gabriela Rocha", "Coordenadora"},
            {"78912345600", "Henrique Martins", "Analista de Sistemas"},
            {"14725836900", "Isabela Ribeiro", "Recrutadora"},
            {"25836914700", "João Oliveira", "Desenvolvedor"},
            {"36914725800", "Karen Ferreira", "Contadora"},
            {"74185296300", "Lucas Carvalho", "Administrador"},
            {"85296374100", "Mariana Gomes", "Analista de RH"},
            {"96374185200", "Nicolas Barros", "Técnico de Redes"},
            {"15935725800", "Olivia Nunes", "Assistente Financeira"},
            {"35715925800", "Paulo Azevedo", "Gerente de Projetos"},
            {"75315925800", "Rafaela Dias", "Designer"},
            {"95175325800", "Samuel Freitas", "Desenvolvedor"},
            {"15975325800", "Talita Moraes", "Analista de Dados"},
            {"35795125800", "Ubirajara Lopes", "Supervisor"},
            {"75395125800", "Valentina Castro", "Psicóloga"},
            {"95135725800", "Wesley Pires", "Técnico de Segurança"},
            {"15925835700", "Yasmin Cardoso", "Assistente de RH"},
            {"25815935700", "André Monteiro", "Analista de Sistemas"},
            {"35725815900", "Bianca Teixeira", "Secretária"},
            {"85235715900", "Caio Batista", "Engenheiro"},
            {"25835715900", "Débora Farias", "Analista de Qualidade"},
            {"35785215900", "Enzo Moreira", "Estagiário"},
            {"75325815900", "Fernanda Reis", "Coordenadora de RH"},
            {"95125835700", "Gustavo Vieira", "Diretor"}
        };

        for (String[] f : iniciais) {
            arvore.inserir(f[0], f[1], f[2]);
        }

        int opcao;
        do {
            Main.limparTela();

            System.out.println("\n===== ÍNDICE DE CPF — ÁRVORE AVL =====");
            System.out.println("Funcionários no índice: " + arvore.quantidade());
            System.out.println("1. Cadastrar funcionário");
            System.out.println("2. Buscar funcionário por CPF");
            System.out.println("3. Remover funcionário por CPF");
            System.out.println("4. Listar funcionários por CPF");
            System.out.println("5. Consultar altura e balanceamento");
            System.out.println("6. Executar testes e salvar relatório");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");

            opcao = lerInteiro();

            switch (opcao) {
                case 1 -> cadastrar(arvore);
                case 2 -> buscar(arvore);
                case 3 -> remover(arvore);
                case 4 -> listar(arvore);
                case 5 -> mostrarEstado(arvore);
                case 6 -> testarESalvar(arvore);
                case 0 -> System.out.println("Voltando ao menu principal.");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void cadastrar(ArvoreAVL arvore) {
        System.out.println("\n===== CADASTRO DE FUNCIONÁRIO =====");
        System.out.print("CPF: ");
        String cpf = scanner.nextLine().trim();
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Cargo: ");
        String cargo = scanner.nextLine().trim();

        if (nome.isBlank() || cargo.isBlank()) {
            System.out.println("Nome e cargo são obrigatórios.");
            return;
        }

        try {
            int antes = arvore.quantidade();
            arvore.inserir(cpf, nome, cargo);

            if (arvore.quantidade() > antes) {
                System.out.println("Funcionário cadastrado.");
            } else {
                System.out.println("CPF duplicado ou funcionário não inserido.");
            }
        } catch (RuntimeException e) {
            System.out.println("Não foi possível cadastrar: " + e.getMessage());
        }
    }

    private static void buscar(ArvoreAVL arvore) {
        System.out.print("CPF para buscar: ");
        String cpf = scanner.nextLine().trim();

        try {
            Funcionario funcionario = arvore.buscar(cpf);
            if (funcionario == null) {
                System.out.println("Funcionário não encontrado.");
            } else {
                System.out.println("Funcionário encontrado: " + funcionario);
            }
        } catch (RuntimeException e) {
            System.out.println("Erro na busca: " + e.getMessage());
        }
    }

    private static void remover(ArvoreAVL arvore) {
        System.out.print("CPF para remover: ");
        String cpf = scanner.nextLine().trim();

        try {
            boolean removido = arvore.remover(cpf);
            System.out.println(removido
                    ? "Funcionário removido."
                    : "CPF não encontrado.");
            mostrarEstado(arvore);
        } catch (RuntimeException e) {
            System.out.println("Erro na remoção: " + e.getMessage());
        }
    }

    private static void listar(ArvoreAVL arvore) {
        System.out.println("\n===== FUNCIONÁRIOS EM ORDEM DE CPF =====");
        List<Funcionario> funcionarios = arvore.listarEmOrdem();

        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
            return;
        }

        for (Funcionario funcionario : funcionarios) {
            System.out.println(funcionario);
        }
        System.out.println("Total: " + funcionarios.size());
    }

    private static void mostrarEstado(ArvoreAVL arvore) {
        System.out.println("\n===== ESTADO DA ÁRVORE AVL =====");
        System.out.println("Quantidade: " + arvore.quantidade());
        System.out.println("Altura: " + arvore.altura());
        System.out.println("Fator de balanceamento da raiz: "
                + arvore.fatorBalanceamentoRaiz());
        System.out.println("Árvore balanceada: " + arvore.estaBalanceada());
    }

    private static void testarESalvar(ArvoreAVL arvore) {
        StringBuilder relatorio = new StringBuilder();

        relatorio.append("PROBLEMA 09 — ÍNDICE DE CPF COM ÁRVORE AVL\n");
        relatorio.append("Funcionários no índice: ")
                .append(arvore.quantidade()).append('\n');
        relatorio.append("Altura: ").append(arvore.altura()).append('\n');
        relatorio.append("Fator de balanceamento da raiz: ")
                .append(arvore.fatorBalanceamentoRaiz()).append('\n');
        relatorio.append("Árvore balanceada: ")
                .append(arvore.estaBalanceada()).append("\n\n");

        Funcionario encontrado = arvore.buscar("52998224725");
        relatorio.append("Busca de CPF de teste: ")
                .append(encontrado == null ? "não encontrado" : "encontrado")
                .append('\n');

        Funcionario inexistente = arvore.buscar("00000000000");
        relatorio.append("Busca de CPF inexistente: ")
                .append(inexistente == null ? "não encontrado, conforme esperado"
                        : "encontrado inesperadamente")
                .append('\n');

        relatorio.append("\nFUNCIONÁRIOS EM ORDEM DE CPF:\n");
        for (Funcionario funcionario : arvore.listarEmOrdem()) {
            relatorio.append(funcionario).append('\n');
        }

        relatorio.append("\nCOMPLEXIDADE:\n");
        relatorio.append("Busca, inserção e remoção: O(log n) em árvore AVL.\n");
        relatorio.append("Listagem ordenada: O(n).\n");
        relatorio.append("Espaço: O(n).\n");

        try {
            Path caminho = Path.of(SAIDA);
            Files.createDirectories(caminho.getParent());
            Files.writeString(caminho, relatorio.toString(),
                    StandardCharsets.UTF_8);
            System.out.println("\n" + relatorio);
            System.out.println("Relatório salvo em " + SAIDA);
        } catch (IOException e) {
            System.out.println("Não foi possível salvar o relatório: "
                    + e.getMessage());
        }
    }

    private static int lerInteiro() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        executar();
    }
}

package ed.app;

import ed.naolineares.TabelaHash;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Problema13 {

    private static final String ARQUIVO = "saidas/problema_13.txt";

    private static class Usuario {
        private final String nome;
        private final String email;
        private final String senhaHash;

        private Usuario(String nome, String email, String senha) {
            this.nome = nome;
            this.email = email;
            this.senhaHash = hashSenha(senha);
        }

        private static String hashSenha(String senha) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] bytes = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
                StringBuilder resultado = new StringBuilder();

                for (byte b : bytes) {
                    resultado.append(String.format("%02x", b & 0xff));
                }

                return resultado.toString();
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("SHA-256 indisponível.", e);
            }
        }

        private boolean senhaCorreta(String senha) {
            return senhaHash.equals(hashSenha(senha));
        }

        @Override
        public String toString() {
            return nome + " (" + email + ")";
        }
    }

    private static final TabelaHash<String, Usuario> usuarios =
            new TabelaHash<>(11);

    private static final String[] NOMES = {
        "Ana Silva", "Bruno Costa", "Carla Souza", "Daniel Lima",
        "Eduarda Alves", "Felipe Santos", "Gabriela Rocha", "Henrique Martins",
        "Isabela Ferreira", "João Oliveira", "Karen Ribeiro", "Lucas Carvalho",
        "Mariana Gomes", "Nicolas Barros", "Olivia Mendes", "Paulo Azevedo",
        "Rafaela Dias", "Samuel Freitas", "Tatiane Castro", "Vitor Almeida"
    };

    private static void cadastrar(String nome, String email, String senha) {
        String chave = email.trim().toLowerCase();

        if (chave.isBlank() || nome.isBlank() || senha.isBlank()) {
            throw new IllegalArgumentException("Preencha todos os campos.");
        }

        if (usuarios.contemChave(chave)) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        usuarios.inserir(chave, new Usuario(nome.trim(), chave, senha));
    }

    private static boolean autenticar(String email, String senha) {
        Usuario usuario = usuarios.buscar(email.trim().toLowerCase());
        return usuario != null && usuario.senhaCorreta(senha);
    }

    private static boolean removerConta(String email) {
        return usuarios.remover(email.trim().toLowerCase()) != null;
    }

    public static void executar() {
        try (Scanner scanner = new Scanner(System.in)) {
            int opcao;

            do {
                System.out.println("\n======================================");
                System.out.println(" PROBLEMA 13 - CADASTRO DE USUÁRIOS");
                System.out.println("======================================");
                System.out.println("1 - Cadastrar usuário");
                System.out.println("2 - Fazer login");
                System.out.println("3 - Remover conta");
                System.out.println("4 - Listar usuários");
                System.out.println("5 - Executar testes automatizados");
                System.out.println("0 - Voltar ao menu principal");
                System.out.print("Opção: ");

                try {
                    opcao = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    opcao = -1;
                }

                try {
                    switch (opcao) {
                        case 1 -> {
                            System.out.print("Nome: ");
                            String nome = scanner.nextLine();
                            System.out.print("E-mail: ");
                            String email = scanner.nextLine();
                            System.out.print("Senha: ");
                            String senha = scanner.nextLine();

                            cadastrar(nome, email, senha);
                            System.out.println("Usuário cadastrado com sucesso.");
                        }
                        case 2 -> {
                            System.out.print("E-mail: ");
                            String email = scanner.nextLine();
                            System.out.print("Senha: ");
                            String senha = scanner.nextLine();

                            System.out.println(autenticar(email, senha)
                                    ? "Login realizado com sucesso."
                                    : "E-mail ou senha inválidos.");
                        }
                        case 3 -> {
                            System.out.print("E-mail da conta: ");
                            String email = scanner.nextLine();

                            System.out.println(removerConta(email)
                                    ? "Conta removida."
                                    : "Usuário não encontrado.");
                        }
                        case 4 -> {
                            System.out.println("Usuários cadastrados:");
                            usuarios.listar();
                            System.out.println("Total: " + usuarios.tamanho());
                        }
                        case 5 -> executarTestes();
                        case 0 -> System.out.println("Retornando ao menu principal...");
                        default -> System.out.println("Opção inválida.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Erro: " + e.getMessage());
                }

            } while (opcao != 0);
        }
    }

    private static void executarTestes() {
        TabelaHash<String, Usuario> backup = new TabelaHash<>(11);

        // Preserva os dados atuais: os testes usam uma tabela independente.
        TabelaHash<String, Usuario> teste = backup;
        int aprovados = 0;
        int total = 6;

        for (int i = 0; i < NOMES.length; i++) {
            String email = "usuario" + i + "@exemplo.com";
            teste.inserir(email, new Usuario(NOMES[i], email, "Senha" + i));
        }

        boolean cadastro = teste.tamanho() == 20;
        aprovados += verificar("Cadastro de 20 usuários", cadastro);

        // Duas chaves distintas com o mesmo índice.
        String chaveA = "colisao" + 0;
        String chaveB = null;

        for (int i = 1; i < 10000; i++) {
            String candidata = "colisao" + i;
            if (Math.floorMod(chaveA.hashCode(), teste.capacidade())
                    == Math.floorMod(candidata.hashCode(), teste.capacidade())) {
                chaveB = candidata;
                break;
            }
        }

        boolean colisao = chaveB != null;
        if (colisao) {
            teste.inserir(chaveA, new Usuario("Teste A", chaveA, "senhaA"));
            teste.inserir(chaveB, new Usuario("Teste B", chaveB, "senhaB"));
            colisao = teste.buscar(chaveA) != null && teste.buscar(chaveB) != null;
        }
        aprovados += verificar("Tratamento de colisões", colisao);

        boolean loginCorreto =
                teste.buscar("usuario0@exemplo.com").senhaCorreta("Senha0");
        aprovados += verificar("Autenticação com senha correta", loginCorreto);

        boolean loginIncorreto =
                !teste.buscar("usuario0@exemplo.com").senhaCorreta("errada");
        aprovados += verificar("Rejeição de senha incorreta", loginIncorreto);

        boolean duplicacao;
        try {
            // A estrutura atual substitui valores para uma chave existente.
            teste.inserir("usuario0@exemplo.com",
                    new Usuario("Outro nome", "usuario0@exemplo.com", "outra"));
            duplicacao = teste.tamanho() == 22;
        } catch (RuntimeException e) {
            duplicacao = false;
        }
        aprovados += verificar("Atualização de chave sem duplicar entradas",
                duplicacao);

        boolean remocao = teste.remover("usuario1@exemplo.com") != null
                && !teste.contemChave("usuario1@exemplo.com");
        aprovados += verificar("Remoção de conta", remocao);

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("PROBLEMA 13 - CADASTRO DE USUÁRIOS COM LOGIN RÁPIDO\n");
        relatorio.append("Estrutura: Tabela Hash com encadeamento | Linguagem: Java\n\n");
        relatorio.append("TESTES AUTOMATIZADOS\n");
        relatorio.append("Usuários cadastrados na simulação inicial: 20\n");
        relatorio.append("Colisões registradas pela tabela de teste: ")
                .append(teste.totalColisoes()).append('\n');
        relatorio.append("Testes aprovados: ").append(aprovados)
                .append("/").append(total).append("\n\n");
        relatorio.append("COMPLEXIDADE MÉDIA\n");
        relatorio.append("Cadastro/busca/remoção: O(1) em média; O(n) no pior caso.\n");
        relatorio.append("Espaço: O(n), sendo n a quantidade de usuários.\n");

        try {
            Files.createDirectories(Path.of("saidas"));
            Files.writeString(Path.of(ARQUIVO), relatorio.toString(),
                    StandardCharsets.UTF_8);
            System.out.println("\n" + relatorio);
            System.out.println("Relatório salvo em " + ARQUIVO);
        } catch (IOException e) {
            System.out.println("Não foi possível salvar a saída: " + e.getMessage());
        }
    }

    private static int verificar(String nome, boolean resultado) {
        System.out.println((resultado ? "[APROVADO] " : "[FALHOU] ") + nome);
        return resultado ? 1 : 0;
    }
}

package ed.app;

import ed.naolineares.ArvoreAVL;
import ed.naolineares.ArvoreAVL.Funcionario;

public class Problema09 {
    public static void executar() {
        ArvoreAVL arvore = new ArvoreAVL();

        String[][] funcionarios = {
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

        System.out.println("=== PROBLEMA 09: ÍNDICE DE CPF COM ÁRVORE AVL ===");
        System.out.println("\n1. Inserção de funcionários");

        for (String[] funcionario : funcionarios) {
            arvore.inserir(funcionario[0], funcionario[1], funcionario[2]);
        }

        System.out.println("Funcionários inseridos: " + arvore.quantidade());
        System.out.println("Altura da árvore: " + arvore.altura());
        System.out.println("Fator de balanceamento da raiz: " + arvore.fatorBalanceamentoRaiz());
        System.out.println("Árvore balanceada: " + arvore.estaBalanceada());

        System.out.println("\n2. Busca por CPF");
        Funcionario encontrado = arvore.buscar("529.982.247-25");

        if (encontrado != null) {
            System.out.println("Funcionário encontrado: " + encontrado);
        } else {
            System.out.println("Funcionário não encontrado.");
        }

        System.out.println("\nBusca por CPF inexistente:");
        System.out.println(arvore.buscar("00000000000") == null
                ? "Funcionário não encontrado, conforme esperado."
                : "Funcionário encontrado.");

        System.out.println("\n3. Remoção de funcionário");
        boolean removido = arvore.remover("11144477735");
        System.out.println("Remoção realizada: " + removido);
        System.out.println("Quantidade após remoção: " + arvore.quantidade());
        System.out.println("Árvore balanceada após remoção: " + arvore.estaBalanceada());
        System.out.println("Busca pelo CPF removido: "
                + (arvore.buscar("11144477735") == null ? "não encontrado" : "encontrado"));

        System.out.println("\n4. Funcionários em ordem crescente de CPF");
        for (Funcionario funcionario : arvore.listarEmOrdem()) {
            System.out.println(funcionario);
        }

        System.out.println("\n5. Verificação final");
        System.out.println("Altura final: " + arvore.altura());
        System.out.println("Fator de balanceamento da raiz: " + arvore.fatorBalanceamentoRaiz());
        System.out.println("Todos os nós estão balanceados: " + arvore.estaBalanceada());
    }

    public static void main(String[] args) {
        executar();
    }
}

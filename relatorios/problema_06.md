# Problema 06 — Controle de Estoque de uma Pequena Loja

**Linguagem:** Java  
**Estrutura de dados:** Lista simplesmente encadeada

## 1. Objetivo

Desenvolver um sistema para controlar os produtos em estoque de uma loja de materiais de construção. A aplicação permite cadastrar produtos, consultar informações, atualizar quantidades, remover produtos e identificar itens abaixo de um limite mínimo.

O problema também prevê a simulação de entradas e saídas de mercadorias ao longo de sete dias.

## 2. Estrutura de dados utilizada

Foi criada a classe Produto para representar os itens do estoque. Cada produto possui código, nome, quantidade disponível e preço unitário.

A classe ListaProdutos utiliza nós encadeados para armazenar os produtos. Cada nó mantém uma referência para um produto e outra para o próximo nó da lista.

Essa organização permite adicionar e remover produtos dinamicamente sem precisar deslocar os demais elementos.

## 3. Operações implementadas

- Cadastro de produtos, evitando códigos duplicados.
- Listagem dos produtos cadastrados.
- Busca de produtos pelo código.
- Busca de produtos pelo nome.
- Atualização da quantidade em estoque.
- Remoção de produtos pelo código.
- Consulta dos produtos abaixo do estoque mínimo.
- Simulação de movimentações de entrada e saída durante sete dias.

Na simulação, as entradas aumentam a quantidade disponível e as saídas diminuem o estoque. O sistema verifica se o produto existe e se há unidades suficientes para registrar uma saída.

## 4. Exemplo de utilização

Considere um produto com os seguintes dados:

- Código: 1001
- Nome: Cimento
- Quantidade inicial: 50 unidades
- Preço unitário: R$ 35,90

Se forem registradas dez unidades de saída, a quantidade esperada após a movimentação será de 40 unidades.

Esse exemplo é ilustrativo. Os resultados finais devem ser confirmados por meio da execução do programa.

## 5. Simulação semanal

A simulação percorre sete dias consecutivos. Em cada dia, podem ser registradas movimentações para os produtos cadastrados.

O usuário informa o código do produto, o tipo de movimentação e a quantidade. Ao finalizar cada dia, a simulação passa para o próximo. No encerramento, o sistema apresenta as quantidades finais dos produtos.

A simulação permite observar como as movimentações afetam o estoque e verificar quais produtos precisam de reposição.

## 6. Análise de complexidade

Considerando n como a quantidade de produtos cadastrados:

| Operação | Complexidade de tempo |
|---|---|
| Cadastro de produto | O(n) |
| Busca por código | O(n) |
| Busca por nome | O(n) |
| Atualização de quantidade | O(n) |
| Remoção por código | O(n) |
| Listagem de produtos | O(n) |
| Consulta de estoque mínimo | O(n) |

O cadastro apresenta complexidade O(n) porque verifica a existência de um código duplicado e percorre a lista para inserir o produto no final.

As buscas, a atualização e a remoção também podem percorrer toda a lista. A listagem e a consulta de estoque mínimo percorrem os produtos uma única vez.

O espaço total utilizado pela estrutura é O(n), pois cada produto cadastrado ocupa um nó da lista.

## 7. Considerações sobre a implementação

As classes Produto e ListaProdutos separam a representação dos itens das operações de gerenciamento do estoque.

O tratamento de códigos duplicados, produtos inexistentes, quantidades inválidas e saídas superiores ao estoque disponível ajuda a manter a consistência das informações.

A integração com o programa principal e a realização de testes devem ser verificadas na versão final do projeto.

## 8. Conclusão

O Problema 06 demonstra a aplicação de listas encadeadas em uma situação prática de gerenciamento de estoque. A estrutura permite cadastrar, localizar, atualizar e remover produtos, além de consultar itens abaixo de um limite mínimo.

A simulação semanal complementa a aplicação ao demonstrar como entradas e saídas modificam as quantidades disponíveis. Dessa forma, o problema contribui para compreender a utilização de estruturas de dados lineares na organização de informações.

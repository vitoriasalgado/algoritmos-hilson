# Relatório Técnico — Problema 8: Sistema de Autocomplete de Nomes de Produtos

## Objetivo

Implementar um sistema de autocomplete que sugere nomes de produtos a partir de um prefixo digitado, utilizando uma Árvore Binária de Busca (BST).

## Estrutura utilizada

Cada nó armazena os dados do produto e uma chave baseada no nome normalizado. A normalização converte letras para minúsculas e remove acentos, facilitando buscas que não dependem de maiúsculas, minúsculas ou acentuação.

## Operações implementadas

- Leitura dos produtos de `dados/produtos.csv`.
- Inserção dos nomes na árvore.
- Busca de produtos por prefixo.
- Percurso limitado aos ramos que podem conter correspondências.
- Apresentação das sugestões em ordem alfabética.
- Testes com prefixos existentes e inexistentes.

## Testes e resultados

A execução carregou 100 produtos e inseriu 100 nomes distintos na árvore.

- Prefixo `Paraf`: 5 sugestões.
- Prefixo `Silicone`: 5 sugestões.
- Prefixo `Furadeira`: 5 sugestões.
- Prefixo `xyz-inexistente`: nenhuma sugestão.

Os resultados completos estão em `saidas/problema_08.txt`.

## Complexidade

Considerando `n` produtos, altura `h`, `v` nós visitados e `k` sugestões:

- Inserção: O(h) por produto.
- Autocomplete: O(v + k), desconsiderando o custo das comparações de strings.
- Espaço da árvore: O(n).
- Espaço para armazenar sugestões: O(k).

No pior caso, uma BST não balanceada pode atingir altura O(n), tornando a busca linear.

## Conclusão

A implementação demonstra o uso de uma BST para organizar nomes de produtos e gerar sugestões por prefixo. Os testes confirmaram resultados para três prefixos existentes e nenhum resultado para um prefixo inexistente.

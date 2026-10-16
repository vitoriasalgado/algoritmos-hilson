# Relatório Técnico — Problema 7: Cadastro e Busca de Alunos por Matrícula

## Objetivo

Implementar uma Árvore Binária de Busca (BST) para cadastrar alunos e realizar operações utilizando a matrícula como chave.

## Estrutura utilizada

Cada nó armazena matrícula, nome e curso, além de referências para os filhos esquerdo e direito. Matrículas menores ficam à esquerda e matrículas maiores à direita. Matrículas duplicadas não são inseridas.

## Operações implementadas

- Inserção de alunos.
- Busca por matrícula.
- Remoção de alunos, incluindo os casos de nó folha, nó com um filho e nó com dois filhos.
- Percurso em ordem para apresentar os alunos em ordem crescente de matrícula.
- Carregamento dos registros de `dados/alunos.csv`.

## Justificativa

A BST é apropriada para o cadastro acadêmico porque organiza os registros pela matrícula e permite inserir, localizar e remover alunos sem ordenar toda a coleção após cada operação. O percurso em ordem fornece uma listagem ordenada diretamente pela estrutura.

## Testes e resultados

A execução carrega os registros do CSV, verifica a quantidade de alunos, pesquisa uma matrícula existente e uma inexistente, remove um aluno e verifica a busca após a remoção. Por fim, apresenta o percurso em ordem. Os resultados concretos estão em `saidas/problema_07.txt`.

## Complexidade

Considerando `n` alunos e altura `h` da árvore:

- Inserção: O(h).
- Busca: O(h).
- Remoção: O(h).
- Percurso em ordem: O(n).
- Espaço: O(n).

Em uma árvore razoavelmente equilibrada, as operações de busca, inserção e remoção tendem a O(log n). No pior caso, quando a árvore fica degenerada, podem chegar a O(n).

## Dificuldades e soluções

A remoção de um nó com dois filhos exige substituir seu registro pelo sucessor em ordem e removê-lo da posição original. A implementação trata esse caso e também verifica matrículas inexistentes e duplicadas.

## Conclusão

A BST atende ao cadastro e à consulta de alunos por matrícula, mantendo os registros organizados e permitindo gerar uma listagem ordenada por meio do percurso em ordem.

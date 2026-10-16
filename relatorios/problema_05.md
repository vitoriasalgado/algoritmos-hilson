# Problema 05 — Gerenciamento de Playlist de Músicas

**Linguagem:** Java  
**Estrutura de dados:** Lista simplesmente encadeada

## 1. Objetivo

Desenvolver uma aplicação para gerenciar uma playlist de músicas utilizando uma lista simplesmente encadeada implementada no próprio projeto. O programa demonstra operações de inserção, busca, remoção e listagem de músicas.

## 2. Estrutura de dados utilizada

A lista encadeada é formada por nós que armazenam os dados de cada música e uma referência para o próximo nó. Cada música possui título, artista e duração em segundos.

A implementação utiliza uma classe para representar as músicas e outra para gerenciar a lista, separando os dados das operações realizadas sobre a estrutura.

## 3. Operações implementadas

- Inserção de músicas no início da lista.
- Inserção de músicas no final da lista.
- Inserção em uma posição específica.
- Busca de músicas pelo título.
- Remoção de músicas pelo título.
- Listagem das músicas cadastradas.
- Consulta da quantidade de músicas e da duração total da playlist.
- Tratamento de situações inválidas, como posições inexistentes e tentativas de remoção de elementos que não estão na lista.

A demonstração utiliza músicas previamente definidas no código e executa diferentes operações para verificar o comportamento da estrutura.

## 4. Exemplo de utilização

Uma música pode ser representada pelos seguintes dados:

- Título: A Sky Full of Stars
- Artista: Coldplay
- Duração: 268 segundos

Após o cadastro, a música pode ser exibida na playlist, localizada por seu título ou removida da estrutura.

Os resultados específicos devem ser conferidos na saída real da execução do programa.

## 5. Análise de complexidade

Considerando n como a quantidade de músicas armazenadas:

| Operação | Complexidade de tempo |
|---|---|
| Inserção no início | O(1) |
| Inserção no final | O(1), quando a lista mantém referência ao último nó |
| Inserção em posição específica | O(n) |
| Busca por título | O(n) |
| Remoção por título | O(n) |
| Listagem | O(n) |
| Cálculo da duração total | O(n) |

O espaço total utilizado pela lista é O(n), pois cada música armazenada possui um nó correspondente.

A complexidade da inserção no final depende da implementação da lista. Quando não existe uma referência direta ao último nó, essa operação pode exigir uma travessia e apresentar complexidade O(n).

## 6. Conclusão

O Problema 05 demonstra a utilização de listas simplesmente encadeadas para organizar uma coleção dinâmica de músicas. As operações implementadas permitem modificar a playlist, localizar elementos e percorrer a estrutura.

A atividade contribui para compreender o funcionamento dos nós e das referências entre elementos, além de evidenciar as diferenças de custo entre inserções, buscas e remoções.

# Problema 05 — Gerenciamento de Playlist de Músicas

## 1. Objetivo

O objetivo deste problema é desenvolver um sistema de gerenciamento de playlist utilizando uma lista simplesmente encadeada em Java. O programa permite inserir, buscar, remover e listar músicas, além de calcular a duração total da playlist.

## 2. Estrutura de Dados Utilizada

A solução utiliza uma lista simplesmente encadeada, implementada na classe `Lista`, localizada no pacote `ed.lineares`.

A estrutura é composta por nós, representados pela classe `No`. Cada nó armazena uma música e uma referência para o próximo nó da lista.

A classe `Lista` mantém uma referência para o primeiro nó (`inicio`) e um contador de elementos (`tamanho`). A lista não mantém uma referência direta para o último nó.

Cada música é representada pela classe `Musica`, que contém informações como título, artista e duração em segundos.

As músicas iniciais são carregadas do arquivo `dados/playlist.csv`, utilizando ponto e vírgula como separador.

## 3. Funcionalidades Implementadas

O programa apresenta um menu interativo com as seguintes operações:

- Listar todas as músicas da playlist.
- Inserir uma música no início da lista.
- Inserir uma música no final da lista.
- Inserir uma música em uma posição específica, considerando a primeira posição como índice zero.
- Buscar uma música pelo título.
- Remover uma música pelo título.
- Calcular e exibir a duração total da playlist.
- Encerrar o programa e salvar o registro das operações em `saidas/problema_05.txt`.

A busca e a remoção comparam os títulos sem diferenciar letras maiúsculas de minúsculas.

O programa também trata entradas inválidas e posições fora dos limites da lista durante a inserção.

## 4. Carregamento e Registro de Dados

A playlist inicial é carregada a partir do arquivo `dados/playlist.csv`. O programa verifica se o arquivo possui pelo menos 12 músicas e interrompe o carregamento caso essa condição não seja atendida.

Durante a execução, as mensagens apresentadas pelo programa são registradas em um objeto `StringBuilder`. Ao encerrar o programa, esse conteúdo é gravado em `saidas/problema_05.txt`.

O arquivo de saída registra as mensagens e os resultados das operações executadas. A implementação atual não grava as alterações da playlist de volta no arquivo CSV; portanto, as inserções e remoções feitas durante a execução não são persistidas nesse arquivo.

## 5. Análise de Complexidade

Considere `n` como o número de músicas armazenadas na lista.

| Operação | Complexidade | Justificativa |
|---|---|---|
| Verificar se a lista está vazia | O(1) | Consulta a referência inicial. |
| Consultar o tamanho | O(1) | Retorna o contador de elementos. |
| Inserir no início | O(1) | Atualiza as referências do primeiro nó. |
| Inserir no fim | O(n) | Percorre a lista até o último nó. |
| Inserir em posição específica | O(n) | Pode percorrer a lista até a posição desejada. |
| Buscar por título | O(n) | Pode examinar todos os nós. |
| Remover por título | O(n) | Pode percorrer a lista para localizar a música. |
| Listar músicas | O(n) | Percorre todos os nós. |
| Calcular duração total | O(n) | Soma a duração de todas as músicas. |

A complexidade espacial da lista é O(n), pois cada música armazenada ocupa um nó adicional.

## 6. Considerações Finais

A implementação demonstra o uso de uma lista simplesmente encadeada para organizar e manipular uma playlist. A estrutura permite realizar operações de inserção, busca, remoção e consulta, evidenciando a importância das referências entre nós.

A solução também permite observar como a escolha da estrutura de dados influencia o custo das operações. Como a lista não possui uma referência direta para o último nó, a inserção no final exige uma busca sequencial, apresentando complexidade O(n).

Por fim, o carregamento inicial por CSV separa os dados da implementação do programa, enquanto o arquivo de saída permite registrar as mensagens geradas durante a execução.

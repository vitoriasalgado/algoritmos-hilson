# Relatório Técnico — Problema 10: Ranking de Jogadores em um Jogo Online

## Objetivo

Implementar uma Árvore AVL para gerenciar o ranking de jogadores de um jogo online, permitindo cadastrar jogadores, atualizar pontuações e consultar os melhores colocados de forma eficiente.

## Estrutura utilizada

Cada nó armazena o nome e a pontuação de um jogador, além de referências para os filhos esquerdo e direito e a altura do nó. A árvore é organizada pela pontuação, utilizando o nome como critério de desempate.

A AVL mantém o balanceamento por meio de rotações simples e duplas, evitando que a estrutura fique excessivamente inclinada após inserções e remoções.

## Operações implementadas

- Inserção de jogadores com nome e pontuação inicial.
- Busca de jogadores pelo nome.
- Atualização da pontuação de jogadores existentes, removendo e reinserindo o registro na posição adequada.
- Remoção de jogadores da árvore.
- Exibição do ranking completo em ordem decrescente de pontuação.
- Consulta dos Top-K jogadores com maiores pontuações.
- Verificação da altura e do balanceamento da árvore.
- Simulação inicial com 30 jogadores.
- Execução de testes automatizados e geração do relatório em `../saidas/problema_10.txt`.

## Justificativa

A Árvore AVL é adequada para o gerenciamento de rankings porque mantém a estrutura balanceada durante as operações de inserção e remoção. Isso permite organizar os jogadores por pontuação sem precisar ordenar toda a coleção após cada atualização.

O uso do nome como critério de desempate estabelece uma ordem consistente entre jogadores com a mesma pontuação. A consulta Top-K permite apresentar os melhores colocados de acordo com a necessidade do sistema.

## Testes e resultados

A execução dos testes automatizados verifica a inserção de 30 jogadores, a atualização de pontuação sem duplicação, a ordenação decrescente do ranking, a consulta dos cinco melhores jogadores e o balanceamento da árvore.

Na execução realizada, todos os cinco testes foram aprovados. A árvore principal, contendo 30 jogadores, apresentou altura 6 e permaneceu balanceada.

Os resultados detalhados e o Top 10 da simulação estão registrados em `../saidas/problema_10.txt`.

## Complexidade

Considerando `n` jogadores e uma Árvore AVL balanceada:

- Inserção: O(log n).
- Remoção: O(log n).
- Busca pela chave de ordenação: O(log n).
- Busca por nome na implementação atual: O(n) no pior caso, pois percorre a árvore até localizar o jogador.
- Atualização de pontuação: O(n) no pior caso, devido à busca inicial por nome, além da remoção e reinserção.
- Listagem completa do ranking: O(n).
- Consulta Top-K nesta implementação: O(n + k), em que `k` é a quantidade de jogadores solicitada.
- Espaço: O(n).

## Dificuldades e soluções

A principal dificuldade é manter a árvore balanceada após inserções, remoções e alterações de pontuação. Para resolver esse problema, a implementação recalcula as alturas dos nós e aplica rotações quando necessário.

A atualização da pontuação modifica a posição do jogador na árvore. Por isso, o registro precisa ser removido e inserido novamente com a nova pontuação. A busca por nome percorre os dois lados da árvore, pois o nome não é a chave principal de ordenação.

## Conclusão

A Árvore AVL implementada atende aos requisitos da situação-problema, permitindo gerenciar as pontuações dos jogadores, atualizar registros e consultar o ranking. Os testes realizados foram aprovados, demonstrando o funcionamento das operações avaliadas e a manutenção do balanceamento da estrutura.

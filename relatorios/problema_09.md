# Problema 9 — Índice de CPF para Sistema de RH

## 1. Objetivo

Implementar um índice de funcionários para o setor de Recursos Humanos utilizando uma árvore AVL. Cada funcionário é identificado por CPF e possui nome e cargo associados.

## 2. Justificativa da estrutura escolhida

A árvore AVL é uma árvore binária de busca autobalanceada. Ela é adequada para este problema porque permite localizar funcionários por CPF sem precisar percorrer todos os registros sequencialmente.

Após inserções e remoções, a estrutura atualiza as alturas dos nós e realiza rotações quando necessário. Isso mantém a diferença entre as alturas das subárvores esquerda e direita de cada nó entre -1 e 1.

## 3. Funcionalidades implementadas

- Inserção de funcionários com CPF, nome e cargo.
- Normalização do CPF para comparação consistente.
- Rejeição de CPFs inválidos e duplicados.
- Busca de funcionários por CPF.
- Remoção de funcionários, com rebalanceamento.
- Cálculo da altura da árvore.
- Consulta do fator de balanceamento da raiz.
- Verificação do balanceamento de todos os nós.
- Listagem dos funcionários em ordem crescente de CPF.

## 4. Complexidade

Considerando n como a quantidade de funcionários:

| Operação | Complexidade de tempo |
|---|---|
| Busca | O(log n) |
| Inserção | O(log n) |
| Remoção | O(log n) |
| Consulta da altura | O(1) |
| Fator de balanceamento da raiz | O(1) |
| Verificação de balanceamento completo | O(n) |
| Listagem em ordem | O(n) |

A altura da AVL é O(log n), garantindo operações eficientes de busca, inserção e remoção. O espaço total ocupado pela árvore é O(n).

## 5. Testes realizados

A execução do programa utilizou 30 funcionários fictícios.

| Teste | Resultado observado |
|---|---|
| Inserção inicial | 30 funcionários |
| Altura inicial | 6 |
| Fator de balanceamento inicial da raiz | 1 |
| Verificação inicial da árvore | Balanceada |
| Busca por CPF existente | Funcionário encontrado |
| Busca por CPF inexistente | Funcionário não encontrado |
| Remoção de funcionário existente | Sucesso |
| Quantidade após remoção | 29 funcionários |
| Busca pelo CPF removido | Funcionário não encontrado |
| Altura final | 5 |
| Fator de balanceamento final da raiz | 0 |
| Verificação final da árvore | Balanceada |

A listagem em ordem crescente confirmou que os funcionários são apresentados de acordo com o CPF.

## 6. Dificuldades e soluções adotadas

O principal cuidado na implementação é preservar as propriedades da árvore após cada alteração. Para isso, foram implementadas rotações simples à esquerda e à direita, além das rotações duplas necessárias aos casos de desbalanceamento.

Na remoção de um nó com dois filhos, o registro é substituído pelo sucessor em ordem e a árvore é rebalanceada. A implementação também trata CPFs inválidos, duplicados e inexistentes.

## 7. Conclusão

A árvore AVL atende ao objetivo de manter um índice de funcionários por CPF com operações eficientes. Os testes demonstraram que a estrutura permaneceu balanceada após a inserção de 30 funcionários e a remoção de um registro.

Os dados utilizados são fictícios e servem exclusivamente para demonstração acadêmica.

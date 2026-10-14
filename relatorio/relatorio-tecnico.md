---
title: "Relatório Técnico"
subtitle: "Trabalho Prático de Estrutura de Dados"
author:
  - "João Guilherme — Matrícula: "
  - "Vitória — Matrícula: "
date: "Teresina — PI, 2026"
lang: pt-BR
---

<!--
Documento de trabalho. Editar aqui durante o desenvolvimento, uma seção por problema
concluído. Converter para entrega com:
    pandoc relatorio/relatorio-tecnico.md -o relatorio/Relatorio_Tecnico.docx
-->

**iCEV — Instituto de Ensino Superior** · Engenharia de Software, 2º Período

Este relatório atende ao item 5.2 do enunciado: para cada situação-problema implementada, registra a
justificativa de escolha da estrutura, a análise de complexidade, um exemplo de execução e as
dificuldades encontradas durante o desenvolvimento. Complementa o `relatorio-decisoes.md`, que trata
das decisões de projeto tomadas antes da implementação.

As saídas seguem um padrão compartilhado pelas duas linguagens: título com número do problema,
estrutura e linguagem no início; entrada, execução, resumo e complexidade em seções separadas.
O mesmo conteúdo é exibido no terminal e salvo em `saidas/problema_NN.txt`. Essa organização
facilita localizar os resultados e comparar a execução com a explicação deste relatório.

---

## Situação-Problema 1 — Sistema de Atendimento de uma Clínica Médica

**Estrutura utilizada:** Fila (implementação própria, `python/lineares/fila.py`)
**Linguagem:** Python

### Justificativa de escolha

Os pacientes precisam ser atendidos exatamente na ordem em que chegam — isso é o comportamento
FIFO (primeiro a entrar, primeiro a sair), que é a base de uma fila. Não é preciso acessar um paciente
no meio da lista nem reordenar por prioridade, só respeitar quem chegou primeiro. Uma pilha inverteria
essa ordem; uma lista com acesso por posição livre ofereceria mais do que o problema precisa. A fila
resolve exatamente o que o problema pede, sem sobrar nem faltar nada.

### Decisão de implementação

O item 5.4 do enunciado proíbe usar `collections.deque` ou qualquer fila pronta do Python. A
implementação usa uma lista (`list`) como armazenamento, com um índice `_inicio` que avança a cada
remoção em vez de tirar o elemento fisicamente da lista. Isso mantém `enfileirar` e `desenfileirar` em
O(1) amortizado para enfileirar e O(1) para desenfileirar — se fosse usado `pop(0)` pra remover o primeiro elemento, seria O(n), porque todos os outros
elementos precisariam se deslocar uma posição. O preço dessa escolha é que a lista interna nunca
diminui: os pacientes já atendidos continuam ocupando espaço até o fim da execução. Com 15 pacientes
isso não importa; numa fila que rodasse sem parar, seria necessário reaproveitar esse espaço de algum
jeito — por exemplo, com uma fila circular.

### Estimativa de tempo de espera

O CSV de entrada (`dados/pacientes.csv`) fornece apenas nome, horário de chegada e tipo de
atendimento — não há duração de consulta. Para calcular um tempo médio de espera com algum
significado, foi necessário atribuir uma duração estimada por tipo de atendimento:

| Tipo | Duração estimada |
| --- | --- |
| Consulta | 15 min |
| Exame | 20 min |
| Cirurgia | 60 min |
| Urgência | 10 min |
| Retorno | 10 min |

A simulação processa a fila em ordem de chegada. Pra cada paciente, o horário de início do
atendimento é o maior entre o horário de chegada dele e o horário em que o atendimento anterior
terminou; a espera é a diferença entre os dois. Isso reproduz o que acontece numa fila de atendimento
único de verdade: se os pacientes chegam mais rápido do que a clínica consegue atender, o atraso vai
se acumulando ao longo do dia — e é o que acontece com os dados de teste, onde a espera vai de 0
minutos (primeiro paciente) até 263 minutos (último), com média de 117,7 minutos.

### Complexidade

| Operação | Tempo | Espaço |
| --- | --- | --- |
| `enfileirar` | O(1) amortizado | — |
| `desenfileirar` | O(1) | — |
| Simulação completa (n pacientes) | O(n) | O(n) |

O espaço O(n) inclui o armazenamento interno da fila, a lista de tempos de espera e os registros
da execução. Essa análise considera tamanho limitado por registro de paciente.

### Exemplo de execução

Trecho da execução; o registro completo está em `saidas/problema_01.txt`.

```
========================================================================
PROBLEMA 01 — SISTEMA DE ATENDIMENTO DE UMA CLÍNICA MÉDICA
Estrutura: Fila | Linguagem: Python
========================================================================

ENTRADA: 15 pacientes lidos de dados/pacientes.csv

ATENDIMENTOS:
  Paciente: Ana Beatriz Souza | Chegada: 08:04
  Espera: 0 min | Início: 08:04 | Fim: 08:14

  ...

  Paciente: Olivia Ribeiro Sampaio | Chegada: 09:57
  Espera: 263 min | Início: 14:20 | Fim: 14:35

RESUMO:
  15 pacientes atendidos em ordem de chegada.
  Tempo médio de espera: 117.7 minutos.
  Operações: 15 enfileiramentos e 15 desenfileiramentos.

COMPLEXIDADE:
  Enfileirar: O(1) amortizado. Desenfileirar: O(1).
  Simulação: tempo O(n) e espaço O(n), sendo n o número de pacientes.
```

### Dificuldades encontradas e soluções adotadas

- **Indentação definindo o bloco.** O cálculo da espera de cada paciente ficou escrito, no começo, fora
  do laço `while`, por um erro de indentação — em Python, diferente de Java, o que pertence a um
  `while`/`if`/`for` é definido pelo recuo do código, não por chaves. Não deu erro (o código era válido),
  só o resultado saiu errado: a simulação calculava a espera uma única vez, usando os dados do último
  paciente, em vez de calcular uma vez pra cada um. Percebi olhando a saída do programa e reparando
  que o número de linhas impressas não batia com o número de pacientes.
- **Docstring na mesma linha do `def`.** Numa tentativa inicial, a docstring dos métodos da fila ficou
  na mesma linha dos dois-pontos do `def`, o que dá `IndentationError` na linha seguinte — o corpo da
  função precisa começar numa linha nova, indentada, quando tem docstring.
- **Arquivo aberto sem fechar.** A leitura do CSV, no começo, usava `open()` sem fechar o arquivo
  depois. Troquei por `with open(...) as arquivo:`, que fecha automaticamente no fim do bloco, mesmo
  se der erro no meio do caminho.

### Conclusão

A fila resolveu o problema sem precisar de nenhuma operação fora do que ela já faz naturalmente
(inserir no fim, remover do início). A parte mais difícil não foi a estrutura de dados em si, mas montar
a simulação — decidir como estimar a duração de um atendimento a partir de um dado que o CSV não
fornece, e fazer o atraso se propagar corretamente ao longo do dia.

---

## Situação-Problema 2 — Fila de Impressão em Laboratório de Informática

**Estrutura utilizada:** Fila (mesma implementação do Problema 1, `python/lineares/fila.py`, estendida)
**Linguagem:** Python

### Justificativa de escolha

Uma impressora processa os trabalhos na ordem em que chegam, sem prioridade — é o mesmo
comportamento do Problema 1. Por isso deu pra reaproveitar a mesma `Fila`, sem mudar a parte principal
dela.

### O que precisou ser adicionado: `cancelar_ultimo` e `listar`

O enunciado pede uma operação que uma fila comum não faz sozinha: cancelar o último trabalho enviado,
ou seja, mexer no fim da fila, não no início. Foram adicionados dois métodos novos na classe `Fila`:

- `cancelar_ultimo()`: remover o último item de uma lista Python é uma operação rápida (não precisa
  deslocar nada, diferente de remover o primeiro). Por isso deu pra resolver com um método a mais na
  própria classe, sem precisar de outra estrutura.
- `listar()`: mostra todos os trabalhos que ainda estão na fila, sem remover nenhum — usado pra atender
  o pedido de "listar fila" do enunciado.

Os dois seguem o mesmo padrão de erro do Problema 1: `cancelar_ultimo()` dá erro se a fila estiver
vazia, `listar()` não, porque uma fila vazia listada é só uma lista vazia, não é um erro.

### Simulação

A simulação foi dividida em três partes separadas, sem misturar tudo no mesmo laço:

1. todos os 15 trabalhos do CSV são enfileirados;
2. a fila é listada, e o último trabalho enviado é cancelado — pra mostrar que o cancelamento é algo
   pontual, não uma regra aplicada em todo trabalho;
3. os trabalhos que sobraram são processados em ordem, um por vez, simulando a impressora.

### Complexidade

| Operação | Tempo | Espaço |
| --- | --- | --- |
| `enfileirar` | O(1) amortizado | — |
| `desenfileirar` | O(1) | — |
| `cancelar_ultimo` | O(1) amortizado | — |
| `listar` | O(n) | O(n) (cópia da fatia) |
| Simulação completa (n trabalhos) | O(n) | O(n) |

### Exemplo de execução

Trecho da execução; o registro completo está em `saidas/problema_02.txt`.

```
========================================================================
PROBLEMA 02 — FILA DE IMPRESSÃO EM LABORATÓRIO DE INFORMÁTICA
Estrutura: Fila | Linguagem: Python
========================================================================

ENTRADA: 15 trabalhos lidos de dados/impressoes.csv

FILA INICIAL:
  - Aluno: Ana Beatriz Souza | Arquivo: relatorio_final.pdf | Páginas: 22
  ...
  - Aluno: Olivia Ribeiro Sampaio | Arquivo: poster_congresso.pdf | Páginas: 30

CANCELAMENTO:
  poster_congresso.pdf enviado por Olivia Ribeiro Sampaio (30 páginas).

SEQUÊNCIA DE IMPRESSÃO:
  - Aluno: Ana Beatriz Souza | Arquivo: relatorio_final.pdf | Páginas: 22
  ...
  - Aluno: Nicolas Almeida Cruz | Arquivo: referencias.docx | Páginas: 24

RESUMO:
  14 trabalhos impressos em ordem de chegada e 1 cancelado.
  Operações: 15 enfileiramentos, 1 listagem, 1 cancelamento
  e 14 desenfileiramentos.

COMPLEXIDADE:
  Enfileirar/cancelar último: O(1) amortizado. Desenfileirar: O(1).
  Listar: O(n). Simulação: tempo O(n) e espaço O(n).
  n é o número de trabalhos de impressão.
```

O `poster_congresso.pdf` aparece na listagem (ainda estava na fila naquele momento), mas não aparece
na sequência final de impressão — foi o trabalho cancelado, que era o último enviado.

### Dificuldades encontradas e soluções adotadas

- **Tentar achar a regra de cancelamento no CSV.** No começo, tentei decidir quando cancelar um
  trabalho olhando uma coluna do CSV (`impressoes[2]`, que na verdade é o número de páginas),
  procurando algo tipo `"Cancelar"`. Mas essa informação não existe nos dados — o cancelamento é uma
  decisão da simulação, não algo que vem do arquivo.
- **Misturar as operações no mesmo laço.** Em duas tentativas, o código enfileirava e, na mesma
  volta do laço, já desenfileirava (ou cancelava) o item que acabou de entrar — a fila nunca chegava a
  ter mais de um trabalho por vez. Resolvido separando em laços diferentes, do mesmo jeito que já
  tinha sido feito no Problema 1.
- **Ignorar o retorno de `listar()`.** Uma primeira versão chamava `fila.listar()` mas não guardava o
  resultado em nenhuma variável, então nada aparecia na tela. Só funcionou depois de percorrer o
  retorno com um `for` e imprimir cada item.

### Conclusão

Reaproveitar a `Fila` do Problema 1 e só adicionar dois métodos novos mostrou que a estrutura já
estava bem pensada desde o início — não precisou refazer nada, só completar. A parte mais difícil não
foi o FIFO em si, que já estava resolvido, e sim entender qual ponta da fila cada operação nova
precisava mexer, e decidir se isso cabia dentro da própria classe ou exigia outra coisa.

---

## Situação-Problema 3 — Sistema de Desfazer em Editor de Texto Simples

**Estrutura utilizada:** Pilha (implementação própria, `python/lineares/pilha.py`)
**Linguagem:** Python

### Justificativa de escolha

A função de desfazer precisa reverter primeiro a ação mais recente do usuário. Esse é o comportamento
LIFO (*Last In, First Out*): o último item colocado na estrutura é o primeiro a ser retirado. A pilha
representa exatamente essa regra. Uma fila teria o comportamento oposto e desfaria primeiro a ação
mais antiga, o que não corresponde ao funcionamento de um editor de texto.

### Decisão de implementação

A classe `Pilha` usa uma lista Python apenas como armazenamento interno, conforme permitido pelo
enunciado. O método `empilhar` usa `append` para inserir no fim da lista; `desempilhar` usa `pop()` sem
índice para remover o último item. `topo` consulta `self._itens[-1]` sem remover a ação, e operações
que dependem de uma ação existente lançam `IndexError` quando a pilha está vazia.

Cada ação da simulação guarda três dados: tipo, trecho digitado e estado anterior do texto. Guardar o
estado anterior permite restaurar o editor diretamente ao desfazer a ação. Foram registradas dez
digitações que formam a frase "Olá, mundo! Este é um problema de pilha.".

### Simulação

A simulação ocorre em duas etapas. Primeiro, cada trecho é transformado em uma ação, empilhado e
acrescentado ao texto atual. Depois, enquanto a pilha não está vazia, o programa desempilha a ação do
topo e restaura o texto que existia antes dela.

A primeira ação desfeita é `" pilha."`; a última é `"Olá"`. Ao final das dez operações de desfazer, o
texto volta a ser vazio, comprovando que a ordem foi exatamente a inversa da ordem de digitação.

### Complexidade

| Operação | Tempo | Espaço |
| --- | --- | --- |
| `empilhar` | O(1) amortizado | O(1) por referência de ação armazenada |
| `desempilhar` | O(1) amortizado | — |
| `topo` | O(1) | — |
| `tamanho` e `vazia` | O(1) | — |
| Simulação com n ações | O(n) operações da pilha | O(n) registros |

As inserções e remoções no fim da lista têm custo O(1) amortizado: uma operação isolada pode
redimensionar o armazenamento, mas uma sequência de n operações tem custo O(n).
A simulação completa também cria e registra textos. Seu tempo e espaço são O(n + C), sendo C
a soma dos tamanhos dos estados de texto e dos registros gerados. Portanto, O(n) descreve
as operações da pilha, mas não todo o custo de copiar, armazenar e exibir os textos.

### Exemplo de execução

Trecho da execução; o registro completo está em `saidas/problema_03.txt`.

```
========================================================================
PROBLEMA 03 — SISTEMA DE DESFAZER EM EDITOR DE TEXTO
Estrutura: Pilha | Linguagem: Python
========================================================================

ENTRADA: 10 ações de digitação simuladas

AÇÕES EXECUTADAS:
  Digitação: 'Olá'
  Texto atual: 'Olá'

  ...

  Digitação: ' pilha.'
  Texto atual: 'Olá, mundo! Este é um problema de pilha.'

DESFAZENDO AÇÕES:
  Ação desfeita: digitação - ' pilha.'
  Texto após desfazer: 'Olá, mundo! Este é um problema de'

  ...

  Ação desfeita: digitação - 'Olá'
  Texto após desfazer: ''

RESUMO:
  10 ações executadas e desfeitas da mais recente à mais antiga.
  Texto final: '' (vazio).
  Operações: 10 empilhamentos e 10 desempilhamentos.

COMPLEXIDADE:
  Empilhar/desempilhar: O(1) amortizado por operação da pilha.
  n ações: O(n) operações da pilha.
  Os textos e registros também têm custo: tempo e espaço O(n + C),
  sendo C a soma dos tamanhos dos estados de texto e registros gerados.
```

### Dificuldades encontradas e soluções adotadas

- **Diferenciar pilha de fila.** A fila usa um índice de início, mas isso não se aplica à pilha: nela a
  remoção sempre ocorre no fim da lista. A solução foi usar `pop()` sem índice.
- **Consultar sem remover.** O método `topo` precisava mostrar a última ação sem alterá-la. O índice
  `-1` acessa o último elemento de uma lista Python sem removê-lo.
- **Restaurar o estado correto.** Guardar apenas o trecho digitado não atenderia ações futuras de
  exclusão ou formatação. Registrar o estado anterior junto da ação torna o desfazer direto.

### Conclusão

A pilha modela a função de desfazer de forma direta: cada ação é registrada quando ocorre e a mais
recente é recuperada primeiro. A simulação com dez ações termina no mesmo estado inicial, sem usar
estruturas prontas além da lista que serve de armazenamento interno.

---

## Situação-Problema 4 — Validação de Expressões Balanceadas

**Estrutura utilizada:** Pilha (implementação própria, `python/lineares/pilha.py`)
**Linguagem:** Python

### Justificativa de escolha

Ao fechar um parêntese, colchete ou chave, é necessário verificar primeiro o último símbolo
de abertura que ainda não foi fechado. Essa ordem é LIFO e corresponde ao funcionamento de
uma pilha. Contar apenas aberturas e fechamentos não seria suficiente: a expressão `([)]`
tem quantidades iguais, mas os símbolos estão na ordem errada.

### Decisão de implementação

A função `expressao_balanceada(expressao)` cria uma pilha vazia e percorre os caracteres:

1. Se o caractere for `(`, `[` ou `{`, empilha a abertura.
2. Se for um fechamento, verifica se existe uma abertura na pilha. Caso esteja vazia, retorna `False`.
3. Remove a abertura do topo e confere se ela corresponde ao fechamento. Se o par for diferente,
   retorna `False`.
4. Depois de percorrer a expressão, retorna `pilha.vazia()`: aberturas restantes indicam
   símbolos sem fechamento.

Outros caracteres são ignorados. A função verifica somente o balanceamento dos delimitadores,
sem avaliar a expressão nem validar sua sintaxe completa.

### Simulação

Foram usadas seis expressões: `{[()]}`, `([])`, `((()))`, `([)]`, `({[]})` e `((()`.
Quatro foram classificadas como válidas e duas como inválidas. A expressão `([)]` demonstra
fechamentos fora de ordem; `((()` demonstra aberturas que permanecem sem fechamento.
Cada chamada cria uma pilha nova, de modo que uma expressão não interfere na seguinte.

### Complexidade

| Operação | Tempo | Espaço auxiliar |
| --- | --- | --- |
| Empilhar ou desempilhar | O(1) amortizado | — |
| Validar uma expressão de n caracteres | O(n) no pior caso | O(n) no pior caso |

Cada caractere é examinado no máximo uma vez. No pior caso, a pilha guarda até n aberturas.
Um erro encontrado durante o percurso permite encerrar a verificação antes do final.
Para um conjunto de expressões, o tempo de validação é O(N), sendo N a soma de seus comprimentos.
Os registros salvos também ocupam espaço proporcional ao conteúdo da saída.

### Exemplo de execução

```
========================================================================
PROBLEMA 04 — VALIDAÇÃO DE EXPRESSÕES BALANCEADAS
Estrutura: Pilha | Linguagem: Python
========================================================================

ENTRADA: 6 expressões com parênteses, colchetes e chaves

RESULTADOS:
  {[()]}   → VÁLIDA
  ([])     → VÁLIDA
  ((()))   → VÁLIDA
  ([)]     → INVÁLIDA
  ({[]})   → VÁLIDA
  ((()     → INVÁLIDA

RESUMO:
  4 expressões válidas e 2 inválidas.
  Operações: empilhar aberturas e conferir os fechamentos ao desempilhar.

COMPLEXIDADE:
  Tempo: O(n) por expressão no pior caso.
  Espaço: O(n), sendo n o número de caracteres da expressão.
```

### Dificuldades encontradas e soluções adotadas

- **Relacionar abertura e fechamento.** A validação foi organizada para comparar o fechamento
  atual com a abertura retirada do topo. Assim, pares de tipos diferentes são rejeitados.
- **Distinguir o resultado da função da apresentação.** A função retorna `True` ou `False`;
  o laço da simulação transforma esse retorno em VÁLIDA ou INVÁLIDA para a leitura do resultado.
- **Organizar os registros.** A função local `registrar` exibe cada mensagem e a guarda em
  uma lista. Ao final, as linhas são gravadas em `saidas/problema_04.txt`, mantendo o mesmo
  conteúdo do terminal.

### Conclusão

A pilha permite verificar tanto o tipo quanto a ordem dos delimitadores. A simulação distingue
expressões balanceadas de fechamentos incompatíveis e de aberturas sem fechamento, reaproveitando
a estrutura implementada no problema 3.

---

## Apêndice A — Algoritmos de ordenação implementados

Esta seção documenta os algoritmos reutilizáveis antes de sua aplicação às situações-problema.
A implementação isolada de um algoritmo não significa que os problemas que dependem dele
estejam concluídos.

### Bubble Sort — Python

**Arquivo:** `python/ordenacao/bubble_sort.py`
**Função:** `bubble_sort(valores)`

#### Funcionamento e decisões de implementação

A função percorre a lista comparando elementos vizinhos. Quando o elemento da esquerda é maior
que o da direita, troca suas posições usando uma variável temporária. A cada passagem, o maior
elemento da parte ainda não ordenada chega ao final dessa parte.

O laço externo realiza n − 1 passagens. O laço interno faz n − 1 − i comparações na passagem i,
evitando a parte final já ordenada. Esta versão não usa parada antecipada: mesmo uma lista já
ordenada percorre todas as comparações previstas.

A lista recebida é modificada diretamente. A função retorna `comparacoes, trocas, tempo`.
A contagem de comparações aumenta a cada comparação entre valores; a de trocas aumenta somente
quando um par efetivamente troca de posição. O tempo é medido em segundos por `perf_counter()`,
com a leitura inicial antes dos laços e a final após todas as passagens.

#### Complexidade e propriedades

| Aspecto | Resultado |
| --- | --- |
| Comparações, para n ≥ 1 | n(n − 1)/2 |
| Tempo no melhor, médio e pior caso | O(n²), nesta versão sem parada antecipada |
| Espaço auxiliar | O(1), pois não cria outra lista |
| Estabilidade | Preserva a ordem relativa dos elementos iguais |

Para uma lista vazia, são feitas zero comparações. Elementos iguais não são trocados porque
a condição usa maior que, sem incluir igualdade. O algoritmo foi escolhido como uma base
simples de comparação; seu custo quadrático limita o uso com listas grandes.

#### Verificação por execução

| Entrada | Resultado | Comparações | Trocas |
| --- | --- | --- | --- |
| [5, 3, 8, 1] | [1, 3, 5, 8] | 6 | 4 |
| [] | [] | 0 | 0 |
| [7] | [7] | 0 | 0 |
| [1, 2, 3] | [1, 2, 3] | 3 | 0 |
| [3, 2, 1] | [1, 2, 3] | 3 | 3 |
| [2, -1, 2, 0] | [-1, 0, 2, 2] | 6 | 3 |

As seis execuções produziram os resultados e contadores esperados. O registro está em
`saidas/bubble_sort.txt`. Os tempos registrados variam entre execuções e, para entradas tão
pequenas, não sustentam uma comparação de desempenho entre algoritmos.

#### Pontos trabalhados durante a implementação

A implementação foi feita por etapas, com revisão dos limites dos laços e dos recuos.
A variável temporária preserva o primeiro valor durante a troca. A contagem de trocas fica
dentro do `if`, enquanto o cálculo do tempo e o retorno ficam fora dos dois laços, para
serem executados somente após terminar a ordenação.

#### Estado de conclusão

A função e sua verificação isolada estão concluídas. Ainda falta integrá-la às situações-problema
que exigem ordenação, com seus dados, saídas numeradas e análises específicas.

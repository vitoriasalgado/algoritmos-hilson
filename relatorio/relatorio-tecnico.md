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
O(1) — se fosse usado `pop(0)` pra remover o primeiro elemento, seria O(n), porque todos os outros
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
| `enfileirar` | O(1) | — |
| `desenfileirar` | O(1) | — |
| Simulação completa (n pacientes) | O(n) | O(n) |

O espaço O(n) vem de duas listas que crescem junto com o número de pacientes: o armazenamento
interno da fila e a lista de tempos de espera usada pra calcular a média.

### Exemplo de execução

```
Paciente: Ana Beatriz Souza, Horário: 08:04
Tempo de espera: 0 minutos, Início do atendimento: 08:04, Fim do atendimento: 08:14
Paciente: Bruno Carvalho Lima, Horário: 08:15
Tempo de espera: 0 minutos, Início do atendimento: 08:15, Fim do atendimento: 08:25
Paciente: Carla Menezes Rocha, Horário: 08:19
Tempo de espera: 6 minutos, Início do atendimento: 08:25, Fim do atendimento: 08:45
...
Paciente: Olivia Ribeiro Sampaio, Horário: 09:57
Tempo de espera: 263 minutos, Início do atendimento: 14:20, Fim do atendimento: 14:35
Média de tempo de espera: 117.7 minutos
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

O enunciado permite uma operação que uma fila comum não faz sozinha: cancelar o último trabalho
enviado, ou seja, mexer no fim da fila, não no início. Para demonstrar essa variação, foram adicionados
dois métodos novos na classe `Fila`:

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
| `enfileirar` | O(1) | — |
| `desenfileirar` | O(1) | — |
| `cancelar_ultimo` | O(1) | — |
| `listar` | O(n) | O(n) (cópia da fatia) |
| Simulação completa (n trabalhos) | O(n) | O(n) |

### Exemplo de execução

```
Impressões na fila:
  - Aluno: Ana Beatriz Souza | Arquivo: relatorio_final.pdf | Páginas: 22
  - Aluno: Bruno Carvalho Lima | Arquivo: tcc_capitulo1.docx | Páginas: 6
...
  - Aluno: Nicolas Almeida Cruz | Arquivo: referencias.docx | Páginas: 24
  - Aluno: Olivia Ribeiro Sampaio | Arquivo: poster_congresso.pdf | Páginas: 30
Cancelamento: poster_congresso.pdf enviado por Olivia Ribeiro Sampaio (30 páginas).
Sequência de impressão:
  - Aluno: Ana Beatriz Souza | Arquivo: relatorio_final.pdf | Páginas: 22
  - Aluno: Bruno Carvalho Lima | Arquivo: tcc_capitulo1.docx | Páginas: 6
...
  - Aluno: Nicolas Almeida Cruz | Arquivo: referencias.docx | Páginas: 24

=== PROBLEMA 02 — FILA DE IMPRESSÃO EM LABORATÓRIO DE INFORMÁTICA ===
Estrutura: Fila   |   Linguagem: Python

[ENTRADA]    15 trabalhos lidos de dados/impressoes.csv
[OPERAÇÕES]  15 enfileiramentos, 1 listagem, 1 cancelamento, 14 desenfileiramentos
[SAÍDA]      14 trabalhos impressos em ordem de chegada (1 cancelado antes da impressão)
[COMPLEX.]   enfileirar/desenfileirar/cancelar_ultimo O(1) | listar/simulação O(n) | espaço O(n)
==========================================================
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
estava bem pensada desde o início (não precisou refazer nada, só completar). A parte mais difícil não
foi o FIFO em si, que já estava resolvido, e sim entender qual ponta da fila cada operação nova
precisava mexer, e decidir se isso cabia dentro da própria classe ou exigia outra coisa.

---

## Situação-Problema 3 — Sistema de Desfazer em Editor de Texto Simples

**Estrutura utilizada:** Pilha (implementação própria, `python/lineares/pilha.py`)
**Linguagem:** Python

### Justificativa de escolha

O recurso de desfazer precisa reverter a ação mais recente antes de qualquer ação anterior. Esse
comportamento é LIFO (*Last In, First Out*): a última ação executada é a primeira a sair. Uma pilha
representa exatamente essa regra. Uma fila produziria o resultado contrário, desfazendo primeiro a
ação mais antiga, o que não corresponde ao botão "Desfazer" de um editor.

### Decisão de implementação

A classe `Pilha` usa uma lista Python apenas como armazenamento interno, conforme permitido pelo
enunciado. `empilhar` usa `append`, que insere no fim da lista, e `desempilhar` usa `pop()` sem índice,
que remove o último item. O método `topo` consulta `self._itens[-1]` sem remover a ação, e os métodos
que dependem de um item existente lançam `IndexError` se a pilha estiver vazia.

Cada ação é armazenada em uma lista com três informações: tipo da ação, trecho digitado e estado do
texto antes da alteração. Guardar o estado anterior permite desfazer sem precisar reconstruir a regra
inversa de cada ação. Na simulação, foram registradas dez digitações que formam a frase "Olá, mundo!
Este é um problema de pilha.". Depois, as ações são removidas uma a uma até a pilha ficar vazia.

### Simulação

A execução é dividida em duas etapas:

1. para cada trecho digitado, o programa registra a ação na pilha e atualiza o texto atual;
2. enquanto a pilha não estiver vazia, o programa remove a ação do topo e restaura o texto que existia
   antes dela.

Assim, a primeira ação desfeita é a digitação de `" pilha."`; a última é `"Olá"`. Ao final, o texto
volta a ficar vazio, comprovando que todas as dez ações foram revertidas na ordem inversa em que foram
executadas.

### Complexidade

| Operação | Tempo | Espaço |
| --- | --- | --- |
| `empilhar` | O(1) | O(1) por ação armazenada |
| `desempilhar` | O(1) | — |
| `topo` | O(1) | — |
| `tamanho` e `vazia` | O(1) | — |
| Simulação com n ações | O(n) operações da pilha | O(n) registros |

Na análise da estrutura, cada operação de pilha é O(1). Como o estado anterior do texto é guardado em
cada ação, a memória efetiva também depende do tamanho acumulado desses textos. Para a simulação curta
do trabalho, esse custo é pequeno e deixa o desfazer mais fácil de entender.

### Exemplo de execução

```
Ações executadas:
Digitação: 'Olá' | Texto atual: Olá
Digitação: ', ' | Texto atual: Olá,
...
Digitação: ' pilha.' | Texto atual: Olá, mundo! Este é um problema de pilha.
Desfazendo ações:
Ação desfeita: digitação - ' pilha.'
Texto após desfazer: Olá, mundo! Este é um problema de
...
Ação desfeita: digitação - 'Olá'
Texto após desfazer:

=== PROBLEMA 03 — SISTEMA DE DESFAZER EM EDITOR DE TEXTO ===
Estrutura: Pilha   |   Linguagem: Python

[ENTRADA]    10 ações de digitação simuladas
[OPERAÇÕES]  10 empilhamentos, 10 desfazimentos
[SAÍDA]      texto final após desfazer todas as ações = ''
[COMPLEX.]   empilhar/desempilhar O(1) | simulação O(n) | espaço O(n)
==========================================================
```

### Dificuldades encontradas e soluções adotadas

- **Diferenciar pilha de fila.** A primeira ideia reaproveitava um índice de início, usado na fila. Esse
  índice não se aplica à pilha, porque nela a remoção ocorre sempre no fim da lista. A solução foi usar
  `pop()` sem índice.
- **Consultar sem remover.** Para implementar `topo`, foi necessário acessar o último elemento sem
  alterar a pilha. O índice `-1` resolve isso: em Python, ele representa o último elemento de uma lista.
- **Restaurar o texto correto.** Guardar apenas o trecho digitado não seria suficiente para todos os
  tipos de ação possíveis, como exclusão e formatação. Registrar o estado anterior junto da ação torna
  o desfazer direto e também permite ampliar a simulação depois.

### Conclusão

A pilha modela a operação de desfazer de forma direta: cada ação é guardada quando acontece e a última
é recuperada primeiro quando o usuário decide revertê-la. A simulação confirma esse comportamento com
dez ações e termina no mesmo estado inicial, sem usar estruturas prontas além da lista que serve de
armazenamento interno.

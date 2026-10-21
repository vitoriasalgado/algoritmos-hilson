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
  Paciente: Ana Beatriz Souza | Chegada: 08:04 | Tipo: Retorno
  Espera: 0 min | Início: 08:04 | Fim: 08:14

  ...

  Paciente: Olivia Ribeiro Sampaio | Chegada: 09:57 | Tipo: Consulta
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

A fila manteve a ordem de chegada dos 15 pacientes. Na simulação, a duração de cada atendimento
afetou a espera dos seguintes, resultando em uma média de 117,7 minutos. Esse resultado depende
das durações estimadas para cada tipo de atendimento, já que o CSV não fornece esse dado.
A aplicação mostra como uma fila pode organizar um atendimento sequencial e permitir a análise
do tempo de espera sob condições definidas.

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

A simulação processou 14 trabalhos na ordem de envio após cancelar o último dos 15 trabalhos
recebidos. A estrutura do problema 1 foi reaproveitada com a inclusão da listagem e do cancelamento.
Essas operações permitem consultar os trabalhos pendentes e retirar o último envio sem alterar
a ordem dos demais, mantendo o comportamento esperado de uma fila de impressão.

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

As dez ações de digitação foram desfeitas na ordem inversa à execução, até o texto retornar ao
estado inicial vazio. Esse comportamento demonstra a relação entre a pilha e a função de desfazer:
a última ação registrada é a primeira a ser revertida. A demonstração se limita à digitação;
outras ações de edição exigem registrar as informações necessárias para restaurar o estado anterior.

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

Das seis expressões verificadas, quatro foram classificadas como válidas e duas como inválidas.
A pilha permitiu conferir se cada fechamento correspondia à abertura mais recente e identificar
aberturas que permaneceram sem fechamento. A mesma estrutura usada para desfazer ações no problema 3
foi aplicada à validação de delimitadores, que também depende da ordem inversa dos elementos.

---

## Situação-Problema 11 — Roteamento de Entrega de Mercadorias (Caminho Mínimo)

**Estrutura utilizada:** Grafo ponderado com listas de adjacência, `python/nao_lineares/grafo.py`.
**Algoritmo:** Dijkstra com seleção por varredura linear.
**Linguagem:** Python.

### Justificativa de escolha

Os pontos do mapa são vértices e as ruas são arestas com distâncias em quilômetros.
Um grafo permite representar as diferentes rotas entre esses pontos. O Dijkstra compara
os custos acumulados para encontrar a rota de menor distância, considerando pesos não negativos.
O menor número de ruas percorridas não implica necessariamente a menor distância.

### Implementação e simulação

A função `problema_11()` monta um mapa fictício com 10 locais e 13 ruas de mão dupla.
As listas `vertices` e `adjacencias` usam índices correspondentes. Cada conexão armazena
o índice do vizinho e seu peso; uma rua é registrada nos dois sentidos.

O Dijkstra mantém listas de distâncias, visitados e anteriores. A origem começa com distância
zero e os demais vértices, com infinito. A cada etapa, uma varredura escolhe o vértice não
visitado de menor distância conhecida e atualiza os custos dos vizinhos quando encontra
uma rota mais barata. Não é utilizada fila de prioridade pronta.

O método `menor_caminho()` segue os anteriores do destino até a origem e monta a sequência
na ordem correta. Para um destino inacessível, retorna lista vazia e distância infinita.
A inserção de arestas rejeita pesos negativos e extremos não cadastrados.

### Complexidade

Para V vértices e E ruas, a consulta usa O(V² + E) de tempo e O(V) de espaço auxiliar.
A reconstrução do caminho usa O(V) de tempo e espaço no pior caso. O armazenamento do grafo
ocupa O(V + E), mesmo registrando cada rua nos dois sentidos. A montagem também faz buscas
lineares por nomes: cadastrar os vértices custa O(V²) e inserir as ruas, O(EV) no pior caso.

### Exemplo de execução

Trecho de `saidas/problema_11.txt`:

```text
Origem: Casa | Destino: Universidade
Menor caminho: Casa → Praça → Escola → Biblioteca → Parque → Universidade
Distância total: 10 km
```

A rua direta entre Casa e Universidade tem 15 km. A rota encontrada passa por cinco ruas,
com distâncias 2 + 2 + 1 + 2 + 3, totalizando 10 km.
O arquivo completo registra todos os locais, ruas, trechos percorridos, resumo e complexidade.

### Dificuldades encontradas e soluções adotadas

- **Relacionar nomes e posições:** as conexões usam índices, e a apresentação converte esses
  índices novamente para os nomes dos locais.
- **Reconstruir a rota:** a lista de anteriores permite recuperar o caminho depois de calcular
  as distâncias, sem guardar uma cópia completa da rota a cada atualização.
- **Tratar pontos inacessíveis:** quando não existe vértice alcançável a visitar, a busca termina;
  a simulação prevê uma mensagem para a ausência de caminho.

### Conclusão

A consulta demonstrada encontra uma rota de 10 km, mais curta que a ligação direta de 15 km.
A saída foi gerada por execução e conferida com o conteúdo exibido no terminal.
A simulação atual consulta somente Casa e Universidade; a demonstração de rotas entre diferentes
pares de pontos, solicitada no enunciado, ainda precisa ser acrescentada.

---

## Situação-Problema 12 — Rede de Amizades em uma Rede Social Simples

**Estrutura utilizada:** Grafo não direcionado e fila de implementação própria.
**Algoritmo:** Busca em largura (BFS) para graus de separação.
**Linguagem:** Python.

### Justificativa de escolha

Cada usuário é um vértice e cada amizade liga duas pessoas nos dois sentidos. O grafo permite
consultar amigos diretos, identificar amigos em comum e explorar conexões indiretas.
A BFS visita primeiro as pessoas a uma ligação de distância, depois as que estão a duas,
e assim por diante, encontrando o menor número de ligações entre duas pessoas.

### Implementação e simulação

A função `problema_12()` cadastra 15 usuários e 14 amizades. Olivia fica sem conexões para
demonstrar o caso de uma pessoa isolada. O método `adicionar_amizade()` valida os nomes,
rejeita amizade da pessoa consigo mesma e retorna `False` quando a amizade já existe.
Cada amizade usa peso 1 para aproveitar o armazenamento do grafo; a BFS conta ligações.

`listar_amigos()` converte os índices dos vizinhos em nomes. `amigos_em_comum()` compara
as duas listas e retorna os nomes presentes em ambas. As sugestões são amigos de amigos,
excluindo a própria pessoa, amigos diretos e sugestões repetidas.

`graus_de_separacao()` usa a classe `Fila`, de `python/lineares/fila.py`, e uma lista de
distâncias iniciada com -1. A origem recebe zero. Cada vizinho recebe a distância do vértice
atual mais um e é marcado antes de entrar na fila, evitando visitas repetidas em ciclos.
O retorno é -1 quando não há conexão e zero quando origem e destino são a mesma pessoa.

### Complexidade

A BFS custa O(V + E) de tempo e O(V) de espaço auxiliar, para V usuários e E amizades.
Listar amigos custa O(V + d), incluindo a busca do nome e os d vizinhos. Amigos em comum
custam O(V + d1 + d2 + d1*d2), porque a busca com `in` percorre uma lista; d1 e d2 são
as quantidades de amigos das duas pessoas. Essas consultas usam O(V) de espaço no pior caso.

As sugestões podem custar O(V³) no pior caso de um grafo simples denso, devido às consultas
repetidas e às buscas lineares nas listas de amigos e sugestões. Seu espaço auxiliar é O(V).
O grafo ocupa O(V + E). A consulta de sugestões é uma demonstração simples, sem ranking
por quantidade de amigos em comum.

### Exemplo de execução

Trechos de `saidas/problema_12.txt`:

```text
Ana e Carla: Bruno, Daniel
Ana e Olivia: nenhum
Para Ana: Carla, Elisa
Ana → Bruno: 1 ligação(ões)
Ana → Carla: 2 ligação(ões)
Ana → Nicolas: 11 ligação(ões)
Ana → Olivia: sem conexão
Ana → Ana: 0 ligação(ões)
```

O registro completo contém as amizades, a lista de amigos dos 15 usuários, duas consultas
de amigos em comum, uma de sugestões e cinco consultas de graus de separação.

### Dificuldades encontradas e soluções adotadas

- **Distinguir índices e nomes:** as operações percorrem os índices das adjacências e retornam
  nomes para que os resultados sejam legíveis.
- **Evitar repetições:** a inserção verifica amizades existentes; a busca em largura marca
  os vértices antes de enfileirar; a sugestão verifica se o candidato já foi incluído.
- **Tratar ausência de relações:** listas vazias e o retorno -1 recebem mensagens específicas
  na simulação, sem confundir pessoa isolada com usuário não cadastrado.

### Conclusão

A rede demonstra amizades diretas, amigos em comum, sugestões e graus de separação,
incluindo uma pessoa isolada. Foram verificadas amizades repetidas, ciclos, nomes não
cadastrados e a consulta de uma pessoa para ela mesma. A saída salva corresponde ao terminal.

---

## Situação-Problema 15 — Ordenação de Notas de uma Turma Pequena

**Algoritmo principal:** Bubble Sort com parada antecipada.
**Comparação:** Selection Sort.
**Linguagem:** Python.

### Justificativa de escolha

O Bubble Sort atende ao algoritmo solicitado e permite acompanhar a ordenação de um conjunto
pequeno de notas por comparações entre vizinhos. A otimização encerra as passagens quando
não ocorre nenhuma troca. O Selection Sort foi usado para comparar as quantidades de operações:
ele procura o menor valor restante e faz no máximo uma troca por passagem.

### Implementação e simulação

A função `problema_15()` usa 15 notas entre 0 e 10, incluindo duas notas 5,5. Cada algoritmo
recebe uma cópia independente da mesma lista original, evitando que o segundo algoritmo
receba dados já ordenados pelo primeiro. Ambos alteram sua cópia e retornam comparações,
trocas e tempo em segundos. A contagem inclui somente comparações entre valores e trocas
efetivas de pares; verificações de índices e dos laços não entram nesses contadores.

### Complexidade

| Algoritmo | Melhor caso | Caso médio | Pior caso | Espaço auxiliar |
| --- | --- | --- | --- | --- |
| Bubble Sort otimizado | O(n) | O(n²) | O(n²) | O(1) |
| Selection Sort | O(n²) | O(n²) | O(n²) | O(1) |

n representa a quantidade de notas. A simulação usa O(n) de espaço adicional para as cópias
e os registros, considerando tamanho limitado por nota.

### Exemplo de execução

Entrada:

```
[7.5, 4, 9, 6.5, 8, 5, 10, 3.5, 5.5, 6, 8.5, 2, 9.5, 5.5, 7]
```

Resultado de ambos os algoritmos:

```
[2, 3.5, 4, 5, 5.5, 5.5, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5, 10]
```

| Algoritmo | Comparações | Trocas |
| --- | --- | --- |
| Bubble Sort | 102 | 53 |
| Selection Sort | 105 | 13 |

O registro completo, com cabeçalho, entrada, resultados, tempos, resumo e complexidade,
está em `saidas/problema_15.txt`. Os tempos são medições de uma execução curta e não
permitem concluir que um algoritmo será sempre mais rápido.

### Dificuldades encontradas e soluções adotadas

- **Preservar condições iguais de comparação:** foram criadas cópias da entrada original
  antes de executar os algoritmos.
- **Exibir o resultado correto:** o rascunho repetia as notas originais na seção de resultados;
  a versão final exibe as listas efetivamente ordenadas.
- **Aplicar a parada antecipada:** o indicador de troca é reiniciado a cada passagem e
  consultado somente depois de percorrer os pares dessa passagem.

### Conclusão

Os dois algoritmos produziram a mesma sequência de notas, preservando os valores repetidos.
Nessa entrada, o Bubble Sort realizou três comparações a menos, enquanto o Selection Sort realizou
40 trocas a menos. Portanto, a redução de comparações não veio acompanhada de uma redução de trocas.
O resultado permite comparar o trabalho realizado por cada algoritmo nesse conjunto, mas não
estabelece qual deles será mais rápido para qualquer entrada.

---

## Situação-Problema 16 — Ordenação de Produtos por Preço em uma Vitrine Pequena

**Algoritmo:** Bubble Sort com parada antecipada.
**Linguagem:** Python.

### Justificativa de escolha

O enunciado solicita Bubble Sort para uma vitrine pequena. A demonstração usa 15 produtos,
um volume que permite observar as trocas entre vizinhos sem exigir um algoritmo voltado a
grandes conjuntos. A comparação considera apenas o preço e preserva a ordem relativa de
produtos com preços iguais.

### Implementação e simulação

A função `problema_16()` lê `dados/produtos.csv` e seleciona os primeiros 15 registros após
o cabeçalho. Cada produto é representado por `[nome, preco]`, convertendo o preço para `float`.
Duas cópias da lista original são usadas para demonstrar as duas opções de ordenação.

A chamada com `indice_preco=1` ordena pelo preço crescente; a opção `decrescente=True`
ordena pelo preço decrescente. O algoritmo troca registros inteiros, mantendo nome e preço
associados. As cópias superficiais são suficientes porque somente a posição dos registros
é alterada, não seu conteúdo. A opção é fornecida como parâmetro, e a simulação demonstra
ambos os valores sem solicitar entrada interativa.

### Complexidade

O Bubble Sort usa O(n) de tempo no melhor caso, O(n²) no caso médio e no pior caso, e O(1)
de espaço auxiliar. Aqui n é o número de produtos selecionados.
A leitura materializa as m linhas do CSV, custando O(m) de tempo e espaço. As cópias e os
registros da simulação usam O(n), considerando tamanho limitado por registro. Assim, a
simulação completa tem tempo O(m + n²) no pior caso e espaço O(m + n).

### Exemplo de execução

Trechos dos resultados; o registro completo das duas listas está em `saidas/problema_16.txt`.

```
PROBLEMA 16 — ORDENAÇÃO DE PRODUTOS POR PREÇO
Algoritmo: Bubble Sort | Linguagem: Python

ENTRADA: 15 produtos de dados/produtos.csv

RESULTADOS — ORDEM CRESCENTE:
  Parafuso Frances 10mm | R$ 90.96
  ...
  Telha Galvanizada Trapezio | R$ 705.17

RESULTADOS — ORDEM DECRESCENTE:
  Telha Galvanizada Trapezio | R$ 705.17
  ...
  Parafuso Frances 10mm | R$ 90.96
```

| Ordem | Comparações | Trocas |
| --- | --- | --- |
| Crescente | 95 | 48 |
| Decrescente | 99 | 57 |

Os tempos medidos estão no arquivo de saída e variam entre execuções. Foram conferidas
as duas ordens, a preservação dos 15 registros e a igualdade entre a saída do terminal e
o arquivo salvo. Também foi verificada a preservação da ordem entre preços iguais e a
compatibilidade do Bubble Sort com listas numéricas.

### Dificuldades encontradas e soluções adotadas

- **Distinguir posições:** o preço está na coluna 3 do CSV, mas na posição 1 do registro
  `[nome, preco]`. A conversão numérica evita comparar preços como texto.
- **Posicionar as cópias:** elas são criadas depois do laço de leitura, quando todos os
  produtos já foram adicionados à lista.
- **Separar as medições:** os sufixos `_c` e `_d` preservam os contadores e tempos de cada
  direção, evitando que a segunda chamada sobrescreva os resultados da primeira.
- **Adaptar a comparação:** o algoritmo seleciona os preços antes de decidir a troca,
  mantendo o funcionamento anterior para notas quando `indice_preco` é `None`.

### Conclusão

Os 15 produtos foram apresentados em ordem crescente e decrescente de preço, mantendo cada nome
associado ao seu valor. A mudança do sentido da comparação permitiu oferecer as duas opções
com a mesma função de ordenação. O Bubble Sort é suficiente para a vitrine pequena proposta;
em um catálogo maior, seu custo O(n²) no pior caso motivaria a escolha de outro algoritmo.

---

## Situação-Problema 17 — Seleção dos Melhores Candidatos em um Processo Seletivo

**Algoritmo:** Selection Sort.
**Linguagem:** Python.

### Justificativa de escolha

O Selection Sort permite procurar sucessivamente a maior pontuação restante e colocá-la
na próxima posição do ranking. Esse funcionamento atende ao algoritmo solicitado pelo
enunciado e facilita selecionar os melhores candidatos depois de ordenar a lista.
Em cada passagem, realiza no máximo uma troca, embora mantenha custo quadrático de tempo.

### Implementação e simulação

A função `problema_17()` define no próprio código 15 candidatos no formato `[nome, pontuacao]`,
conforme o planejamento dos dados do projeto. Uma cópia preserva a ordem original para
exibir a entrada. A chamada `selection_sort(candidatos_ordenados, indice_pontuacao=1,
decrescente=True)` compara as pontuações e troca os registros inteiros, mantendo nomes
e pontuações associados.

A quantidade selecionada é definida como 5. Depois da ordenação, a fatia
`candidatos_ordenados[:quantidades_selecionados]` recupera os cinco primeiros colocados.
A execução mostra a entrada, o ranking completo, o Top 5 e as medições, salvando o mesmo
conteúdo em `saidas/problema_17.txt`.

### Complexidade

| Etapa | Tempo | Espaço auxiliar |
| --- | --- | --- |
| Selection Sort | O(n²) em todos os casos | O(1) |
| Cópia da entrada | O(n) | O(n) |
| Extração dos Top-N | O(k) | O(k) |
| Simulação completa, com k ≤ n | O(n²) | O(n) |

n representa a quantidade de candidatos e k a quantidade selecionada. A análise dos
registros considera tamanho limitado por nome e pontuação. O algoritmo faz n(n − 1)/2
comparações entre valores; para 15 candidatos, são 105 comparações.

### Exemplo de execução

Trecho do resultado; a entrada e o ranking completos estão em `saidas/problema_17.txt`.

```
PROBLEMA 17 — SELEÇÃO DOS MELHORES CANDIDATOS
Algoritmo: Selection Sort | Linguagem: Python

ENTRADA: 15 candidatos

TOP 5:
  1. Isabela | Pontuação: 97
  2. Eduarda | Pontuação: 95
  3. Maria | Pontuação: 94
  4. Bruno | Pontuação: 92
  5. Edson | Pontuação: 91

RESUMO:
  5 candidatos selecionados.
  Comparações: 105
  Trocas: 12
```

Foram conferidos a ordem decrescente do ranking, a presença dos 15 candidatos com suas
pontuações originais, a seleção dos cinco primeiros e a igualdade entre terminal e arquivo.
Os tempos observados estão no arquivo e variam entre execuções.

### Dificuldades encontradas e soluções adotadas

- **Escolher o campo antes de comparar:** as variáveis com a pontuação atual e a pontuação
  escolhida são definidas antes da condição de comparação.
- **Preservar o problema 15:** os padrões `indice_pontuacao=None` e `decrescente=False`
  mantêm a ordenação numérica crescente; as opções específicas são passadas na chamada
  do problema 17.
- **Criar a cópia:** usar `copy()` com parênteses para obter uma lista, em vez de guardar
  o método `copy`.
- **Separar argumentos:** inserir a vírgula entre `indice_pontuacao=1` e `decrescente=True`.

### Conclusão

A ordenação dos 15 candidatos por pontuação permitiu selecionar os cinco primeiros colocados.
Como a entrada não contém empates, a pontuação foi suficiente para definir esse resultado.
Em uma seleção com notas iguais, seria necessário estabelecer um critério de desempate, pois
o Selection Sort utilizado não garante a manutenção da ordem original entre candidatos empatados.

---

## Situação-Problema 18 — Organização de Tarefas por Prioridade (Valor Numérico)

**Algoritmo:** Selection Sort.
**Linguagem:** Python.

### Justificativa de escolha

O Selection Sort atende ao algoritmo solicitado e permite selecionar sucessivamente a
tarefa de maior prioridade restante. Foi adotada a convenção de que 1 é a prioridade
mais alta e 5 a mais baixa; portanto, os números são ordenados de forma crescente.
Para as 15 tarefas da demonstração, o funcionamento simples permite acompanhar a
organização de um dia de trabalho, embora o custo seja quadrático.

### Implementação e simulação

A função `problema_18()` define 15 registros `[descricao, prioridade]` no próprio código,
conforme o planejamento do projeto. Uma cópia mantém a entrada original disponível para
exibição. A chamada `selection_sort(tarefas_ordenadas, indice_pontuacao=1)` usa o campo
numérico na posição 1 para ordenar. O nome do parâmetro vem da aplicação aos candidatos,
mas representa o índice do campo comparado; nesta aplicação, esse campo é a prioridade.

A saída mostra a lista original e a ordem de execução. Depois, a simulação percorre a
lista ordenada e registra cada tarefa como concluída. É uma demonstração sequencial,
sem medir a duração real das atividades. O tempo apresentado é somente o da ordenação.
O mesmo conteúdo é mostrado no terminal e salvo em `saidas/problema_18.txt`.

### Complexidade

O Selection Sort faz n(n − 1)/2 comparações e usa O(n²) de tempo em todos os casos,
com O(1) de espaço auxiliar. A cópia, a exibição e o percurso das tarefas custam O(n).
A simulação completa tem tempo O(n²) e espaço O(n), incluindo cópia e registros,
considerando tamanho limitado por descrição. n é o número de tarefas.

### Exemplo de execução

Trecho do resultado; a lista completa e a simulação estão em `saidas/problema_18.txt`.

```
PROBLEMA 18 — ORGANIZAÇÃO DE TAREFAS POR PRIORIDADE
Algoritmo: Selection Sort | Linguagem: Python

ENTRADA: 15 tarefas de um dia de trabalho
  Prioridades de 1 (mais alta) a 5 (mais baixa).

RESULTADOS — ORDEM DE EXECUÇÃO DO DIA:
  1. Prioridade 1 | Enviar trabalho com prazo para hoje
  2. Prioridade 1 | Preparar apresentação para amanhã
  3. Prioridade 1 | Confirmar a entrega na plataforma
  ...
  15. Prioridade 5 | Revisar anotações antigas

RESUMO:
  15 tarefas executadas em ordem de prioridade.
  Comparações: 105
  Trocas: 8
```

Foram verificados a quantidade de tarefas, os valores de prioridade entre 1 e 5,
a preservação dos registros, a ordenação crescente das prioridades e o percurso da
simulação na mesma ordem. O arquivo gerado corresponde à saída do terminal.

### Cuidados de implementação e soluções adotadas

- **Definir o sentido da prioridade:** a convenção numérica é informada na entrada para
  deixar claro por que a ordenação é crescente.
- **Preservar descrição e prioridade:** o algoritmo troca o registro inteiro, mantendo
  as duas informações associadas.
- **Tratar empates com transparência:** existem tarefas com prioridades iguais. A troca
  direta do Selection Sort pode mudar a ordem relativa delas, o que é informado na saída.
  Nenhum critério adicional de desempate foi exigido ou aplicado.

### Conclusão

As 15 tarefas foram organizadas e executadas na simulação conforme a prioridade, considerando
1 como a mais alta e 5 como a mais baixa. A ordenação permite definir uma sequência de atendimento
a partir desse critério. Entretanto, tarefas com a mesma prioridade podem mudar de posição entre
si, pois a implementação não é estável. Se a ordem de chegada também fosse um requisito,
seria necessário considerá-la na solução.

---

## Apêndice A — Algoritmos de ordenação implementados

Esta seção documenta os algoritmos reutilizáveis antes de sua aplicação às situações-problema.
A implementação isolada de um algoritmo não significa que os problemas que dependem dele
estejam concluídos.

### Bubble Sort — Python

**Arquivo:** `python/ordenacao/bubble_sort.py`
**Função:** `bubble_sort(valores, indice_preco=None, decrescente=False)`

#### Justificativa de escolha

O Bubble Sort é solicitado nos problemas 15 e 16. Para uma turma de até 30 alunos e uma
vitrine de 10–20 produtos, seu funcionamento simples permite acompanhar as comparações e
as trocas. A estabilidade preserva a ordem relativa de registros com a mesma chave quando
a comparação considera apenas a nota ou o preço. A parada antecipada evita passagens
desnecessárias em entradas já ordenadas. O custo quadrático, porém, o torna pouco adequado
para grandes volumes. As aplicações a notas e produtos estão descritas nos problemas 15 e 16.

#### Funcionamento e decisões de implementação

A função percorre a lista comparando elementos vizinhos. Por padrão compara números diretamente;
com `indice_preco=1`, compara os preços dos registros de produtos. Na ordem crescente, troca
quando o valor à esquerda é maior; na decrescente, quando é menor. A troca usa uma variável
temporária e move o registro inteiro. A cada passagem, um elemento chega à posição final
da parte ainda não ordenada, conforme a direção escolhida.

O laço externo realiza até n − 1 passagens. O laço interno faz n − 1 − i comparações na passagem i,
evitando a parte final já ordenada. A variável `houve_troca` começa como `False` em cada passagem
e passa a `True` quando ocorre uma troca. Se a passagem terminar sem trocas, `break` encerra
o laço externo: a lista já está ordenada. Essa otimização atende à exigência de parada antecipada
do problema 15 (página 11 do enunciado).

A lista recebida é modificada diretamente. A função retorna `comparacoes, trocas, tempo`.
A contagem de comparações aumenta a cada comparação entre valores; a de trocas aumenta somente
quando um par efetivamente troca de posição. O tempo é medido em segundos por `perf_counter()`,
com a leitura inicial antes dos laços e a final após todas as passagens.

#### Complexidade e propriedades

| Aspecto | Resultado |
| --- | --- |
| Comparações no melhor caso, para n ≥ 2 | n − 1 |
| Comparações no pior caso, para n ≥ 1 | n(n − 1)/2 |
| Tempo no melhor caso | O(n), com entrada já ordenada |
| Tempo no caso médio e no pior caso | O(n²) |
| Espaço auxiliar | O(1), pois não cria outra lista |
| Estabilidade | Preserva a ordem relativa dos elementos iguais |

Para uma lista vazia ou com um único elemento, são feitas zero comparações. Elementos iguais não são trocados porque
a condição usa maior que, sem incluir igualdade. O algoritmo foi escolhido como uma base
simples de comparação; seu custo quadrático limita o uso com listas grandes.

#### Verificação por execução

| Entrada | Resultado | Comparações | Trocas |
| --- | --- | --- | --- |
| [5, 3, 8, 1] | [1, 3, 5, 8] | 6 | 4 |
| [] | [] | 0 | 0 |
| [7] | [7] | 0 | 0 |
| [1, 2, 3] | [1, 2, 3] | 2 | 0 |
| [3, 2, 1] | [1, 2, 3] | 3 | 3 |
| [2, -1, 2, 0] | [-1, 0, 2, 2] | 6 | 3 |
| [2, 2, 2] | [2, 2, 2] | 2 | 0 |

As sete execuções produziram os resultados e contadores esperados. O registro está em
`saidas/bubble_sort.txt`. Os tempos registrados variam entre execuções e, para entradas tão
pequenas, não sustentam uma comparação de desempenho entre algoritmos.

#### Dificuldades encontradas e soluções adotadas

A implementação foi feita por etapas, com revisão dos limites dos laços e dos recuos.
A variável temporária preserva o primeiro valor durante a troca. A contagem de trocas fica
dentro do `if`, enquanto o cálculo do tempo e o retorno ficam fora dos dois laços, para
serem executados somente após terminar a ordenação. Na otimização, a verificação de
`houve_troca` fica após o laço interno, para avaliar a passagem inteira, e o indicador é
reiniciado a cada passagem.

#### Conclusão sobre a aplicabilidade

A verificação isolada mostrou o funcionamento da ordenação e dos contadores. A parada após uma
passagem sem trocas evita percursos desnecessários quando a lista já está ordenada, embora o pior
caso continue sendo O(n²). As aplicações dos problemas 15 e 16 mostram seu uso em conjuntos pequenos
de notas e produtos, nos quais é possível acompanhar as comparações e trocas realizadas.

#### Estado de conclusão

A função otimizada e sua verificação isolada estão concluídas. O problema 15 já aplica a
ordenação às notas e compara as contagens com o Selection Sort. O problema 16 demonstra
15 produtos em ordem crescente e decrescente de preço. As duas aplicações possuem saídas
numeradas e análises específicas neste relatório.

### Selection Sort — Python

**Arquivo:** `python/ordenacao/selection_sort.py`
**Função:** `selection_sort(valores, indice_pontuacao=None, decrescente=False)`

#### Justificativa de escolha

O Selection Sort é solicitado nos problemas 17 e 18 e também participa da comparação de
operações do problema 15. Seu funcionamento permite observar a busca do menor elemento
restante e limita as trocas a, no máximo, uma por passagem. Essa característica é útil
para discutir situações em que mover registros tem um custo elevado, embora a busca
continue exigindo uma quantidade quadrática de comparações.

#### Funcionamento e decisões de implementação

A função modifica a lista recebida. Por padrão, ordena números em ordem crescente.
Com `indice_pontuacao=1`, compara o campo de pontuação dos registros, e com
`decrescente=True`, escolhe o maior valor restante. Para cada posição `i`, começa com
`menor = i` e percorre as posições seguintes. Apesar do nome, `menor` guarda a posição
escolhida conforme a direção: menor na crescente ou maior na decrescente.

Depois de terminar a busca, se `menor != i`, o algoritmo troca os dois elementos usando uma
variável temporária. Caso o elemento escolhido já esteja na posição correta, não realiza troca.
A função retorna `comparacoes, trocas, tempo`, com o tempo em segundos medido por
`perf_counter()`. São contadas apenas comparações entre valores e trocas efetivas de pares.

#### Complexidade e propriedades

| Aspecto | Resultado |
| --- | --- |
| Comparações, para n ≥ 1 | n(n − 1)/2 |
| Tempo no melhor, médio e pior caso | O(n²) |
| Número de trocas, para n ≥ 1 | No máximo n − 1 |
| Espaço auxiliar | O(1) |
| Estabilidade | A implementação com troca direta não garante estabilidade |

n representa a quantidade de elementos. Mesmo uma lista já ordenada passa por todas as
comparações, mas realiza zero trocas. Listas vazias ou com um elemento são aceitas e
realizam zero comparações e zero trocas. A troca direta pode alterar a ordem relativa de
registros com chaves iguais; essa propriedade deverá ser considerada nas aplicações.

#### Verificação por execução

| Entrada | Resultado | Comparações | Trocas |
| --- | --- | --- | --- |
| [5, 3, 8, 1] | [1, 3, 5, 8] | 6 | 2 |
| [] | [] | 0 | 0 |
| [7] | [7] | 0 | 0 |
| [1, 2, 3] | [1, 2, 3] | 3 | 0 |
| [3, 2, 1] | [1, 2, 3] | 3 | 1 |
| [2, -1, 2, 0] | [-1, 0, 2, 2] | 6 | 2 |

Os seis casos produziram a ordenação e os contadores esperados. O registro da execução,
incluindo os tempos observados, está em `saidas/selection_sort.txt`. Os tempos variam entre
execuções e não sustentam conclusões de desempenho geral para entradas tão pequenas.

#### Dificuldades encontradas e soluções adotadas

- **Chamar o relógio:** usar `perf_counter()` com parênteses para obter uma leitura numérica,
  em vez de guardar a própria função.
- **Atualizar a posição do menor:** colocar a atribuição `menor = j` dentro do `if`,
  evitando atualizar a posição em toda comparação.
- **Trocar depois da busca:** posicionar o bloco de troca fora do laço interno, para
  realizar uma única troca depois de examinar toda a parte restante.
- **Evitar atribuição duplicada:** remover a linha antiga `inicio = perf_counter`,
  mantendo somente a chamada que registra o tempo inicial.

#### Conclusão sobre a aplicabilidade

Nas entradas verificadas, o Selection Sort ordenou os elementos procurando o menor valor da parte
restante a cada passagem. Essa busca mantém o número de comparações mesmo quando a entrada já está
ordenada, enquanto as trocas dependem das posições dos elementos. A comparação do problema 15
e as aplicações dos problemas 17 e 18 mostram essas características em contextos diferentes.

#### Estado de conclusão

A função e sua verificação numérica isolada estão concluídas. Ela é usada na comparação
do problema 15, na ordenação dos candidatos do problema 17 e na organização das tarefas
do problema 18. As aplicações possuem saídas numeradas e documentação própria.

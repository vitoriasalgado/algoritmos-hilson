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

O problema exige que os pacientes sejam atendidos exatamente na ordem de chegada — é a definição de
um comportamento FIFO (First-In, First-Out), que é a garantia central de uma fila. Não há necessidade
de acessar um paciente no meio da estrutura, nem de reordenar por prioridade: a única operação
relevante é "quem chegou primeiro é atendido primeiro". Uma pilha inverteria essa ordem; uma lista com
acesso por posição arbitrária ofereceria capacidade que o problema não usa. A fila é a estrutura que
expressa exatamente a regra de negócio, sem funcionalidade sobrando.

### Decisão de implementação

A restrição do item 5.4 do enunciado proíbe o uso de `collections.deque` ou qualquer fila pronta da
biblioteca padrão. A implementação usa uma lista (`list`) como área de armazenamento, mantendo um
índice `_inicio` que avança a cada remoção em vez de deslocar fisicamente os elementos. Essa escolha
mantém `enfileirar` e `desenfileirar` em O(1) — a alternativa mais direta, remover o primeiro elemento
de uma lista com `pop(0)`, é O(n), porque desloca todos os elementos restantes uma posição. O
custo dessa escolha é que a lista interna nunca encolhe: elementos já atendidos continuam ocupando
posição em memória até o fim da execução. Para o volume do problema (15 pacientes), esse custo é
irrelevante; para uma fila que rodasse indefinidamente, seria necessário reciclar o espaço — por
exemplo, com uma implementação circular.

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

A simulação processa a fila em ordem de chegada. Para cada paciente, o horário de início do
atendimento é o maior valor entre o horário de chegada dele e o horário em que o atendimento anterior
terminou; a espera é a diferença entre esses dois valores. Essa regra reproduz o comportamento real de
uma fila de atendimento único: se a demanda (frequência de chegadas) supera a capacidade de
atendimento (duração média), o atraso se acumula ao longo do dia — o que de fato ocorre com os dados
de teste, em que a espera cresce de 0 minutos (primeiro paciente) a 263 minutos (último paciente),
com média de 117,7 minutos.

### Complexidade

| Operação | Tempo | Espaço |
| --- | --- | --- |
| `enfileirar` | O(1) | — |
| `desenfileirar` | O(1) | — |
| Simulação completa (n pacientes) | O(n) | O(n) |

O espaço O(n) decorre de duas listas proporcionais ao número de pacientes: a área de armazenamento
interna da fila e a lista de tempos de espera mantida para o cálculo da média.

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

- **Indentação como estrutura de bloco.** O cálculo de espera de cada paciente foi escrito inicialmente
  fora do laço `while`, por engano de indentação — em Python, ao contrário de Java, o bloco pertencente
  a um `while`/`if`/`for` é definido pelo recuo do código, não por chaves. O erro não gerava exceção
  (o código era sintaticamente válido), só produzia um resultado funcionalmente errado: a simulação
  calculava a espera uma única vez, com os dados do último paciente, em vez de uma vez por paciente.
  Foi identificado ao inspecionar a saída do programa e comparar o número de linhas impressas com o
  número de pacientes esperado.
- **Docstring na mesma linha do `def`.** Uma tentativa inicial de documentar os métodos da fila colocou
  a docstring na mesma linha dos dois-pontos do `def`, o que gera `IndentationError` na linha seguinte
  — o corpo da função precisa começar em uma nova linha, indentado, quando a docstring está presente.
- **Gerenciamento do arquivo aberto.** A leitura do CSV inicialmente usava `open()` sem fechamento
  explícito do arquivo. Foi substituído por `with open(...) as arquivo:`, que garante o fechamento
  automático ao final do bloco, inclusive em caso de exceção.

### Conclusão

A fila resolveu o problema sem exigir nenhuma operação fora do seu contrato natural (inserir no fim,
remover do início). A parte não trivial do problema não estava na estrutura de dados em si, mas na
modelagem da simulação — decidir como estimar uma duração de atendimento a partir de um dado que o
CSV não fornece, e propagar corretamente o efeito de fila (atraso acumulado) ao longo do dia.

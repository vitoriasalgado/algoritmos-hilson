---
title: "Relatório de Decisões de Projeto"
subtitle: "Trabalho Prático de Estrutura de Dados"
author:
  - "João Guilherme — Matrícula: "
  - "Vitória — Matrícula: "
date: "Teresina — PI, 2026"
lang: pt-BR
---

<!--
Documento de trabalho. Editar aqui durante o desenvolvimento.
Converter para entrega com:
    pandoc relatorio/relatorio-decisoes.md -o relatorio/Relatorio_de_Decisoes.docx
-->

**iCEV — Instituto de Ensino Superior** · Engenharia de Software, 2º Período

| Identificação | |
| --- | --- |
| Disciplina | Algoritmo e Estrutura de Dados I |
| Professor | Hilson Silva |
| Turmas | ADELLE e SNYDER |
| Avaliação | Trabalho Avaliativo — P2 |
| Data de entrega | 11 de novembro de 2026 |
| Aluno 1 | João Guilherme — Matrícula: |
| Aluno 2 | Vitória — Matrícula: |
| Repositório | github.com/vitoriasalgado/algoritmos-hilson |

---

## 1. Apresentação

Este relatório apresenta a organização do trabalho: a divisão dos 24 problemas, as linguagens,
os dados, o formato das saídas e os prazos.

O documento explica as escolhas feitas para o desenvolvimento. Os resultados, a complexidade,
os exemplos de entrada e saída e as dificuldades são apresentados no Relatório Técnico, previsto no item 5.2
do enunciado.

O registro dessas escolhas também permite esclarecer com o professor as dúvidas sobre o trabalho
ao longo do desenvolvimento.

## 2. Escopo e distribuição dos problemas

O enunciado divide os 24 problemas em três grupos. A tabela mostra essa divisão e os pesos da
avaliação, conforme o item 5.3.

| Grupo | Estruturas | Problemas | Peso |
| --- | --- | --- | --- |
| Estruturas lineares | Filas, Pilhas, Listas | 1 a 6 | 25% |
| Estruturas não-lineares | BST, AVL, Grafos, Hash | 7 a 14 | 30% |
| Algoritmos de ordenação | Bubble, Selection, Insertion, Merge, Quick | 15 a 24 | 20% |
| Justificativas e relatório | — | — | 15% |
| Organização e testes | — | — | 10% |

O planejamento inclui a implementação dos 24 problemas.

## 3. Decisão 1 — Linguagens de programação

As linguagens escolhidas são Java e Python, com autorização do professor. Essa autorização foi necessária porque
o item 1.2 do enunciado prevê a escolha de uma linguagem.

A distribuição das estruturas considera as características de cada linguagem e a facilidade de
acompanhar a lógica do código.

| Linguagem | Estruturas atendidas | Motivo da escolha |
| --- | --- | --- |
| Java | Listas encadeadas, Árvore Binária de Busca, Árvore AVL, Tabela Hash | As classes e os tipos ajudam a representar os nós e suas referências. O compilador também ajuda a identificar erros de tipo nas estruturas. |
| Python | Filas, Pilhas, Grafos, Algoritmos de Ordenação | A sintaxe mais curta facilita a leitura dos percursos em grafos e dos algoritmos de ordenação, incluindo a contagem das operações. |

Os arquivos de entrada serão os mesmos nas duas linguagens. O planejamento também prevê a comparação
de desempenho com os mesmos dados, como descrito na seção 6.

## 4. Decisão 2 — Divisão de responsabilidades

O trabalho está dividido por estrutura. Assim, cada integrante pode reaproveitar o código nos problemas
do seu bloco, evitando a edição dos mesmos arquivos ao mesmo tempo.

| Bloco | Problemas | Responsável | Motivo da divisão |
| --- | --- | --- | --- |
| Listas encadeadas | 5 e 6 | João Guilherme | Estrutura de nó reaproveitada nos blocos seguintes |
| Árvore Binária de Busca | 7 e 8 | João Guilherme | Evolução direta da estrutura de nó da lista |
| Árvore AVL | 9 e 10 | João Guilherme | Estende a BST com controle de balanceamento |
| Tabela Hash | 13 e 14 | João Guilherme | O encadeamento reutiliza a lista do bloco inicial |
| Filas | 1 e 2 | Vitória | Estrutura de fluxo, base para os demais blocos |
| Pilhas | 3 e 4 | Vitória | Complementa o bloco de estruturas lineares |
| Grafos | 11 e 12 | Vitória | Algoritmos de percurso e caminho mínimo |
| Algoritmos de ordenação | 15 a 24 | Vitória | Dez problemas apoiados em cinco implementações |

A tabela define o responsável pela implementação de cada bloco. Ambos acompanham o projeto completo e devem saber
explicar as estruturas usadas.

## 5. Decisão 3 — Fonte única de dados

As bases compartilhadas estão organizadas em arquivos na pasta `dados/`, para leitura em Java e Python.

Essa organização evita manter duas cópias diferentes dos dados, permite usar a mesma entrada nas
comparações e facilita a consulta aos valores sem depender da leitura do código.

### 5.1 Formato dos arquivos

- codificação UTF-8, sem BOM;
- separador de campos: vírgula;
- **separador decimal: ponto**, necessário para a leitura com `Double.parseDouble` em Java;
- cabeçalho fixo na primeira linha, com identificadores de coluna idênticos nas duas implementações;
- os arquivos são gerados uma vez e salvos no repositório. Cada execução usa os mesmos dados,
  permitindo repetir os testes.

| Arquivo | Colunas | Registros | Problemas atendidos |
| --- | --- | --- | --- |
| `pacientes.csv` | nome, horario, tipo | 15 | 1 |
| `impressoes.csv` | aluno, arquivo, paginas | 15 | 2 |
| `alunos.csv` | matricula, nome, curso | 20 | 7 |
| `produtos.csv` | codigo, nome, quantidade, preco | 100 | 6, 8, 16 e 23 |
| `funcionarios.csv` | cpf, nome, cargo | 25 | 9 |
| `jogadores.csv` | nome, pontuacao | 30 | 10 |
| `transacoes.csv` | data_hora, valor, descricao | 50 | 21 |
| `contatos_a.csv` e `contatos_b.csv` | nome, telefone | 20 + 20 | 22 |
| `atletas.csv` | nome, tempo | 30 | 24 |
| `usuarios.csv` | email, senha_hash | 20 | 13 |
| `texto.txt` | texto corrido | notícia real | 14 |

Nos problemas 15, 17, 18, 19 e 20, os dados são definidos no próprio código, pois são poucos e específicos
de cada simulação.

## 6. Decisão 4 — Contagem de operações e comparação dos resultados

Os cinco algoritmos de ordenação registram o número de comparações, o número de trocas e o tempo
de cada execução.

Essas medidas apoiam as comparações solicitadas nos problemas abaixo e a discussão da complexidade
do item 5.2.

| Situação | Exigência do enunciado | Atendimento |
| --- | --- | --- |
| 15 | Comparação do número de comparações e trocas entre algoritmos | Contadores apresentados ao final de cada ordenação |
| 20 | Comparação de desempenho entre lista ordenada e lista aleatória | Mesma implementação aplicada às duas entradas |
| 23 | Comparação de tempo entre Quick Sort e Merge Sort | Cronômetro em cada algoritmo |

**Comparação entre as linguagens.** Para a mesma entrada, a contagem esperada de comparações é a mesma
em Java e Python. Uma diferença nessa contagem será tratada como sinal de erro a revisar.
O tempo pode variar conforme o ambiente de execução. Essas diferenças serão discutidas
no Relatório Técnico.

## 7. Decisão 5 — Padronização das saídas

Cada situação-problema apresenta título numerado, estrutura e linguagem antes dos resultados,
seguidos de entrada, execução, resumo com operações relevantes e complexidade com suas variáveis
explicadas. Linhas em branco separam as seções. O terminal e o arquivo
`saidas/problema_NN.txt` apresentam o mesmo conteúdo detalhado.

Esse formato foi definido pela dupla para facilitar a identificação de cada problema
e a leitura dos exemplos de entrada e saída. O padrão se aplica às implementações atuais e futuras
em Python e Java.

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

## 8. Decisão 6 — Organização do código

O código é organizado por linguagem e tipo de estrutura. Cada estrutura tem seu módulo, classe
ou arquivo, conforme o item 5.1 do enunciado.

```
dados/          arquivos de entrada compartilhados
java/
  src/ed/lineares/      Lista, No
  src/ed/naolineares/   BST, AVL, TabelaHash
  src/ed/app/           programas principais
python/
  lineares/             fila.py, pilha.py
  nao_lineares/         grafo.py
  ordenacao/            sorts.py
  main.py               programas principais
saidas/         registros de execução
relatorio/      relatórios do projeto
```

### 8.1 Convenções adotadas

- nomes em português;
- mesmos nomes nas duas linguagens: um método chamado `inserir` em Python também se chama
  `inserir` em Java;
- comentários nas partes principais do código;
- tratamento dos erros previstos no item 5.1: estrutura vazia, chave inexistente e índice
  fora do intervalo.

## 9. Implementação do zero — item 5.4

O item 5.4 exige que as estruturas sejam implementadas do zero. Arrays e listas nativas podem ser
usados para armazenar os dados, mas a lógica das operações deve ser implementada pela dupla.
A tabela apresenta os recursos que não serão utilizados para substituir essas implementações.

| Linguagem | Recursos não utilizados | Recursos permitidos como base |
| --- | --- | --- |
| Python | `sorted`, `list.sort`, `dict` e `collections.Counter` empregados como tabela hash, `collections.deque`, `heapq`, `bisect` | `list` como área de armazenamento; estruturas de controle e recursão da linguagem |
| Java | `HashMap`, `TreeMap`, `LinkedList`, `ArrayDeque`, `PriorityQueue`, `Collections.sort`, `Arrays.sort` | arrays nativos e `ArrayList` exclusivamente como área de armazenamento |

**Problema 11 — Dijkstra.** A implementação não utilizará uma fila de prioridade pronta. As opções
são procurar o vértice de menor distância com uma varredura linear, com complexidade O(V²), suficiente
para os dez vértices solicitados, ou implementar um heap próprio.

## 10. Requisitos de execução

O projeto usa apenas a biblioteca padrão de Java e Python. Não é necessário instalar bibliotecas
extras nem usar gerenciadores de pacotes.

| Componente | Requisito |
| --- | --- |
| Python | versão 3.10 ou superior |
| Java | JDK 26 ou superior |
| Dependências | nenhuma — sem Maven, sem Gradle, sem instalação de bibliotecas |

**Ponto a confirmar com o professor.** O código Java foi desenvolvido no JDK 26.0.2. Os arquivos
compilados nessa versão não são executados em versões anteriores. É necessário confirmar qual versão
será usada na correção. Caso seja anterior, o ajuste previsto é gerar arquivos compatíveis por meio
da configuração de compilação, sem alterar o código-fonte.

## 11. Cronograma de execução

O cronograma foi reorganizado para que os integrantes trabalhem em paralelo a partir de 21 de setembro.
A previsão é concluir as implementações em 25 de outubro. As duas semanas seguintes estão reservadas
para integração, testes, relatório técnico e organização da entrega. Os dias 9 e 10 de novembro servem como margem
para ajustes antes da entrega em 11 de novembro.

| Sem. | Período | João Guilherme — Java | Vitória — Python |
| --- | --- | --- | --- |
| 1 | 21/09 a 27/09 | Listas encadeadas (5 e 6) | Problemas 1 e 2: revisão final das filas; concluir Pilhas (3 e 4) |
| 2 | 28/09 a 04/10 | Árvore Binária de Busca (7 e 8) | Implementar Bubble, Selection, Insertion, Merge e Quick Sort |
| 3 | 05/10 a 11/10 | Árvore AVL (9 e 10) | Aplicar ordenação: problemas 15 a 18 |
| 4 | 12/10 a 18/10 | Tabela Hash (13 e 14) | Grafos: problemas 11 e 12 |
| 5 | 19/10 a 25/10 | Revisão do bloco não-linear | Aplicar Merge e Quick: problemas 19 a 24 |
| 6 | 26/10 a 01/11 | Integração, saídas e Relatório Técnico | Integração, saídas e Relatório Técnico |
| 7 | 02/11 a 08/11 | Revisão final, testes e organização do arquivo `.zip` | Revisão final, testes e organização do arquivo `.zip` |
| — | 11/11 | Entrega pela plataforma iCEV Digital | Entrega pela plataforma iCEV Digital |

## 12. Documentos e arquivos para entrega

A entrega reunirá os seguintes arquivos, considerando os itens 1.3 e 5.2 do enunciado:

- ficha de identificação preenchida;
- código completo e funcional dos 24 problemas;
- Relatório Técnico com justificativa de cada estrutura e algoritmo, análise de complexidade de
  tempo e espaço, exemplos de entrada e saída, dificuldades encontradas e conclusão sobre a
  aplicabilidade das estruturas;
- registros de execução de todas as situações-problema;
- instruções para executar o projeto;
- este Relatório de Decisões de Projeto.

A entrega será feita pela plataforma iCEV Digital, conforme o enunciado.

## 13. Considerações finais

O objetivo do trabalho é compreender o funcionamento de cada estrutura por meio da implementação
e dos testes de suas operações.
Os dados e os registros de execução ajudam a conferir os resultados, e o Relatório Técnico explica
as escolhas e as dificuldades de cada problema.

O planejamento pode ser ajustado conforme a orientação do professor, incluindo os requisitos
de execução da seção 10.

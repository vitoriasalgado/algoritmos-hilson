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

Este relatório apresenta e fundamenta as decisões de projeto adotadas pela dupla para a execução do
Trabalho Prático de Estrutura de Dados, cujo escopo compreende vinte e quatro situações-problema
distribuídas entre estruturas lineares, estruturas não-lineares e algoritmos de ordenação.

O documento antecede o Relatório Técnico previsto no item 5.2 do enunciado e tem finalidade
distinta: enquanto aquele apresentará os resultados das implementações — complexidade obtida,
exemplos de entrada e saída, dificuldades encontradas e conclusões —, este registra as escolhas
estruturais que orientam o desenvolvimento e as razões técnicas que as sustentam.

A submissão antecipada tem por objetivo permitir que eventuais divergências de entendimento quanto
ao escopo sejam identificadas antes do início da implementação, e não na correção.

## 2. Escopo e distribuição dos problemas

O enunciado estabelece vinte e quatro situações-problema, organizadas em três grupos. A tabela a
seguir consolida a distribuição e o peso atribuído a cada grupo conforme o item 5.3.

| Grupo | Estruturas | Problemas | Peso |
| --- | --- | --- | --- |
| Estruturas lineares | Filas, Pilhas, Listas | 1 a 6 | 25% |
| Estruturas não-lineares | BST, AVL, Grafos, Hash | 7 a 14 | 30% |
| Algoritmos de ordenação | Bubble, Selection, Insertion, Merge, Quick | 15 a 24 | 20% |
| Justificativas e relatório | — | — | 15% |
| Organização e testes | — | — | 10% |

Todas as vinte e quatro situações serão implementadas, sem supressão ou substituição.

## 3. Decisão 1 — Linguagens de programação

A dupla utilizará duas linguagens: Java e Python. A adoção conjunta foi previamente autorizada pelo
professor da disciplina, uma vez que o item 1.2 do enunciado prevê a escolha de uma linguagem entre
as três permitidas.

A alocação não é arbitrária. Cada linguagem foi atribuída ao grupo de estruturas em que suas
características contribuem para evidenciar o funcionamento interno das implementações, que é o foco
declarado no item 5.4.

| Linguagem | Estruturas atendidas | Fundamentação técnica |
| --- | --- | --- |
| Java | Listas encadeadas, Árvore Binária de Busca, Árvore AVL, Tabela Hash | A tipagem estática e a orientação a objetos tornam explícita a relação entre nó e referência, que constitui o núcleo dessas estruturas. A verificação em tempo de compilação auxilia na detecção de inconsistências nas rotações da AVL e no encadeamento da tabela hash. |
| Python | Filas, Pilhas, Grafos, Algoritmos de Ordenação | A sintaxe reduzida mantém legível o código dos algoritmos de Dijkstra e de busca em largura, bem como dos cinco algoritmos de ordenação, nos quais a avaliação recai sobre a lógica e sobre a contagem de operações. |

Como consequência da adoção conjunta, e considerando que ambas as implementações processam
exatamente os mesmos arquivos de entrada, o projeto incorpora uma análise comparativa de desempenho
sobre dados idênticos, apresentada na seção 6 deste relatório.

## 4. Decisão 2 — Divisão de responsabilidades

A divisão adotada é por bloco de estrutura, e não por problema isolado. Cada integrante assume um
conjunto coerente de estruturas, o que permite o reaproveitamento de código entre problemas
correlatos e evita a edição simultânea dos mesmos arquivos.

| Bloco | Problemas | Responsável | Fundamento da alocação |
| --- | --- | --- | --- |
| Listas encadeadas | 5 e 6 | João Guilherme | Estrutura de nó reaproveitada nos blocos seguintes |
| Árvore Binária de Busca | 7 e 8 | João Guilherme | Evolução direta da estrutura de nó da lista |
| Árvore AVL | 9 e 10 | João Guilherme | Estende a BST com controle de balanceamento |
| Tabela Hash | 13 e 14 | João Guilherme | O encadeamento reutiliza a lista do bloco inicial |
| Filas | 1 e 2 | Vitória | Estrutura de fluxo, base para os demais blocos |
| Pilhas | 3 e 4 | Vitória | Complementa o bloco de estruturas lineares |
| Grafos | 11 e 12 | Vitória | Algoritmos de percurso e caminho mínimo |
| Algoritmos de ordenação | 15 a 24 | Vitória | Dez problemas apoiados em cinco implementações |

A divisão refere-se exclusivamente à responsabilidade de implementação. Ambos os integrantes
acompanham o desenvolvimento integral do projeto e estão aptos a apresentar qualquer das estruturas.

## 5. Decisão 3 — Fonte única de dados

Os dados de entrada exigidos pelo enunciado não serão codificados no interior dos programas. Foram
organizados em arquivos CSV mantidos no diretório `dados/` e lidos por ambas as implementações.

A decisão atende a três finalidades: garante que as duas linguagens processem exatamente o mesmo
conjunto de entrada, viabilizando a comparação descrita na seção 6; evita a duplicação dos dados em
dois formatos distintos; e permite a inspeção direta dos valores utilizados nos testes, sem
necessidade de leitura do código-fonte.

### 5.1 Especificação dos arquivos

- codificação UTF-8, sem marca de ordem de byte;
- separador de campos: vírgula;
- **separador decimal: ponto**, em conformidade com o comportamento do método `Double.parseDouble`
  na leitura pelo lado Java;
- cabeçalho fixo na primeira linha, com identificadores de coluna idênticos nas duas implementações;
- os arquivos são gerados uma única vez e mantidos sob controle de versão, não sendo regerados a
  cada execução, de modo a assegurar a reprodutibilidade dos resultados.

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

Os dados das situações 15, 17, 18, 19 e 20 são construídos no próprio código, por serem de pequeno
volume e específicos da simulação descrita em cada enunciado.

## 6. Decisão 4 — Instrumentação e validação cruzada

Os cinco algoritmos de ordenação são implementados com instrumentação que registra, a cada execução,
o número de comparações, o número de trocas e o tempo decorrido.

A medida atende diretamente a três exigências do enunciado, conforme a tabela abaixo, e fundamenta a
análise de complexidade requerida no item 5.2.

| Situação | Exigência do enunciado | Atendimento |
| --- | --- | --- |
| 15 | Comparação do número de comparações e trocas entre algoritmos | Contadores apresentados ao final de cada ordenação |
| 20 | Comparação de desempenho entre lista ordenada e lista aleatória | Mesma implementação aplicada às duas entradas |
| 23 | Comparação de tempo entre Quick Sort e Merge Sort | Medição de tempo integrada à instrumentação |

**Validação cruzada entre as linguagens.** Para uma mesma entrada, a contagem de comparações
produzida pela implementação em Java e pela implementação em Python deve ser idêntica, uma vez que o
número de comparações é determinado pelo algoritmo e não pela linguagem. Divergência nesse valor
indica erro de implementação em um dos lados. O tempo de execução, por depender do ambiente de
execução, apresenta diferença esperada — cuja análise integra o Relatório Técnico.

## 7. Decisão 5 — Padronização das saídas

Cada situação-problema produz um bloco de saída em formato único, gravado em arquivo próprio no
diretório `saidas/`, nomeado de `problema_01.txt` a `problema_24.txt`. A padronização atende ao
requisito 3 do item 1.3, relativo à apresentação de exemplos de execução com dados de entrada e
saída.

```
=== PROBLEMA 09 — ÍNDICE DE CPF PARA SISTEMA DE RH ===
Estrutura: Árvore AVL   |   Linguagem: Java

[ENTRADA]    25 funcionários lidos de dados/funcionarios.csv
[OPERAÇÕES]  25 inserções, 3 buscas, 2 remoções
[SAÍDA]      altura final = 5 | fator de balanceamento máx = 1
[COMPLEX.]   busca O(log n) | espaço O(n)
==========================================================
```

## 8. Decisão 6 — Organização do código

A estrutura do repositório separa as implementações por linguagem e por natureza da estrutura,
atendendo ao item 5.1 do enunciado, que determina a separação de cada estrutura em módulos, classes
ou arquivos distintos.

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

- identificadores em português, coerentes com a terminologia do enunciado;
- nomenclatura espelhada entre as linguagens: um método denominado `inserir` em Python recebe a
  mesma denominação em Java;
- comentários explicativos nas seções principais de cada implementação;
- tratamento explícito das condições de erro previstas no item 5.1: estrutura vazia, chave
  inexistente e índice fora de faixa.

## 9. Aderência ao item 5.4 — implementação do zero

O item 5.4 do enunciado determina que as estruturas sejam implementadas integralmente, admitindo o
uso de arrays e listas nativas apenas como área de armazenamento. A dupla estabeleceu previamente a
relação de recursos vedados em cada linguagem, de modo a evitar o emprego involuntário de
implementações prontas.

| Linguagem | Recursos vedados | Recursos admitidos como base |
| --- | --- | --- |
| Python | `sorted`, `list.sort`, `dict` e `collections.Counter` empregados como tabela hash, `collections.deque`, `heapq`, `bisect` | `list` como área de armazenamento; estruturas de controle e recursão da linguagem |
| Java | `HashMap`, `TreeMap`, `LinkedList`, `ArrayDeque`, `PriorityQueue`, `Collections.sort`, `Arrays.sort` | arrays nativos e `ArrayList` exclusivamente como área de armazenamento |

**Situação 11 — Dijkstra.** Em coerência com a restrição acima, o algoritmo de caminho mínimo não
empregará fila de prioridade fornecida pela biblioteca padrão. A dupla adotará a implementação com
varredura linear do vértice de menor distância, de complexidade O(V²), suficiente para o volume de
dez vértices exigido pelo enunciado, ou implementará estrutura de heap própria.

## 10. Requisitos de execução

O projeto não possui dependências externas: utiliza exclusivamente a biblioteca padrão de ambas as
linguagens, sem necessidade de gerenciadores de pacotes ou de ferramentas de construção.

| Componente | Requisito |
| --- | --- |
| Python | versão 3.10 ou superior |
| Java | JDK 26 ou superior |
| Dependências | nenhuma — sem Maven, sem Gradle, sem instalação de bibliotecas |

**Consulta ao professor.** A implementação em Java foi desenvolvida em JDK 26.0.2. Como o bytecode
gerado não é executável em versões anteriores da plataforma, solicita-se a confirmação de que o
ambiente utilizado na correção atende a esse requisito. Em caso negativo, a dupla providenciará a
compilação com bytecode compatível com a versão indicada pelo professor, mediante ajuste na
configuração de compilação, sem alteração do código-fonte.

## 11. Cronograma de execução

O desenvolvimento está organizado em sete semanas, conforme a sugestão apresentada no item 6 do
enunciado, com execução paralela entre os integrantes. A conclusão está prevista para 25 de outubro,
com o intervalo restante reservado à integração, à revisão e à elaboração do Relatório Técnico.

| Sem. | Período | João Guilherme — Java | Vitória — Python |
| --- | --- | --- | --- |
| 1 | 07/09 a 13/09 | Listas encadeadas (5 e 6) | Filas (1 e 2) e Pilhas (3 e 4) |
| 2 | 14/09 a 20/09 | Árvore Binária de Busca (7 e 8) | Implementação dos cinco algoritmos de ordenação |
| 3 | 21/09 a 27/09 | AVL — inserção e rotações (9) | Ordenação aplicada (15 a 18) |
| 4 | 28/09 a 04/10 | AVL — remoção e consulta Top-K (10) | Grafos — caminho mínimo (11) |
| 5 | 05/10 a 11/10 | Tabela Hash (13 e 14) | Grafos — percurso em largura (12) |
| 6 | 12/10 a 18/10 | Revisão do bloco não-linear | Merge e Quick aplicados (19 a 24) |
| 7 | 19/10 a 25/10 | Integração, testes finais e Relatório Técnico | |

## 12. Entregáveis

Conforme os itens 1.3 e 5.2 do enunciado, a entrega compreenderá:

- ficha de identificação devidamente preenchida;
- código-fonte completo e funcional das vinte e quatro implementações;
- Relatório Técnico com justificativa de cada estrutura e algoritmo, análise de complexidade de
  tempo e espaço, exemplos de entrada e saída, dificuldades encontradas e conclusão sobre a
  aplicabilidade das estruturas;
- registros de execução de todas as situações-problema;
- documentação de instruções de execução;
- o presente Relatório de Decisões de Projeto.

A submissão será realizada pela plataforma iCEV Digital, conforme determinado no enunciado.

## 13. Considerações finais

As decisões registradas neste relatório orientam-se pelo objetivo declarado no enunciado: a
compreensão do funcionamento interno das estruturas de dados, em detrimento do uso de soluções
prontas. A organização proposta procura assegurar que cada estrutura seja implementada, testada e
justificada de forma independente, e que os resultados sejam verificáveis a partir de dados de
entrada explícitos.

A dupla permanece à disposição para os ajustes que o professor entender necessários, especialmente
quanto ao escopo e aos requisitos de execução tratados na seção 10.
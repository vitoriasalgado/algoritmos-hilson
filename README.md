# Trabalho Prático — Algoritmo e Estrutura de Dados I

Implementação de estruturas lineares, não-lineares e algoritmos de ordenação aplicados a problemas
reais do cotidiano.

**iCEV — Engenharia de Software, 2º Período** · Turmas ADELLE e SNYDER · Prof. Hilson Silva
Avaliação P2 · Entrega: 11/11/2026

## Como executar

```bash
./run.sh          # macOS e Linux — roda os dois lados
run.bat           # Windows
```

Ou separadamente:

```bash
# Python — problemas 1 a 4, 11, 12 e 15 a 24
python3 python/main.py

# Java — problemas 5 a 10, 13 e 14
find java/src -name "*.java" -print0 | xargs -0 javac -d java/out
java -cp java/out ed.app.Main
```

**Requisitos:** Python 3.10 ou superior e **JDK 26 ou superior** — o projeto Java foi desenvolvido no
JDK 26.0.2 e o bytecode gerado não executa em JDK anterior. Fora isso, nenhuma dependência externa:
apenas a biblioteca padrão das duas linguagens. Sem Maven, sem Gradle, sem `pip install`.

A alocação não é arbitrária: Java cobre as estruturas baseadas em nós e referências, onde a tipagem
estática torna explícita a relação nó/referência e o compilador acusa erros nas rotações da AVL e no
encadeamento da hash; Python cobre as estruturas de fluxo, os grafos e a ordenação, onde o foco da
avaliação está na lógica e na contagem de operações. Como as duas linguagens processam exatamente
os mesmos arquivos de `dados/`, o projeto ganha uma análise comparativa de desempenho sobre entrada
idêntica — a variação criativa prevista no item 5.4.

| Linguagem | Blocos | Problemas | Responsável |
|---|---|---|---|
| Python | Filas, Pilhas, Grafos, Ordenação | 1–4, 11–12, 15–24 | Vitória |
| Java | Listas encadeadas, BST, AVL, Tabela Hash | 5–10, 13–14 | Jotage |

## Estrutura do repositório

```
dados/        CSVs compartilhados — fonte única, lida pelas duas linguagens
python/
  lineares/       fila.py, pilha.py
  nao_lineares/   grafo.py
  ordenacao/      sorts.py (instrumentado)
  main.py         mains dos problemas 1-4, 11-12, 15-24
java/
  src/ed/lineares/      Lista, No
  src/ed/naolineares/   BST, AVL, TabelaHash
  src/ed/app/           mains dos problemas 5-10, 13-14
saidas/       logs padronizados de execução (problema_01.txt … problema_24.txt)
relatorio/    relatório técnico e documento de decisões
```

## Contrato dos arquivos de dados

Os CSVs de `dados/` são gerados uma única vez e versionados — não são regerados por script a cada
execução. Regras válidas para as duas linguagens:

- codificação UTF-8, sem BOM;
- separador de campos: vírgula;
- **separador decimal: ponto** (`19.90`, nunca `19,90`) — `Double.parseDouble` exige ponto, e um
  `Locale` pt-BR quebra a leitura de forma silenciosa;
- cabeçalho fixo na primeira linha, com nomes de coluna idênticos nos dois lados;
- em `texto.txt`, a última linha traz a fonte da notícia e **deve ser ignorada na leitura** (linhas
  iniciadas por `Fonte:`). Sem isso a URL entra na contagem do problema 14 e infla justamente as
  palavras-chave da matéria, além de criar chaves como `https` e `ghtml`.

| Arquivo | Colunas | Registros | Problemas |
|---|---|---|---|
| `pacientes.csv` | nome, horario, tipo | 15 | 1 |
| `impressoes.csv` | aluno, arquivo, paginas | 15 | 2 |
| `alunos.csv` | matricula, nome, curso | 20 | 7 |
| `produtos.csv` | codigo, nome, quantidade, preco | 100 | 6, 8, 16, 23 |
| `funcionarios.csv` | cpf, nome, cargo | 25 | 9 |
| `jogadores.csv` | nome, pontuacao | 30 | 10 |
| `transacoes.csv` | data_hora, valor, descricao | 50 | 21 |
| `contatos_a.csv` / `contatos_b.csv` | nome, telefone | 20 + 20 | 22 |
| `atletas.csv` | nome, tempo | 30 | 24 |
| `usuarios.csv` | email, senha_hash | 20 | 13 |
| `texto.txt` | (texto corrido) | notícia real | 14 |

Os dados dos problemas 15, 17, 18, 19 e 20 são gerados no próprio código, por serem pequenos e
específicos daquela simulação.

## Restrições de implementação

O item 5.4 do enunciado exige que as estruturas sejam implementadas **do zero**. Arrays e listas
nativas são permitidos apenas como área de armazenamento.

| Linguagem | Proibido | Permitido como base |
|---|---|---|
| Python | `sorted()`, `list.sort()`, `dict`/`Counter` como tabela hash, `collections.deque`, `heapq`, `bisect` | `list` como array de apoio; laços, condicionais e recursão |
| Java | `HashMap`, `TreeMap`, `LinkedList`, `ArrayDeque`, `PriorityQueue`, `Collections.sort`, `Arrays.sort` | arrays nativos e `ArrayList` apenas como armazenamento |

O Dijkstra (problema 11) não pode usar fila de prioridade pronta: ou a versão O(V²) com varredura
linear, ou um min-heap implementado à mão.

## Convenções

- nomes de métodos espelhados entre as linguagens: se é `inserir()` em Python, é `inserir()` em Java;
- identificadores em português, coerentes com o enunciado;
- cada estrutura em seu próprio módulo, classe ou arquivo;
- tratamento explícito de erro: estrutura vazia, chave inexistente, índice fora de faixa;
- os cinco algoritmos de ordenação são instrumentados com contadores de comparações, de trocas e
  cronômetro. Para uma mesma entrada, a contagem de comparações deve ser **idêntica** em Java e em
  Python; divergência indica erro de implementação. O tempo, esse sim, diverge — e a explicação
  dessa divergência é conteúdo do relatório.

## Padrão de saída

Todo problema imprime um bloco no mesmo formato, gravado em `saidas/problema_NN.txt`:

```
=== PROBLEMA 09 — ÍNDICE DE CPF PARA SISTEMA DE RH ===
Estrutura: Árvore AVL   |   Linguagem: Java

[ENTRADA]    25 funcionários lidos de dados/funcionarios.csv
[OPERAÇÕES]  25 inserções, 3 buscas, 2 remoções
[SAÍDA]      altura final = 5 | fator de balanceamento máx = 1
[COMPLEX.]   busca O(log n) | espaço O(n)
==========================================================
```

## Fluxo de branches

- `main` — base compartilhada: `dados/`, estrutura de pastas, README, relatório e integração final;
- `quejava` — implementações do Jotage (Java);
- `vitoria-python` — implementações da Vitória (Python).

Cada integrante trabalha na própria branch e abre PR para a `main`. Alterações em `dados/` e no
README são feitas direto na `main`, porque afetam os dois lados.
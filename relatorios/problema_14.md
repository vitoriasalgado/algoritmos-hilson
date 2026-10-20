# Problema 14 — Contagem de Frequência de Palavras

## 1. Objetivo

Desenvolver um analisador de frequência de palavras utilizando uma tabela hash com encadeamento, aplicando a estrutura de dados ao texto de uma obra literária.

## 2. Obra analisada

- **Título:** Noites Brancas
- **Autor:** Fiódor Dostoiévski
- **Arquivo de entrada:** `dados/noitesbrancas.csv`

## 3. Estrutura de dados

Foi utilizada uma tabela hash para armazenar cada palavra como chave e sua respectiva frequência como valor.

Durante o processamento, as palavras são convertidas para minúsculas e seus acentos são removidos. Essa normalização permite agrupar palavras que diferem apenas por maiúsculas, minúsculas ou acentuação.

A tabela hash utiliza encadeamento para tratar colisões. Após a contagem, as palavras são organizadas por frequência decrescente para formar o ranking.

## 4. Funcionalidades implementadas

- Contagem total de palavras do texto.
- Identificação da quantidade de palavras distintas.
- Exibição das dez palavras mais frequentes.
- Pesquisa da frequência de uma palavra informada pelo usuário.
- Tratamento específico das variações `Nástienka` e `Nástienhka` durante a pesquisa.
- Testes automatizados para verificar a contagem, a normalização e o tratamento de pontuação e números.
- Geração de arquivo de saída com os resultados, testes e informações de complexidade.

O tratamento especial de `Nástienka` e `Nástienhka` permite somar as ocorrências dessas duas grafias durante a pesquisa. Essa regra é aplicada somente à consulta desse nome e não modifica as frequências armazenadas nem o ranking geral.

## 5. Análise de complexidade

Considere:

- \(n\): número total de palavras processadas.
- \(d\): número de palavras distintas.

As complexidades consideradas são:

- **Contagem:** \(O(n)\) esperado, considerando operações de tabela hash em tempo constante médio; \(O(n^2)\) no pior caso, quando as colisões prejudicam o desempenho das operações.
- **Ranking:** \(O(d \log d)\), devido à ordenação das palavras distintas.
- **Espaço:** \(O(d)\), para armazenar as palavras distintas e suas frequências.

## 6. Resultados da execução

Os resultados abaixo foram obtidos na execução registrada em `saidas/problema_14.txt`.

### Resumo

| Métrica | Resultado |
|---|---:|
| Total de palavras | 23.963 |
| Palavras distintas | 3.855 |
| Colisões registradas | 3.754 |
| Testes aprovados | 5 de 5 |

### Dez palavras mais frequentes

| Posição | Palavra | Frequência |
|---:|---|---:|
| 1 | que | 1.069 |
| 2 | e | 1.065 |
| 3 | a | 855 |
| 4 | de | 790 |
| 5 | nao | 546 |
| 6 | o | 466 |
| 7 | me | 450 |
| 8 | se | 413 |
| 9 | eu | 312 |
| 10 | por | 292 |

### Testes automatizados

Os cinco testes foram aprovados, verificando:

1. Contagem total de palavras.
2. Quantidade de palavras distintas.
3. Contagem sem diferenciação entre maiúsculas e minúsculas.
4. Normalização de acentos.
5. Desconsideração de números e pontuação.

## 7. Arquivos do problema

- **Código-fonte:** `java/src/ed/app/Problema14.java`
- **Texto analisado:** `dados/noitesbrancas.csv`
- **Saída da execução:** `saidas/problema_14.txt`
- **Relatório técnico:** `relatorios/problema_14.md`

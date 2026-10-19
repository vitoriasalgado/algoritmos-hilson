# Problema 13 — Cadastro de Usuários com Login Rápido

## 1. Objetivo

Implementar um sistema de cadastro e autenticação de usuários utilizando uma tabela hash, permitindo cadastrar, consultar e autenticar usuários de maneira eficiente.

O programa foi desenvolvido em Java e utiliza arquivos CSV para manter os dados de teste separados do código-fonte.

## 2. Estrutura de dados

A principal estrutura utilizada é uma tabela hash, implementada pela classe `TabelaHash<K, V>`.

A tabela permite armazenar e localizar registros por meio de chaves, utilizando o código hash da chave para determinar a posição de armazenamento.

A implementação também contabiliza colisões, que ocorrem quando chaves diferentes são direcionadas à mesma posição da tabela.

No problema, a tabela é utilizada para armazenar usuários identificados por seus e-mails.

## 3. Funcionalidades implementadas

O sistema contempla as seguintes funcionalidades:

- Cadastro de usuários.
- Consulta de usuários.
- Autenticação por e-mail e senha.
- Armazenamento de senhas por meio de resumo criptográfico SHA-256.
- Utilização de tabela hash para organizar e localizar os registros.
- Execução de testes automatizados das operações.
- Carregamento dos usuários de teste a partir de um arquivo CSV externo.

### Arquivo de dados

Os usuários utilizados nos testes são mantidos em:

`dados/usuarios_teste.csv`

O arquivo possui as colunas `nome`, `email` e `senha`. Dessa forma, os dados iniciais de teste ficam separados da implementação Java, facilitando sua manutenção e alteração sem a necessidade de editar o código-fonte.

Esses dados são destinados ao ambiente acadêmico de testes. O uso de SHA-256 isoladamente não é recomendado para armazenamento de senhas em sistemas reais de produção.

## 4. Testes realizados

O relatório original registra seis testes, com resultado de 6/6 aprovados, e 11 colisões contabilizadas.

Os resultados registrados devem ser entendidos como os resultados da execução documentada anteriormente. A alteração para carregar os usuários pelo CSV não confirma, por si só, que todos os testes continuam passando.

## 5. Análise de complexidade

| Operação | Caso médio | Pior caso |
|---|---|---|
| Inserção na tabela hash | O(1) | O(n) |
| Busca na tabela hash | O(1) | O(n) |
| Remoção na tabela hash | O(1) | O(n) |

As complexidades dependem da distribuição das chaves, da quantidade de colisões e da estratégia utilizada para resolvê-las.

No caso médio, as operações de inserção, busca e remoção podem apresentar custo constante. No pior caso, quando muitas chaves colidem, o custo pode crescer linearmente com a quantidade de elementos.

## 6. Conclusão

O problema demonstra a utilização de uma tabela hash para implementar operações de cadastro e autenticação de usuários. A estrutura oferece acesso eficiente aos registros no caso médio e permite observar o impacto das colisões no desempenho.

A separação dos usuários de teste no arquivo `dados/usuarios_teste.csv` também melhora a organização do projeto, reduzindo a dependência entre os dados de teste e o código-fonte.

A validação final deve ser realizada após a compilação e a execução dos testes, para confirmar que o carregamento dos dados externos funciona corretamente.

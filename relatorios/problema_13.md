# Problema 13 — Cadastro de Usuários com Login Rápido

## 1. Objetivo

Desenvolver um sistema de cadastro de usuários com autenticação por e-mail e senha, utilizando uma tabela hash para armazenar e localizar as contas de maneira eficiente.

O sistema permite cadastrar usuários, realizar login, remover contas e listar os usuários cadastrados. Também possui testes automatizados para verificar o funcionamento das operações principais.

## 2. Estrutura de dados utilizada

Foi utilizada uma tabela hash com tratamento de colisões por encadeamento separado (*separate chaining*).

A tabela é organizada em posições calculadas a partir do código hash da chave. Neste projeto, o e-mail normalizado é utilizado como chave de identificação do usuário.

Quando duas chaves são direcionadas à mesma posição, as entradas são armazenadas em uma lista vinculada naquela posição. Dessa forma, uma colisão não impede o armazenamento nem a recuperação dos registros.

A implementação foi desenvolvida na classe `TabelaHash<K,V>`, que oferece operações de inserção, busca, verificação de existência, remoção e listagem.

## 3. Funcionalidades implementadas

- Cadastro de usuários com nome, e-mail e senha.
- Normalização do e-mail para reduzir duplicidades causadas por diferenças entre letras maiúsculas e minúsculas.
- Verificação de e-mails já cadastrados.
- Autenticação por e-mail e senha.
- Remoção de contas cadastradas.
- Listagem dos usuários registrados.
- Contagem de entradas e registro de colisões.
- Execução de testes automatizados.

As senhas não são armazenadas diretamente em texto puro. A implementação utiliza um resumo criptográfico SHA-256 para compará-las durante a autenticação. Essa abordagem atende ao propósito didático do exercício, mas sistemas reais devem utilizar algoritmos específicos para armazenamento de senhas, como Argon2id, scrypt ou bcrypt, com salt apropriado.

## 4. Testes realizados

Foram executados seis testes automatizados:

1. **Cadastro de 20 usuários:** verifica a inserção de pelo menos 20 registros na tabela de teste.
2. **Tratamento de colisões:** verifica se chaves diferentes que compartilham uma posição podem ser armazenadas e recuperadas.
3. **Autenticação com senha correta:** confirma que a senha correspondente permite a autenticação.
4. **Rejeição de senha incorreta:** verifica se uma senha diferente é rejeitada.
5. **Atualização de chave sem duplicar entradas:** verifica se inserir novamente uma chave existente atualiza seu valor sem aumentar a quantidade de chaves.
6. **Remoção de conta:** confirma que um registro removido deixa de ser encontrado.

### Resultado

Os seis testes foram aprovados, totalizando **6 de 6 testes**. Na execução registrada, a tabela de teste contabilizou 11 colisões durante as operações realizadas.

Os usuários utilizados na simulação automatizada pertencem a uma tabela independente daquela usada pelo menu interativo. Por esse motivo, a listagem do menu pode apresentar zero usuários antes de um cadastro feito durante a própria sessão.

## 5. Análise de complexidade

Considerando `n` como o número de usuários armazenados:

| Operação | Caso médio esperado | Pior caso |
|---|---|---|
| Cadastro/inserção | O(1) | O(n) |
| Busca | O(1) | O(n) |
| Autenticação | O(1), além do cálculo do resumo da senha | O(n), além do cálculo do resumo da senha |
| Remoção | O(1) | O(n) |
| Listagem | O(n) | O(n) |

As operações básicas apresentam custo médio constante quando as chaves estão bem distribuídas e o fator de carga permanece adequado. No pior caso, muitas chaves podem ocupar o mesmo encadeamento, tornando a busca linear.

O espaço utilizado pela estrutura é O(n), pois cada usuário armazenado corresponde a uma entrada na tabela.

## 6. Conclusão

O projeto demonstra a aplicação de uma tabela hash para o gerenciamento de contas de usuários. O encadeamento separado permite tratar colisões, enquanto a utilização do e-mail como chave facilita as operações de cadastro, autenticação, busca e remoção.

Os testes executados indicaram o funcionamento esperado das operações verificadas. Assim, a solução atende aos objetivos didáticos de implementar uma tabela hash, tratar colisões e demonstrar suas aplicações em um sistema de cadastro e login.

import csv 
from lineares.fila import Fila
from lineares.pilha import Pilha
from nao_lineares.grafo import Grafo
from ordenacao.bubble_sort import bubble_sort
from ordenacao.selection_sort import selection_sort

# valores escolhidos para estimar o tempo fora do csv
duracao_por_tipo ={
    "Consulta": 15,
    "Exame": 20,
    "Cirurgia": 60,
    "Urgencia": 10,
    "Retorno": 10,
}
def horario_para_minutos(horario): # problema 2 : converte o horário "00:00" em minutos
    partes = horario.split(':')
    horas = int(partes[0])
    minutos = int(partes[1])
    return horas * 60 + minutos

def expressao_balanceada(expressao): # problema 4 : cria uma pilha vazia
    pilha = Pilha()
    for caractere in expressao:
        if caractere in "([{":
            pilha.empilhar(caractere)
        elif caractere in ")]}":
            if pilha.vazia():
                return False
            abertura = pilha.desempilhar()
            if caractere == ")" and abertura != "(":
                return False
            elif caractere == "]" and abertura != "[":
                return False
            elif caractere == "}" and abertura != "{":
                return False
    return pilha.vazia()

def problema_01():

    with open('dados/pacientes.csv', newline="", encoding='utf-8') as arquivo:
        leitor = csv.reader(arquivo)
        linhas = list(leitor)
        cabecalho = linhas[0]
        pacientes = linhas[1:]

    fila = Fila()
    for paciente in pacientes:
        fila.enfileirar(paciente)

    fim_do_atendimento = 0
    esperas = []
    linhas_saida = []

    def registrar(mensagem):
        print(mensagem)
        linhas_saida.append(mensagem)

    registrar("=" * 72)
    registrar("PROBLEMA 01 — SISTEMA DE ATENDIMENTO DE UMA CLÍNICA MÉDICA")
    registrar("Estrutura: Fila | Linguagem: Python")
    registrar("=" * 72)
    registrar(f"\nENTRADA: {len(pacientes)} pacientes lidos de dados/pacientes.csv")
    registrar("\nATENDIMENTOS:")

    while not fila.vazia():
        paciente = fila.desenfileirar()
        registrar(f"  Paciente: {paciente[0]} | Chegada: {paciente[1]} | Tipo: {paciente[2]}")
        chegada = horario_para_minutos(paciente[1])
        duracao = duracao_por_tipo[paciente[2]]
        if chegada > fim_do_atendimento: # calcula o tempo de espera e o horário de início do atendimento
            inicio = chegada
        else:
            inicio = fim_do_atendimento
        espera = inicio - chegada
        fim_do_atendimento = inicio + duracao
        esperas.append(espera)
        registrar(f"  Espera: {espera} min | Início: {inicio // 60:02d}:{inicio % 60:02d} | Fim: {fim_do_atendimento // 60:02d}:{fim_do_atendimento % 60:02d}")
        registrar("")

    media_espera = sum(esperas) / len(esperas)
    registrar("RESUMO:")
    registrar(f"  {len(pacientes)} pacientes atendidos em ordem de chegada.")
    registrar(f"  Tempo médio de espera: {media_espera:.1f} minutos.")
    registrar(f"  Operações: {len(pacientes)} enfileiramentos e {len(pacientes)} desenfileiramentos.")
    registrar("\nCOMPLEXIDADE:")
    registrar("  Enfileirar: O(1) amortizado. Desenfileirar: O(1).")
    registrar("  Simulação: tempo O(n) e espaço O(n), sendo n o número de pacientes.")

    with open('saidas/problema_01.txt', 'w', encoding='utf-8') as arquivo_saida:
        arquivo_saida.write("\n".join(linhas_saida) + "\n")
    

def problema_02():
    with open('dados/impressoes.csv', newline="", encoding='utf-8') as arquivo:
        leitor = csv.reader(arquivo)
        linhas = list(leitor)
        cabecalho = linhas[0]
        impressoes = linhas[1:]

    fila = Fila()
    for impressao in impressoes:
        fila.enfileirar(impressao)

    linhas_saida = []

    def registrar(mensagem):
        print(mensagem)
        linhas_saida.append(mensagem)

    registrar("=" * 72)
    registrar("PROBLEMA 02 — FILA DE IMPRESSÃO EM LABORATÓRIO DE INFORMÁTICA")
    registrar("Estrutura: Fila | Linguagem: Python")
    registrar("=" * 72)
    registrar(f"\nENTRADA: {len(impressoes)} trabalhos lidos de dados/impressoes.csv")
    registrar("\nFILA INICIAL:")
    if fila.vazia():
        registrar("Nenhuma impressão na fila.")
    else:
        for impressao in fila.listar():
            registrar(
                f"  - Aluno: {impressao[0]} | Arquivo: {impressao[1]} "
                f"| Páginas: {impressao[2]}"
            )

    cancelada = fila.cancelar_ultimo()
    registrar("\nCANCELAMENTO:")
    registrar(
        f"  {cancelada[1]} enviado por {cancelada[0]} "
        f"({cancelada[2]} páginas)."
    )

    registrar("\nSEQUÊNCIA DE IMPRESSÃO:")
    total_impressos = 0
    while not fila.vazia():
        impressao = fila.desenfileirar()
        registrar(
            f"  - Aluno: {impressao[0]} | Arquivo: {impressao[1]} "
            f"| Páginas: {impressao[2]}"
        )
        total_impressos += 1

    registrar("\nRESUMO:")
    registrar(f"  {total_impressos} trabalhos impressos em ordem de chegada e 1 cancelado.")
    registrar(f"  Operações: {len(impressoes)} enfileiramentos, 1 listagem, 1 cancelamento")
    registrar(f"  e {total_impressos} desenfileiramentos.")
    registrar("\nCOMPLEXIDADE:")
    registrar("  Enfileirar/cancelar último: O(1) amortizado. Desenfileirar: O(1).")
    registrar("  Listar: O(n). Simulação: tempo O(n) e espaço O(n).")
    registrar("  n é o número de trabalhos de impressão.")

    with open('saidas/problema_02.txt', 'w', encoding='utf-8') as arquivo_saida:
        arquivo_saida.write("\n".join(linhas_saida) + "\n")


def problema_03():
    pilha = Pilha()
    texto = ""
    linhas_saida = []

    def registrar(mensagem):
        print(mensagem)
        linhas_saida.append(mensagem)

    trechos = ["Olá", ", ", "mundo", "!", " Este", " é", " um", " problema", " de", " pilha."]

    registrar("=" * 72)
    registrar("PROBLEMA 03 — SISTEMA DE DESFAZER EM EDITOR DE TEXTO")
    registrar("Estrutura: Pilha | Linguagem: Python")
    registrar("=" * 72)
    registrar(f"\nENTRADA: {len(trechos)} ações de digitação simuladas")
    registrar("\nAÇÕES EXECUTADAS:")
    for trecho in trechos:
        acao = ["digitação", trecho, texto]
        pilha.empilhar(acao)
        texto = texto + trecho
        registrar(f"  Digitação: {trecho!r}")
        registrar(f"  Texto atual: {texto!r}")
        registrar("")

    registrar("DESFAZENDO AÇÕES:")
    while not pilha.vazia():
        acao_desfeita = pilha.desempilhar()
        texto = acao_desfeita[2]
        registrar(f"  Ação desfeita: {acao_desfeita[0]} - {acao_desfeita[1]!r}")
        registrar(f"  Texto após desfazer: {texto!r}")
        registrar("")

    registrar("RESUMO:")
    registrar(f"  {len(trechos)} ações executadas e desfeitas da mais recente à mais antiga.")
    registrar(f"  Texto final: {texto!r} (vazio).")
    registrar(f"  Operações: {len(trechos)} empilhamentos e {len(trechos)} desempilhamentos.")
    registrar("\nCOMPLEXIDADE:")
    registrar("  Empilhar/desempilhar: O(1) amortizado por operação da pilha.")
    registrar("  n ações: O(n) operações da pilha.")
    registrar("  Os textos e registros também têm custo: tempo e espaço O(n + C),")
    registrar("  sendo C a soma dos tamanhos dos estados de texto e registros gerados.")

    with open('saidas/problema_03.txt', 'w', encoding='utf-8') as arquivo_saida:
        arquivo_saida.write("\n".join(linhas_saida) + "\n")

def problema_04():

    linhas_saida = []

    def registrar(mensagem):
        print(mensagem)
        linhas_saida.append(mensagem)

    expressoes = [
        "{[()]}",
        "([])",
        "((()))",
        "([)]",
        "({[]})",
        "((()",
    ]

    registrar("=" * 72)
    registrar("PROBLEMA 04 — VALIDAÇÃO DE EXPRESSÕES BALANCEADAS")
    registrar("Estrutura: Pilha | Linguagem: Python")
    registrar("=" * 72)
    registrar(f"\nENTRADA: {len(expressoes)} expressões com parênteses, colchetes e chaves")
    registrar("\nRESULTADOS:")
    total_validas = 0
    for expressao in expressoes:
        resultado = expressao_balanceada(expressao)

        if resultado:
            total_validas += 1
            registrar(f"  {expressao:<8} → VÁLIDA")
        else:
            registrar(f"  {expressao:<8} → INVÁLIDA")
    registrar("\nRESUMO:")
    registrar(f"  {total_validas} expressões válidas e {len(expressoes) - total_validas} inválidas.")
    registrar("  Operações: empilhar aberturas e conferir os fechamentos ao desempilhar.")
    registrar("\nCOMPLEXIDADE:")
    registrar("  Tempo: O(n) por expressão no pior caso.")
    registrar("  Espaço: O(n), sendo n o número de caracteres da expressão.")

    with open("saidas/problema_04.txt", "w", encoding="utf-8") as arquivo_saida:
        arquivo_saida.write("\n".join(linhas_saida) + "\n")

def problema_11():
    grafo = Grafo()
    locais = [
        "Casa", "Praça", "Escola", "Mercado", "Hospital",
        "Biblioteca", "Parque", "Terminal", "Universidade", "Centro",
    ]
    # distâncias fictícias em km, onde cada rua permite os dois sentidos.
    ruas = [
        ["Casa", "Praça", 2],
        ["Casa", "Mercado", 7],
        ["Praça", "Escola", 2],
        ["Praça", "Mercado", 3],
        ["Escola", "Biblioteca", 1],
        ["Mercado", "Hospital", 2],
        ["Biblioteca", "Parque", 2],
        ["Hospital", "Terminal", 3],
        ["Parque", "Universidade", 3],
        ["Terminal", "Centro", 2],
        ["Universidade", "Centro", 2],
        ["Mercado", "Parque", 4],
        ["Casa", "Universidade", 15],
    ]

    for local in locais:
        grafo.adicionar_vertice(local)
    for origem, destino, distancia in ruas:
        grafo.adicionar_aresta(origem, destino, distancia)

    origem = "Casa"
    destino = "Universidade"
    caminho, distancia_total = grafo.menor_caminho(origem, destino)
    linhas_saida = []

    def registrar(mensagem):
        print(mensagem)
        linhas_saida.append(mensagem)

    registrar("=" * 72)
    registrar("PROBLEMA 11 — MENOR CAMINHO ENTRE LOCAIS")
    registrar("Estrutura: Grafo | Algoritmo: Dijkstra | Linguagem: Python")
    registrar("=" * 72)
    registrar(f"\nENTRADA: {len(locais)} locais e {len(ruas)} ruas de mão dupla")
    registrar("  Mapa fictício com distâncias em km.")
    registrar("  Locais: " + ", ".join(locais))
    for inicio, fim, distancia in ruas:
        registrar(f"  {inicio} ↔ {fim}: {distancia} km")

    registrar("\nRESULTADOS:")
    registrar(f"  Origem: {origem} | Destino: {destino}")
    if caminho:
        registrar("  Menor caminho: " + " → ".join(caminho))
        registrar(f"  Distância total: {distancia_total} km")
        registrar("  Trechos percorridos:")
        for indice in range(len(caminho) - 1):
            registrar(f"    {indice + 1}. {caminho[indice]} → {caminho[indice + 1]}")
    else:
        registrar("  Não existe caminho entre esses locais.")

    registrar("\nRESUMO:")
    registrar(f"  Operações: {len(locais)} cadastros de vértices, {len(ruas)} inserções")
    registrar("  de arestas e 1 consulta de menor caminho com Dijkstra.")
    registrar("  Cada rua é armazenada nas listas de seus dois extremos.")
    registrar("\nCOMPLEXIDADE:")
    registrar("  Consulta: tempo O(V² + E), com seleção por varredura linear.")
    registrar("  Reconstrução do caminho: O(V). Espaço auxiliar da consulta: O(V).")
    registrar("  Armazenamento do grafo: O(V + E).")
    registrar("  V é o número de locais; E, o número de ruas.")

    with open("saidas/problema_11.txt", "w", encoding="utf-8") as arquivo_saida:
        arquivo_saida.write("\n".join(linhas_saida) + "\n")

def problema_12():
    grafo = Grafo()

    usuarios = [
        "Ana", "Bruno", "Carla", "Daniel", "Elisa",
        "Felipe", "Gabriela", "Henrique", "Isabela", "João",
        "Karina", "Lucas", "Mariana", "Nicolas", "Olivia",
    ]

    amizades = [
        ["Ana", "Bruno"],
        ["Ana", "Daniel"],
        ["Bruno", "Carla"],
        ["Carla", "Daniel"],
        ["Daniel", "Elisa"],
        ["Elisa", "Felipe"],
        ["Felipe", "Gabriela"],
        ["Gabriela", "Henrique"],
        ["Henrique", "Isabela"],
        ["Isabela", "João"],
        ["João", "Karina"],
        ["Karina", "Lucas"],
        ["Lucas", "Mariana"],
        ["Mariana", "Nicolas"],
    ]
    # Olivia fica sem amizades para demonstrar uma pessoa desconectada.

    for usuario in usuarios:
        grafo.adicionar_vertice(usuario)

    for primeira_pessoa, segunda_pessoa in amizades:
        grafo.adicionar_amizade(primeira_pessoa, segunda_pessoa)

    linhas_saida = []

    def registrar(mensagem):
        print(mensagem)
        linhas_saida.append(mensagem)

    registrar("=" * 72)
    registrar("PROBLEMA 12 — REDE DE AMIZADES EM UMA REDE SOCIAL SIMPLES")
    registrar("Estrutura: Grafo não direcionado | Linguagem: Python")
    registrar("=" * 72)

    registrar(
        f"\nENTRADA: {len(usuarios)} usuários e {len(amizades)} amizades"
    )
    registrar("  Usuários: " + ", ".join(usuarios))
    for primeira_pessoa, segunda_pessoa in amizades:
        registrar(f"  {primeira_pessoa} ↔ {segunda_pessoa}")

    registrar("\nRESULTADOS — LISTA DE AMIGOS:")
    for usuario in usuarios:
        amigos = grafo.listar_amigos(usuario)
        descricao = ", ".join(amigos) if amigos else "sem amigos"
        registrar(f"  {usuario}: {descricao}")

    registrar("\nAMIGOS EM COMUM:")
    pares = [["Ana", "Carla"], ["Ana", "Olivia"]]
    for primeira_pessoa, segunda_pessoa in pares:
        comuns = grafo.amigos_em_comum(primeira_pessoa, segunda_pessoa)
        descricao = ", ".join(comuns) if comuns else "nenhum"
        registrar(f"  {primeira_pessoa} e {segunda_pessoa}: {descricao}")

    registrar("\nSUGESTÕES DE AMIZADE:")
    sugestoes = grafo.sugerir_amigos("Ana")
    descricao = ", ".join(sugestoes) if sugestoes else "nenhuma"
    registrar(f"  Para Ana: {descricao}")
    registrar("  Critério: amigos de amigos que ainda não são amigos diretos.")

    registrar("\nGRAUS DE SEPARAÇÃO — BFS:")
    consultas = [
        ["Ana", "Bruno"],
        ["Ana", "Carla"],
        ["Ana", "Nicolas"],
        ["Ana", "Olivia"],
        ["Ana", "Ana"],
    ]
    for origem, destino in consultas:
        graus = grafo.graus_de_separacao(origem, destino)
        if graus == -1:
            registrar(f"  {origem} → {destino}: sem conexão")
        else:
            registrar(f"  {origem} → {destino}: {graus} ligação(ões)")

    registrar("\nRESUMO:")
    registrar(
        f"  {len(usuarios)} usuários cadastrados e "
        f"{len(amizades)} amizades adicionadas."
    )
    registrar(f"  {len(usuarios)} consultas de listagem de amigos.")
    registrar(f"  {len(pares)} consultas de amigos em comum.")
    registrar("  1 consulta de sugestões de amizade.")
    registrar(f"  {len(consultas)} consultas de graus de separação por BFS.")

    registrar("\nCOMPLEXIDADE:")
    registrar("  BFS: tempo O(V + E) e espaço auxiliar O(V).")
    registrar("  Listar amigos: O(V + d), incluindo a busca pelo nome.")
    registrar("  Amigos em comum: O(V + d1 + d2 + d1*d2).")
    registrar("  Sugestões: O(V³) no pior caso, com buscas em listas.")
    registrar("  Armazenamento do grafo: O(V + E).")
    registrar("  V é o número de usuários; E, o número de amizades.")
    registrar("  d é a quantidade de amigos; d1 e d2 são dos usuários consultados.")

    with open("saidas/problema_12.txt", "w", encoding="utf-8") as arquivo_saida:
        arquivo_saida.write("\n".join(linhas_saida) + "\n")


def problema_15():
    notas = [7.5, 4, 9, 6.5, 8, 5, 10, 3.5,
             5.5, 6, 8.5, 2, 9.5, 5.5, 7]

    linhas_saida = []

    def registrar(mensagem):
        print(mensagem)
        linhas_saida.append(mensagem)

    registrar("=" * 72)
    registrar("PROBLEMA 15 — ORDENAÇÃO DE NOTAS DE UMA TURMA PEQUENA")
    registrar("Algoritmos: Bubble Sort e Selection Sort | Linguagem: Python")
    registrar("=" * 72)
    registrar(f"\nENTRADA: {len(notas)} notas")
    registrar(f"  Notas originais: {notas}")

    # cada algoritmo recebe uma cópia da mesma entrada original
    notas_bubble = notas.copy()
    notas_selection = notas.copy()
    comparacoes_bubble, trocas_bubble, tempo_bubble = bubble_sort(notas_bubble)
    comparacoes_selection, trocas_selection, tempo_selection = selection_sort(notas_selection)

    registrar("\nRESULTADOS:")
    registrar(f"  Bubble Sort:    {notas_bubble}")
    registrar(f"  Selection Sort: {notas_selection}")

    registrar("\nCOMPARAÇÃO DE OPERAÇÕES:")
    registrar("  Algoritmo       | Comparações | Trocas | Tempo (segundos)")
    registrar(f"  Bubble Sort     | {comparacoes_bubble:11} | {trocas_bubble:6} | {tempo_bubble:.9f}")
    registrar(f"  Selection Sort  | {comparacoes_selection:11} | {trocas_selection:6} | {tempo_selection:.9f}")
    registrar("  Contagem: comparação entre valores e troca efetiva de um par.")

    registrar("\nRESUMO:")
    registrar(f"  Resultados iguais: {'sim' if notas_bubble == notas_selection else 'não'}.")
    registrar("  Bubble Sort encerra quando uma passagem inteira não faz trocas.")
    registrar("  As contagens acima se referem à mesma lista original de notas.")
    registrar("  Os tempos podem variar entre execuções; a amostra é pequena.")

    registrar("\nCOMPLEXIDADE:")
    registrar("  Bubble Sort: O(n) no melhor caso; O(n²) no médio e no pior caso.")
    registrar("  Selection Sort: O(n²) no melhor, médio e pior caso.")
    registrar("  Espaço auxiliar de cada algoritmo: O(1).")
    registrar("  A simulação usa O(n) para as cópias e os registros.")
    registrar("  n é o número de notas.")

    with open("saidas/problema_15.txt", "w", encoding="utf-8") as arquivo_saida:
        arquivo_saida.write("\n".join(linhas_saida) + "\n")

def problema_16():
    with open('dados/produtos.csv', newline="", encoding='utf-8') as arquivo:
        leitor = csv.reader(arquivo)
        linhas = list(leitor)
        cabecalho = linhas[0]
        dados_produtos = linhas[1:16]  # pula o cabeçalho e seleciona 15 produtos

    produtos = []

    for linha in dados_produtos:
        nome = linha[1]
        preco = float(linha[3])
        produtos.append([nome, preco])
        
    # as duas ordens partem da mesma entrada original
    produtos_crescente = produtos.copy()
    produtos_decrescente = produtos.copy()

    # o preço fica na posição 1 do registro [nome, preco]
    comparacoes_c, trocas_c, tempo_c = bubble_sort(
        produtos_crescente, indice_preco=1
    )

    comparacoes_d, trocas_d, tempo_d = bubble_sort(
        produtos_decrescente, indice_preco=1, decrescente=True
    )

    linhas_saida = []

    def registrar(mensagem):
        print(mensagem)
        linhas_saida.append(mensagem)

    registrar("=" * 72)
    registrar("PROBLEMA 16 — ORDENAÇÃO DE PRODUTOS POR PREÇO")
    registrar("Algoritmo: Bubble Sort | Linguagem: Python")
    registrar("=" * 72)

    registrar(f"\nENTRADA: {len(produtos)} produtos de dados/produtos.csv")
    for nome, preco in produtos:
        registrar(f"  {nome} | R$ {preco:.2f}")

    registrar("\nRESULTADOS — ORDEM CRESCENTE:")
    for nome, preco in produtos_crescente:
        registrar(f"  {nome} | R$ {preco:.2f}")

    registrar("\nRESULTADOS — ORDEM DECRESCENTE:")
    for nome, preco in produtos_decrescente:
        registrar(f"  {nome} | R$ {preco:.2f}")

    registrar("\nRESUMO:")
    registrar(
        f"  Crescente: {comparacoes_c} comparações, "
        f"{trocas_c} trocas, {tempo_c:.9f} segundos."
    )
    registrar(
        f"  Decrescente: {comparacoes_d} comparações, "
        f"{trocas_d} trocas, {tempo_d:.9f} segundos."
    )
    registrar("  As duas ordenações partiram da mesma lista original.")
    registrar("  Os tempos podem variar entre execuções.")

    registrar("\nCOMPLEXIDADE:")
    registrar("  Bubble Sort: O(n) no melhor caso; O(n²) no médio e no pior.")
    registrar("  Espaço auxiliar do algoritmo: O(1).")
    registrar("  Cópias e registros dos produtos selecionados: O(n).")
    registrar("  A leitura completa do CSV ocupa O(m) de tempo e espaço.")
    registrar("  Simulação: O(m + n²) de tempo no pior caso e O(m + n) de espaço.")
    registrar("  n é o número de produtos selecionados; m, de linhas do CSV.")

    with open("saidas/problema_16.txt", "w", encoding="utf-8") as arquivo_saida:
        arquivo_saida.write("\n".join(linhas_saida) + "\n")

def problema_17():

    candidatos = [
        ["Ana", 85],
        ["Bruno", 92],
        ["Carla", 70],
        ["Daniel", 88],
        ["Eduarda", 95],
        ["Felipe", 76],
        ["Gabriela", 90],
        ["Henrique", 82],
        ["Isabela", 97],
        ["João", 68],
        ["Edson", 91],
        ["Lucas", 79],
        ["Mariana", 86],
        ["Nicolas", 73],
        ["Maria", 94],
    ]
    quantidades_selecionados = 5
    # preserva a ordem original para exibir a entrada
    candidatos_ordenados = candidatos.copy()

    # compara a pontuação na posição 1, da maior para a menor
    comparacoes, trocas, tempo = selection_sort(
        candidatos_ordenados,
        indice_pontuacao=1,
        decrescente=True,
    )

    # após ordenar, os primeiros números são os melhores colocados
    selecionados = candidatos_ordenados[:quantidades_selecionados]

    linhas_saida = []

    def registrar(mensagem):
        print(mensagem)
        linhas_saida.append(mensagem)

    registrar("=" * 72)
    registrar("PROBLEMA 17 — SELEÇÃO DOS MELHORES CANDIDATOS")
    registrar("Algoritmo: Selection Sort | Linguagem: Python")
    registrar("=" * 72)

    registrar(f"\nENTRADA: {len(candidatos)} candidatos")
    for nome, pontuacao in candidatos:
        registrar(f"  {nome} | Pontuação: {pontuacao}")

    registrar("\nRESULTADOS — RANKING COMPLETO:")
    for posicao, candidato in enumerate(candidatos_ordenados, start=1):
        nome, pontuacao = candidato
        registrar(f"  {posicao}. {nome} | Pontuação: {pontuacao}")

    registrar(f"\nTOP {quantidades_selecionados}:")
    for posicao, candidato in enumerate(selecionados, start=1):
        nome, pontuacao = candidato
        registrar(f"  {posicao}. {nome} | Pontuação: {pontuacao}")

    registrar("\nRESUMO:")
    registrar(f"  {len(selecionados)} candidatos selecionados.")
    registrar(f"  Comparações: {comparacoes}")
    registrar(f"  Trocas: {trocas}")
    registrar(f"  Tempo: {tempo:.9f} segundos.")
    registrar("  Empates não têm garantia de preservar a ordem original.")

    registrar("\nCOMPLEXIDADE:")
    registrar("  Ordenação: O(n²) de tempo e O(1) de espaço auxiliar.")
    registrar("  Extração dos Top-N: O(k) de tempo e espaço, sendo k a quantidade selecionada.")
    registrar("  Cópia, seleção e registros: O(n) de espaço no total, para k <= n.")
    registrar("  n é o número de candidatos.")

    with open("saidas/problema_17.txt", "w", encoding="utf-8") as arquivo_saida:
        arquivo_saida.write("\n".join(linhas_saida) + "\n")

def problema_18():
    tarefas = [
        ["Revisar o conteúdo da aula", 3],
        ["Enviar trabalho com prazo para hoje", 1],
        ["Organizar os arquivos do computador", 5],
        ["Resolver exercícios de algoritmos", 2],
        ["Responder mensagens do grupo", 4],
        ["Preparar apresentação para amanhã", 1],
        ["Ler capítulo do livro", 3],
        ["Conferir os dados do relatório", 2],
        ["Atualizar o calendário de estudos", 4],
        ["Separar materiais para a aula", 3],
        ["Fazer cópia de segurança do trabalho", 2],
        ["Limpar a caixa de entrada", 5],
        ["Confirmar a entrega na plataforma", 1],
        ["Planejar os estudos da próxima semana", 4],
        ["Revisar anotações antigas", 5],
    ]

    # preserva a entrada; 1 representa a prioridade mais alta
    tarefas_ordenadas = tarefas.copy()
    # o campo numérico fica na posição 1, assim como a pontuação dos candidatos
    comparacoes, trocas, tempo = selection_sort(
        tarefas_ordenadas, indice_pontuacao=1
    )

    linhas_saida = []

    def registrar(mensagem):
        print(mensagem)
        linhas_saida.append(mensagem)

    registrar("=" * 72)
    registrar("PROBLEMA 18 — ORGANIZAÇÃO DE TAREFAS POR PRIORIDADE")
    registrar("Algoritmo: Selection Sort | Linguagem: Python")
    registrar("=" * 72)

    registrar(f"\nENTRADA: {len(tarefas)} tarefas de um dia de trabalho")
    registrar("  Prioridades de 1 (mais alta) a 5 (mais baixa).")
    for descricao, prioridade in tarefas:
        registrar(f"  Prioridade {prioridade} | {descricao}")

    registrar("\nRESULTADOS — ORDEM DE EXECUÇÃO DO DIA:")
    for posicao, tarefa in enumerate(tarefas_ordenadas, start=1):
        descricao, prioridade = tarefa
        registrar(f"  {posicao}. Prioridade {prioridade} | {descricao}")

    registrar("\nSIMULAÇÃO DO DIA:")
    for descricao, prioridade in tarefas_ordenadas:
        registrar(f"  Concluída: {descricao} | Prioridade {prioridade}")

    registrar("\nRESUMO:")
    registrar(f"  {len(tarefas_ordenadas)} tarefas executadas em ordem de prioridade.")
    registrar(f"  Comparações: {comparacoes}")
    registrar(f"  Trocas: {trocas}")
    registrar(f"  Tempo de ordenação: {tempo:.9f} segundos.")
    registrar("  O tempo medido é do algoritmo, não da duração das tarefas.")
    registrar("  Tarefas com a mesma prioridade não têm ordem original garantida.")

    registrar("\nCOMPLEXIDADE:")
    registrar("  Ordenação: O(n²) de tempo e O(1) de espaço auxiliar.")
    registrar("  Simulação: O(n²) de tempo e O(n) de espaço para cópia e registros.")
    registrar("  n é o número de tarefas.")

    with open("saidas/problema_18.txt", "w", encoding="utf-8") as arquivo_saida:
        arquivo_saida.write("\n".join(linhas_saida) + "\n")


if __name__ == "__main__":
    problema_01()
    print("\n")

    problema_02()
    print("\n")

    problema_03()
    print("\n")

    problema_04()
    print("\n")

    problema_11()
    print("\n")

    problema_12()
    print("\n")

    problema_15()
    print("\n")

    problema_16()
    print("\n")

    problema_17()
    print("\n")

    problema_18()
    print("\n")

    

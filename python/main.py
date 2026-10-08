import csv 
from lineares.fila import Fila
from lineares.pilha import Pilha

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
        registrar(f"  Paciente: {paciente[0]} | Chegada: {paciente[1]}")
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

if __name__ == "__main__":
    problema_01()
    print("\n")

    problema_02()
    print("\n")

    problema_03()
    print("\n")

    problema_04()

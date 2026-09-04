import csv 
from lineares.fila import Fila

# valores escolhidos para estimar o tempo fora do csv
duracao_por_tipo ={
    "Consulta": 15,
    "Exame": 20,
    "Cirurgia": 60,
    "Urgencia": 10,
    "Retorno": 10,
}
def horario_para_minutos(horario): #converte o horário "00:00" em minutos
    partes = horario.split(':')
    horas = int(partes[0])
    minutos = int(partes[1])
    return horas * 60 + minutos


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

    while not fila.vazia():
        paciente = fila.desenfileirar()
        print(f"Paciente: {paciente[0]}, Horário: {paciente[1]}")
        chegada = horario_para_minutos(paciente[1])
        duracao = duracao_por_tipo[paciente[2]]
        if chegada > fim_do_atendimento: # calcula o tempo de espera e o horário de início do atendimento
            inicio = chegada
        else:
            inicio = fim_do_atendimento
        espera = inicio - chegada
        fim_do_atendimento = inicio + duracao
        esperas.append(espera)
        print(f"Tempo de espera: {espera} minutos, Início do atendimento: {inicio // 60:02d}:{inicio % 60:02d}, Fim do atendimento: {fim_do_atendimento // 60:02d}:{fim_do_atendimento % 60:02d}")

    media_espera = sum(esperas) / len(esperas)
    print(f"Média de tempo de espera: {media_espera:.1f} minutos")

    bloco = f"""=== PROBLEMA 01 — SISTEMA DE ATENDIMENTO DE UMA CLÍNICA MÉDICA ===
Estrutura: Fila   |   Linguagem: Python

[ENTRADA]    {len(pacientes)} pacientes lidos de dados/pacientes.csv
[OPERAÇÕES]  {len(pacientes)} enfileiramentos, {len(pacientes)} desenfileiramentos
[SAÍDA]      ordem de atendimento por chegada | tempo médio de espera = {media_espera:.1f} min
[COMPLEX.]   enfileirar/desenfileirar O(1) | simulação O(n) | espaço O(n)
=========================================================="""

    print(bloco)

    with open('saidas/problema_01.txt', 'w', encoding='utf-8') as arquivo_saida:
        arquivo_saida.write(bloco)
    

def problema_02():
    with open('dados/impressoes.csv', newline="", encoding='utf-8') as arquivo:
        leitor = csv.reader(arquivo)
        linhas = list(leitor)
        cabecalho = linhas[0]
        impressoes = linhas[1:]

    fila = Fila()
    for impressao in impressoes:
        fila.enfileirar(impressao)

    if fila.vazia():
        print("Nenhuma impressão na fila.")
    else:
        print("Impressões na fila: ")
        for impressao in fila.listar():
            print(f"  - {impressao[1]} (Páginas: {impressao[2]})")

    fila.cancelar_ultimo() # cancela a última impressão da fila
    total_impressos = 0
    while not fila.vazia():
        impressao = fila.desenfileirar()
        print(f"Impressão: {impressao[1]}, Páginas: {impressao[2]}")
        total_impressos += 1

    bloco = f"""=== PROBLEMA 02 — FILA DE IMPRESSÃO EM LABORATÓRIO DE INFORMÁTICA ===
Estrutura: Fila   |   Linguagem: Python

[ENTRADA]    {len(impressoes)} trabalhos lidos de dados/impressoes.csv
[OPERAÇÕES]  {len(impressoes)} enfileiramentos, 1 listagem, 1 cancelamento, {total_impressos} desenfileiramentos
[SAÍDA]      {total_impressos} trabalhos impressos em ordem de chegada (1 cancelado antes da impressão)
[COMPLEX.]   enfileirar/desenfileirar/cancelar_ultimo O(1) | listar/simulação O(n) | espaço O(n)
=========================================================="""

    print(bloco)

    with open('saidas/problema_02.txt', 'w', encoding='utf-8') as arquivo_saida:
        arquivo_saida.write(bloco)

if __name__ == "__main__":
    problema_01()
    problema_02()
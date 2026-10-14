from time import perf_counter # disponibiliza a função de relógio do módulo time em python.

def bubble_sort(valores):

    comparacoes = 0
    trocas = 0
    inicio = perf_counter()
    n = len(valores)

    # leva o maior valor da parte não ordenada para o final
    for i in range(n-1): 
        houve_troca = False

        # ignora os últimos elementos i que já estão na posição correta
        for j in range (n - 1 - i): 
            comparacoes += 1 # conta a comparação entre vizinhos

            if valores[j] > valores [j + 1]:
                temporario = valores[j]
                valores[j] = valores[j + 1]
                valores[j + 1] = temporario
                trocas += 1
                houve_troca = True
        if not houve_troca:
            break

    tempo = perf_counter() - inicio
    return comparacoes, trocas, tempo
from time import perf_counter # disponibiliza a função de relógio do módulo time em python.

def bubble_sort(valores):

    comparacoes = 0
    trocas = 0
    inicio = perf_counter()
    n = len(valores)

    for i in range(n-1):
        for j in range (n - 1 - i):
            comparacoes += 1
            if valores[j] > valores [j + 1]:
                temporario = valores[j]
                valores[j] = valores[j + 1]
                valores[j + 1] = temporario
                trocas += 1

    tempo = perf_counter() - inicio
    return comparacoes, trocas, tempo
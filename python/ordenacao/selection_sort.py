from time import perf_counter

def selection_sort(valores, indice_pontuacao=None, decrescente=False):
    comparacoes = 0
    trocas = 0
    inicio = perf_counter()
    n = len(valores)

    for i in range(n - 1):
        menor = i
        for j in range(i + 1, n):
            comparacoes += 1 
            if indice_pontuacao is None:
                atual = valores [j]
                escolhido = valores [menor]
            else:
                atual = valores[j][indice_pontuacao]
                escolhido = valores[menor][indice_pontuacao]
            if(not decrescente and atual < escolhido) or \
                (decrescente and atual > escolhido):
                menor = j
        if menor != i:
            trocas += 1 
            temporario = valores[i]
            valores[i] = valores[menor]
            valores[menor] = temporario

    tempo = perf_counter() - inicio
    return comparacoes, trocas, tempo
            
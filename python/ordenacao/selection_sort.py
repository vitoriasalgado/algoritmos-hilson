from time import perf_counter


def selection_sort(valores):
    comparacoes = 0
    trocas = 0
    inicio = perf_counter()
    n = len(valores)

    for i in range(n - 1):
        menor = i

        # procura a posição do menor valor na parte ainda não ordenada
        for j in range(i + 1, n):
            comparacoes += 1
            if valores[j] < valores[menor]:
                menor = j

        # troca somente após terminar a busca e se a posição for diferente
        if menor != i:
            temporario = valores[i]
            valores[i] = valores[menor]
            valores[menor] = temporario
            trocas += 1

    tempo = perf_counter() - inicio
    return comparacoes, trocas, tempo

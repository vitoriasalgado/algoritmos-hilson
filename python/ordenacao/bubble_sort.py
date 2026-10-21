from time import perf_counter # importa o relógio para medir o tempo

def bubble_sort(valores, indice_preco=None, decrescente=False):

    comparacoes = 0
    trocas = 0
    inicio = perf_counter()
    n = len(valores)

    # leva um valor para o fim da parte não ordenada
    for i in range(n-1): 
        houve_troca = False

        # pula os valores do fim que já estão no lugar
        for j in range (n - 1 - i): 
            comparacoes += 1 # conta uma comparação entre vizinhos
            if indice_preco is None:
                atual = valores[j]
                proximo = valores [j + 1]
            else:
                atual = valores[j][indice_preco]
                proximo = valores[j + 1][indice_preco]

            if (not decrescente and atual > proximo) or \
               (decrescente and atual < proximo):
                temporario = valores[j]
                valores[j] = valores[j + 1]
                valores[j + 1] = temporario
                trocas += 1
                houve_troca = True

        if not houve_troca:
            break

    tempo = perf_counter() - inicio
    return comparacoes, trocas, tempo

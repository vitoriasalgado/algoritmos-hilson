from time import perf_counter # disponibiliza a função de relógio do módulo time em python.

def bubble_sort(valores, indice_preco=None, decrescente=False):

    comparacoes = 0
    trocas = 0
    inicio = perf_counter()
    n = len(valores)

    # posiciona um elemento no final da parte não ordenada, conforme a direção
    for i in range(n-1): 
        houve_troca = False

        # ignora os últimos elementos i que já estão na posição correta
        for j in range (n - 1 - i): 
            comparacoes += 1 # conta a comparação entre vizinhos
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

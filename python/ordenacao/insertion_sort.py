from time import perf_counter


def insertion_sort(cartas, indice_chave=None, decrescente=False):
    comparacoes = 0
    trocas = 0
    inicio = perf_counter()

    for indice in range(1, len(cartas)):
        posicao = indice

        while posicao > 0:
            anterior = cartas[posicao - 1]
            atual = cartas[posicao]
            if indice_chave is not None:
                anterior = anterior[indice_chave]
                atual = atual[indice_chave]

            comparacoes += 1
            if (not decrescente and anterior <= atual) or (
                decrescente and anterior >= atual
            ):
                break

            temporario = cartas[posicao]
            cartas[posicao] = cartas[posicao - 1]
            cartas[posicao - 1] = temporario

            trocas += 1
            posicao -= 1

    tempo = perf_counter() - inicio
    return comparacoes, trocas, tempo

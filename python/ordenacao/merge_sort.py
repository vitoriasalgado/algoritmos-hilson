from time import perf_counter


def merge_sort(valores, indice_chave=None, decrescente=False):
    comparacoes = 0
    auxiliar = [None] * len(valores)
    inicio = perf_counter()

    def ordenar(esquerda, direita):
        nonlocal comparacoes
        if direita - esquerda <= 1:
            return

        meio = (esquerda + direita) // 2
        ordenar(esquerda, meio)
        ordenar(meio, direita)
        i = esquerda
        j = meio
        destino = esquerda

        while i < meio and j < direita:
            primeiro = valores[i]
            segundo = valores[j]
            if indice_chave is not None:
                primeiro = primeiro[indice_chave]
                segundo = segundo[indice_chave]

            comparacoes += 1
            if (not decrescente and primeiro <= segundo) or (
                decrescente and primeiro >= segundo
            ):
                auxiliar[destino] = valores[i]
                i += 1
            else:
                auxiliar[destino] = valores[j]
                j += 1
            destino += 1

        while i < meio:
            auxiliar[destino] = valores[i]
            i += 1
            destino += 1

        while j < direita:
            auxiliar[destino] = valores[j]
            j += 1
            destino += 1

        for indice in range(esquerda, direita):
            valores[indice] = auxiliar[indice]

    ordenar(0, len(valores))
    # intercala os valores usando uma lista auxiliar
    return comparacoes, 0, perf_counter() - inicio


def mesclar_contatos(primeira, segunda):
    inicio = perf_counter()
    resultado = []
    comparacoes = 0

    for lista in [primeira, segunda]:
        for indice in range(1, len(lista)):
            comparacoes += 1
            if lista[indice - 1][0] > lista[indice][0]:
                raise ValueError("As listas de contatos devem estar ordenadas.")

    i = 0
    j = 0
    while i < len(primeira) or j < len(segunda):
        if i == len(primeira):
            contato = segunda[j]
            j += 1
        elif j == len(segunda):
            contato = primeira[i]
            i += 1
        else:
            comparacoes += 1
            if primeira[i][0] <= segunda[j][0]:
                contato = primeira[i]
                i += 1
            else:
                contato = segunda[j]
                j += 1

        if resultado:
            comparacoes += 1
            if resultado[-1][0] == contato[0]:
                continue
        resultado.append(contato)

    return resultado, comparacoes, perf_counter() - inicio

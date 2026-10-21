from time import perf_counter


def quick_sort(valores, indice_chave=None, decrescente=False):
    comparacoes = 0
    trocas = 0
    inicio = perf_counter()

    def chave(indice):
        if indice_chave is None:
            return valores[indice]
        return valores[indice][indice_chave]

    def trocar(primeiro, segundo):
        nonlocal trocas
        if primeiro != segundo:
            temporario = valores[primeiro]
            valores[primeiro] = valores[segundo]
            valores[segundo] = temporario
            trocas += 1

    def particionar(esquerda, direita):
        nonlocal comparacoes
        candidatos = [esquerda, (esquerda + direita) // 2, direita]

        # ordena três índices para escolher o pivô do meio
        for primeiro, segundo in [(0, 1), (1, 2), (0, 1)]:
            comparacoes += 1
            if chave(candidatos[primeiro]) > chave(candidatos[segundo]):
                temporario = candidatos[primeiro]
                candidatos[primeiro] = candidatos[segundo]
                candidatos[segundo] = temporario

        trocar(candidatos[1], direita)
        pivo = chave(direita)
        limite = esquerda

        for indice in range(esquerda, direita):
            comparacoes += 1
            atual = chave(indice)
            if (not decrescente and atual <= pivo) or (
                decrescente and atual >= pivo
            ):
                trocar(indice, limite)
                limite += 1

        trocar(limite, direita)
        return limite

    def ordenar(esquerda, direita):
        # usa recursão na parte menor e um laço na maior
        while esquerda < direita:
            pivo = particionar(esquerda, direita)
            if pivo - esquerda < direita - pivo:
                ordenar(esquerda, pivo - 1)
                esquerda = pivo + 1
            else:
                ordenar(pivo + 1, direita)
                direita = pivo - 1

    ordenar(0, len(valores) - 1)
    return comparacoes, trocas, perf_counter() - inicio

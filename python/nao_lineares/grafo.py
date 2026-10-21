from lineares.fila import Fila


class Grafo():

    def __init__(self):
        self.vertices = []
        self.adjacencias = []

    def adicionar_vertice(self, nome):
        if nome in self.vertices:
            return
        self.vertices.append(nome)
        self.adjacencias.append([])

    def adicionar_aresta(self, origem, destino, peso):
        if origem not in self.vertices or destino not in self.vertices:
            raise ValueError("Origem e destino devem estar cadastrado")
        if peso < 0:
            raise ValueError("O peso não pode ser negativo.")
        for indice, nome in enumerate(self.vertices):
            if nome == origem:
                indice_origem = indice
            if nome == destino:
                indice_destino = indice

        self.adjacencias[indice_origem].append([indice_destino, peso])
        self.adjacencias[indice_destino].append([indice_origem, peso])

    def dijkstra(self, origem):
        if origem not in self.vertices:
            raise ValueError("A origem precisa estar cadastrada.")

        quantidade = len(self.vertices)

        distancias =[float("inf")] * quantidade
        visitados = [False] * quantidade
        anteriores = [None] * quantidade

        for indice, nome in enumerate(self.vertices):
            if nome == origem:
                indice_origem = indice
        distancias[indice_origem] = 0

        for _ in range(quantidade):
            atual = None
            menor_distancia = float("inf")

            for indice in range(quantidade):
                if not visitados[indice] and distancias[indice] < menor_distancia:
                    atual = indice
                    menor_distancia = distancias[indice]

            if atual is None:
                break

            visitados[atual] = True

            for vizinho, peso in self.adjacencias[atual]:
                if not visitados[vizinho]:
                    nova_distancia = distancias[atual] + peso
                    if nova_distancia < distancias[vizinho]:
                        distancias[vizinho] = nova_distancia
                        anteriores[vizinho] = atual

        return distancias, anteriores

    def menor_caminho(self, origem, destino):
        if destino not in self.vertices:
            raise ValueError("O destino precisa estar cadastrado.")

        distancias, anteriores = self.dijkstra(origem)

        for indice, nome in enumerate(self.vertices):
            if nome == destino:
                indice_destino = indice

        if distancias[indice_destino] == float("inf"):
            return [], float("inf")

        caminho_inverso = []
        atual = indice_destino

        # segue os anteriores do destino até a origem
        while atual is not None:
            caminho_inverso.append(self.vertices[atual])
            atual = anteriores[atual]

        caminho = []
        for indice in range(len(caminho_inverso) - 1, -1, -1):
            caminho.append(caminho_inverso[indice])

        return caminho, distancias[indice_destino]

    def listar_amigos(self, nome):
        if nome not in self.vertices:
            raise ValueError("A pessoa não está cadastrada.")

        for indice, pessoa in enumerate(self.vertices):
            if pessoa == nome:
                indice_pessoa = indice
                break

        amigos = []
        for vizinho, peso in self.adjacencias[indice_pessoa]:
            amigos.append(self.vertices[vizinho])

        return amigos

    def amigos_em_comum(self, primeira_pessoa, segunda_pessoa):
        amigos_primeira = self.listar_amigos(primeira_pessoa)
        amigos_segunda = self.listar_amigos(segunda_pessoa)

        comuns = []

        for amigo in amigos_primeira:
            if amigo in amigos_segunda:
                comuns.append(amigo)

        return comuns

    def adicionar_amizade(self, primeira_pessoa, segunda_pessoa):
        amigos = self.listar_amigos(primeira_pessoa)
        self.listar_amigos(segunda_pessoa)

        if primeira_pessoa == segunda_pessoa:
            raise ValueError("Uma pessoa não pode adicionar a si mesma.")

        if segunda_pessoa in amigos:
            return False

        self.adicionar_aresta(primeira_pessoa, segunda_pessoa, 1)
        return True

    def graus_de_separacao(self, origem, destino):
        """Usa busca em largura (BFS) para contar ligações entre pessoas."""
        if origem not in self.vertices or destino not in self.vertices:
            raise ValueError("As duas pessoas precisam estar cadastradas.")

        for indice, pessoa in enumerate(self.vertices):
            if pessoa == origem:
                indice_origem = indice
            if pessoa == destino:
                indice_destino = indice

        # -1 significa que a pessoa ainda não foi alcançada.
        distancias = [-1] * len(self.vertices)
        distancias[indice_origem] = 0

        fila = Fila()
        fila.enfileirar(indice_origem)

        while not fila.vazia():
            atual = fila.desenfileirar()

            if atual == indice_destino:
                return distancias[atual]

            for vizinho, peso in self.adjacencias[atual]:
                if distancias[vizinho] == -1:
                    distancias[vizinho] = distancias[atual] + 1
                    fila.enfileirar(vizinho)

        return -1

    def sugerir_amigos(self, nome):
        amigos = self.listar_amigos(nome)
        sugestoes = []

        for amigo in amigos:
            for candidato in self.listar_amigos(amigo):
                if candidato != nome and candidato not in amigos:
                    if candidato not in sugestoes:
                        sugestoes.append(candidato)

        return sugestoes


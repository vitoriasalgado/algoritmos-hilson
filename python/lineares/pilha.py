class Pilha:
    def __init__(self):
        self._itens = [] 

    def vazia(self):
        """ mapeia para is_Empty """
        return len(self._itens) == 0

    def empilhar(self, item):
        """ mapeia para push """
        self._itens.append(item)

    def desempilhar(self):
        """ mapeia para pop """
        if self.vazia():
            raise IndexError("Pilha vazia (desempilhar)")
        return self._itens.pop()

    def topo(self):
        """ mapeia para top """
        if self.vazia():
            raise IndexError("Pilha vazia (topo)")
        return self._itens[-1]


    def tamanho(self):
        """ mapeia para size """
        return len(self._itens)


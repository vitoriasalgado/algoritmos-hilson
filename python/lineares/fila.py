class Fila:
    def __init__(self):
        self._itens = []
        self._inicio = 0 # evita remover elementos do início da lista

    def vazia(self):
        """ mapeia para is_empty """
        return self._inicio == len(self._itens)

    def enfileirar(self, item):
        """ mapeia para enqueue """
        self._itens.append(item)

    def desenfileirar(self): 
        """ mapeia para dequeue """
        if not self.vazia():
            item = self._itens[self._inicio]
            self._inicio += 1
            return item
        else:
            raise IndexError("Fila vazia (desenfileirar)") # erro aparecerá imediatamente se tentar remover de uma fila vazia
        
    def tamanho(self):
        """ mapeia para size """
        return len(self._itens) - self._inicio
    
    def frente(self):
        """ mapeia para front """
        if not self.vazia():
            return self._itens[self._inicio]
        else:
            raise IndexError("Fila vazia (frente)") # erro aparecerá imediatamente se tentar acessar a frente de uma fila vazia

    def cancelar_ultimo(self):
        """ cancela o último item da fila """
        if not self.vazia():
            self._itens.pop() # remove o último elemento da lista
        else:
            raise IndexError("Fila vazia (cancelar último)") # erro aparecerá imediatamente se tentar cancelar de uma fila vazia

    def listar(self):
        """ lista os itens da fila """
        return self._itens[self._inicio:] # retorna apenas os itens que ainda estão na fila
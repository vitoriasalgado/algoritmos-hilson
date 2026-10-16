
package ed.lineares;

import ed.app.Musica;

public class No {
    private Musica musica;
    private No proximo;

    public No(Musica musica) {
        this.musica = musica;
        this.proximo = null;
    }

    public Musica getMusica() {
        return musica;
    }

    public void setMusica(Musica musica) {
        this.musica = musica;
    }

    public No getProximo() {
        return proximo;
    }

    public void setProximo(No proximo) {
        this.proximo = proximo;
    }
}

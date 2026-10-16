
package ed.app;

public class Musica {
    private String titulo;
    private String artista;
    private int duracao;

    public Musica(String titulo, String artista, int duracao) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("O título não pode ser vazio.");
        }
        if (artista == null || artista.isBlank()) {
            throw new IllegalArgumentException("O artista não pode ser vazio.");
        }
        if (duracao < 0) {
            throw new IllegalArgumentException("A duração não pode ser negativa.");
        }

        this.titulo = titulo;
        this.artista = artista;
        this.duracao = duracao;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public int getDuracao() {
        return duracao;
    }

    @Override
    public String toString() {
        String tempo = duracao == 0 ? "duração desconhecida" : duracao + " segundos";
        return titulo + " — " + artista + " (" + tempo + ")";
    }
}

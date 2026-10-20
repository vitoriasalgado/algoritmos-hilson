package ed.app;

public class Musica {
    private final String titulo;
    private final String artista;
    private final int duracao;

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
        int minutos = duracao / 60;
        int segundos = duracao % 60;

        return String.format("%s — %s (%d:%02d)",
            titulo, artista, minutos, segundos);
    }
}

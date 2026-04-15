import java.time.LocalDate;

public class Emprestimo {
    private Livro livro;
    private Usuario usuario;
    private LocalDate data;

    public Emprestimo(Livro livro, Usuario usuario, LocalDate data) {
        this.livro = livro;
        this.usuario = usuario;
        this.data = data;
    }

    public Livro getLivro() {
        return livro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getData() {
        return data;
    }

    @Override
    public String toString() {
        return String.format("|| Livro: %s ||\n || Emprestado para: %s ||\n || Dia: %s ||", livro.getTitulo(), usuario.getNome(), data);
    }
}

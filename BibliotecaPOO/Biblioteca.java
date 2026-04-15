import java.util.ArrayList;
import java.util.List;

public class Biblioteca {
    private List<Livro> livros;
    private List<Usuario> usuarios;
    private List<Emprestimo> emprestimos;

    public Biblioteca() {
        this.livros = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.emprestimos = new ArrayList<>();
    }

    public void cadastrarLivro(Livro livro) {
        livros.add(livro);
    }

    public void cadastrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public Livro buscarLivroPorIsbn(String isbn) {
        for (Livro livro : livros) {
            if (livro.getIsbn().equalsIgnoreCase(isbn)) {
                return livro;
            }
        }
        return null;
    }

    public Usuario buscarUsuarioPorId(String id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(id)) {
                return usuario;
            }
        }
        return null;
    }

    public boolean verificarDisponibilidade(String isbn) {
        Livro livro = buscarLivroPorIsbn(isbn);
        return livro != null && livro.verificarDisponibilidade();
    }

    public boolean emprestarLivro(String isbn, String usuarioId) {
        Livro livro = buscarLivroPorIsbn(isbn);
        Usuario usuario = buscarUsuarioPorId(usuarioId);

        if (livro == null || usuario == null) {
            return false;
        }
        if (!livro.verificarDisponibilidade()) {
            return false;
        }
        if (!usuario.Usuariodisponivel()) {
            return false;
        }

        livro.setDisponivel(false);
        usuario.AumentarEmprestimo();
        emprestimos.add(new Emprestimo(livro, usuario, java.time.LocalDate.now()));
        return true;
    }

    public boolean devolverLivro(String isbn, String usuarioId) {
        Emprestimo ativo = buscarEmprestimoAtivo(isbn, usuarioId);
        if (ativo == null) {
            return false;
        }

        ativo.getLivro().setDisponivel(true);
        ativo.getUsuario().SubtrairEmprestimo();
        emprestimos.remove(ativo);
        return true;
    }

    private Emprestimo buscarEmprestimoAtivo(String isbn, String usuarioId) {
        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getLivro().getIsbn().equalsIgnoreCase(isbn)
                    && emprestimo.getUsuario().getId().equals(usuarioId)) {
                return emprestimo;
            }
        }
        return null;
    }

    public List<Livro> listarLivrosDisponiveis() {
        List<Livro> disponiveis = new ArrayList<>();
        for (Livro livro : livros) {
            if (livro.isDisponivel()) {
                disponiveis.add(livro);
            }
        }
        return disponiveis;
    }

    public List<Emprestimo> listarEmprestimos() {
        return new ArrayList<>(emprestimos);
    }
}

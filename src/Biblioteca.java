import java.time.LocalDate;
import java.util.List;

/**
 * Classe principal de regras de negócio.
 * Usa os repositórios via interface, sem depender da implementação SQLite diretamente.
 */
public class Biblioteca {

    private final LivroRepository livroRepo;
    private final UsuarioRepository usuarioRepo;
    private final EmprestimoRepository emprestimoRepo;

    public Biblioteca(LivroRepository livroRepo, UsuarioRepository usuarioRepo,
                      EmprestimoRepository emprestimoRepo) {
        this.livroRepo = livroRepo;
        this.usuarioRepo = usuarioRepo;
        this.emprestimoRepo = emprestimoRepo;
    }

    // ── Livros ──────────────────────────────────────────────────────────────

    public void cadastrarLivro(Livro livro) {
        livroRepo.salvar(livro);
    }

    public Livro buscarLivroPorIsbn(String isbn) {
        return livroRepo.buscarPorIsbn(isbn);
    }

    public List<Livro> listarLivrosDisponiveis() {
        return livroRepo.listarDisponiveis();
    }

    public boolean verificarDisponibilidade(String isbn) {
        Livro livro = buscarLivroPorIsbn(isbn);
        return livro != null && livro.verificarDisponibilidade();
    }

    // ── Usuários ─────────────────────────────────────────────────────────────

    public void cadastrarUsuario(Usuario usuario) {
        usuarioRepo.salvar(usuario);
    }

    public Usuario buscarUsuarioPorId(String id) {
        return usuarioRepo.buscarPorId(id);
    }

    // ── Empréstimos ──────────────────────────────────────────────────────────

    public boolean emprestarLivro(String isbn, String usuarioId) {
        Livro livro = livroRepo.buscarPorIsbn(isbn);
        Usuario usuario = usuarioRepo.buscarPorId(usuarioId);

        if (livro == null || usuario == null) return false;
        if (!livro.verificarDisponibilidade()) return false;
        if (!usuario.Usuariodisponivel()) return false;

        // Atualiza estado
        livro.setDisponivel(false);
        usuario.AumentarEmprestimo();

        // Persiste alterações
        livroRepo.atualizar(livro);
        usuarioRepo.atualizar(usuario);
        emprestimoRepo.salvar(new Emprestimo(livro, usuario, LocalDate.now()));

        return true;
    }

    public boolean devolverLivro(String isbn, String usuarioId) {
        Emprestimo ativo = emprestimoRepo.buscarAtivo(isbn, usuarioId);
        if (ativo == null) return false;

        Livro livro = ativo.getLivro();
        Usuario usuario = ativo.getUsuario();

        // Atualiza estado
        livro.setDisponivel(true);
        usuario.SubtrairEmprestimo();

        // Persiste alterações
        livroRepo.atualizar(livro);
        usuarioRepo.atualizar(usuario);
        emprestimoRepo.remover(ativo.getId());

        return true;
    }

    public List<Emprestimo> listarEmprestimos() {
        return emprestimoRepo.listarTodos();
    }
}

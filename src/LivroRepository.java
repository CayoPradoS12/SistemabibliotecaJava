import java.util.List;

/**
 * Interface que define o contrato de persistência para Livros.
 *
 * Justificativa:
 * - Desacopla a lógica de negócio da implementação de persistência.
 * - Permite trocar o banco de dados (SQLite → PostgreSQL, etc.) sem alterar Biblioteca.java.
 * - Facilita testes unitários com implementações mock.
 */
public interface LivroRepository {
    void salvar(Livro livro);
    Livro buscarPorIsbn(String isbn);
    List<Livro> listarTodos();
    List<Livro> listarDisponiveis();
    void atualizar(Livro livro);
}

import java.util.List;

/**
 * Interface que define o contrato de persistência para Empréstimos.
 */
public interface EmprestimoRepository {
    void salvar(Emprestimo emprestimo);
    Emprestimo buscarAtivo(String isbn, String usuarioId);
    List<Emprestimo> listarTodos();
    void remover(int id);
}

import java.util.List;

/**
 * Interface que define o contrato de persistência para Usuários.
 */
public interface UsuarioRepository {
    void salvar(Usuario usuario);
    Usuario buscarPorId(String id);
    List<Usuario> listarTodos();
    void atualizar(Usuario usuario);
}

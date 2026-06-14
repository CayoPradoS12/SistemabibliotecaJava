import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoRepositorySQLite implements EmprestimoRepository {

    private final LivroRepository livroRepo;
    private final UsuarioRepository usuarioRepo;

    public EmprestimoRepositorySQLite(LivroRepository livroRepo, UsuarioRepository usuarioRepo) {
        this.livroRepo = livroRepo;
        this.usuarioRepo = usuarioRepo;
    }

    private Connection conn() {
        return DatabaseConnection.getInstancia().getConnection();
    }

    @Override
    public void salvar(Emprestimo emprestimo) {
        String sql = "INSERT INTO emprestimos (isbn_livro, id_usuario, data) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, emprestimo.getLivro().getIsbn());
            ps.setString(2, emprestimo.getUsuario().getId());
            ps.setString(3, emprestimo.getData().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar empréstimo: " + e.getMessage(), e);
        }
    }

    @Override
    public Emprestimo buscarAtivo(String isbn, String usuarioId) {
        String sql = "SELECT * FROM emprestimos WHERE UPPER(isbn_livro) = UPPER(?) AND id_usuario = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, isbn);
            ps.setString(2, usuarioId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar empréstimo ativo: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Emprestimo> listarTodos() {
        List<Emprestimo> lista = new ArrayList<>();
        String sql = "SELECT * FROM emprestimos";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar empréstimos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public void remover(int id) {
        String sql = "DELETE FROM emprestimos WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover empréstimo: " + e.getMessage(), e);
        }
    }

    private Emprestimo mapear(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String isbn = rs.getString("isbn_livro");
        String usuarioId = rs.getString("id_usuario");
        LocalDate data = LocalDate.parse(rs.getString("data"));

        Livro livro = livroRepo.buscarPorIsbn(isbn);
        Usuario usuario = usuarioRepo.buscarPorId(usuarioId);

        return new Emprestimo(id, livro, usuario, data);
    }
}

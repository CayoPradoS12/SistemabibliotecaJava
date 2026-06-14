import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroRepositorySQLite implements LivroRepository {

    private Connection conn() {
        return DatabaseConnection.getInstancia().getConnection();
    }

    @Override
    public void salvar(Livro livro) {
        String sql = "INSERT INTO livros (isbn, titulo, autor, disponivel) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, livro.getIsbn());
            ps.setString(2, livro.getTitulo());
            ps.setString(3, livro.getAutor());
            ps.setInt(4, livro.isDisponivel() ? 1 : 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar livro: " + e.getMessage(), e);
        }
    }

    @Override
    public Livro buscarPorIsbn(String isbn) {
        String sql = "SELECT * FROM livros WHERE UPPER(isbn) = UPPER(?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Livro> listarTodos() {
        List<Livro> lista = new ArrayList<>();
        String sql = "SELECT * FROM livros";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Livro> listarDisponiveis() {
        List<Livro> lista = new ArrayList<>();
        String sql = "SELECT * FROM livros WHERE disponivel = 1";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros disponíveis: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public void atualizar(Livro livro) {
        String sql = "UPDATE livros SET titulo = ?, autor = ?, disponivel = ? WHERE isbn = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getAutor());
            ps.setInt(3, livro.isDisponivel() ? 1 : 0);
            ps.setString(4, livro.getIsbn());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar livro: " + e.getMessage(), e);
        }
    }

    private Livro mapear(ResultSet rs) throws SQLException {
        Livro livro = new Livro(
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("isbn")
        );
        livro.setDisponivel(rs.getInt("disponivel") == 1);
        return livro;
    }
}

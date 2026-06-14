import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositorySQLite implements UsuarioRepository {

    private Connection conn() {
        return DatabaseConnection.getInstancia().getConnection();
    }

    @Override
    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (id, nome, tipo, emprestimos_ativos) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, usuario.getId());
            ps.setString(2, usuario.getNome());
            ps.setString(3, usuario.getTipo());
            ps.setInt(4, usuario.getEmprestimosAtivos());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário: " + e.getMessage(), e);
        }
    }

    @Override
    public Usuario buscarPorId(String id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome = ?, tipo = ?, emprestimos_ativos = ? WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getTipo());
            ps.setInt(3, usuario.getEmprestimosAtivos());
            ps.setString(4, usuario.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage(), e);
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        String id = rs.getString("id");
        String nome = rs.getString("nome");
        int empAtivos = rs.getInt("emprestimos_ativos");

        // Usa o Factory para recriar o objeto correto
        Usuario usuario = UsuarioFactory.criar(tipo, id, nome);
        usuario.setEmprestimosAtivos(empAtivos);
        return usuario;
    }
}

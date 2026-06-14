import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Padrão Singleton para gerenciar a conexão com o banco de dados SQLite.
 *
 * Justificativa do Singleton:
 * - Uma única conexão é suficiente e mais eficiente para uma aplicação de terminal.
 * - Evita que múltiplas partes do sistema abram conexões redundantes com o banco.
 * - Garante que todas as operações compartilhem o mesmo estado transacional.
 * - Centraliza a configuração e o ciclo de vida da conexão em um único lugar.
 */
public class DatabaseConnection {

    private static DatabaseConnection instancia;
    private Connection connection;
    private static final String URL = "jdbc:sqlite:biblioteca.db";

    // Construtor privado: impede instanciação externa
    private DatabaseConnection() {
        try { Class.forName("org.sqlite.JDBC"); } catch (ClassNotFoundException e) { throw new RuntimeException("Driver SQLite não encontrado", e); }
        try {
            connection = DriverManager.getConnection(URL);
            inicializarTabelas();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }

    /**
     * Retorna a única instância da conexão (thread-safe com synchronized).
     */
    public static synchronized DatabaseConnection getInstancia() {
        if (instancia == null || !instanciaValida()) {
            instancia = new DatabaseConnection();
        }
        return instancia;
    }

    private static boolean instanciaValida() {
        try {
            return instancia != null && !instancia.connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void fechar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }

    /**
     * Cria as tabelas do banco caso ainda não existam.
     */
    private void inicializarTabelas() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Tabela de livros
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS livros (
                    isbn TEXT PRIMARY KEY,
                    titulo TEXT NOT NULL,
                    autor TEXT NOT NULL,
                    disponivel INTEGER NOT NULL DEFAULT 1
                )
            """);

            // Tabela de usuários
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS usuarios (
                    id TEXT PRIMARY KEY,
                    nome TEXT NOT NULL,
                    tipo TEXT NOT NULL,
                    emprestimos_ativos INTEGER NOT NULL DEFAULT 0
                )
            """);

            // Tabela de empréstimos
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS emprestimos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    isbn_livro TEXT NOT NULL,
                    id_usuario TEXT NOT NULL,
                    data TEXT NOT NULL,
                    FOREIGN KEY (isbn_livro) REFERENCES livros(isbn),
                    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
                )
            """);
        }
    }
}

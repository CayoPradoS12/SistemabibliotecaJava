import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Inicializa repositórios via SQLite (Singleton de conexão é usado internamente)
        LivroRepository livroRepo       = new LivroRepositorySQLite();
        UsuarioRepository usuarioRepo   = new UsuarioRepositorySQLite();
        EmprestimoRepository empRepo    = new EmprestimoRepositorySQLite(livroRepo, usuarioRepo);
        Biblioteca biblioteca           = new Biblioteca(livroRepo, usuarioRepo, empRepo);

        Scanner scanner = new Scanner(System.in);
        int opcao;

        System.out.println("==============================================");
        System.out.println("  Sistema de Biblioteca  —  v2.0 com SQLite  ");
        System.out.println("==============================================");

        do {
            mostrarMenu();
            opcao = lerInteiro(scanner, "Selecione a opção desejada: ");

            switch (opcao) {
                case 1  -> cadastrarLivro(scanner, biblioteca);
                case 2  -> cadastrarUsuario(scanner, biblioteca);
                case 3  -> emprestarLivro(scanner, biblioteca);
                case 4  -> devolverLivro(scanner, biblioteca);
                case 5  -> listarLivrosDisponiveis(biblioteca);
                case 6  -> listarEmprestimos(biblioteca);
                case 0  -> System.out.println("Encerrando. Até logo!");
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
            System.out.println();
        } while (opcao != 0);

        DatabaseConnection.getInstancia().fechar();
        scanner.close();
    }

    // ── Menu ─────────────────────────────────────────────────────────────────

    private static void mostrarMenu() {
        System.out.println("=== Biblioteca ===");
        System.out.println("1 - Cadastrar livro");
        System.out.println("2 - Cadastrar usuário");
        System.out.println("3 - Emprestar livro");
        System.out.println("4 - Devolver livro");
        System.out.println("5 - Listar livros disponíveis");
        System.out.println("6 - Listar empréstimos ativos");
        System.out.println("0 - Sair");
    }

    // ── Handlers de menu ─────────────────────────────────────────────────────

    private static void cadastrarLivro(Scanner scanner, Biblioteca biblioteca) {
        System.out.println("\n--- Cadastro de Livro ---");
        System.out.print("Título: ");
        String titulo = scanner.nextLine().trim();
        System.out.print("Autor: ");
        String autor = scanner.nextLine().trim();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine().trim();

        if (biblioteca.buscarLivroPorIsbn(isbn) != null) {
            System.out.println("Este livro já está cadastrado.");
            return;
        }

        biblioteca.cadastrarLivro(new Livro(titulo, autor, isbn));
        System.out.println("Livro cadastrado com sucesso.");
    }

    private static void cadastrarUsuario(Scanner scanner, Biblioteca biblioteca) {
        System.out.println("\n--- Cadastro de Usuário ---");

        int tipo;
        do {
            System.out.println("1 - Aluno  |  2 - Professor");
            tipo = lerInteiro(scanner, "Tipo de usuário: ");
        } while (tipo != 1 && tipo != 2);

        String cpf = lerCpfValido(scanner, "CPF (11 dígitos): ");

        if (biblioteca.buscarUsuarioPorId(cpf) != null) {
            System.out.println("Já existe um usuário com esse CPF.");
            return;
        }

        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();

        // UsuarioFactory cria o tipo certo sem if/else espalhado
        String tipoStr = (tipo == 1) ? "aluno" : "professor";
        Usuario usuario = UsuarioFactory.criar(tipoStr, cpf, nome);

        biblioteca.cadastrarUsuario(usuario);
        System.out.println("Usuário cadastrado com sucesso.");
    }

    private static void emprestarLivro(Scanner scanner, Biblioteca biblioteca) {
        System.out.println("\n--- Empréstimo de Livro ---");
        System.out.print("ISBN do livro: ");
        String isbn = scanner.nextLine().trim();
        String cpf = lerCpfValido(scanner, "CPF do usuário: ");

        if (biblioteca.emprestarLivro(isbn, cpf)) {
            System.out.println("Empréstimo realizado com sucesso.");
        } else {
            System.out.println("Não foi possível realizar o empréstimo.");
            System.out.println("Verifique se o livro existe, está disponível e se o usuário não atingiu seu limite.");
        }
    }

    private static void devolverLivro(Scanner scanner, Biblioteca biblioteca) {
        System.out.println("\n--- Devolução de Livro ---");
        System.out.print("ISBN do livro: ");
        String isbn = scanner.nextLine().trim();
        String cpf = lerCpfValido(scanner, "CPF do usuário: ");

        if (biblioteca.devolverLivro(isbn, cpf)) {
            System.out.println("Livro devolvido com sucesso.");
        } else {
            System.out.println("Não foi possível devolver o livro.");
            System.out.println("Verifique se os dados estão corretos e se o livro está emprestado para esse usuário.");
        }
    }

    private static void listarLivrosDisponiveis(Biblioteca biblioteca) {
        System.out.println("\n--- Livros Disponíveis ---");
        List<Livro> lista = biblioteca.listarLivrosDisponiveis();
        if (lista.isEmpty()) {
            System.out.println("Nenhum livro disponível no momento.");
            return;
        }
        lista.forEach(System.out::println);
    }

    private static void listarEmprestimos(Biblioteca biblioteca) {
        System.out.println("\n--- Empréstimos Ativos ---");
        List<Emprestimo> lista = biblioteca.listarEmprestimos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum empréstimo ativo no momento.");
            return;
        }
        lista.forEach(System.out::println);
    }

    // ── Utilitários de entrada ────────────────────────────────────────────────

    private static int lerInteiro(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número inteiro.");
            }
        }
    }

    private static String lerCpfValido(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String cpf = scanner.nextLine().trim();
            if (cpf.matches("\\d{11}")) return cpf;
            System.out.println("CPF inválido. Digite exatamente 11 dígitos numéricos.");
        }
    }
}

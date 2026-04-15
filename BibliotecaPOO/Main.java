import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Biblioteca biblioteca = new Biblioteca();
        int opcao;

        do {
            mostrarMenu();
            opcao = lerInteiro(scanner, "Selecione a opção desejada: ");

            switch (opcao) {
                case 1:
                    cadastrarLivro(scanner, biblioteca);
                    break;
                case 2:
                    cadastrarUsuario(scanner, biblioteca);
                    break;
                case 3:
                    emprestarLivro(scanner, biblioteca);
                    break;
                case 4:
                    devolverLivro(scanner, biblioteca);
                    break;
                case 5:
                    listarLivrosDisponiveis(biblioteca);
                    break;
                case 6:
                    listarEmprestimos(biblioteca);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
            System.out.println();
        } while (opcao != 0);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("=== Biblioteca ===");
        System.out.println("1 - Cadastrar livro");
        System.out.println("2 - Cadastrar usuário");
        System.out.println("3 - Emprestar livro");
        System.out.println("4 - Devolver livro");
        System.out.println("5 - Listar livros disponíveis");
        System.out.println("6 - Listar empréstimos");
        System.out.println("0 - Sair");
    }

    private static void cadastrarLivro(Scanner scanner, Biblioteca biblioteca) {
        System.out.println("Cadastro de livro");
        System.out.print("Digite o Título: ");
        String titulo = scanner.nextLine().trim();
        System.out.print("Digite o nome do autor: ");
        String autor = scanner.nextLine().trim();
        System.out.print("Digite o ISBN do livro: ");
        String isbn = scanner.nextLine().trim();

        if (biblioteca.buscarLivroPorIsbn(isbn) != null) {
            System.out.println("Este livro já está cadastrado na biblioteca");
            return;
        }

        biblioteca.cadastrarLivro(new Livro(titulo, autor, isbn));
        System.out.println("Livro cadastrado com êxito.");
    }

    private static void cadastrarUsuario(Scanner scanner, Biblioteca biblioteca) {
        System.out.println("Cadastro de usuário");
        System.out.println("Digite 1 se você for aluno ou 2 se for professor");
        int tipo;
        do {
            System.out.println("1 - Aluno");
            System.out.println("2 - Professor");
            tipo = lerInteiro(scanner, "Tipo de usuário: ");
        } while (tipo != 1 && tipo != 2);

        String cpf = lerCpfValido(scanner, "Digite o seu CPF (11 dígitos): ");
        if (biblioteca.buscarUsuarioPorId(cpf) != null) {
            System.out.println("Já existe um usuário cadastrado com esse CPF.");
            return;
        }

        System.out.print("Digite o nome do usuário: ");
        String nome = scanner.nextLine().trim();

        Usuario usuario;
        if (tipo == 1) {
            usuario = new Aluno(cpf, nome);
        } else {
            usuario = new Professor(cpf, nome);
        }

        biblioteca.cadastrarUsuario(usuario);
        System.out.println("Usuário cadastrado com sucesso.");
    }

    private static void emprestarLivro(Scanner scanner, Biblioteca biblioteca) {
        System.out.println("Empréstimo de livro");
        System.out.print("Digite o ISBN do livro: ");
        String isbn = scanner.nextLine().trim();
        String usuarioCpf = lerCpfValido(scanner, "Digite o CPF de usuário: ");

        if (biblioteca.emprestarLivro(isbn, usuarioCpf)) {
            System.out.println("Empréstimo realizado com sucesso.");
        } else {
            System.out.println("Não foi possível realizar o empréstimo. Verifique livro, usuário ou disponibilidade.");
        }
    }

    private static void devolverLivro(Scanner scanner, Biblioteca biblioteca) {
        System.out.println("Devolução de livro");
        System.out.print("Digite o ISBN do livro: ");
        String isbn = scanner.nextLine().trim();
        String usuarioCpf = lerCpfValido(scanner, "Digite o CPF do usuário: ");

        if (biblioteca.devolverLivro(isbn, usuarioCpf)) {
            System.out.println("Livro devolvido com sucesso.");
        } else {
            System.out.println("Não foi possível devolver o livro. Verifique se os dados digitados estão corretos e se o livro está emprestado para esse usuário.");
        }
    }

    private static void listarLivrosDisponiveis(Biblioteca biblioteca) {
        System.out.println("Livros disponíveis:");
        List<Livro> disponiveis = biblioteca.listarLivrosDisponiveis();
        if (disponiveis.isEmpty()) {
            System.out.println("Nenhum livro disponível no momento.");
            return;
        }
        for (Livro livro : disponiveis) {
            System.out.println(livro);
        }
    }

    private static void listarEmprestimos(Biblioteca biblioteca) {
        System.out.println("Empréstimos ativos:");
        List<Emprestimo> emprestimos = biblioteca.listarEmprestimos();
        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo ativo no momento.");
            return;
        }
        for (Emprestimo emprestimo : emprestimos) {
            System.out.println(emprestimo);
        }
    }

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
            if (cpf.matches("\\d{11}")) {
                return cpf;
            }
            System.out.println("CPF inválido. Digite exatamente 11 dígitos numéricos.");
        }
    }
}

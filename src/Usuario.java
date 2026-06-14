public abstract class Usuario {
    private String id;
    private String nome;
    private int emprestimosAtivos;

    public Usuario(String id, String nome) {
        this.id = id;
        this.nome = nome;
        this.emprestimosAtivos = 0;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getEmprestimosAtivos() {
        return emprestimosAtivos;
    }

    public void setEmprestimosAtivos(int emprestimosAtivos) {
        this.emprestimosAtivos = emprestimosAtivos;
    }

    public boolean Usuariodisponivel() {
        return emprestimosAtivos < getLimiteEmprestimos();
    }

    public void AumentarEmprestimo() {
        emprestimosAtivos++;
    }

    public void SubtrairEmprestimo() {
        if (emprestimosAtivos > 0) emprestimosAtivos--;
    }

    public abstract int getLimiteEmprestimos();

    public abstract String getTipo();

    @Override
    public String toString() {
        return String.format("|| %s: %s || ID: %s || Empréstimos: %d/%d ||",
                getTipo(), nome, id, emprestimosAtivos, getLimiteEmprestimos());
    }
}

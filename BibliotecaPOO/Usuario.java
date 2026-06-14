public abstract class Usuario {
    private String id;
    private String nome;
    private int emprestimosRecentes;

    public Usuario(String id, String nome) {
        this.id = id;
        this.nome = nome;
        this.emprestimosRecentes = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getEmprestimosRecentes() {
        return emprestimosRecentes;
    }

    public void setEmprestimosRecentes(int emprestimosRecentes) {
        this.emprestimosRecentes = emprestimosRecentes;
    }

    public void AumentarEmprestimo() {
        emprestimosRecentes++;
    }

    public void SubtrairEmprestimo() {
        if (emprestimosRecentes > 0) {
            emprestimosRecentes--;
        }
    }

    public boolean Usuariodisponivel() {
        return emprestimosRecentes < getLimiteEmprestimos();
    }

    public abstract int getLimiteEmprestimos();

    public abstract String getTipo();

    @Override
    public String toString() {
        return String.format("|| %s - %s || \n || %s -> %d de %d empréstimos ||", getTipo(), id, nome, emprestimosRecentes, getLimiteEmprestimos());
    }
}

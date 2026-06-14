public class Aluno extends Usuario {
    private static final int LIMITE = 3;

    public Aluno(String id, String nome) {
        super(id, nome);
    }

    @Override
    public int getLimiteEmprestimos() {
        return LIMITE;
    }

    @Override
    public String getTipo() {
        return "Aluno";
    }
}

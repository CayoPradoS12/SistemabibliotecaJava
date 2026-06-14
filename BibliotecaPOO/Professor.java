public class Professor extends Usuario {
    private static final int LIMITE = 5;

    public Professor(String id, String nome) {
        super(id, nome);
    }

    @Override
    public int getLimiteEmprestimos() {
        return LIMITE;
    }

    @Override
    public String getTipo() {
        return "Professor";
    }
}

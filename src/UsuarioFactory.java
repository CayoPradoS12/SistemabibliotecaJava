/**
 * Factory para criação de objetos Usuario (Aluno ou Professor).
 *
 * Problema que o Factory resolve:
 * - Sem o Factory, toda vez que precisamos criar ou reconstruir um usuário a partir
 *   de uma string de tipo (ex: "Aluno", "Professor" vindo do banco), teríamos que
 *   espalhar blocos if/else ou switch por múltiplas classes (Biblioteca, UsuarioRepositorySQLite, Main).
 *
 * Alternativa sem Factory:
 * - Duplicação de lógica de instanciação em cada ponto do código.
 * - Risco de inconsistência ao adicionar novos tipos de usuário.
 *
 * Vantagens do Factory aqui:
 * - Centraliza a lógica de criação em um único lugar.
 * - Facilita a adição de novos tipos (ex: Funcionario) sem alterar outras classes.
 * - O repositório pode reconstruir objetos do banco sem conhecer os subtipos diretamente.
 */
public class UsuarioFactory {

    /**
     * Cria um usuário do tipo correto com base na string de tipo.
     *
     * @param tipo     "Aluno" ou "Professor"
     * @param id       CPF do usuário
     * @param nome     Nome do usuário
     * @return         Instância de Aluno ou Professor
     */
    public static Usuario criar(String tipo, String id, String nome) {
        return switch (tipo.toLowerCase()) {
            case "aluno"     -> new Aluno(id, nome);
            case "professor" -> new Professor(id, nome);
            default          -> throw new IllegalArgumentException("Tipo de usuário desconhecido: " + tipo);
        };
    }
}

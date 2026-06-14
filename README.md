# Sistema de Biblioteca

## Como executar

### Opção 1 — JAR executável (recomendado)
```bash
java -jar Biblioteca.jar
```

### Opção 2 — Compilar e rodar manualmente
```bash
javac -cp sqlite-jdbc.jar:slf4j-api.jar:slf4j-nop.jar src/*.java -d out/
java -cp "out:sqlite-jdbc.jar:slf4j-api.jar:slf4j-nop.jar" Main
```
> No Windows, troque `:` por `;` no classpath.

## Padrões implementados

| Padrão | Classe | Justificativa |
|--------|--------|---------------|
| **Singleton** | `DatabaseConnection` | Garante uma única conexão ao banco durante toda a execução |
| **Factory** | `UsuarioFactory` | Centraliza a criação de Aluno/Professor, evitando if/else duplicados |
| **Repository (Interface)** | `LivroRepository`, `UsuarioRepository`, `EmprestimoRepository` | Desacopla regras de negócio da implementação SQLite |

## Estrutura do projeto

```
src/
├── Main.java                      # Interface via terminal (Scanner)
├── Biblioteca.java                # Regras de negócio
├── Livro.java                     # Entidade Livro
├── Emprestimo.java                # Entidade Empréstimo
├── Usuario.java                   # Classe abstrata base
├── Aluno.java                     # Subclasse (limite: 3 empréstimos)
├── Professor.java                 # Subclasse (limite: 5 empréstimos)
├── DatabaseConnection.java        # Singleton de conexão SQLite
├── UsuarioFactory.java            # Factory para Aluno/Professor
├── LivroRepository.java           # Interface de persistência
├── LivroRepositorySQLite.java     # Implementação SQLite
├── UsuarioRepository.java         # Interface de persistência
├── UsuarioRepositorySQLite.java   # Implementação SQLite
├── EmprestimoRepository.java      # Interface de persistência
└── EmprestimoRepositorySQLite.java# Implementação SQLite
```

## Banco de dados

O arquivo `biblioteca.db` (SQLite) é criado automaticamente na pasta onde o JAR for executado.

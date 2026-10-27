# Modelagem do Gerenciador de Tarefas Web

## Tema e suposições

Sistema web para criar, atribuir e acompanhar tarefas. Reutiliza as quatro tabelas do banco SQL Server da aplicação desktop. Uma tarefa possui exatamente um criador, um responsável e uma categoria. Comentários pertencem a uma tarefa e a um autor. Status e prioridade são enumerações armazenadas na tabela `tarefas`.

Para o CRUD web, usuários podem editar ou excluir **a própria conta**; uma conta vinculada a tarefas ou comentários não pode ser excluída. O criador edita/exclui sua tarefa, o responsável altera o status, e somente o autor edita/exclui seu comentário. Usuários autenticados gerenciam categorias; `Geral` e categorias em uso não podem ser excluídas.

## Protótipos

Veja a [galeria de protótipos web](prototipos/index.html): login, listagem, formulário de tarefa, detalhes/comentários, categorias e usuários. As páginas Thymeleaf implementam as mesmas estruturas em `src/main/resources/templates`.

## Diagrama de classes

```mermaid
classDiagram
  class Usuario {
    +Long id
    +String nome
    +String nomeUsuario
    +String email
    -String senhaHash
  }
  class Categoria {
    +Long id
    +String nome
    +String descricao
  }
  class Tarefa {
    +Long id
    +String titulo
    +String descricao
    +LocalDate prazo
    +PrioridadeTarefa prioridade
    +StatusTarefa status
    +LocalDateTime criadaEm
  }
  class ComentarioTarefa {
    +Long id
    +String conteudo
    +LocalDateTime criadoEm
  }
  class PrioridadeTarefa {
    <<enumeration>>
    BAIXA
    MEDIA
    ALTA
  }
  class StatusTarefa {
    <<enumeration>>
    PENDENTE
    EM_ANDAMENTO
    CONCLUIDA
  }
  Usuario "1" <-- "0..*" Tarefa : cria
  Usuario "1" <-- "0..*" Tarefa : recebe
  Categoria "1" <-- "0..*" Tarefa : classifica
  Tarefa "1" <-- "0..*" ComentarioTarefa : possui
  Usuario "1" <-- "0..*" ComentarioTarefa : escreve
  Tarefa --> PrioridadeTarefa
  Tarefa --> StatusTarefa
```

As quatro classes persistentes são entidades JPA. `UsuarioRepository`, `TarefaRepository`, `CategoriaRepository` e `ComentarioRepository` implementam persistência via Spring Data; serviços concentram validação e permissões; controllers HTTP chamam somente serviços.

## Diagrama MER

```mermaid
erDiagram
  USUARIOS ||--o{ TAREFAS : cria
  USUARIOS ||--o{ TAREFAS : recebe
  CATEGORIAS ||--o{ TAREFAS : classifica
  TAREFAS ||--o{ COMENTARIOS_TAREFA : possui
  USUARIOS ||--o{ COMENTARIOS_TAREFA : escreve
  USUARIOS {
    BIGINT id PK
    NVARCHAR nome
    NVARCHAR nome_usuario UK
    NVARCHAR email UK
    NVARCHAR senha_hash
  }
  CATEGORIAS {
    BIGINT id PK
    NVARCHAR nome UK
    NVARCHAR descricao
  }
  TAREFAS {
    BIGINT id PK
    NVARCHAR titulo
    NVARCHAR descricao
    BIGINT criador_id FK
    BIGINT responsavel_id FK
    BIGINT categoria_id FK
    DATE prazo
    NVARCHAR prioridade
    NVARCHAR status
    DATETIME2 criada_em
  }
  COMENTARIOS_TAREFA {
    BIGINT id PK
    BIGINT tarefa_id FK
    BIGINT autor_id FK
    NVARCHAR conteudo
    DATETIME2 criado_em
  }
```

O banco conserva as restrições `NOT NULL`, `UNIQUE`, `CHECK` de status/prioridade e as chaves estrangeiras da versão desktop. A exclusão de tarefa remove seus comentários por cascata no banco existente.

## Coerência entre telas, classes e tabelas

| Tela | Operação | Entidade/Tabela |
|---|---|---|
| Login/cadastro/usuários | autenticar, criar, listar, editar e excluir a própria conta | `Usuario` / `usuarios` |
| Tarefas | criar, listar recebidas/criadas, editar e excluir | `Tarefa` / `tarefas` |
| Categorias | criar, listar, editar e excluir | `Categoria` / `categorias` |
| Detalhes/comentários | criar, listar, editar e excluir comentários | `ComentarioTarefa` / `comentarios_tarefa` |

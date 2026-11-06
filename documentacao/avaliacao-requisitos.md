# Avaliação dos requisitos — Sistema WEB

Avaliação realizada em 06/11/2026 após a reorganização das pastas.

| Exigência | Evidência no projeto | Resultado |
|---|---|---|
| Tema definido | Gerenciamento de tarefas, atribuição e acompanhamento | Atende |
| Protótipos | `Mockups/index.html` e `Mockups/visao-geral.png` | Atende |
| Diagrama de classes | `diagrama-classes-uml.mmd` e `documentacao/modelagem.md` | Atende |
| MER | `diagrama-mer.mmd` e `documentacao/modelagem.md` | Atende |
| Banco relacional | SQL Server, JDBC, JPA e `ConnectionFactory`; esquema existente validado por Hibernate | Atende |
| Quatro ou mais tabelas | `usuarios`, `tarefas`, `categorias`, `comentarios_tarefa` | Atende |
| CRUD com telas | Usuários (cadastro, lista, edição/exclusão da própria conta), tarefas, categorias e comentários | Atende com regras de permissão |
| Spring MVC, Thymeleaf, Spring Data | Dependências no `Projeto/pom.xml`, controllers, templates e quatro `JpaRepository` | Atende |
| Arquitetura MVC | Pacotes `model`, `repository`, `service`, `controller`; `templates` e `static` | Atende |
| Controller sem acesso direto a Repository | Imports e construtores dos controllers usam serviços, sem referência a repositórios | Atende |
| README e links da entrega | `README.md` liga aos mockups e diagramas | Atende localmente |
| URL do GitHub | Repositório ainda não publicado pelo proprietário | Pendente para a entrega |

## Limites da verificação

`mvnw.cmd clean test` passou com 1 teste, voltado à compatibilidade de senha. A suíte automatizada não cobre os fluxos de CRUD e permissões. O teste anterior de ponta a ponta contra o SQL Server está registrado em `ProjetoJavaSwingWeb.md`; esta reorganização foi verificada por compilação e teste unitário. Para a entrega, convém abrir o sistema pelo `iniciar-web.cmd` e percorrer as quatro áreas com o banco local disponível.

## Organização

A raiz mantém `README.md`, iniciador, arquivos Mermaid e o repositório Git. `documentacao` contém a explicação e esta avaliação; `Mockups` contém os protótipos; `Projeto` contém o Maven, código e recursos. A credencial local permanece ignorada pelo Git.
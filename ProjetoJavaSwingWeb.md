# Continuidade do projeto — Gerenciador de Tarefas Web

> Atualizado em 03/11/2026. Este arquivo é um resumo para retomar o trabalho em outra IA.

## 1. Objetivo e localização

O trabalho acadêmico é um **sistema web de gerenciamento de tarefas** feito com Spring MVC, Thymeleaf, Spring Data JPA e arquitetura MVC. Ele deve usar banco relacional, manipular pelo menos quatro tabelas e oferecer telas de cadastro, edição, exclusão e listagem.

- **Projeto web principal:** `C:\MEUS PROJETOS\ProjetoGerenciamentoTarefasWeb`
- **Projeto desktop anterior (Java Swing), separado:** `C:\MEUS PROJETOS\ProjetoGerenciamentoTarefas\projeto`
- O projeto web possui **repositório Git próprio**. A pasta antiga `C:\MEUS PROJETOS\GerenciadorTarefasWeb` foi renomeada/movida para o caminho principal acima.
- **GitHub:** o usuário decidiu publicar por conta própria. Ainda não há remote configurado nem URL pública no README.

## 2. Estado atual verificado

- Commit de implementação: `40ee444 feat: criar gerenciador de tarefas web com Spring MVC`. Este handoff foi acrescentado em um commit de documentação posterior.
- Uma atualização posterior adicionou `config/ConnectionFactory.java`, `iniciar-web.cmd` e `iniciar-web.ps1` para simplificar a conexão local.
- O pacote executável `target\gerenciamento-tarefas-web-1.0.0.jar` foi gerado com sucesso em 27/10/2026. A pasta `target/` é ignorada pelo Git.
- Em 03/11/2026, o novo iniciador conectou ao SQL Server, o Spring iniciou na porta 8080 e a tela de login respondeu HTTP 200. O servidor de teste foi encerrado depois da validação.
- Este handoff deve acompanhar o projeto no Git para estar disponível após a publicação.

## 3. Tecnologias e estrutura

- Java 17 como alvo de compilação; Spring Boot **3.5.16**; Maven Wrapper **3.9.11**.
- Spring MVC + Thymeleaf; Spring Data JPA/Hibernate; Spring Security; SQL Server com driver JDBC da Microsoft.
- `src/main/java/br/edu/tarefas/web/model`: entidades `Usuario`, `Tarefa`, `Categoria`, `ComentarioTarefa` e enumerações.
- `repository`: quatro interfaces `JpaRepository`, uma por tabela.
- `service`: regras, validações, operações e permissões.
- `controller`: rotas HTTP; **controllers chamam serviços, não repositories**.
- `security`: login, logout, proteção de rotas, CSRF e `SenhaLegadaEncoder`.
- `config/ConnectionFactory.java`: cria o `HikariDataSource` usado pelo Spring Data JPA. URL, usuário e senha continuam fora do código Java.
- `src/main/resources/templates`: páginas Thymeleaf; `static/css/app.css` e `static/js/app.js`: apresentação.
- `pom.xml`, `mvnw.cmd`, `.mvn/wrapper`: build. `.gitignore` exclui `target/`, credenciais e arquivos locais.

## 4. Banco de dados reutilizado

Banco SQL Server: `GerenciamentoTarefas`; esquema `dbo`.

| Tabela | Papel | Vínculos principais |
|---|---|---|
| `usuarios` | Conta, nome, usuário, e-mail, hash da senha | Cria/recebe tarefas e escreve comentários |
| `categorias` | Classificação | Uma categoria para várias tarefas |
| `tarefas` | Título, descrição, prazo, prioridade, status, data de criação | `criador_id`, `responsavel_id`, `categoria_id` |
| `comentarios_tarefa` | Conversa vinculada à tarefa | `tarefa_id`, `autor_id` |

O banco foi criado durante o trabalho desktop. O projeto web usa `spring.jpa.hibernate.ddl-auto=validate`: **valida o esquema existente e não cria/altera tabelas**. Não há scripts SQL neste novo repositório. Para executar em outra máquina, é necessário provisionar o mesmo esquema; o MER e os campos estão documentados em `docs/modelagem.md`.

O hash PBKDF2 das senhas existentes no Swing é aceito pelo `SenhaLegadaEncoder`; novas contas web também usam esse formato. O arquivo local `credenciais-sql.dat` é protegido pela conta Windows e ignorado pelo Git. **Não colocar usuário/senha SQL nem arquivo de credenciais no Git.**

## 5. Funcionalidades e permissões

- `/login`, `/cadastro`: autenticação por nome de usuário ou e-mail e criação de conta.
- `/tarefas`: listas de recebidas e criadas; formulário de criação/edição; detalhes; alteração de status; exclusão.
- `/categorias`: listagem, criação, edição e exclusão.
- `/usuarios`: listagem; cada usuário pode editar e excluir a própria conta. Conta com tarefas/comentários vinculados não pode ser excluída.
- `/tarefas/{id}/comentarios`: criação, listagem, edição e exclusão de comentários.
- Criador edita/exclui sua tarefa; responsável muda o status. Criador e responsável acessam comentários; só o autor edita/exclui o próprio comentário. Categoria `Geral` e categorias com tarefas não podem ser excluídas.
- Rotas privadas exigem login; formulários POST usam token CSRF do Spring Security.

## 6. Protótipos e diagramas

- `docs/prototipos/index.html`: galeria HTML com seis mockups web.
- `docs/prototipos/visao-geral.png`: prévia incluída no README.
- `docs/modelagem.md`: diagrama de classes `classDiagram`, MER `erDiagram`, suposições e matriz tela/entidade/tabela.
- `README.md`: resumo para entrega, links para protótipos/diagramas e instruções de execução. A seção GitHub aguarda a URL após a publicação pelo usuário.

## 7. Compilar e executar no Windows

Pré-requisitos: Java 17+, SQL Server em `127.0.0.1:1433`, banco `GerenciamentoTarefas` com as quatro tabelas, autenticação SQL habilitada. O Maven Wrapper baixa Maven na primeira execução.

Nesta máquina, **duplo clique em `iniciar-web.cmd`**. Ele chama `iniciar-web.ps1`, lê a credencial local protegida, define a conexão e executa `mvnw.cmd spring-boot:run`. Mantenha a janela aberta durante o uso.

Em outra máquina, no PowerShell dentro de `C:\MEUS PROJETOS\ProjetoGerenciamentoTarefasWeb`:

```powershell
$env:TAREFAS_DB_URL = 'jdbc:sqlserver://127.0.0.1:1433;databaseName=GerenciamentoTarefas;encrypt=true;trustServerCertificate=true'
$env:TAREFAS_DB_USER = 'SEU_USUARIO_SQL'
$env:TAREFAS_DB_PASSWORD = 'SUA_SENHA_SQL'
.\mvnw.cmd clean package
java -jar .\target\gerenciamento-tarefas-web-1.0.0.jar
```

Depois, abrir `http://localhost:8080/login`. É possível usar uma conta já cadastrada no aplicativo Swing ou criar uma conta web. Para desenvolvimento, também funciona `.\mvnw.cmd spring-boot:run` após definir as variáveis. A senha SQL deve ser fornecida localmente; o projeto não armazena a senha no código.

## 8. Validação já realizada

- `.\mvnw.cmd clean package` em 27/10/2026: **BUILD SUCCESS**; teste unitário `SenhaLegadaEncoderTest` passou.
- Inicialização contra o SQL Server existente: Hibernate validou as quatro entidades e o servidor iniciou na porta 8080.
- Verificação HTTP: login e cadastro carregaram; rota `/tarefas` redirecionou visitante não autenticado para login.
- Fluxo ponta a ponta com dados temporários: cadastro, login, criação/listagem/edição/exclusão de categoria, tarefa e comentário; alteração de status; edição de perfil. Fluxos passaram. Registros temporários foram removidos do banco.
- O protótipo HTML foi renderizado e revisado visualmente. A última compilação da versão movida para o nome atual também passou.
- Em 03/11/2026, `.\mvnw.cmd test` passou após adicionar a ConnectionFactory; o novo script iniciou o Spring e a tela de login respondeu HTTP 200.

## 9. Próximas ações possíveis

1. Se o usuário quiser visualizar novamente nesta máquina, dar duplo clique em `iniciar-web.cmd` e abrir `http://localhost:8080/login`.
2. Revisar manualmente as telas no navegador com a conta do usuário e corrigir qualquer detalhe visual/funcional encontrado.
3. Publicar **este** repositório no GitHub quando o usuário desejar e acrescentar a URL ao `README.md`.
4. Manter este arquivo atualizado no Git. Não versionar `target/` ou credenciais.

Não assumir que a URL local `localhost:8080` está ativa em outra sessão: verificar e iniciar o processo quando necessário.

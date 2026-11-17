# Atividade GenAI — Gerenciador de Tarefas

## Requisitos e implementação

| Requisito | Implementação |
|---|---|
| Banco de dados | SQL Server existente; quatro entidades JPA e respectivos repositórios |
| Interface web | Spring MVC, Thymeleaf, HTML/CSS/JS e login |
| Integração GenAI | Spring AI 1.1.8 com modelo de chat OpenAI via `ChatClient` |
| Funcionalidades com IA | Resumo da tarefa e sugestão de plano de execução na tela de detalhes |
| Proteção da chave | `OPENAI_API_KEY` fornecida pelo ambiente; nenhuma chave no código ou no Git |

## Fluxo

O usuário autenticado abre uma tarefa da qual é criador ou responsável. Ao clicar em uma ação, o controller obtém o usuário pelo serviço e chama `AssistenteIaService`. O serviço confirma o acesso por `TarefaService.buscar`, envia à IA apenas os campos necessários e mostra a resposta como texto simples na mesma tela. Nenhum resultado é persistido.

Sem `OPENAI_API_KEY` e `TAREFAS_AI_MODEL=openai`, a integração permanece desativada e o CRUD original continua disponível. A chamada externa exige chave válida, conexão com o provedor e créditos de API.

## Verificação

Execute `Projeto\mvnw.cmd clean verify`. Para testar a geração real, forneça uma chave de API própria somente no ambiente local e use os botões em uma tarefa acessível. O teste automatizado cobre o comportamento sem chave e a rejeição de ações inválidas; não realiza chamadas pagas à API.

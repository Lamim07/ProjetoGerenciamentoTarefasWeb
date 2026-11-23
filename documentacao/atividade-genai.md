# Atividade GenAI — Gerenciador de Tarefas

Este é um trabalho acadêmico separado da atividade Spring WEB original. A entrega GenAI está isolada na branch `genai`; a branch `main` mantém a versão sem IA.

## Requisitos e implementação

| Requisito | Implementação |
|---|---|
| Banco de dados | SQL Server existente; quatro entidades JPA e respectivos repositórios |
| Interface web | Spring MVC, Thymeleaf, HTML/CSS/JS e login |
| Integração GenAI | Spring AI 1.1.8 com modelo de chat Google Gemini via `ChatClient` |
| Funcionalidades com IA | Resumo da tarefa e sugestão de plano de execução na tela de detalhes |
| Proteção da chave | `GEMINI_API_KEY` fornecida pelo ambiente ou arquivo local protegido `credenciais-gemini.dat`; nenhuma chave no código ou no Git |

## Configuração rápida

Dê duplo clique em `configurar-gemini.cmd`, cole sua chave existente e depois inicie com `iniciar-web.cmd`. O configurador usa a proteção de dados do Windows para a conta atual. A chave não acompanha o repositório; em outro computador, repita a configuração.

## Fluxo

O usuário autenticado abre uma tarefa da qual é criador ou responsável. Ao clicar em uma ação, o controller obtém o usuário pelo serviço e chama `AssistenteIaService`. O serviço confirma o acesso por `TarefaService.buscar`, envia à IA apenas os campos necessários e mostra a resposta como texto simples na mesma tela. Nenhum resultado é persistido.

Sem `GEMINI_API_KEY` e `TAREFAS_AI_MODEL=google-genai`, a integração permanece desativada e o CRUD original continua disponível. A chamada externa exige chave válida, conexão com o provedor e créditos de API.

## Critérios para apresentação

Demonstre o login, a consulta de uma tarefa persistida no SQL Server, os dois resultados diferentes do Gemini e a arquitetura controller → service → Spring AI. Explique que a resposta não altera dados automaticamente e que a chave é lida de variável de ambiente. As duas funções cumprem literalmente o requisito de funcionalidades com GenAI, mas uma avaliação de nota depende dos critérios do professor e da demonstração prática.

## Verificação

Execute `Projeto\mvnw.cmd clean verify`. Para testar a geração real, forneça uma chave de API própria somente no ambiente local e use os botões em uma tarefa acessível. O teste automatizado cobre o comportamento sem chave e a rejeição de ações inválidas; não realiza chamadas pagas à API.

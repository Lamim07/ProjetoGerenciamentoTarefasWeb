package br.edu.tarefas.web.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import br.edu.tarefas.web.model.PrioridadeTarefa;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.ai.chat.client.ChatClient;

class AssistenteIaServiceTest {
    @Test
    void semChaveNaoChamaModelo() {
        TarefaService tarefas = mock(TarefaService.class);
        @SuppressWarnings("unchecked") ObjectProvider<ChatClient.Builder> builders = mock(ObjectProvider.class);
        AssistenteIaService service = new AssistenteIaService(tarefas, builders, "none", "");
        assertFalse(service.disponivel());
        assertThrows(RegraException.class, () -> service.gerar(7L, 2L, "resumo"));
        verifyNoInteractions(tarefas, builders);
    }

    @Test
    void geminiComChaveFicaDisponivel() {
        TarefaService tarefas = mock(TarefaService.class);
        @SuppressWarnings("unchecked") ObjectProvider<ChatClient.Builder> builders = mock(ObjectProvider.class);
        when(builders.getIfAvailable()).thenReturn(mock(ChatClient.Builder.class));
        AssistenteIaService service = new AssistenteIaService(tarefas, builders, "google-genai", "chave-teste");
        assertTrue(service.disponivel());
    }

    @Test
    void acaoDesconhecidaEhRejeitada() {
        TarefaService tarefas = mock(TarefaService.class);
        @SuppressWarnings("unchecked") ObjectProvider<ChatClient.Builder> builders = mock(ObjectProvider.class);
        AssistenteIaService service = new AssistenteIaService(tarefas, builders, "google-genai", "chave-teste");
        assertThrows(RegraException.class, () -> service.gerar(7L, 2L, "outra"));
        verifyNoInteractions(tarefas, builders);
    }
    @Test
    void tituloVazioNaoConsultaIaParaDescricao() {
        TarefaService tarefas = mock(TarefaService.class);
        @SuppressWarnings("unchecked") ObjectProvider<ChatClient.Builder> builders = mock(ObjectProvider.class);
        AssistenteIaService service = new AssistenteIaService(tarefas, builders, "google-genai", "chave-teste");
        assertThrows(RegraException.class, () -> service.sugerirDescricao("  ", ""));
        verifyNoInteractions(tarefas, builders);
    }

    @Test
    void prioridadeInvalidaNaoConsultaIa() {
        TarefaService tarefas = mock(TarefaService.class);
        @SuppressWarnings("unchecked") ObjectProvider<ChatClient.Builder> builders = mock(ObjectProvider.class);
        AssistenteIaService service = new AssistenteIaService(tarefas, builders, "google-genai", "chave-teste");
        assertThrows(RegraException.class, () -> service.sugerirPrioridade("Estudar", "Ler o capítulo", null));
        verifyNoInteractions(tarefas, builders);
    }
    @Test
    void sugestaoPreencheDescricaoSemSalvarTarefa() {
        TarefaService tarefas = mock(TarefaService.class);
        @SuppressWarnings("unchecked") ObjectProvider<ChatClient.Builder> builders = mock(ObjectProvider.class);
        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        ChatClient client = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        when(builders.getIfAvailable()).thenReturn(builder);
        when(builder.build()).thenReturn(client);
        when(client.prompt().system(anyString()).user(anyString()).call().content())
            .thenReturn("Preparar uma apresentação clara para a turma.");
        AssistenteIaService service = new AssistenteIaService(tarefas, builders, "google-genai", "chave-teste");
        assertEquals("Preparar uma apresentação clara para a turma.",
            service.sugerirDescricao("Preparar apresentação", ""));
        verifyNoInteractions(tarefas);
    }

    @Test
    void sugestaoDePrioridadeEhConvertidaParaEnum() {
        TarefaService tarefas = mock(TarefaService.class);
        @SuppressWarnings("unchecked") ObjectProvider<ChatClient.Builder> builders = mock(ObjectProvider.class);
        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        ChatClient client = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        when(builders.getIfAvailable()).thenReturn(builder);
        when(builder.build()).thenReturn(client);
        when(client.prompt().system(anyString()).user(anyString()).call().content()).thenReturn("ALTA");
        AssistenteIaService service = new AssistenteIaService(tarefas, builders, "google-genai", "chave-teste");
        assertEquals(PrioridadeTarefa.ALTA,
            service.sugerirPrioridade("Entregar trabalho", "Até amanhã", LocalDate.now().plusDays(1)));
        verifyNoInteractions(tarefas);
    }
}

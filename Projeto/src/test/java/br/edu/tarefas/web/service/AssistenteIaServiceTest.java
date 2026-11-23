package br.edu.tarefas.web.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
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
}

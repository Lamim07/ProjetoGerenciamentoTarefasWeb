package br.edu.tarefas.web.security;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SenhaLegadaEncoderTest {
    private final SenhaLegadaEncoder encoder = new SenhaLegadaEncoder();
    @Test void preservaFormatoDoAplicativoDesktop() {
        String hash = encoder.encode("senhaDeTeste123");
        assertTrue(hash.startsWith("pbkdf2$210000$"));
        assertTrue(encoder.matches("senhaDeTeste123", hash));
        assertFalse(encoder.matches("outraSenha", hash));
    }
}

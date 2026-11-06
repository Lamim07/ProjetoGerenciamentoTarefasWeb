package br.edu.tarefas.web.security;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SenhaLegadaEncoder implements PasswordEncoder {
    private static final SecureRandom RANDOM = new SecureRandom();
    @Override public String encode(CharSequence senha) {
        byte[] salt = new byte[16]; RANDOM.nextBytes(salt);
        return "pbkdf2$210000$" + Base64.getEncoder().encodeToString(salt) + "$" +
                Base64.getEncoder().encodeToString(derivar(senha, salt, 210000));
    }
    @Override public boolean matches(CharSequence senha, String hash) {
        try {
            String[] p = hash.split("\\$");
            if (p.length != 4 || !p[0].equals("pbkdf2")) return false;
            int n = Integer.parseInt(p[1]);
            if (n < 1 || n > 1_000_000) return false;
            return MessageDigest.isEqual(Base64.getDecoder().decode(p[3]),
                    derivar(senha, Base64.getDecoder().decode(p[2]), n));
        } catch (RuntimeException e) { return false; }
    }
    private byte[] derivar(CharSequence senha, byte[] salt, int n) {
        PBEKeySpec spec = new PBEKeySpec(senha.toString().toCharArray(), salt, n, 256);
        try { return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded(); }
        catch (Exception e) { throw new IllegalStateException("Falha ao proteger senha.", e); }
        finally { spec.clearPassword(); }
    }
}

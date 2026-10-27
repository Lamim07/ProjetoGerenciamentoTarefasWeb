package br.edu.tarefas.web.repository;

import br.edu.tarefas.web.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNomeUsuarioIgnoreCaseOrEmailIgnoreCase(String usuario, String email);
    Optional<Usuario> findByNomeUsuario(String usuario);
    boolean existsByNomeUsuarioIgnoreCase(String usuario);
    boolean existsByEmailIgnoreCase(String email);
}

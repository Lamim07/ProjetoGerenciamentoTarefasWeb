package br.edu.tarefas.web.repository;

import br.edu.tarefas.web.model.ComentarioTarefa;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioRepository extends JpaRepository<ComentarioTarefa, Long> {
    @EntityGraph(attributePaths = {"autor"})
    List<ComentarioTarefa> findByTarefaIdOrderByCriadoEmAscIdAsc(Long tarefaId);
    boolean existsByAutorId(Long autorId);
    long countByTarefaId(Long tarefaId);
}

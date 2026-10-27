package br.edu.tarefas.web.repository;

import br.edu.tarefas.web.model.Tarefa;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    @EntityGraph(attributePaths = {"criador", "responsavel", "categoria"})
    List<Tarefa> findByCriadorIdOrderByCriadaEmDesc(Long id);
    @EntityGraph(attributePaths = {"criador", "responsavel", "categoria"})
    List<Tarefa> findByResponsavelIdOrderByCriadaEmDesc(Long id);
    @Override @EntityGraph(attributePaths = {"criador", "responsavel", "categoria"})
    java.util.Optional<Tarefa> findById(Long id);
    boolean existsByCriadorIdOrResponsavelId(Long criador, Long responsavel);
    boolean existsByCategoriaId(Long id);
}

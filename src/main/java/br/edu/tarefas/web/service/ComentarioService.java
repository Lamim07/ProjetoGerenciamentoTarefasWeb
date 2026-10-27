package br.edu.tarefas.web.service;

import br.edu.tarefas.web.model.*;
import br.edu.tarefas.web.repository.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComentarioService {
    private final ComentarioRepository comentarios;
    private final TarefaService tarefas;
    private final UsuarioRepository usuarios;
    public ComentarioService(ComentarioRepository comentarios, TarefaService tarefas, UsuarioRepository usuarios) {
        this.comentarios = comentarios; this.tarefas = tarefas; this.usuarios = usuarios;
    }
    public List<ComentarioTarefa> listar(Long tarefaId, Long atorId) {
        tarefas.buscar(tarefaId, atorId);
        return comentarios.findByTarefaIdOrderByCriadoEmAscIdAsc(tarefaId);
    }
    public ComentarioTarefa buscar(Long id, Long atorId) {
        ComentarioTarefa c = comentarios.findById(id).orElseThrow(() -> new RegraException("Comentário não encontrado."));
        tarefas.buscar(c.getTarefa().getId(), atorId);
        return c;
    }
    @Transactional
    public void salvar(Long id, Long tarefaId, Long atorId, String conteudo) {
        conteudo = UsuarioService.obrigatorio(conteudo, 2000, "Comentário");
        Tarefa tarefa = tarefas.buscar(tarefaId, atorId);
        ComentarioTarefa c = id == null ? new ComentarioTarefa() : buscar(id, atorId);
        if (id != null && (!c.getAutor().getId().equals(atorId) || !c.getTarefa().getId().equals(tarefaId)))
            throw new RegraException("Somente o autor pode editar este comentário.");
        if (id == null) {
            c.setAutor(usuarios.findById(atorId).orElseThrow());
            c.setTarefa(tarefa);
        }
        c.setConteudo(conteudo); comentarios.save(c);
    }
    @Transactional
    public void excluir(Long id, Long atorId) {
        ComentarioTarefa c = buscar(id, atorId);
        if (!c.getAutor().getId().equals(atorId)) throw new RegraException("Somente o autor pode excluir este comentário.");
        comentarios.delete(c);
    }
}

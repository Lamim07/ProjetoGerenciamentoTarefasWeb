package br.edu.tarefas.web.service;

import br.edu.tarefas.web.model.*;
import br.edu.tarefas.web.repository.*;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TarefaService {
    private final TarefaRepository tarefas;
    private final UsuarioRepository usuarios;
    private final CategoriaRepository categorias;
    public TarefaService(TarefaRepository tarefas, UsuarioRepository usuarios, CategoriaRepository categorias) {
        this.tarefas = tarefas; this.usuarios = usuarios; this.categorias = categorias;
    }
    public List<Tarefa> criadas(Long usuarioId) { return tarefas.findByCriadorIdOrderByCriadaEmDesc(usuarioId); }
    public List<Tarefa> recebidas(Long usuarioId) { return tarefas.findByResponsavelIdOrderByCriadaEmDesc(usuarioId); }
    public Tarefa buscar(Long id, Long usuarioId) {
        Tarefa t = tarefas.findById(id).orElseThrow(() -> new RegraException("Tarefa não encontrada."));
        if (!t.getCriador().getId().equals(usuarioId) && !t.getResponsavel().getId().equals(usuarioId))
            throw new RegraException("Você não participa desta tarefa.");
        return t;
    }
    @Transactional
    public void salvar(Long id, Long atorId, String titulo, String descricao, Long responsavelId,
                       Long categoriaId, LocalDate prazo, PrioridadeTarefa prioridade) {
        titulo = UsuarioService.obrigatorio(titulo, 180, "Título");
        if (descricao == null || descricao.isBlank()) throw new RegraException("Informe a descrição.");
        if (prazo == null || prioridade == null) throw new RegraException("Informe prazo e prioridade.");
        Tarefa t = id == null ? new Tarefa() : buscar(id, atorId);
        if (id != null && !t.getCriador().getId().equals(atorId))
            throw new RegraException("Somente o criador pode editar a tarefa.");
        if (id == null) {
            t.setCriador(usuarios.findById(atorId).orElseThrow(() -> new RegraException("Usuário não encontrado.")));
            t.setStatus(StatusTarefa.PENDENTE);
        }
        t.setResponsavel(usuarios.findById(responsavelId).orElseThrow(() -> new RegraException("Responsável inválido.")));
        t.setCategoria(categorias.findById(categoriaId).orElseThrow(() -> new RegraException("Categoria inválida.")));
        t.setTitulo(titulo); t.setDescricao(descricao.trim()); t.setPrazo(prazo); t.setPrioridade(prioridade);
        tarefas.save(t);
    }
    @Transactional
    public void status(Long id, Long atorId, StatusTarefa status) {
        Tarefa t = buscar(id, atorId);
        if (!t.getResponsavel().getId().equals(atorId)) throw new RegraException("Somente o responsável pode alterar o status.");
        if (status == null) throw new RegraException("Selecione o status.");
        t.setStatus(status);
    }
    @Transactional
    public void excluir(Long id, Long atorId) {
        Tarefa t = buscar(id, atorId);
        if (!t.getCriador().getId().equals(atorId)) throw new RegraException("Somente o criador pode excluir a tarefa.");
        tarefas.delete(t);
    }
}

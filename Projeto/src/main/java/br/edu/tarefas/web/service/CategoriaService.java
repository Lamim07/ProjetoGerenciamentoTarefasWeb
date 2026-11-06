package br.edu.tarefas.web.service;

import br.edu.tarefas.web.model.Categoria;
import br.edu.tarefas.web.repository.CategoriaRepository;
import br.edu.tarefas.web.repository.TarefaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {
    private final CategoriaRepository categorias;
    private final TarefaRepository tarefas;
    public CategoriaService(CategoriaRepository categorias, TarefaRepository tarefas) {
        this.categorias = categorias; this.tarefas = tarefas;
    }
    public List<Categoria> listar() { return categorias.findAllByOrderByNomeAsc(); }
    public Categoria buscar(Long id) { return categorias.findById(id).orElseThrow(() -> new RegraException("Categoria não encontrada.")); }
    @Transactional
    public void salvar(Long id, String nome, String descricao) {
        nome = UsuarioService.obrigatorio(nome, 80, "Nome da categoria");
        descricao = descricao == null ? "" : descricao.trim();
        if (descricao.length() > 255) throw new RegraException("Descrição deve ter até 255 caracteres.");
        Categoria c = id == null ? new Categoria() : buscar(id);
        if (c.getId() != null && "Geral".equalsIgnoreCase(c.getNome()) && !"Geral".equalsIgnoreCase(nome))
            throw new RegraException("A categoria Geral não pode ser renomeada.");
        if ((c.getNome() == null || !c.getNome().equalsIgnoreCase(nome)) && categorias.existsByNomeIgnoreCase(nome))
            throw new RegraException("Já existe uma categoria com esse nome.");
        c.setNome(nome); c.setDescricao(descricao.isBlank() ? null : descricao); categorias.save(c);
    }
    @Transactional
    public void excluir(Long id) {
        Categoria c = buscar(id);
        if ("Geral".equalsIgnoreCase(c.getNome()) || tarefas.existsByCategoriaId(id))
            throw new RegraException("A categoria Geral ou uma categoria com tarefas não pode ser excluída.");
        categorias.delete(c);
    }
}

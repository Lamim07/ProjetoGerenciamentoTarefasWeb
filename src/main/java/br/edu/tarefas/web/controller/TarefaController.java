package br.edu.tarefas.web.controller;

import br.edu.tarefas.web.model.*;
import br.edu.tarefas.web.service.*;
import java.security.Principal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/tarefas")
public class TarefaController {
    private final TarefaService tarefas;
    private final UsuarioService usuarios;
    private final CategoriaService categorias;
    private final ComentarioService comentarios;
    public TarefaController(TarefaService tarefas, UsuarioService usuarios,
                            CategoriaService categorias, ComentarioService comentarios) {
        this.tarefas = tarefas; this.usuarios = usuarios; this.categorias = categorias; this.comentarios = comentarios;
    }
    private Long ator(Principal p) { return usuarios.atual(p.getName()).getId(); }
    @GetMapping
    String listar(@RequestParam(defaultValue = "recebidas") String filtro, Principal principal, Model model) {
        Long id = ator(principal);
        model.addAttribute("filtro", filtro);
        model.addAttribute("tarefas", "criadas".equals(filtro) ? tarefas.criadas(id) : tarefas.recebidas(id));
        model.addAttribute("atorId", id);
        return "tarefas/lista";
    }
    @GetMapping("/nova")
    String nova(Model model) { preparar(model, new Tarefa()); return "tarefas/formulario"; }
    @GetMapping("/{id}/editar")
    String editar(@PathVariable Long id, Principal principal, Model model) {
        Tarefa t = tarefas.buscar(id, ator(principal));
        if (!t.getCriador().getId().equals(ator(principal))) throw new RegraException("Somente o criador pode editar a tarefa.");
        preparar(model, t); return "tarefas/formulario";
    }
    private void preparar(Model model, Tarefa t) {
        model.addAttribute("tarefa", t);
        model.addAttribute("usuarios", usuarios.listar());
        model.addAttribute("categorias", categorias.listar());
        model.addAttribute("prioridades", PrioridadeTarefa.values());
    }
    @PostMapping("/salvar")
    String salvar(@RequestParam(required = false) Long id, @RequestParam String titulo, @RequestParam String descricao,
                  @RequestParam Long responsavelId, @RequestParam Long categoriaId,
                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate prazo,
                  @RequestParam PrioridadeTarefa prioridade, Principal principal, RedirectAttributes flash) {
        try {
            tarefas.salvar(id, ator(principal), titulo, descricao, responsavelId, categoriaId, prazo, prioridade);
            flash.addFlashAttribute("sucesso", "Tarefa salva.");
            return "redirect:/tarefas?filtro=criadas";
        } catch (RuntimeException e) {
            flash.addFlashAttribute("erro", e.getMessage());
            return id == null ? "redirect:/tarefas/nova" : "redirect:/tarefas/" + id + "/editar";
        }
    }
    @GetMapping("/{id}")
    String detalhes(@PathVariable Long id, Principal principal, Model model) {
        Long atorId = ator(principal);
        model.addAttribute("tarefa", tarefas.buscar(id, atorId));
        model.addAttribute("comentarios", comentarios.listar(id, atorId));
        model.addAttribute("atorId", atorId);
        model.addAttribute("statusPossiveis", StatusTarefa.values());
        return "tarefas/detalhes";
    }
    @PostMapping("/{id}/status")
    String status(@PathVariable Long id, @RequestParam StatusTarefa status, Principal principal, RedirectAttributes flash) {
        try { tarefas.status(id, ator(principal), status); flash.addFlashAttribute("sucesso", "Status atualizado."); }
        catch (RuntimeException e) { flash.addFlashAttribute("erro", e.getMessage()); }
        return "redirect:/tarefas/" + id;
    }
    @PostMapping("/{id}/excluir")
    String excluir(@PathVariable Long id, Principal principal, RedirectAttributes flash) {
        try { tarefas.excluir(id, ator(principal)); flash.addFlashAttribute("sucesso", "Tarefa excluída."); }
        catch (RuntimeException e) { flash.addFlashAttribute("erro", e.getMessage()); }
        return "redirect:/tarefas?filtro=criadas";
    }
}

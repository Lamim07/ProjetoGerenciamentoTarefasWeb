package br.edu.tarefas.web.controller;

import br.edu.tarefas.web.service.*;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ComentarioController {
    private final ComentarioService comentarios;
    private final UsuarioService usuarios;
    public ComentarioController(ComentarioService comentarios, UsuarioService usuarios) {
        this.comentarios = comentarios; this.usuarios = usuarios;
    }
    private Long ator(Principal p) { return usuarios.atual(p.getName()).getId(); }
    @GetMapping("/tarefas/{tarefaId}/comentarios/{id}/editar")
    String editar(@PathVariable Long tarefaId, @PathVariable Long id, Principal principal, Model model) {
        var c = comentarios.buscar(id, ator(principal));
        if (!c.getAutor().getId().equals(ator(principal)) || !c.getTarefa().getId().equals(tarefaId))
            throw new RegraException("Somente o autor pode editar o comentário.");
        model.addAttribute("comentario", c); model.addAttribute("tarefaId", tarefaId);
        return "comentarios/formulario";
    }
    @PostMapping("/tarefas/{tarefaId}/comentarios")
    String salvar(@PathVariable Long tarefaId, @RequestParam(required = false) Long id,
                  @RequestParam String conteudo, Principal principal, RedirectAttributes flash) {
        try { comentarios.salvar(id, tarefaId, ator(principal), conteudo); flash.addFlashAttribute("sucesso", "Comentário salvo."); }
        catch (RuntimeException e) { flash.addFlashAttribute("erro", e.getMessage()); }
        return "redirect:/tarefas/" + tarefaId;
    }
    @PostMapping("/tarefas/{tarefaId}/comentarios/{id}/excluir")
    String excluir(@PathVariable Long tarefaId, @PathVariable Long id, Principal principal, RedirectAttributes flash) {
        try { comentarios.excluir(id, ator(principal)); flash.addFlashAttribute("sucesso", "Comentário excluído."); }
        catch (RuntimeException e) { flash.addFlashAttribute("erro", e.getMessage()); }
        return "redirect:/tarefas/" + tarefaId;
    }
}

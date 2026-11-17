package br.edu.tarefas.web.controller;

import br.edu.tarefas.web.service.AssistenteIaService;
import br.edu.tarefas.web.service.UsuarioService;
import br.edu.tarefas.web.service.RegraException;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AssistenteIaController {
    private final AssistenteIaService assistente;
    private final UsuarioService usuarios;

    public AssistenteIaController(AssistenteIaService assistente, UsuarioService usuarios) {
        this.assistente = assistente;
        this.usuarios = usuarios;
    }

    @PostMapping("/tarefas/{id}/assistente")
    String gerar(@PathVariable Long id, @RequestParam String acao, Principal principal, RedirectAttributes flash) {
        try {
            Long usuarioId = usuarios.atual(principal.getName()).getId();
            flash.addFlashAttribute("resultadoIa", assistente.gerar(id, usuarioId, acao));
            flash.addFlashAttribute("acaoIa", "resumo".equals(acao) ? "Resumo da tarefa" : "Plano sugerido");
        } catch (RegraException e) {
            flash.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/tarefas/" + id;
    }
}

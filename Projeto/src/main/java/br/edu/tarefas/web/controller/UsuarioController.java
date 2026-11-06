package br.edu.tarefas.web.controller;

import br.edu.tarefas.web.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarios;
    public UsuarioController(UsuarioService usuarios) { this.usuarios = usuarios; }
    @GetMapping String listar(Model model, Principal principal) {
        model.addAttribute("usuarios", usuarios.listar());
        model.addAttribute("atorId", usuarios.atual(principal.getName()).getId());
        return "usuarios/lista";
    }
    @GetMapping("/{id}/editar") String editar(@PathVariable Long id, Principal principal, Model model) {
        var u = usuarios.buscar(id);
        if (!u.getNomeUsuario().equals(principal.getName())) throw new RegraException("Você só pode editar sua conta.");
        model.addAttribute("usuario", u); return "usuarios/formulario";
    }
    @PostMapping("/{id}/salvar") String salvar(@PathVariable Long id, @RequestParam String nome,
            @RequestParam String email, Principal principal, RedirectAttributes flash) {
        try { usuarios.editar(id, principal.getName(), nome, email); flash.addFlashAttribute("sucesso", "Conta atualizada."); }
        catch (RuntimeException e) { flash.addFlashAttribute("erro", e.getMessage()); return "redirect:/usuarios/" + id + "/editar"; }
        return "redirect:/usuarios";
    }
    @PostMapping("/{id}/excluir") String excluir(@PathVariable Long id, Principal principal,
                                                HttpServletRequest request, RedirectAttributes flash) throws ServletException {
        try { usuarios.excluir(id, principal.getName()); request.logout(); return "redirect:/login?logout"; }
        catch (RuntimeException e) { flash.addFlashAttribute("erro", e.getMessage()); return "redirect:/usuarios"; }
    }
}

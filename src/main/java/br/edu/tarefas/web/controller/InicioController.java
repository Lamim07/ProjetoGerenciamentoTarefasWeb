package br.edu.tarefas.web.controller;

import br.edu.tarefas.web.service.UsuarioService;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InicioController {
    private final UsuarioService usuarios;
    public InicioController(UsuarioService usuarios) { this.usuarios = usuarios; }
    @GetMapping("/") String inicio() { return "redirect:/tarefas"; }
    @GetMapping("/login") String login() { return "login"; }
    @GetMapping("/cadastro") String cadastro() { return "cadastro"; }
    @PostMapping("/cadastro")
    String cadastrar(@RequestParam String nome, @RequestParam String usuario, @RequestParam String email,
                     @RequestParam String senha, @RequestParam String confirmacao, RedirectAttributes flash) {
        try {
            usuarios.cadastrar(nome, usuario, email, senha, confirmacao);
            flash.addFlashAttribute("sucesso", "Conta criada. Faça login para continuar.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            flash.addFlashAttribute("erro", e.getMessage());
            return "redirect:/cadastro";
        }
    }
    @ModelAttribute("usuarioLogado")
    public Object usuarioLogado(Principal principal) {
        return principal == null ? null : usuarios.atual(principal.getName());
    }
}

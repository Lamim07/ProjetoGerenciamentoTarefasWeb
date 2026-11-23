package br.edu.tarefas.web.controller;

import br.edu.tarefas.web.service.AssistenteIaService;
import br.edu.tarefas.web.service.UsuarioService;
import br.edu.tarefas.web.service.RegraException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
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
    @PostMapping("/tarefas/assistente/descricao")
    @ResponseBody
    ResponseEntity<Map<String, String>> sugerirDescricao(@RequestParam String titulo,
            @RequestParam(required = false) String descricao) {
        try {
            return ResponseEntity.ok(Map.of("valor", assistente.sugerirDescricao(titulo, descricao)));
        } catch (RegraException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping("/tarefas/assistente/prioridade")
    @ResponseBody
    ResponseEntity<Map<String, String>> sugerirPrioridade(@RequestParam String titulo,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate prazo) {
        try {
            return ResponseEntity.ok(Map.of("valor", assistente.sugerirPrioridade(titulo, descricao, prazo).name()));
        } catch (RegraException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }}

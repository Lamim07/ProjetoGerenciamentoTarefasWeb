package br.edu.tarefas.web.controller;

import br.edu.tarefas.web.model.Categoria;
import br.edu.tarefas.web.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaService categorias;
    public CategoriaController(CategoriaService categorias) { this.categorias = categorias; }
    @GetMapping String listar(Model model) {
        model.addAttribute("categorias", categorias.listar()); return "categorias/lista";
    }
    @GetMapping("/nova") String nova(Model model) {
        model.addAttribute("categoria", new Categoria()); return "categorias/formulario";
    }
    @GetMapping("/{id}/editar") String editar(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", categorias.buscar(id)); return "categorias/formulario";
    }
    @PostMapping("/salvar")
    String salvar(@RequestParam(required = false) Long id, @RequestParam String nome,
                  @RequestParam(required = false) String descricao, RedirectAttributes flash) {
        try { categorias.salvar(id, nome, descricao); flash.addFlashAttribute("sucesso", "Categoria salva."); }
        catch (RuntimeException e) {
            flash.addFlashAttribute("erro", e.getMessage());
            return id == null ? "redirect:/categorias/nova" : "redirect:/categorias/" + id + "/editar";
        }
        return "redirect:/categorias";
    }
    @PostMapping("/{id}/excluir") String excluir(@PathVariable Long id, RedirectAttributes flash) {
        try { categorias.excluir(id); flash.addFlashAttribute("sucesso", "Categoria excluída."); }
        catch (RuntimeException e) { flash.addFlashAttribute("erro", e.getMessage()); }
        return "redirect:/categorias";
    }
}

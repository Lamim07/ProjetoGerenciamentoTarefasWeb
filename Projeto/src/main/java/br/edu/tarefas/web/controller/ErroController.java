package br.edu.tarefas.web.controller;

import br.edu.tarefas.web.service.RegraException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErroController {
    @ExceptionHandler(RegraException.class) @ResponseStatus(HttpStatus.FORBIDDEN)
    String regra(RegraException e, Model model) { model.addAttribute("mensagem", e.getMessage()); return "erro"; }
}

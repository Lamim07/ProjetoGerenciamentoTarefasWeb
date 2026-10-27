package br.edu.tarefas.web.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "comentarios_tarefa", schema = "dbo")
public class ComentarioTarefa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "tarefa_id") private Tarefa tarefa;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "autor_id") private Usuario autor;
    @Column(nullable = false, length = 2000) private String conteudo;
    @Column(name = "criado_em", nullable = false) private LocalDateTime criadoEm;
    @PrePersist void preparar() { if (criadoEm == null) criadoEm = LocalDateTime.now(); }
    public Long getId() { return id; }
    public Tarefa getTarefa() { return tarefa; }
    public void setTarefa(Tarefa tarefa) { this.tarefa = tarefa; }
    public Usuario getAutor() { return autor; }
    public void setAutor(Usuario autor) { this.autor = autor; }
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}

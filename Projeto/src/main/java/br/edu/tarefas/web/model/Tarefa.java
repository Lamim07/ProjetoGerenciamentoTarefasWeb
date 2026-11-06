package br.edu.tarefas.web.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name = "tarefas", schema = "dbo")
public class Tarefa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 180) private String titulo;
    @Column(nullable = false, columnDefinition = "nvarchar(max)") private String descricao;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "criador_id") private Usuario criador;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "responsavel_id") private Usuario responsavel;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "categoria_id") private Categoria categoria;
    @Column(nullable = false) private LocalDate prazo;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 10) private PrioridadeTarefa prioridade;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private StatusTarefa status;
    @Column(name = "criada_em", nullable = false) private LocalDateTime criadaEm;
    @PrePersist void preparar() { if (criadaEm == null) criadaEm = LocalDateTime.now(); if (status == null) status = StatusTarefa.PENDENTE; }
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Usuario getCriador() { return criador; }
    public void setCriador(Usuario criador) { this.criador = criador; }
    public Usuario getResponsavel() { return responsavel; }
    public void setResponsavel(Usuario responsavel) { this.responsavel = responsavel; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public LocalDate getPrazo() { return prazo; }
    public void setPrazo(LocalDate prazo) { this.prazo = prazo; }
    public PrioridadeTarefa getPrioridade() { return prioridade; }
    public void setPrioridade(PrioridadeTarefa prioridade) { this.prioridade = prioridade; }
    public StatusTarefa getStatus() { return status; }
    public void setStatus(StatusTarefa status) { this.status = status; }
    public LocalDateTime getCriadaEm() { return criadaEm; }
}

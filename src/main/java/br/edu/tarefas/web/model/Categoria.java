package br.edu.tarefas.web.model;

import jakarta.persistence.*;

@Entity @Table(name = "categorias", schema = "dbo")
public class Categoria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 80) private String nome;
    @Column(length = 255) private String descricao;
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}

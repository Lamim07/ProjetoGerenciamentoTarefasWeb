package br.edu.tarefas.web.model;

import jakarta.persistence.*;

@Entity @Table(name = "usuarios", schema = "dbo")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 120) private String nome;
    @Column(name = "nome_usuario", nullable = false, unique = true, length = 60) private String nomeUsuario;
    @Column(nullable = false, unique = true, length = 180) private String email;
    @Column(name = "senha_hash", nullable = false, length = 300) private String senhaHash;
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }
}

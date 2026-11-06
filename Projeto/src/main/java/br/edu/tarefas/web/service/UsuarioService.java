package br.edu.tarefas.web.service;

import br.edu.tarefas.web.model.Usuario;
import br.edu.tarefas.web.repository.*;
import br.edu.tarefas.web.security.SenhaLegadaEncoder;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarios;
    private final TarefaRepository tarefas;
    private final ComentarioRepository comentarios;
    private final SenhaLegadaEncoder senhas;
    public UsuarioService(UsuarioRepository usuarios, TarefaRepository tarefas,
                          ComentarioRepository comentarios, SenhaLegadaEncoder senhas) {
        this.usuarios = usuarios; this.tarefas = tarefas; this.comentarios = comentarios; this.senhas = senhas;
    }
    public List<Usuario> listar() { return usuarios.findAll(Sort.by("nome")); }
    public Usuario buscar(Long id) { return usuarios.findById(id).orElseThrow(() -> new RegraException("Usuário não encontrado.")); }
    public Usuario atual(String login) { return usuarios.findByNomeUsuario(login).orElseThrow(() -> new RegraException("Sessão inválida.")); }

    @Transactional
    public void cadastrar(String nome, String login, String email, String senha, String confirmacao) {
        nome = obrigatorio(nome, 120, "Nome");
        login = obrigatorio(login, 60, "Usuário");
        email = obrigatorio(email, 180, "E-mail");
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) throw new RegraException("Informe um e-mail válido.");
        if (senha == null || senha.length() < 8) throw new RegraException("A senha deve ter ao menos 8 caracteres.");
        if (!senha.equals(confirmacao)) throw new RegraException("As senhas não coincidem.");
        if (usuarios.existsByNomeUsuarioIgnoreCase(login) || usuarios.existsByEmailIgnoreCase(email))
            throw new RegraException("Usuário ou e-mail já cadastrado.");
        Usuario u = new Usuario(); u.setNome(nome); u.setNomeUsuario(login); u.setEmail(email);
        u.setSenhaHash(senhas.encode(senha)); usuarios.save(u);
    }
    @Transactional
    public void editar(Long id, String loginAtual, String nome, String email) {
        Usuario u = buscar(id);
        if (!u.getNomeUsuario().equals(loginAtual)) throw new RegraException("Você só pode editar sua conta.");
        nome = obrigatorio(nome, 120, "Nome");
        email = obrigatorio(email, 180, "E-mail");
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) throw new RegraException("Informe um e-mail válido.");
        if (!u.getEmail().equalsIgnoreCase(email) && usuarios.existsByEmailIgnoreCase(email))
            throw new RegraException("E-mail já cadastrado.");
        u.setNome(nome); u.setEmail(email);
    }
    @Transactional
    public void excluir(Long id, String loginAtual) {
        Usuario u = buscar(id);
        if (!u.getNomeUsuario().equals(loginAtual)) throw new RegraException("Você só pode excluir sua conta.");
        if (tarefas.existsByCriadorIdOrResponsavelId(id, id) || comentarios.existsByAutorId(id))
            throw new RegraException("A conta possui tarefas ou comentários e não pode ser excluída.");
        usuarios.delete(u);
    }
    static String obrigatorio(String valor, int maximo, String campo) {
        if (valor == null || valor.isBlank() || valor.trim().length() > maximo)
            throw new RegraException(campo + " deve ter de 1 a " + maximo + " caracteres.");
        return valor.trim();
    }
}

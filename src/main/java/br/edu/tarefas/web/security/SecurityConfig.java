package br.edu.tarefas.web.security;

import br.edu.tarefas.web.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig {
    @Bean UserDetailsService usuarios(UsuarioRepository repo) {
        return login -> {
            var u = repo.findByNomeUsuarioIgnoreCaseOrEmailIgnoreCase(login, login)
                    .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas."));
            return User.withUsername(u.getNomeUsuario()).password(u.getSenhaHash()).roles("USER").build();
        };
    }
    @Bean SecurityFilterChain filtros(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(a -> a
                    .requestMatchers("/css/**", "/js/**", "/login", "/cadastro", "/error").permitAll()
                    .anyRequest().authenticated())
                .formLogin(f -> f.loginPage("/login").defaultSuccessUrl("/tarefas", true).permitAll())
                .logout(l -> l.logoutSuccessUrl("/login?logout").permitAll())
                .build();
    }
}

package com.gabrielflores.myfortune.security;

import com.gabrielflores.myfortune.model.user.Perfil;
import com.gabrielflores.myfortune.model.user.Usuario;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
@Getter
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class UserPrincipal extends User implements OAuth2User {

    private static final long serialVersionUID = -2826211351214347389L;

    @ToString.Include
    private final Long id;

    @ToString.Include
    private final String nome;

    public UserPrincipal(Usuario usuario) {
        super(usuario.getEmail(), usuario.getSenha(),
                usuario.getAtivo(), true, true, usuario.getConfirmado(),
                getAuthorities(usuario));
        this.id = usuario.getId();
        this.nome = usuario.getNome();
    }

    private static Set<SimpleGrantedAuthority> getAuthorities(Usuario usuario) {
        final Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        usuario.getPerfis().stream().map(Perfil::getNome).map(SimpleGrantedAuthority::new).collect(Collectors.toCollection(() -> authorities));
        usuario.getPerfis().stream().map(p -> "ROLE_" + p.getNome()).map(SimpleGrantedAuthority::new).collect(Collectors.toCollection(() -> authorities));
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        final Map<String, Object> atributos = new HashMap<>(3);
        atributos.put("sub", this.id);
        atributos.put("name", this.nome);
        atributos.put("email", getUsername());
        return atributos;
    }

    @Override
    public String getName() {
        return this.getNome();
    }

    public boolean isAdmin() {
        return this.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(authority -> "ADMIN".equals(authority));
    }

}

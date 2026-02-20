package com.capricho.ms_usuarios.security;

import com.capricho.ms_usuarios.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UsuarioPrincipal implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Composición: Envolvemos a la entidad original
    private final Usuario usuario;

    public UsuarioPrincipal(Usuario usuario) {
        this.usuario = usuario;
    }

    // Un método extra muy útil para cuando generes el JWT y necesites el ID
    public Long getIdUsuario() {
        return usuario.getIdUsuario();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Tu lógica anterior estaba perfecta. Spring requiere el prefijo ROLE_
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombreRol().toUpperCase()));
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail(); // El email es nuestro identificador de login
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(usuario.getActivo());
    }
}
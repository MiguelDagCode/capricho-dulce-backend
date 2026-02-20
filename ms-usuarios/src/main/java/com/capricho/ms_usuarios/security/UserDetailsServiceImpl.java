package com.capricho.ms_usuarios.security;

import com.capricho.ms_usuarios.model.Usuario;
import com.capricho.ms_usuarios.repository.UsuarioRepository;
import com.capricho.ms_usuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // AQUÍ es donde debe ir la excepción de Spring Security usando el poder funcional de Java
    	Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
        return new UsuarioPrincipal(usuario);
    }
}
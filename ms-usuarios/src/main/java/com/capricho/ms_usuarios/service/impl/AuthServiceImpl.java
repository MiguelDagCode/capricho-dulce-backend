package com.capricho.ms_usuarios.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.capricho.ms_usuarios.dto.request.LoginRequest;
import com.capricho.ms_usuarios.dto.response.AuthResponse;
import com.capricho.ms_usuarios.security.UsuarioPrincipal;
import com.capricho.ms_usuarios.security.jwt.JwtService;
import com.capricho.ms_usuarios.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

	private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    
	@Override
	public AuthResponse login(LoginRequest request) {
		// 1. Autenticar credenciales
        // El AuthenticationManager verifica automáticamente si la contraseña hasheada coincide.
        // Si la clave es incorrecta, lanza una excepción (BadCredentialsException).
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Si pasa la autenticación, cargamos los detalles del usuario
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        
        // Hacemos cast a nuestra clase personalizada para poder extraer el ID y el Rol
        UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) userDetails;

        // 3. Generar el Token JWT con los Custom Claims
        String jwtToken = jwtService.generateToken(usuarioPrincipal);

        // 4. Retornar la respuesta
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
	}

}

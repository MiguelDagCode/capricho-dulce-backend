package com.capricho.ms_catalogo.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        String userEmail = null;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            // Extraemos datos sin ir a ninguna base de datos
            userEmail = jwtService.extractUsername(jwt);
            String rolExtraido = jwtService.extractRol(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Convertimos el String "ADMIN" a un objeto que Spring entienda: "ROLE_ADMIN"
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rolExtraido));

                // Creamos el pase de entrada (El segundo parámetro es null porque no tenemos/necesitamos el password)
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail,
                        null,
                        authorities
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // ¡Usuario autenticado exitosamente en ms-catalogo!
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (ExpiredJwtException e) {
            log.warn("El token JWT ha expirado");
        } catch (SignatureException | MalformedJwtException e) {
            log.warn("Token JWT inválido o manipulado");
        } catch (Exception e) {
            log.error("Error validando el token en ms-catalogo", e);
        }

        filterChain.doFilter(request, response);
    }
}
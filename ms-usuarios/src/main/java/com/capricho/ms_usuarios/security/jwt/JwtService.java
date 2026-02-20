package com.capricho.ms_usuarios.security.jwt;

import com.capricho.ms_usuarios.security.UsuarioPrincipal; // Tu adaptador
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET_KEY;
    private final long jwtExpiration;

    public JwtService(
            @Value("${application.security.jwt.secret-key}") String secretKey,
            @Value("${application.security.jwt.expiration}") long jwtExpiration
    ) {
        this.SECRET_KEY = secretKey;
        this.jwtExpiration = jwtExpiration;
    }

    // 🌟 MEJORA 1: Usamos UsuarioPrincipal para extraer datos clave e inyectarlos en el Token
    public String generateToken(UsuarioPrincipal userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id_usuario", userDetails.getIdUsuario());
        
        // Extraemos el rol (ej: "ROLE_ADMIN" -> "ADMIN") para que sea más limpio
        String rol = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        extraClaims.put("rol", rol);
        
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    // 🌟 MEJORA 2: Sintaxis moderna de JJWT 0.12.x
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .claims(extraClaims) 
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey()) // El algoritmo se infiere automáticamente de la llave
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 🌟 MEJORA 2.1: parserBuilder() ya no existe, usamos parser() y verifyWith()
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Cambiamos el retorno a SecretKey en lugar de Key (requerido por JJWT moderno)
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
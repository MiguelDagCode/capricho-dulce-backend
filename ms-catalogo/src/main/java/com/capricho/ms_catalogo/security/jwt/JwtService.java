package com.capricho.ms_catalogo.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    // 1. Extraer Email
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 2. Extraer el Rol que inyectamos en ms-usuarios
    public String extractRol(String token) {
        return extractClaim(token, claims -> claims.get("rol", String.class));
    }

    // 3. Extraer el ID (Te servirá luego cuando ms-pedidos guarde quién hizo la compra)
    public Long extractIdUsuario(String token) {
        return extractClaim(token, claims -> claims.get("id_usuario", Long.class));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey()) // Aquí ocurre la magia: si alguien alteró el token, esto falla
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
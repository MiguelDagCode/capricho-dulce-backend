package com.capricho.ms_usuarios.controller;

import com.capricho.ms_usuarios.dto.request.LoginRequest;
import com.capricho.ms_usuarios.dto.response.AuthResponse;
import com.capricho.ms_usuarios.service.AuthService;
import com.capricho.ms_usuarios.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        
        // 1. Llamamos al servicio (si falla, el GlobalExceptionHandler atrapará el error)
        AuthResponse tokenDto = authService.login(request);
        
        // 2. Construimos la respuesta estandarizada de ÉXITO
        ApiResponse<AuthResponse> response = new ApiResponse<>(
                true,
                "Autenticación exitosa",
                LocalDateTime.now(),
                tokenDto
        );
        
        return ResponseEntity.ok(response);
    }
}
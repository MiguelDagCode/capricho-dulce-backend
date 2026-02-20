package com.capricho.ms_usuarios.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @Email(message = "El formato del correo es inválido")
    @NotBlank(message = "El correo es obligatorio")
    private String email; // Ajustado para coincidir con el UserDetails y BD

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
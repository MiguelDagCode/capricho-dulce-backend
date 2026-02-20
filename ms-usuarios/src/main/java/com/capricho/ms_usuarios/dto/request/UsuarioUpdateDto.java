package com.capricho.ms_usuarios.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UsuarioUpdateDto {
    @Email(message = "El formato de correo no es válido")
    private String email;
    
    private String password; // Opcional
    
    private Integer idRol;
}
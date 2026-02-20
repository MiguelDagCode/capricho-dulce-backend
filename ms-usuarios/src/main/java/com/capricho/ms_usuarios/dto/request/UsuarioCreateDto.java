package com.capricho.ms_usuarios.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UsuarioCreateDto {
    @NotBlank private String nombre;
    @NotBlank private String apellido;
    @Email @NotBlank private String correo;
    private String telefono;
    @NotNull private Integer idRol;
    @NotBlank private String password; // Obligatorio
}
package com.capricho.ms_usuarios.dto.request;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UsuarioUpdateDto {
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private Integer idRol;
    private String password; // Opcional
}
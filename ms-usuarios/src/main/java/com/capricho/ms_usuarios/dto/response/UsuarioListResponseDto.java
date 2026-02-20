package com.capricho.ms_usuarios.dto.response;


import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UsuarioListResponseDto {
    private Integer idUsuario;
    private String nombresCompletos; // "Juan Perez"
    private String correo;
    private String nombreRol;
    private String telefono;
    private Boolean estado;
}
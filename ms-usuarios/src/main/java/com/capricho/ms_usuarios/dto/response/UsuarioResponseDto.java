package com.capricho.ms_usuarios.dto.response;


import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UsuarioResponseDto {
    private Integer idUsuario;
    private String nombre;    // Separado para el input
    private String apellido;  // Separado para el input
    private String correo;
    private String telefono;
    private Integer idRol;    // ID para pre-seleccionar el <select> o combobox
    private Boolean estado;
    // NO devolvemos password
}
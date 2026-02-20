package com.capricho.ms_usuarios.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
public class UsuarioListResponseDto {
	private Long idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String nombreRol;
    private Boolean estado;
}
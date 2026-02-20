package com.capricho.ms_usuarios.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String nombre;
    private String apellido;
    private String correo;
    private String password;
    private String telefono;
    private Integer idRol; // Ahora recibimos el ID, no el nombre
}
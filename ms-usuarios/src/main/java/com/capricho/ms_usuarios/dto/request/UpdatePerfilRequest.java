package com.capricho.ms_usuarios.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public  class UpdatePerfilRequest {
    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    private String celular;
    private String direccionReferencia;

    @NotNull(message = "El distrito es obligatorio")
    private Integer idDistrito;
    
    // No permitimos cambiar DNI ni Correo por aquí (suele requerir otro flujo de seguridad)
    // La contraseña se suele cambiar en un endpoint separado ("Cambiar Password")
}
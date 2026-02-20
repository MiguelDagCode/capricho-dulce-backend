package com.capricho.ms_usuarios.dto.request;

import com.capricho.ms_usuarios.enums.TipoDocumento;
import jakarta.validation.constraints.*;
import lombok.Data;

    @Data
    public  class RegistroClienteRequest {
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Formato de correo inválido")
        private String email;

        @NotBlank(message = "La contraseña es obligatoria")
        private String password;

        @NotBlank(message = "Los nombres son obligatorios")
        private String nombres;

        @NotBlank(message = "Los apellidos son obligatorios")
        private String apellidos;

        private String celular; // Opcional según tu modelo
        private String direccionReferencia; // Opcional

        @NotNull(message = "El distrito es obligatorio")
        private Integer idDistrito;

        @NotNull(message = "El tipo de documento es obligatorio")
        private TipoDocumento tipoDocumento;

        @NotBlank(message = "El documento es obligatorio")
        private String documento;
    }


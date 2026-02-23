package com.capricho.ms_catalogo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsumoRequestDto {

    @NotBlank(message = "El nombre del insumo es obligatorio")
    private String nombre;

    @NotBlank(message = "La unidad de medida es obligatoria")
    private String unidadMedida;
}
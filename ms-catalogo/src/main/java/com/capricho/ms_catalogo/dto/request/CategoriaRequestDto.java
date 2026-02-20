package com.capricho.ms_catalogo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaRequestDto {
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String nombre;
}
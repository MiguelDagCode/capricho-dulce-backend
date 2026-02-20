package com.capricho.ms_catalogo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponseDto {
    private Integer idCategoria;
    private String nombre;
}
package com.capricho.ms_catalogo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsumoResponseDto {

    private Long idInsumo;
    private String nombre;
    private String unidadMedida;
}
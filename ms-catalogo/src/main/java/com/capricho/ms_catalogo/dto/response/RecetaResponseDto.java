package com.capricho.ms_catalogo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RecetaResponseDto {

    private Long idProducto;
    private Long idInsumo;
    private String nombreProducto;
    private String nombreInsumo;
    private BigDecimal cantidadInsumo;
}
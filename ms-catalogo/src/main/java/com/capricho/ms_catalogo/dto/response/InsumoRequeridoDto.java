package com.capricho.ms_catalogo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsumoRequeridoDto {
    private Long idInsumo;
    private String nombreInsumo;
    private BigDecimal cantidadBase; 
}
package com.capricho.ms_inventario.dto.response;

import java.math.BigDecimal;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsumoRequeridoDto {
	private Long idInsumo;
    private String nombreInsumo;
    private BigDecimal cantidadBase;
}

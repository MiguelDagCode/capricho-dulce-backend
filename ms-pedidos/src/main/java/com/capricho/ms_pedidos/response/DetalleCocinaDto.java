package com.capricho.ms_pedidos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCocinaDto {
    private Long idProducto;
    private String nombreProducto;
    private Integer cantidad;
    private String observaciones;
    private BigDecimal precioUnitario;
}

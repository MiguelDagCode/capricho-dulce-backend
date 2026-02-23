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
public class ProductoResponseDto {
    private Long idProducto;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String imgUrl;
    private String categoria; 
}
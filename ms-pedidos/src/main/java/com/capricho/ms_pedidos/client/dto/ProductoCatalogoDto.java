package com.capricho.ms_pedidos.client.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoCatalogoDto {
    private Long id;
    private String nombre;
    private BigDecimal precio;
    // Ignoramos el resto de campos (descripción, imagen) porque no los necesitamos para cobrar
}
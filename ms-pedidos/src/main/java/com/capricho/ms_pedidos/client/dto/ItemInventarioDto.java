package com.capricho.ms_pedidos.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemInventarioDto {
    private Long idProducto;
    private Integer cantidad;
}
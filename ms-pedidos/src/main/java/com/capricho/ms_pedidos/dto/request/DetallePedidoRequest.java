package com.capricho.ms_pedidos.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DetallePedidoRequest {
    @NotNull(message = "El ID del producto es obligatorio")
    private Long idProducto;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "Debe comprar al menos 1 unidad")
    private Integer cantidad;
}
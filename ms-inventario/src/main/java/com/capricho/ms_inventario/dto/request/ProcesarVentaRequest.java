package com.capricho.ms_inventario.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class ProcesarVentaRequest {
    @NotNull(message = "El ID del pedido es obligatorio")
    private Long idPedido;
    
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;
    
    @NotEmpty(message = "La lista de productos no puede estar vacía")
    private List<ItemProductoRequest> items;
}
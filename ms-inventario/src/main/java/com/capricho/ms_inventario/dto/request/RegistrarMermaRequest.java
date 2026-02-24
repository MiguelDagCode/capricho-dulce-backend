package com.capricho.ms_inventario.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarMermaRequest {
    @NotNull(message = "El ID del pedido es obligatorio")
    private Long idPedido;
    
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;
    
    @NotNull(message = "El ID del producto es obligatorio")
    private Long idProducto;
    
    @NotNull(message = "La cantidad es obligatoria")
    private Integer cantidad;
    
    @NotNull(message = "El ID del motivo es obligatorio")
    private Long idMotivo;
    
    private String observacion;
}
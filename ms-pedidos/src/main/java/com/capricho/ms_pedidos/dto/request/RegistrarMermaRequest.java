package com.capricho.ms_pedidos.dto.request;

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
    @NotNull(message = "El ID del producto es obligatorio")
    private Long idProducto;
    
    @NotNull(message = "El ID del motivo es obligatorio")
    private Long idMotivo;
    
    private String observacion;
}
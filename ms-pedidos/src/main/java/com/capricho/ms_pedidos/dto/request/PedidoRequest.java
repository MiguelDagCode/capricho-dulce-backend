package com.capricho.ms_pedidos.dto.request;

import com.capricho.ms_pedidos.enums.TipoEnvio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PedidoRequest {
    // El idUsuarioCliente lo sacaremos del JWT en el backend por seguridad
    
    private Integer idDistrito; // Opcional, solo si es delivery
    private String direccionEntrega;
    
    @NotNull(message = "El tipo de envío es obligatorio")
    private TipoEnvio tipoEnvio;
    
    @NotEmpty(message = "El pedido debe tener al menos un detalle")
    @Valid
    private List<DetallePedidoRequest> detalles;
}
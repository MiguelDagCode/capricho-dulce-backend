package com.capricho.ms_pedidos.client.dto;

import lombok.Data;
import java.util.List;

@Data
public class CatalogoResponse {
    private boolean success;
    private String mensaje;
    private List<ProductoCatalogoDto> data;
}
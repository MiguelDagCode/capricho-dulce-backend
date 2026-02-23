package com.capricho.ms_catalogo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecetaRequestDto {

    @NotNull(message = "El producto es obligatorio")
    private Long idProducto;

    @NotNull(message = "El insumo es obligatorio")
    private Long idInsumo;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private BigDecimal cantidadInsumo;
}
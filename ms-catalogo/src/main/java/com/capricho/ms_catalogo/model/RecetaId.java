package com.capricho.ms_catalogo.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetaId implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long idProducto;
    private Long idInsumo;
}
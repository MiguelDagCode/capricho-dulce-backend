package com.capricho.ms_catalogo.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_receta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Receta implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private RecetaId id;

    // Relación con Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idProducto") // Mapea este objeto al idProducto de RecetaId
    @JoinColumn(name = "id_producto")
    private Producto producto;

    // Relación con Insumo
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idInsumo") // Mapea este objeto al idInsumo de RecetaId
    @JoinColumn(name = "id_insumo")
    private Insumo insumo;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal cantidadInsumo;

    @Column(columnDefinition = "boolean default true")
    private Boolean activo = true;
}
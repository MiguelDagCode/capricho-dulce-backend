package com.capricho.ms_catalogo.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_producto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", referencedColumnName = "idCategoria", nullable = false)
    private Categoria categoria;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(length = 255)
    private String imgUrl;

    @Column(columnDefinition = "boolean default true")
    private Boolean estado = true;
}
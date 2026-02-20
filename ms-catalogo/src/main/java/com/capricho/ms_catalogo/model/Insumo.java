package com.capricho.ms_catalogo.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_insumo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Insumo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInsumo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String unidadMedida; // KG, LT, UNIDAD

    @Column(columnDefinition = "boolean default true")
    private Boolean activo = true;
}
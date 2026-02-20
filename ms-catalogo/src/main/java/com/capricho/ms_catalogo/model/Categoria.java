package com.capricho.ms_catalogo.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_categoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCategoria;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(columnDefinition = "boolean default true")
    private Boolean activo = true;
}
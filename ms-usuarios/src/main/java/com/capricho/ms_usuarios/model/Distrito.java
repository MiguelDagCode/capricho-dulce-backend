package com.capricho.ms_usuarios.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_distrito")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Distrito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDistrito;

    @Column(nullable = false, length = 50)
    private String nombre; // Ej: 'San Miguel', 'Carabayllo'

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal costoEnvio;

    @Column(columnDefinition = "boolean default true")
    private Boolean activo = true;
    //Go
}
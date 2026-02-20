package com.capricho.ms_usuarios.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_rol")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRol;

    @Column(nullable = false, unique = true, length = 50)
    private String nombreRol;

    @Column(columnDefinition = "boolean default true")
    private Boolean activo = true;
}
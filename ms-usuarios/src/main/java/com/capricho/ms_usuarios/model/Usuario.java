package com.capricho.ms_usuarios.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_usuario")
@Inheritance(strategy = InheritanceType.JOINED) // ¡El secreto de tu herencia!
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", referencedColumnName = "idRol")
    private Rol rol;

    @Column(columnDefinition = "boolean default true")
    private Boolean activo = true;
}
package com.capricho.ms_usuarios.model;

import com.capricho.ms_usuarios.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_trabajador")
@PrimaryKeyJoinColumn(name = "id_usuario") // Vincula el ID del hijo con el padre
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trabajador extends Usuario {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, length = 15)
    private String celular;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(nullable = false, unique = true, length = 20)
    private String documento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salario;
}
package com.capricho.ms_usuarios.model;

import com.capricho.ms_usuarios.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_cliente")
@PrimaryKeyJoinColumn(name = "id_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente extends Usuario {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(length = 15)
    private String celular;

    @Column(length = 255)
    private String direccionReferencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_distrito", referencedColumnName = "idDistrito")
    private Distrito distrito;

    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @Column(length = 20)
    private String documento;
}
package com.unfv.biblioteca.bibliotecaapi.autenticacion.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipos_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TipoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_usuario_seq_gen")
    @SequenceGenerator(name = "tipo_usuario_seq_gen", sequenceName = "TIPO_USUARIO_SEQ", allocationSize = 1)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nombre_tipo", unique = true, nullable = false, length = 50)
    private String nombreTipo;

    @Column(name = "limite_prestamos", nullable = false)
    private Integer limitePrestamos;

    @Column(name = "dias_prestamos", nullable = false)
    private Integer diasPrestamos;
}
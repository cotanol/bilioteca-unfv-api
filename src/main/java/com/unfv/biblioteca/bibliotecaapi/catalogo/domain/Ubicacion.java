package com.unfv.biblioteca.bibliotecaapi.catalogo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ubicaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ubicacion {

    @Id
    @SequenceGenerator(
            name = "ubicacion_seq_gen",
            sequenceName = "UBICACION_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ubicacion_seq_gen"
    )
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "facultad", nullable = false, length = 100)
    private String facultad;

    @Column(name = "biblioteca_nombre", length = 100)
    private String bibliotecaNombre;

    @Column(name = "piso")
    private Integer piso;

    @Column(name = "estante", length = 20)
    private String estante;
}
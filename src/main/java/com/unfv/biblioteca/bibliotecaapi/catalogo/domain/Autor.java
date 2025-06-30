package com.unfv.biblioteca.bibliotecaapi.catalogo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "autores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Autor {

    @Id
    @SequenceGenerator(
            name = "autor_seq_gen",
            sequenceName = "AUTOR_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "autor_seq_gen"
    )
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nombre_completo", nullable = false, length = 255)
    private String nombreCompleto;

    @Column(name = "nacionalidad", length = 50)
    private String nacionalidad;
}
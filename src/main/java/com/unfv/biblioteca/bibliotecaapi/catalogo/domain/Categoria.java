package com.unfv.biblioteca.bibliotecaapi.catalogo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Categoria {

    @Id
    @SequenceGenerator(
            name = "categoria_seq_gen",
            sequenceName = "CATEGORIA_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "categoria_seq_gen"
    )
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nombre_categoria", nullable = false, unique = true, length = 100)
    private String nombreCategoria;

    @Column(name = "codigo_dewey", length = 20)
    private String codigoDewey;
}
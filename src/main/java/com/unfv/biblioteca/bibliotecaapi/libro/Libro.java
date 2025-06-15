package com.unfv.biblioteca.bibliotecaapi.libro;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "LIBROS")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LIBRO_SEQ")
    @SequenceGenerator(name = "LIBRO_SEQ", sequenceName = "LIBRO_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "TITULO", nullable = false)
    private String titulo;

    @Column(name = "AUTOR", nullable = false)
    private String autor;
}

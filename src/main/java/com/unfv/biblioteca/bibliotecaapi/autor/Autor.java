package com.unfv.biblioteca.bibliotecaapi.autor;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "AUTORES")

public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTOR_SEQ")
    @SequenceGenerator(name = "AUTOR_SEQ", sequenceName = "AUTOR_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    @Column(name = "APELLIDO", nullable = false)
    private String apellido;
}

package com.unfv.biblioteca.bibliotecaapi.catalogo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "materiales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"editorial", "autores", "categorias"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Material {

    @Id
    @SequenceGenerator(
            name = "material_seq_gen",
            sequenceName = "MATERIAL_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "material_seq_gen"
    )
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true, length = 20)
    private String isbn;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(length = 255)
    private String subtitulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editorial_id")
    private Editorial editorial;

    @Column(length = 50)
    private String edicion;

    @Column(name = "anio_publicacion")
    private Integer anioPublicacion;

    @Column(name = "numero_paginas")
    private Integer numeroPaginas;

    @Column(name = "tipo_material", nullable = false, length = 50)
    private String tipoMaterial;

    @Lob
    private String resumen;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "materiales_autores",
            joinColumns = @JoinColumn(name = "material_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autores = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "materiales_categorias",
            joinColumns = @JoinColumn(name = "material_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new HashSet<>();

    // --- Métodos de Ayuda (Helper Methods) ---
    public void addAutor(Autor autor) {
        this.autores.add(autor);
    }

    public void removeAutor(Autor autor) {
        this.autores.remove(autor);
    }

    public void addCategoria(Categoria categoria) {
        this.categorias.add(categoria);
    }

    public void removeCategoria(Categoria categoria) {
        this.categorias.remove(categoria);
    }
}
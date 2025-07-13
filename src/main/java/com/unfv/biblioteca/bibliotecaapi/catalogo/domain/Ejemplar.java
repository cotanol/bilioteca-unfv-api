package com.unfv.biblioteca.bibliotecaapi.catalogo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ejemplares")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"material", "ubicacion"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ejemplar {

    @Id
    @SequenceGenerator(
            name = "ejemplar_seq_gen",
            sequenceName = "EJEMPLAR_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ejemplar_seq_gen"
    )
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    @JsonIgnore
    private Material material;

    @Column(name = "codigo_barras", unique = true, nullable = false, length = 50)
    private String codigoBarras;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ubicacion_id", nullable = false)
    @JsonIgnore
    private Ubicacion ubicacion;

    @Column(nullable = false, length = 50)
    private String estado;

    @Column(name = "fecha_adquisicion")
    private LocalDate fechaAdquisicion;
}
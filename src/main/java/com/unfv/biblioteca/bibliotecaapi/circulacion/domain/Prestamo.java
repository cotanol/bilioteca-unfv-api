package com.unfv.biblioteca.bibliotecaapi.circulacion.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.Usuario;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Ejemplar;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prestamos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"ejemplar", "usuario"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Prestamo {

    @Id
    @SequenceGenerator(
            name = "prestamo_seq_gen",
            sequenceName = "PRESTAMO_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "prestamo_seq_gen"
    )
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ejemplar_id", nullable = false)
    @JsonIgnore
    private Ejemplar ejemplar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @Column(name = "fecha_prestamo")
    private LocalDateTime fechaPrestamo;

    @Column(name = "fecha_devolucion_pactada", nullable = false)
    private LocalDate fechaDevolucionPactada;

    @Column(name = "fecha_devolucion_real")
    private LocalDateTime fechaDevolucionReal;

    @Column(nullable = false, length = 50)
    private String estado;

    @Column(nullable = false)
    private Integer renovaciones = 0;
}
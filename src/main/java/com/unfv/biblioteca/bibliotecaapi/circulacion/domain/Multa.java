package com.unfv.biblioteca.bibliotecaapi.circulacion.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "multas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "prestamo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Multa {

    @Id
    @SequenceGenerator(
            name = "multa_seq_gen",
            sequenceName = "MULTA_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "multa_seq_gen"
    )
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestamo_id", nullable = false)
    @JsonIgnore
    private Prestamo prestamo;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDate fechaGeneracion;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;
}
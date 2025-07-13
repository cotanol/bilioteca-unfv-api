package com.unfv.biblioteca.bibliotecaapi.reserva.domain;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.Usuario;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Material;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"material", "usuario"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reserva {

    @Id
    @SequenceGenerator(
            name = "reserva_seq_gen",
            sequenceName = "RESERVA_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reserva_seq_gen"
    )
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDateTime fechaReserva;

    @Column(nullable = false, length = 20)
    private String estado;
}

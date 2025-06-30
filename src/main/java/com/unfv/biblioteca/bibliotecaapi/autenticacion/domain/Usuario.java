package com.unfv.biblioteca.bibliotecaapi.autenticacion.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"tipoUsuario"}) // Excluimos la relación para evitar recursividad
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq_gen")
    @SequenceGenerator(name = "usuario_seq_gen", sequenceName = "USUARIO_SEQ", allocationSize = 1)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "codigo_universitario", unique = true, nullable = false, length = 20)
    private String codigoUniversitario;

    @Column(unique = true, nullable = false, length = 8)
    private String dni;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellido_paterno", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(length = 15)
    private String telefono;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_usuario_id")
    private TipoUsuario tipoUsuario;

    @Column(length = 20)
    private String estado;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
}
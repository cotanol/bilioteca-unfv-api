package com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.UsuarioResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PrestamoResponseDTO {

    private Long id;
    private String codigoBarrasEjemplar;
    private String tituloMaterial;
    private LocalDateTime fechaPrestamo;
    private LocalDate fechaDevolucionPactada;
    private LocalDateTime fechaDevolucionReal;
    private String estado;
    private Integer renovaciones;
    private UsuarioResponseDTO usuario;
}
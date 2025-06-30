package com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.UsuarioDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.EjemplarDetalleDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class PrestamoDetalleDTO {

    private Long id;
    private LocalDateTime fechaPrestamo;
    private LocalDate fechaDevolucionPactada;
    private LocalDateTime fechaDevolucionReal;
    private String estado;
    private Integer renovaciones;
    private UsuarioDetalleDTO usuario;
    private EjemplarDetalleDTO ejemplar;
}


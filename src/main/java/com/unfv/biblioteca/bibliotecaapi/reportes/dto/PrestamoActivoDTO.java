package com.unfv.biblioteca.bibliotecaapi.reportes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoActivoDTO {
    private Long id;
    private String usuario;
    private String titulo;
    private LocalDateTime fechaPrestamo;
    private LocalDate fechaDevolucionPactada;
    private String estado;
}


package com.unfv.biblioteca.bibliotecaapi.reportes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadMaterialDTO {
    private Long id;
    private String titulo;
    private String isbn;
    private Long totalEjemplares;
    private Long ejemplaresDisponibles;
    private Long ejemplaresPrestados;
}


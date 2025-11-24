package com.unfv.biblioteca.bibliotecaapi.reportes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadResponseDTO {
    private Long materialId;
    private Integer ejemplaresDisponibles;
}


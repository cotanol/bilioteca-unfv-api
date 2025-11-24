package com.unfv.biblioteca.bibliotecaapi.reportes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevolucionRequestDTO {
    private Long prestamoId;
    private LocalDateTime fechaDevolucion;
}


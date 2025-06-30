package com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class MultaDTO {

    private Long id;
    private BigDecimal monto;
    private LocalDate fechaGeneracion;
    private String estado;
    private LocalDate fechaPago;
    private Long prestamoId;
}


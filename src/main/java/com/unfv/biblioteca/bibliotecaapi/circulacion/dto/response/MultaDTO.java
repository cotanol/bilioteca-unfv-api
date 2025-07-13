package com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class MultaDTO {

    private Long id;
    private Long prestamoId;
    private BigDecimal monto;
    private LocalDate fechaGeneracion;
    private String estado;
    private LocalDate fechaPago;
}
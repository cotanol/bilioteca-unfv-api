package com.unfv.biblioteca.bibliotecaapi.reserva.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservaDetalleDTO {

    private Long id;
    private String tituloMaterial;
    private LocalDateTime fechaReserva;
    private String estado;
}
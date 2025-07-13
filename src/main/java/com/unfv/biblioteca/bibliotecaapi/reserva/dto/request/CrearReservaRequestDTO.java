package com.unfv.biblioteca.bibliotecaapi.reserva.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearReservaRequestDTO {

    @NotNull
    private Long usuarioId;

    @NotNull
    private Long materialId;
}
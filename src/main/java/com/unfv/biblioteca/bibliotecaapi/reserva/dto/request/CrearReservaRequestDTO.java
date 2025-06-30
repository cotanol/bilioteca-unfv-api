package com.unfv.biblioteca.bibliotecaapi.reserva.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CrearReservaRequestDTO {

    @NotNull(message = "El ID del material no puede ser nulo")
    @Positive(message = "El ID del material debe ser un número positivo")
    private Long materialId;

    @NotNull(message = "El ID del usuario no puede ser nulo")
    @Positive(message = "El ID del usuario debe ser un número positivo")
    private Long usuarioId;
}


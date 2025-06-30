package com.unfv.biblioteca.bibliotecaapi.circulacion.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CrearPrestamoRequestDTO {

    @NotNull(message = "El ID del ejemplar no puede ser nulo")
    @Positive(message = "El ID del ejemplar debe ser un número positivo")
    private Long ejemplarId;

    @NotNull(message = "El ID del usuario no puede ser nulo")
    @Positive(message = "El ID del usuario debe ser un número positivo")
    private Long usuarioId;
}


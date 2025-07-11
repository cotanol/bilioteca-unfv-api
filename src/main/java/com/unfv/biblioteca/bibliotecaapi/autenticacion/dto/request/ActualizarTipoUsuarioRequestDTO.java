package com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarTipoUsuarioRequestDTO {

    @NotBlank(message = "El nombre del tipo de usuario no puede estar vacío.")
    @Size(max = 50, message = "El nombre del tipo de usuario no puede tener más de 50 caracteres.")
    private String nombreTipo;

    @NotNull(message = "El límite de préstamos no puede ser nulo.")
    @Positive(message = "El límite de préstamos debe ser un número positivo.")
    private Integer limitePrestamos;

    @NotNull(message = "Los días de préstamo no pueden ser nulos.")
    @Positive(message = "Los días de préstamo deben ser un número positivo.")
    private Integer diasPrestamos;
}

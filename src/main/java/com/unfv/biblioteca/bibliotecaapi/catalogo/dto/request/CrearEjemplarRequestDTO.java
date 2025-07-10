package com.unfv.biblioteca.bibliotecaapi.catalogo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CrearEjemplarRequestDTO {

    @NotNull(message = "El ID del material no puede ser nulo.")
    private Long materialId;

    @NotBlank(message = "El código de barras no puede estar vacío.")
    @Size(max = 50, message = "El código de barras no puede tener más de 50 caracteres.")
    private String codigoBarras;

    @NotNull(message = "El ID de la ubicación no puede ser nulo.")
    private Long ubicacionId;

    @NotBlank(message = "El estado no puede estar vacío.")
    @Size(max = 50, message = "El estado no puede tener más de 50 caracteres.")
    private String estado;

    private LocalDate fechaAdquisicion;
}

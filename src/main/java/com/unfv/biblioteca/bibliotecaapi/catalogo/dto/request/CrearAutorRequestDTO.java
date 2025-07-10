package com.unfv.biblioteca.bibliotecaapi.catalogo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CrearAutorRequestDTO {

    @NotBlank(message = "El nombre completo del autor no puede estar vacío")
    private String nombreCompleto;

    private String nacionalidad;
}

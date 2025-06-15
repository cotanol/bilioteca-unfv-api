package com.unfv.biblioteca.bibliotecaapi.libro.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActualizarLibroDTO {
    @NotBlank(message = "El titulo no puede estar vacio")
    private String titulo;

    @NotBlank(message = "El autor no puede estar vacio")
    private String autor;
}

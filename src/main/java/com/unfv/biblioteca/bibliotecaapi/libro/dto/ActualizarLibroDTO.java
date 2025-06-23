package com.unfv.biblioteca.bibliotecaapi.libro.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ActualizarLibroDTO {
    // Se quita @NotBlank. Ahora es opcional.
    // @Size es una buena validación opcional: si el campo viene, valida su tamaño.
    @Size(min = 2, max = 100)
    private String titulo;

    // Se quita @NotBlank. Ahora es opcional.
    @Size(min = 2, max = 100)
    private String autor;
}

package com.unfv.biblioteca.bibliotecaapi.libro.dto;

import lombok.Data;

@Data
public class LibroDTO {
    // Esto seria la respuesta o response
    private Long id;
    private String titulo;
    private String autor;
}

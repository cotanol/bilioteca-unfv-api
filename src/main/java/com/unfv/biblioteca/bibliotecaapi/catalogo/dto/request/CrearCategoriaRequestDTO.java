package com.unfv.biblioteca.bibliotecaapi.catalogo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CrearCategoriaRequestDTO {

    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    private String nombreCategoria;

    private String codigoDewey;
}

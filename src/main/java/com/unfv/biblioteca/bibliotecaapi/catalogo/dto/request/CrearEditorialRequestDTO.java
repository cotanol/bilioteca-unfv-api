package com.unfv.biblioteca.bibliotecaapi.catalogo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CrearEditorialRequestDTO {

    @NotBlank(message = "El nombre de la editorial no puede estar vacío")
    private String nombre;

    private String pais;
}

package com.unfv.biblioteca.bibliotecaapi.catalogo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Set;

@Data
public class CrearMaterialRequestDTO {

    private String isbn;

    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;

    private String subtitulo;

    @NotNull(message = "El ID de la editorial no puede ser nulo")
    @Positive(message = "El ID de la editorial debe ser un número positivo")
    private Long editorialId;

    private String edicion;

    private Integer anioPublicacion;

    private Integer numeroPaginas;

    @NotBlank(message = "El tipo de material no puede estar vacío")
    private String tipoMaterial;

    private String resumen;

    @NotEmpty(message = "Debe proporcionar al menos un autor")
    private Set<@NotNull @Positive Long> autoresIds;

    @NotEmpty(message = "Debe proporcionar al menos una categoría")
    private Set<@NotNull @Positive Long> categoriasIds;
}


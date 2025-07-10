package com.unfv.biblioteca.bibliotecaapi.catalogo.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class ActualizarMaterialRequestDTO {

    private String titulo;
    private String subtitulo;
    private String edicion;
    private Integer anioPublicacion;
    private Integer numeroPaginas;
    private String tipoMaterial;
    private String resumen;
    private String urlImagen;

    @NotNull(message = "El ID de la editorial no puede ser nulo")
    private Long editorialId;

    @NotEmpty(message = "Debe proporcionar al menos un ID de autor")
    private Set<Long> autoresIds;

    @NotEmpty(message = "Debe proporcionar al menos un ID de categoría")
    private Set<Long> categoriasIds;
}
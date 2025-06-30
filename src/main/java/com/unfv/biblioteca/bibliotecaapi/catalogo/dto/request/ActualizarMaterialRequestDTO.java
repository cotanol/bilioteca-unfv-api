package com.unfv.biblioteca.bibliotecaapi.catalogo.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.util.Set;

@Data
public class ActualizarMaterialRequestDTO {
    // Todos los campos son opcionales. El cliente solo envía lo que quiere cambiar.
    // Los tipos de datos objeto (String, Integer) pueden ser nulos por defecto.

    // El ISBN rara vez se actualiza, pero podríamos permitirlo.
    private String isbn;

    // El título sí es comúnmente actualizable.
    private String titulo;

    private String subtitulo;

    @Positive(message = "El ID de la editorial debe ser un número positivo")
    private Long editorialId;

    private String edicion;

    private Integer anioPublicacion;

    private Integer numeroPaginas;

    private String tipoMaterial;

    private String resumen;

    // Podríamos querer permitir actualizar las listas completas
    private Set<@Positive Long> autoresIds;

    private Set<@Positive Long> categoriasIds;
}

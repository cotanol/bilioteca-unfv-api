package com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class MaterialResponseDTO {

    private Long id;
    private String isbn;
    private String titulo;
    private String subtitulo;
    private String editorial;
    private String edicion;
    private Integer anioPublicacion;
    private Integer numeroPaginas;
    private String tipoMaterial;
    private String resumen;
    private String urlImagen;
    private Set<String> autores;
    private Set<String> categorias;
}


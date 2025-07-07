package com.unfv.biblioteca.bibliotecaapi.catalogo.mapper;

import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.*;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.request.ActualizarMaterialRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.request.CrearMaterialRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.EjemplarDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.MaterialDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.UbicacionDetalleDTO;
import org.mapstruct.*;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CatalogoMapper {

    // =================================================================
    // MÉTODOS DE ENTIDAD A DTO (PARA RESPUESTAS DE LA API)
    // =================================================================

    @Mapping(source = "editorial.nombre", target = "editorial")
    @Mapping(source = "autores", target = "autores", qualifiedByName = "autoresToNombres")
    @Mapping(source = "categorias", target = "categorias", qualifiedByName = "categoriasToNombres")
    @Mapping(source = "urlImagen", target = "urlImagen")
    MaterialDetalleDTO toMaterialDetalleDTO(Material material);

    EjemplarDetalleDTO toEjemplarDetalleDTO(Ejemplar ejemplar);

    UbicacionDetalleDTO toUbicacionDetalleDTO(Ubicacion ubicacion);


    // =================================================================
    // MÉTODOS DE DTO A ENTIDAD (PARA PETICIONES A LA API)
    // =================================================================

    /**
     * Convierte un DTO de creación a una nueva entidad Material.
     * Ignoramos el ID (porque es nuevo) y las relaciones, ya que
     * el Servicio se encargará de buscarlas por ID y asignarlas.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "editorial", ignore = true)
    @Mapping(target = "autores", ignore = true)
    @Mapping(target = "categorias", ignore = true)
    @Mapping(source = "urlImagen", target = "urlImagen")
    Material toMaterial(CrearMaterialRequestDTO dto);


    /**
     * Actualiza una entidad Material existente a partir de un DTO de actualización.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "editorial", ignore = true)
    @Mapping(target = "autores", ignore = true)
    @Mapping(target = "categorias", ignore = true)
    void updateMaterialFromDto(ActualizarMaterialRequestDTO dto, @MappingTarget Material material);


    // =================================================================
    // MÉTODOS DE AYUDA (HELPERS)
    // =================================================================

    @Named("autoresToNombres")
    default Set<String> mapAutoresToNombres(Set<Autor> autores) {
        if (autores == null || autores.isEmpty()) {
            return Collections.emptySet();
        }
        return autores.stream().map(Autor::getNombreCompleto).collect(Collectors.toSet());
    }

    @Named("categoriasToNombres")
    default Set<String> mapCategoriasToNombres(Set<Categoria> categorias) {
        if (categorias == null || categorias.isEmpty()) {
            return Collections.emptySet();
        }
        return categorias.stream().map(Categoria::getNombreCategoria).collect(Collectors.toSet());
    }
}
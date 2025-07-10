package com.unfv.biblioteca.bibliotecaapi.catalogo.mapper;

import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.*;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.request.*;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.*;
import org.mapstruct.*;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CatalogoMapper {

    // =================================================================
    // MAPPERS PARA MATERIAL
    // =================================================================

    @Mapping(source = "editorial.nombre", target = "editorial")
    @Mapping(source = "autores", target = "autores", qualifiedByName = "autoresToNombres")
    @Mapping(source = "categorias", target = "categorias", qualifiedByName = "categoriasToNombres")
    @Mapping(source = "urlImagen", target = "urlImagen")
    MaterialDetalleDTO toMaterialDetalleDTO(Material material);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "editorial", ignore = true)
    @Mapping(target = "autores", ignore = true)
    @Mapping(target = "categorias", ignore = true)
    @Mapping(source = "urlImagen", target = "urlImagen")
    Material toMaterial(CrearMaterialRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "editorial", ignore = true)
    @Mapping(target = "autores", ignore = true)
    @Mapping(target = "categorias", ignore = true)
    void updateMaterialFromDto(ActualizarMaterialRequestDTO dto, @MappingTarget Material material);

    // =================================================================
    // MAPPERS PARA AUTOR
    // =================================================================

    AutorResponseDTO toAutorResponseDTO(Autor autor);

    @Mapping(target = "id", ignore = true)
    Autor toAutor(CrearAutorRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateAutorFromDto(ActualizarAutorRequestDTO dto, @MappingTarget Autor autor);

    // =================================================================
    // MAPPERS PARA CATEGORIA
    // =================================================================

    CategoriaResponseDTO toCategoriaResponseDTO(Categoria categoria);

    @Mapping(target = "id", ignore = true)
    Categoria toCategoria(CrearCategoriaRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateCategoriaFromDto(ActualizarCategoriaRequestDTO dto, @MappingTarget Categoria categoria);

    // =================================================================
    // MAPPERS PARA EDITORIAL
    // =================================================================

    EditorialResponseDTO toEditorialResponseDTO(Editorial editorial);

    @Mapping(target = "id", ignore = true)
    Editorial toEditorial(CrearEditorialRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEditorialFromDto(ActualizarEditorialRequestDTO dto, @MappingTarget Editorial editorial);


    // =================================================================
    // MAPPERS PARA OTROS DOMINIOS
    // =================================================================

    EjemplarDetalleDTO toEjemplarDetalleDTO(Ejemplar ejemplar);

    UbicacionDetalleDTO toUbicacionDetalleDTO(Ubicacion ubicacion);


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

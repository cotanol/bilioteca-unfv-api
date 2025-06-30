package com.unfv.biblioteca.bibliotecaapi.autenticacion.mapper;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.Usuario;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.UsuarioDetalleDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    /**
     * Convierte una entidad Usuario a su DTO de detalle.
     * Es crucial que el passwordHash NUNCA se incluya en el DTO.
     * MapStruct se encarga de no mapearlo porque no existe un campo
     * correspondiente en el DTO.
     */
    @Mapping(source = "tipoUsuario.nombreTipo", target = "tipoUsuario") // Aplanamos la entidad TipoUsuario
    UsuarioDetalleDTO toUsuarioDetalleDTO(Usuario usuario);

    // Nota: El mapeo de CrearUsuarioRequestDTO a la entidad Usuario se
    // manejará principalmente en el servicio debido a la necesidad de
    // hashear la contraseña y buscar el TipoUsuario en la BD.
    // No es necesario añadir un metodo aquí por ahora para mantenerlo simple.

}

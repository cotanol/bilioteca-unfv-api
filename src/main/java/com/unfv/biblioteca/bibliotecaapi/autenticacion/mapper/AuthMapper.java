package com.unfv.biblioteca.bibliotecaapi.autenticacion.mapper;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.TipoUsuario;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.Usuario;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.ActualizarTipoUsuarioRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.ActualizarUsuarioRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.CrearTipoUsuarioRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.TipoUsuarioResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.UsuarioDetalleDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    // Mapeos de Usuario
    @Mapping(source = "tipoUsuario.nombreTipo", target = "tipoUsuario")
    UsuarioDetalleDTO toUsuarioDetalleDTO(Usuario usuario);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigoUniversitario", ignore = true)
    @Mapping(target = "dni", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "tipoUsuario", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    void updateUserFromDto(ActualizarUsuarioRequestDTO dto, @MappingTarget Usuario usuario);

    // Mapeos de TipoUsuario
    TipoUsuarioResponseDTO toTipoUsuarioResponseDTO(TipoUsuario tipoUsuario);

    @Mapping(target = "id", ignore = true)
    TipoUsuario toTipoUsuario(CrearTipoUsuarioRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateTipoUsuarioFromDto(ActualizarTipoUsuarioRequestDTO dto, @MappingTarget TipoUsuario tipoUsuario);
}
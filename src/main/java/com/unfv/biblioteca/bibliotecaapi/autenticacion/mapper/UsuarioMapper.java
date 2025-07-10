package com.unfv.biblioteca.bibliotecaapi.autenticacion.mapper;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.Usuario;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.UsuarioDetalleDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {


    @Mapping(source = "tipoUsuario.nombreTipo", target = "tipoUsuario") // Aplanamos la entidad TipoUsuario
    UsuarioDetalleDTO toUsuarioDetalleDTO(Usuario usuario);



}

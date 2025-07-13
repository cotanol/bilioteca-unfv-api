package com.unfv.biblioteca.bibliotecaapi.reserva.mapper;

import com.unfv.biblioteca.bibliotecaapi.reserva.domain.Reserva;
import com.unfv.biblioteca.bibliotecaapi.reserva.dto.response.ReservaDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.reserva.dto.response.ReservaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservaMapper {

    @Mapping(source = "material.id", target = "materialId")
    @Mapping(source = "material.titulo", target = "materialNombre")
    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "usuario.nombres", target = "usuarioNombre")
    ReservaResponseDTO toDto(Reserva reserva);

    @Mapping(source = "material.titulo", target = "tituloMaterial")
    ReservaDetalleDTO toReservaDetalleDTO(Reserva reserva);
}
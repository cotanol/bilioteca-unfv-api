package com.unfv.biblioteca.bibliotecaapi.reserva.mapper;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.mapper.AuthMapper;

import com.unfv.biblioteca.bibliotecaapi.catalogo.mapper.CatalogoMapper;
import com.unfv.biblioteca.bibliotecaapi.reserva.domain.Reserva;
import com.unfv.biblioteca.bibliotecaapi.reserva.dto.response.ReservaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AuthMapper.class, CatalogoMapper.class})
public interface ReservaMapper {


    ReservaResponseDTO toDto(Reserva reserva);


}
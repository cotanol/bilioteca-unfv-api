package com.unfv.biblioteca.bibliotecaapi.reserva.mapper;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.mapper.UsuarioMapper;
import com.unfv.biblioteca.bibliotecaapi.catalogo.mapper.CatalogoMapper;
import com.unfv.biblioteca.bibliotecaapi.reserva.domain.Reserva;
import com.unfv.biblioteca.bibliotecaapi.reserva.dto.response.ReservaDetalleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CatalogoMapper.class, UsuarioMapper.class})
public interface ReservaMapper {

    /**
     * Convierte una entidad Reserva a su DTO de detalle.
     * Al igual que en CirculacionMapper, reutiliza los otros mappers
     * para convertir Material y Usuario.
     */
    ReservaDetalleDTO toReservaDetalleDTO(Reserva reserva);
}

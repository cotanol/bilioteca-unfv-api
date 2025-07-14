package com.unfv.biblioteca.bibliotecaapi.circulacion.mapper;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.mapper.AuthMapper;
import com.unfv.biblioteca.bibliotecaapi.catalogo.mapper.CatalogoMapper;
import com.unfv.biblioteca.bibliotecaapi.circulacion.domain.Multa;
import com.unfv.biblioteca.bibliotecaapi.circulacion.domain.Prestamo;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.MultaReponseDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.PrestamoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// Aquí le indicamos a MapStruct que puede usar otros mappers que ya hemos definido
@Mapper(componentModel = "spring", uses = {CatalogoMapper.class, AuthMapper.class})
public interface CirculacionMapper {

    /**
     * Convierte una entidad Prestamo a su DTO de detalle.
     * MapStruct reutilizará CatalogoMapper para convertir el Ejemplar
     * y UsuarioMapper para convertir el Usuario.
     */
    PrestamoResponseDTO toPrestamoDetalleDTO(Prestamo prestamo);

    /**
     * Convierte una entidad Multa a su DTO.
     * Mapeamos el ID del préstamo relacionado.
     */
    @Mapping(source = "prestamo.id", target = "prestamoId")
    MultaReponseDTO toMultaDTO(Multa multa);

}

package com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response;

import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.MultaDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.PrestamoDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.reserva.dto.response.ReservaResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PerfilUsuarioDTO {

    private UsuarioResponseDTO usuario;
    private List<PrestamoDetalleDTO> prestamosActivos;
    private List<ReservaResponseDTO> reservasActivas;
    private List<MultaDTO> multasPendientes;
}

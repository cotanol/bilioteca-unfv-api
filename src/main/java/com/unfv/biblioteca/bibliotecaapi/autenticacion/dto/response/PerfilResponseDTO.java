package com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response;

import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.MultaDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.PrestamoDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.reserva.dto.response.ReservaDetalleDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PerfilResponseDTO {

    private UsuarioDetalleDTO datosUsuario;
    private List<PrestamoDetalleDTO> prestamosActivos;
    private List<ReservaDetalleDTO> reservasActivas;
    private List<MultaDTO> multasPendientes;
}
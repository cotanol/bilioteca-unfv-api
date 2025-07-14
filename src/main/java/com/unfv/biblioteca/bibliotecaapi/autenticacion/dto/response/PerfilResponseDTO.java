package com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response;

import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.MultaReponseDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.PrestamoResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.reserva.dto.response.ReservaResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PerfilResponseDTO {

    private UsuarioResponseDTO datosUsuario;
    private List<PrestamoResponseDTO> prestamosActivos;
    private List<ReservaResponseDTO> reservasActivas;
    private List<MultaReponseDTO> multasPendientes;
}
package com.unfv.biblioteca.bibliotecaapi.reserva.dto.response;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.UsuarioDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.MaterialDetalleDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservaDetalleDTO {

    private Long id;
    private LocalDateTime fechaReserva;
    private String estado;
    private UsuarioDetalleDTO usuario;
    private MaterialDetalleDTO material;
}


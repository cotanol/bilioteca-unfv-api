package com.unfv.biblioteca.bibliotecaapi.reserva.dto.response;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.UsuarioResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservaResponseDTO {

    private Long id;
    private Long materialId;
    private String materialNombre;
    private Long usuarioId;
    private String usuarioNombre;
    private LocalDateTime fechaReserva;
    private String estado;
    private UsuarioResponseDTO usuario;
}

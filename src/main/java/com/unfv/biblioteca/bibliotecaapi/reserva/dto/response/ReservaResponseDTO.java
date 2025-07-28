package com.unfv.biblioteca.bibliotecaapi.reserva.dto.response;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.UsuarioResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.EjemplarResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.MaterialResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservaResponseDTO {

    private Long id;
    private LocalDateTime fechaReserva;
    private String estado;


    private UsuarioResponseDTO usuario;
    private MaterialResponseDTO material;
    private EjemplarResponseDTO ejemplar; // Puede ser nulo si la reserva está en lista de espera
}

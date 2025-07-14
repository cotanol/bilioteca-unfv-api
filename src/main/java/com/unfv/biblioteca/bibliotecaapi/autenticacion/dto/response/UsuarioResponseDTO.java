package com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UsuarioResponseDTO {

    private Long id;
    private String codigoUniversitario;
    private String dni;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private String telefono;
    private String tipoUsuario;
    private String estado;
    private LocalDateTime fechaRegistro;
}


package com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request;

import lombok.Data;

@Data
public class CrearUsuarioRequestDTO {

    private String codigoUniversitario;
    private String dni;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private String telefono;
    private String password;
    private Long tipoUsuarioId;
}

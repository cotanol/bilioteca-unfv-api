package com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String codigoUniversitario;
    private String password;
}


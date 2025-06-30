package com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "El código universitario no puede estar vacío")
    private String codigoUniversitario;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
}


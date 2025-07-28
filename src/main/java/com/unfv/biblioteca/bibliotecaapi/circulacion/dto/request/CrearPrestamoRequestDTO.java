package com.unfv.biblioteca.bibliotecaapi.circulacion.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CrearPrestamoRequestDTO {

    @NotBlank(message = "El codigo de barras del ejemplar no puede estar vacio")
    @Size(max = 50, message = "El código de barras no puede tener más de 50 caracteres.")
    private String codigoBarras;

    @NotBlank(message = "El codigo universitario no puede estar vacio")
    @Size(max = 20, message = "El código universitario no puede tener más de 20 caracteres.")
    private String codigoUniversitario;
}


package com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarEstadoUsuarioRequestDTO {

    @NotBlank(message = "El estado no puede estar vacío")
    private String estado;
}

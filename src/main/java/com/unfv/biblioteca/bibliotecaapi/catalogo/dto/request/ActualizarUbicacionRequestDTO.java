package com.unfv.biblioteca.bibliotecaapi.catalogo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ActualizarUbicacionRequestDTO {

    @NotBlank(message = "La facultad no puede estar vacía.")
    @Size(max = 100, message = "La facultad no puede tener más de 100 caracteres.")
    private String facultad;

    @Size(max = 100, message = "El nombre de la biblioteca no puede tener más de 100 caracteres.")
    private String bibliotecaNombre;

    private Integer piso;

    @Size(max = 20, message = "El estante no puede tener más de 20 caracteres.")
    private String estante;
}

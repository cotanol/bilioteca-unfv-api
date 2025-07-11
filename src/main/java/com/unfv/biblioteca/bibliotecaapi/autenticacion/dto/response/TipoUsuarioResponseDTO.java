package com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoUsuarioResponseDTO {

    private Long id;
    private String nombreTipo;
    private Integer limitePrestamos;
    private Integer diasPrestamos;
}

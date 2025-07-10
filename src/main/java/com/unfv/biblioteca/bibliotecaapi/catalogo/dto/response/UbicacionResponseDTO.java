package com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionResponseDTO {

    private Long id;
    private String facultad;
    private String bibliotecaNombre;
    private Integer piso;
    private String estante;
}

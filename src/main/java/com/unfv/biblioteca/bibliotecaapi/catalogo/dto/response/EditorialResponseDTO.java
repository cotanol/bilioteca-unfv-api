package com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditorialResponseDTO {

    private Long id;
    private String nombre;
    private String pais;
}

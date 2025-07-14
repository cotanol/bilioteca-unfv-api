package com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EjemplarResponseDTO {

    private Long id;
    private String codigoBarras;
    private String estado;
    private LocalDate fechaAdquisicion;
    private MaterialResponseDTO material;
    private UbicacionResponseDTO ubicacion;
}

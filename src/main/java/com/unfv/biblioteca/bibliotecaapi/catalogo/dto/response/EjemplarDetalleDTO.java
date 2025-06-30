package com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class EjemplarDetalleDTO {

    private Long id;
    private String codigoBarras;
    private String estado;
    private LocalDate fechaAdquisicion;
    private UbicacionDetalleDTO ubicacion;
    private MaterialDetalleDTO material;
}


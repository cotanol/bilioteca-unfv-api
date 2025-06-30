package com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UbicacionDetalleDTO {
    private String facultad;
    private String bibliotecaNombre;
    private Integer piso;
    private String estante;
}

package com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DashboardStatsDTO {
    private long cantidadAutores;
    private long cantidadCategorias;
    private long cantidadEditoriales;
    private long cantidadMateriales;
    private long cantidadUbicaciones;
    private long cantidadEjemplares;
    private long cantidadUsuarios;
    private long cantidadPrestamos;
    private long cantidadReservas;
    private long cantidadMultas;

}

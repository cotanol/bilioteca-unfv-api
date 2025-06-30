package com.unfv.biblioteca.bibliotecaapi.circulacion.dto.request;

import lombok.Data;

@Data
public class CrearPrestamoRequestDTO {

    private Long ejemplarId;
    private Long usuarioId;
}


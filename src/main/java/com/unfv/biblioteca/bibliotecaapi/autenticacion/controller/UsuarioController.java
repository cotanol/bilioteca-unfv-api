package com.unfv.biblioteca.bibliotecaapi.autenticacion.controller;

import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.MultaDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.service.MultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final MultaService multaService;

    @GetMapping("/{id}/multas")
    public ResponseEntity<List<MultaDTO>> obtenerMultasPorUsuario(@PathVariable Long id) {
        List<MultaDTO> multas = multaService.buscarMultasPendientesPorUsuario(id);
        return ResponseEntity.ok(multas);
    }
}

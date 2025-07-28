package com.unfv.biblioteca.bibliotecaapi.circulacion.controller;


import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.AutorResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.MultaReponseDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.service.MultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/multas")
@RequiredArgsConstructor
public class MultaController {

    private final MultaService multaService;

    @GetMapping
    public ResponseEntity<List<MultaReponseDTO>> listarOFiltrarMultas(
            @RequestParam(required = false) Long usuarioId) {

        List<MultaReponseDTO> multas;

        if (usuarioId != null) {
            // Si se provee el usuarioId, se buscan sus multas pendientes.
            multas = multaService.buscarMultasPendientesPorUsuario(usuarioId);
        } else {
            // Si no, se listan todas las multas del sistema.
            multas = multaService.findAllMultas();
        }

        return ResponseEntity.ok(multas);
    }

    /**
     * Endpoint de ACCIÓN para registrar el pago de una multa.
     * HTTP Method: POST
     * URL: /api/multas/{id}/pago
     */
    @PutMapping("/{id}/pagar")
    public ResponseEntity<MultaReponseDTO> registrarPagoDeMulta(@PathVariable Long id) {
        MultaReponseDTO multaPagada = multaService.registrarPagoDeMulta(id);
        return ResponseEntity.ok(multaPagada);
    }
}
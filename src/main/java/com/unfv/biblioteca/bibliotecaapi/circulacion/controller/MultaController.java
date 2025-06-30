package com.unfv.biblioteca.bibliotecaapi.circulacion.controller;

import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.MultaDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.service.MultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/multas")
@RequiredArgsConstructor
public class MultaController {

    private final MultaService multaService;

    /**
     * Endpoint para buscar las multas pendientes de un usuario.
     * HTTP Method: GET
     * URL: /api/multas?usuarioId=1
     */
    @GetMapping
    public ResponseEntity<List<MultaDTO>> buscarMultasPendientesPorUsuario(@RequestParam Long usuarioId) {
        List<MultaDTO> multas = multaService.buscarMultasPendientesPorUsuario(usuarioId);
        return ResponseEntity.ok(multas);
    }

    /**
     * Endpoint de ACCIÓN para registrar el pago de una multa.
     * HTTP Method: POST
     * URL: /api/multas/{id}/pago
     */
    @PostMapping("/{id}/pago")
    public ResponseEntity<MultaDTO> registrarPagoDeMulta(@PathVariable Long id) {
        MultaDTO multaPagada = multaService.registrarPagoDeMulta(id);
        return ResponseEntity.ok(multaPagada);
    }
}
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
    public ResponseEntity<List<MultaReponseDTO>> listarMultas() {
        return ResponseEntity.ok(multaService.findAllMultas());
    }

    /**
     * Endpoint para buscar las multas pendientes de un usuario.
     * HTTP Method: GET
     * URL: /api/multas?usuarioId=1
     */
//    @GetMapping
//    public ResponseEntity<List<MultaReponseDTO>> buscarMultasPendientesPorUsuario(@RequestParam Long usuarioId) {
//        List<MultaReponseDTO> multas = multaService.buscarMultasPendientesPorUsuario(usuarioId);
//        return ResponseEntity.ok(multas);
//    }

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
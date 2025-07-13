package com.unfv.biblioteca.bibliotecaapi.circulacion.controller;

import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.request.CrearPrestamoRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.PrestamoDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.service.PrestamoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prestamos")
@RequiredArgsConstructor
public class PrestamoController {

    private final PrestamoService prestamoService;

    @PostMapping
    public ResponseEntity<PrestamoDetalleDTO> crearPrestamo(@Valid @RequestBody CrearPrestamoRequestDTO request) {
        PrestamoDetalleDTO prestamoCreado = prestamoService.crearPrestamo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoCreado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoDetalleDTO> buscarPrestamoPorId(@PathVariable Long id) {
        PrestamoDetalleDTO prestamoEncontrado = prestamoService.buscarPrestamoPorId(id);
        return ResponseEntity.ok(prestamoEncontrado);
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<PrestamoDetalleDTO> registrarDevolucion(@PathVariable Long id) {
        PrestamoDetalleDTO prestamoDevuelto = prestamoService.registrarDevolucion(id);
        return ResponseEntity.ok(prestamoDevuelto);
    }

    @PutMapping("/{id}/renovar")
    public ResponseEntity<PrestamoDetalleDTO> renovarPrestamo(@PathVariable Long id) {
        PrestamoDetalleDTO prestamoRenovado = prestamoService.renovarPrestamo(id);
        return ResponseEntity.ok(prestamoRenovado);
    }
}
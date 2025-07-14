package com.unfv.biblioteca.bibliotecaapi.circulacion.controller;

import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.request.CrearPrestamoRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.MultaReponseDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.PrestamoResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.service.PrestamoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prestamos")
@RequiredArgsConstructor
public class PrestamoController {

    private final PrestamoService prestamoService;

    @GetMapping
    public ResponseEntity<List<PrestamoResponseDTO>> listarPrestamos() {
        return ResponseEntity.ok(prestamoService.findAllPrestamos());
    }

    @PostMapping
    public ResponseEntity<PrestamoResponseDTO> crearPrestamo(@Valid @RequestBody CrearPrestamoRequestDTO request) {
        PrestamoResponseDTO prestamoCreado = prestamoService.crearPrestamo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoCreado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponseDTO> buscarPrestamoPorId(@PathVariable Long id) {
        PrestamoResponseDTO prestamoEncontrado = prestamoService.buscarPrestamoPorId(id);
        return ResponseEntity.ok(prestamoEncontrado);
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<PrestamoResponseDTO> registrarDevolucion(@PathVariable Long id) {
        PrestamoResponseDTO prestamoDevuelto = prestamoService.registrarDevolucion(id);
        return ResponseEntity.ok(prestamoDevuelto);
    }

    @PutMapping("/{id}/renovar")
    public ResponseEntity<PrestamoResponseDTO> renovarPrestamo(@PathVariable Long id) {
        PrestamoResponseDTO prestamoRenovado = prestamoService.renovarPrestamo(id);
        return ResponseEntity.ok(prestamoRenovado);
    }

}
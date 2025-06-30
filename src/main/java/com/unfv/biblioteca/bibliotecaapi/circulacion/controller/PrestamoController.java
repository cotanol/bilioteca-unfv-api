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

    /**
     * Endpoint para crear un nuevo préstamo.
     * HTTP Method: POST
     * URL: /api/prestamos
     */
    @PostMapping
    public ResponseEntity<PrestamoDetalleDTO> crearPrestamo(@Valid @RequestBody CrearPrestamoRequestDTO request) {
        PrestamoDetalleDTO prestamoCreado = prestamoService.crearPrestamo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoCreado);
    }

    /**
     * Endpoint para buscar un préstamo por su ID.
     * HTTP Method: GET
     * URL: /api/prestamos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PrestamoDetalleDTO> buscarPrestamoPorId(@PathVariable Long id) {
        PrestamoDetalleDTO prestamoEncontrado = prestamoService.buscarPrestamoPorId(id);
        return ResponseEntity.ok(prestamoEncontrado);
    }

    // --- Endpoints de Acción (Futuros) ---
    // Como discutimos, estos endpoints representan acciones de negocio.
    // Los dejo aquí como plantilla para cuando implementes la lógica en tu servicio.
    /*
    @PostMapping("/{id}/devolucion")
    public ResponseEntity<?> registrarDevolucion(@PathVariable Long id) {
        prestamoService.registrarDevolucion(id); // Este metodo devolvería void o el DTO actualizado
        return ResponseEntity.ok().build(); // Devuelve un 200 OK sin cuerpo
    }
    */
}
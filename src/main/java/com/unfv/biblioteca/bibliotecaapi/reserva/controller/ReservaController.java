package com.unfv.biblioteca.bibliotecaapi.reserva.controller;

import com.unfv.biblioteca.bibliotecaapi.reserva.dto.request.CrearReservaRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.reserva.dto.response.ReservaDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.reserva.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Para el ejemplo de seguridad, necesitarías este import
// import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    /**
     * Endpoint para crear una nueva reserva.
     * HTTP Method: POST
     * URL: /api/reservas
     */
    @PostMapping
    public ResponseEntity<ReservaDetalleDTO> crearReserva(@Valid @RequestBody CrearReservaRequestDTO request) {
        ReservaDetalleDTO reservaCreada = reservaService.crearReserva(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaCreada);
    }

    /**
     * Endpoint de ACCIÓN para cancelar una reserva.
     * HTTP Method: DELETE
     * URL: /api/reservas/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id) {
        // En un sistema con seguridad real, obtendríamos el ID del usuario del token.
        // Por ahora, para la demo, podríamos "quemar" un ID de usuario o pasarlo como cabecera.
        // Ejemplo conceptual con seguridad:
        // Authentication authentication -> Este objeto lo inyecta Spring Security
        // Long usuarioLogueadoId = ((UserDetailsPersonalizado) authentication.getPrincipal()).getId();

        // Para que tu demo funcione mañana, vamos a asumir un ID de usuario fijo por ahora.
        // ¡Recuerda que esto se debe reemplazar con la seguridad real!
        Long usuarioLogueadoId = 1L; // <<-- VALOR FIJO SOLO PARA PRUEBAS

        reservaService.cancelarReserva(id, usuarioLogueadoId);

        // Un DELETE exitoso que no devuelve contenido debe retornar 204 No Content.
        return ResponseEntity.noContent().build();
    }

    // --- Aquí irían otros endpoints ---
    // Por ejemplo: GET /api/reservas/usuario/{usuarioId} para listar las reservas de un usuario.
}
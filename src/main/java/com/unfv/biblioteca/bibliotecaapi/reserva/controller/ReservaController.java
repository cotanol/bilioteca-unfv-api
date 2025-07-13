package com.unfv.biblioteca.bibliotecaapi.reserva.controller;

import com.unfv.biblioteca.bibliotecaapi.reserva.dto.request.CrearReservaRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.reserva.dto.response.ReservaResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.reserva.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crearReserva(@Valid @RequestBody CrearReservaRequestDTO request) {
        ReservaResponseDTO reservaCreada = reservaService.crearReserva(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaCreada);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id) {
        reservaService.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }
}

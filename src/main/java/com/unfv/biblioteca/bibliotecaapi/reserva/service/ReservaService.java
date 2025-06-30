package com.unfv.biblioteca.bibliotecaapi.reserva.service;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.Usuario;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.repository.UsuarioRepository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Material;
import com.unfv.biblioteca.bibliotecaapi.catalogo.repository.EjemplarRepository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.repository.MaterialRepository;
import com.unfv.biblioteca.bibliotecaapi.circulacion.repository.PrestamoRepository;
import com.unfv.biblioteca.bibliotecaapi.reserva.domain.Reserva;
import com.unfv.biblioteca.bibliotecaapi.reserva.dto.request.CrearReservaRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.reserva.dto.response.ReservaDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.reserva.mapper.ReservaMapper;
import com.unfv.biblioteca.bibliotecaapi.reserva.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final MaterialRepository materialRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;
    private final EjemplarRepository exemplarRepository;
    private final ReservaMapper reservaMapper;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository,
                          MaterialRepository materialRepository,
                          UsuarioRepository usuarioRepository,
                          PrestamoRepository prestamoRepository,
                          EjemplarRepository exemplarRepository,
                          ReservaMapper reservaMapper) {
        this.reservaRepository = reservaRepository;
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepository = prestamoRepository;
        this.exemplarRepository = exemplarRepository;
        this.reservaMapper = reservaMapper;
    }

    @Transactional
    public ReservaDetalleDTO crearReserva(CrearReservaRequestDTO request) {
        // 1. Buscamos las entidades a partir de los IDs
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Material material = materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new RuntimeException("Material no encontrado"));

        // === 2. APLICAMOS LAS REGLAS DE NEGOCIO ===

        // Regla 1: El usuario debe estar activo
        if (!"Activo".equalsIgnoreCase(usuario.getEstado())) {
            throw new IllegalStateException("El usuario no se encuentra activo.");
        }

        // Regla 2: El usuario no puede reservar un material que ya tiene en préstamo
        if (prestamoRepository.existsByUsuarioIdAndEjemplar_MaterialIdAndEstado(usuario.getId(), material.getId(), "Activo")) {
            throw new IllegalStateException("No se puede reservar un material que ya tiene en préstamo.");
        }

        // Regla 3: El usuario no puede tener una reserva activa para el mismo material
        if (reservaRepository.existsByUsuarioIdAndMaterialIdAndEstado(usuario.getId(), material.getId(), "Activa")) {
            throw new IllegalStateException("Ya tiene una reserva activa para este material.");
        }

        // Regla 4: Solo se puede reservar si no hay ejemplares disponibles para préstamo inmediato
        long totalEjemplares = exemplarRepository.countByMaterialId(material.getId());
        long ejemplaresEnPrestamo = prestamoRepository.countByEjemplar_MaterialIdAndEstado(material.getId(), "Activo");

        if (totalEjemplares > ejemplaresEnPrestamo) {
            throw new IllegalStateException("Hay ejemplares disponibles de este material. No es necesario reservar, puede solicitar un préstamo directamente.");
        }

        // === 3. SI TODAS LAS REGLAS PASAN, SE CREA LA RESERVA ===

        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setUsuario(usuario);
        nuevaReserva.setMaterial(material);
        nuevaReserva.setEstado("Activa");
        nuevaReserva.setFechaReserva(LocalDateTime.now());

        Reserva reservaGuardada = reservaRepository.save(nuevaReserva);

        // 4. Devolvemos el DTO de respuesta
        return reservaMapper.toReservaDetalleDTO(reservaGuardada);
    }

    @Transactional
    public void cancelarReserva(Long reservaId, Long usuarioId) {
        // 1. Buscamos la reserva
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // 2. Regla de Seguridad: Solo el usuario que hizo la reserva (o un admin) puede cancelarla
        if (!reserva.getUsuario().getId().equals(usuarioId)) {
            // En un sistema real, también verificaríamos si el usuario tiene rol de ADMIN
            throw new SecurityException("No tiene permiso para cancelar esta reserva.");
        }

        // 3. Regla de Negocio: Solo se pueden cancelar reservas activas
        if (!"Activa".equalsIgnoreCase(reserva.getEstado())) {
            throw new IllegalStateException("Solo se pueden cancelar reservas en estado 'Activa'.");
        }

        // 4. Actualizamos el estado
        reserva.setEstado("Cancelada");
        reservaRepository.save(reserva);
    }



}

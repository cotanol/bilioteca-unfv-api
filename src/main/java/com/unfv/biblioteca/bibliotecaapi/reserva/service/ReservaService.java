package com.unfv.biblioteca.bibliotecaapi.reserva.service;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.Usuario;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.repository.UsuarioRepository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Material;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.AutorResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.repository.EjemplarRepository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.repository.MaterialRepository;
import com.unfv.biblioteca.bibliotecaapi.circulacion.repository.PrestamoRepository;
import com.unfv.biblioteca.bibliotecaapi.reserva.domain.Reserva;
import com.unfv.biblioteca.bibliotecaapi.reserva.dto.request.CrearReservaRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.reserva.dto.response.ReservaResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.reserva.mapper.ReservaMapper;
import com.unfv.biblioteca.bibliotecaapi.reserva.repository.ReservaRepository;
import com.unfv.biblioteca.bibliotecaapi.shared.exception.BusinessRuleException;
import com.unfv.biblioteca.bibliotecaapi.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MaterialRepository materialRepository;
    private final PrestamoRepository prestamoRepository;
    private final EjemplarRepository ejemplarRepository;
    private final ReservaMapper reservaMapper;

    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> findAllReservas() {
        return reservaRepository.findAll().stream()
                .map(reservaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservaResponseDTO crearReserva(CrearReservaRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Material material = materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException("Material no encontrado"));

        // REGLA 0: Verificar que existan ejemplares físicos del material
        long totalEjemplares = ejemplarRepository.countByMaterialId(material.getId());

        if (totalEjemplares == 0) {
            throw new BusinessRuleException("No se puede reservar. La biblioteca no posee ningún ejemplar de este material.");
        }

        // Regla 1: El usuario no puede reservar un material que ya tiene en préstamo
        if (prestamoRepository.existsByUsuarioIdAndEjemplar_MaterialIdAndEstado(usuario.getId(), material.getId(), "Activo")) {
            throw new BusinessRuleException("No puede reservar un material que ya tiene en préstamo.");
        }

        // Regla 2: No se puede reservar si hay ejemplares disponibles
        long ejemplaresDisponibles = ejemplarRepository.countByMaterialIdAndEstado(material.getId(), "Disponible");
        if (ejemplaresDisponibles > 0) {
            throw new BusinessRuleException("No se puede reservar, aún hay ejemplares disponibles para préstamo.");
        }

        Reserva nuevaReserva = Reserva.builder()
                .usuario(usuario)
                .material(material)
                .fechaReserva(LocalDateTime.now())
                .estado("Activa")
                .build();

        Reserva reservaGuardada = reservaRepository.save(nuevaReserva);
        return reservaMapper.toDto(reservaGuardada);
    }

    @Transactional
    public void cancelarReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        if (!"Activa".equals(reserva.getEstado())) {
            throw new BusinessRuleException("Solo se pueden cancelar reservas activas.");
        }

        reserva.setEstado("Cancelada");
        reservaRepository.save(reserva);
    }

    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> buscarReservasActivasPorUsuario(Long usuarioId) {
        List<Reserva> reservas = reservaRepository.findByUsuarioIdAndEstado(usuarioId, "Activa");
        return reservas.stream()
                .map(reservaMapper::toDto)
                .collect(Collectors.toList());
    }
}
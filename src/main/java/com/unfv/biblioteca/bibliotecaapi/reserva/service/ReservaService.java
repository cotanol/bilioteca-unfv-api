package com.unfv.biblioteca.bibliotecaapi.reserva.service;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.Usuario;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.repository.UsuarioRepository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Ejemplar;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Material;
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
    private final EjemplarRepository exemplarRepository;
    private final ReservaMapper reservaMapper;

    @Transactional
    public ReservaResponseDTO crearReserva(CrearReservaRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + request.getUsuarioId()));
        Material material = materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException("Material no encontrado con ID: " + request.getMaterialId()));

        validarReserva(usuario, material);

        long ejemplaresDisponibles = exemplarRepository.countByMaterialIdAndEstado(material.getId(), "Disponible");

        Reserva nuevaReserva = (ejemplaresDisponibles > 0)
                ? crearReservaParaRecojo(usuario, material)
                : crearReservaEnListaDeEspera(usuario, material);

        Reserva reservaGuardada = reservaRepository.save(nuevaReserva);
        return reservaMapper.toDto(reservaGuardada);
    }

    private void validarReserva(Usuario usuario, Material material) {
        if (prestamoRepository.existsByUsuarioIdAndEjemplar_MaterialIdAndEstado(usuario.getId(), material.getId(), "Activo")) {
            throw new BusinessRuleException("No puede reservar un material que ya tiene en préstamo activo.");
        }
        if (reservaRepository.existsByUsuarioIdAndMaterialIdAndEstadoIn(usuario.getId(), material.getId(), List.of("Activa", "Pendiente de Recojo"))) {
            throw new BusinessRuleException("Ya tiene una reserva activa o pendiente de recojo para este material.");
        }
    }

    private Reserva crearReservaParaRecojo(Usuario usuario, Material material) {
        Ejemplar ejemplarAReservar = exemplarRepository
                .findFirstByMaterialIdAndEstado(material.getId(), "Disponible")
                .orElseThrow(() -> new BusinessRuleException("Inconsistencia de datos: No se encontró un ejemplar físico disponible."));

        ejemplarAReservar.setEstado("Reservado");
        exemplarRepository.save(ejemplarAReservar);

        System.out.println("INFO: Ejemplar " + ejemplarAReservar.getCodigoBarras() + " ahora en estado RESERVADO para el usuario " + usuario.getNombres());

        return Reserva.builder()
                .usuario(usuario)
                .material(material)
                .ejemplar(ejemplarAReservar)
                .fechaReserva(LocalDateTime.now())
                .estado("Pendiente de Recojo")
                .build();
    }

    private Reserva crearReservaEnListaDeEspera(Usuario usuario, Material material) {
        System.out.println("INFO: Usuario " + usuario.getNombres() + " ha entrado en lista de espera para el material " + material.getTitulo());
        return Reserva.builder()
                .usuario(usuario)
                .material(material)
                .fechaReserva(LocalDateTime.now())
                .estado("Activa")
                .build();
    }

    @Transactional
    public void cancelarReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + reservaId));

        if (!"Activa".equals(reserva.getEstado()) && !"Pendiente de Recojo".equals(reserva.getEstado())) {
            throw new BusinessRuleException("Solo se pueden cancelar reservas activas o pendientes de recojo.");
        }

        if (reserva.getEjemplar() != null && "Reservado".equals(reserva.getEjemplar().getEstado())) {
            Ejemplar ejemplar = reserva.getEjemplar();
            ejemplar.setEstado("Disponible");
            exemplarRepository.save(ejemplar);
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

    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> findAllReservas() {
        return reservaRepository.findAll().stream()
                .map(reservaMapper::toDto)
                .collect(Collectors.toList());
    }
}
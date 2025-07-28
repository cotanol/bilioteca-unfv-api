package com.unfv.biblioteca.bibliotecaapi.circulacion.service;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.Usuario;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.repository.UsuarioRepository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Ejemplar;
import com.unfv.biblioteca.bibliotecaapi.catalogo.repository.EjemplarRepository;
import com.unfv.biblioteca.bibliotecaapi.circulacion.domain.Prestamo;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.request.CrearPrestamoRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.PrestamoResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.mapper.CirculacionMapper;
import com.unfv.biblioteca.bibliotecaapi.circulacion.repository.PrestamoRepository;
import com.unfv.biblioteca.bibliotecaapi.reserva.repository.ReservaRepository;
import com.unfv.biblioteca.bibliotecaapi.shared.exception.BusinessRuleException;
import com.unfv.biblioteca.bibliotecaapi.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EjemplarRepository exemplarRepository;
    private final CirculacionMapper circulacionMapper;
    private final MultaService multaService;
    private final ReservaRepository reservaRepository;

    @Transactional
    public PrestamoResponseDTO crearPrestamo(CrearPrestamoRequestDTO request) {
        Usuario usuario = usuarioRepository.findByCodigoUniversitario(request.getCodigoUniversitario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con código universitario: " + request.getCodigoUniversitario()));
        Ejemplar ejemplar = exemplarRepository.findByCodigoBarras(request.getCodigoBarras())
                .orElseThrow(() -> new ResourceNotFoundException("Ejemplar no encontrado con código de barras: " + request.getCodigoBarras()));

        validarCondicionesDePrestamo(usuario);
        vincularConReservaSiExiste(usuario, ejemplar);

        ejemplar.setEstado("En Prestamo");
        exemplarRepository.save(ejemplar);

        Prestamo nuevoPrestamo = construirPrestamo(usuario, ejemplar);
        Prestamo prestamoGuardado = prestamoRepository.save(nuevoPrestamo);

        return circulacionMapper.toPrestamoDetalleDTO(prestamoGuardado);
    }

    private void validarCondicionesDePrestamo(Usuario usuario) {
        if (!"Activo".equalsIgnoreCase(usuario.getEstado())) {
            throw new BusinessRuleException("El usuario " + usuario.getNombres() + " no está activo.");
        }
        long prestamosActivos = prestamoRepository.countByUsuarioIdAndEstado(usuario.getId(), "Activo");
        if (prestamosActivos >= usuario.getTipoUsuario().getLimitePrestamos()) {
            throw new BusinessRuleException("El usuario ha alcanzado su límite de préstamos.");
        }
    }

    private void vincularConReservaSiExiste(Usuario usuario, Ejemplar ejemplar) {
        reservaRepository.findByUsuarioIdAndEjemplarIdAndEstado(usuario.getId(), ejemplar.getId(), "Pendiente de Recojo")
                .ifPresentOrElse(
                        reserva -> {
                            reserva.setEstado("Recogida");
                            reservaRepository.save(reserva);
                            System.out.println("INFO: Reserva " + reserva.getId() + " completada y actualizada a RECOGIDA.");
                        },
                        () -> {
                            if (!"Disponible".equalsIgnoreCase(ejemplar.getEstado())) {
                                throw new BusinessRuleException("El ejemplar con código " + ejemplar.getCodigoBarras() + " no está disponible para préstamo directo. Puede estar reservado para otro usuario.");
                            }
                        }
                );
    }

    private Prestamo construirPrestamo(Usuario usuario, Ejemplar ejemplar) {
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setEjemplar(ejemplar);
        prestamo.setEstado("Activo");
        prestamo.setFechaPrestamo(LocalDateTime.now());
        LocalDate fechaDevolucion = LocalDate.now().plusDays(usuario.getTipoUsuario().getDiasPrestamos());
        prestamo.setFechaDevolucionPactada(fechaDevolucion);
        return prestamo;
    }

    @Transactional
    public PrestamoResponseDTO registrarDevolucion(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + prestamoId));

        if ("Devuelto".equalsIgnoreCase(prestamo.getEstado())) {
            throw new BusinessRuleException("El préstamo ya ha sido devuelto.");
        }

        prestamo.setEstado("Devuelto");
        prestamo.setFechaDevolucionReal(LocalDateTime.now());

        Ejemplar ejemplar = prestamo.getEjemplar();
        ejemplar.setEstado("Disponible");
        exemplarRepository.save(ejemplar);

        if (prestamo.getFechaDevolucionReal().toLocalDate().isAfter(prestamo.getFechaDevolucionPactada())) {
            multaService.generarMultaParaPrestamo(prestamo);
        }

        Prestamo prestamoActualizado = prestamoRepository.save(prestamo);
        gestionarColaDeReservas(ejemplar.getMaterial().getId());

        return circulacionMapper.toPrestamoDetalleDTO(prestamoActualizado);
    }

    private void gestionarColaDeReservas(Long materialId) {
        reservaRepository.findFirstByMaterialIdAndEstadoOrderByFechaReservaAsc(materialId, "Activa")
                .ifPresent(siguienteEnLaCola -> {
                    exemplarRepository.findFirstByMaterialIdAndEstado(materialId, "Disponible")
                            .ifPresent(ejemplarDisponible -> {
                                ejemplarDisponible.setEstado("Reservado");
                                exemplarRepository.save(ejemplarDisponible);

                                siguienteEnLaCola.setEstado("Pendiente de Recojo");
                                siguienteEnLaCola.setEjemplar(ejemplarDisponible);
                                reservaRepository.save(siguienteEnLaCola);

                                System.out.println("INFO: Notificando al usuario " + siguienteEnLaCola.getUsuario().getEmail() + ". El ejemplar " + ejemplarDisponible.getCodigoBarras() + " ha sido reservado para él.");
                            });
                });
    }

    @Transactional
    public PrestamoResponseDTO renovarPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + prestamoId));

        if (!"Activo".equalsIgnoreCase(prestamo.getEstado())) {
            throw new BusinessRuleException("El préstamo no está activo, no se puede renovar.");
        }
        if (reservaRepository.existsByMaterialIdAndEstado(prestamo.getEjemplar().getMaterial().getId(), "Activa")) {
            throw new BusinessRuleException("El material de este préstamo tiene reservas activas y no puede ser renovado.");
        }
        if (prestamo.getRenovaciones() >= 2) {
            throw new BusinessRuleException("Este préstamo ya ha alcanzado el límite máximo de renovaciones.");
        }

        prestamo.setRenovaciones(prestamo.getRenovaciones() + 1);
        LocalDate nuevaFechaDevolucion = prestamo.getFechaDevolucionPactada().plusDays(prestamo.getUsuario().getTipoUsuario().getDiasPrestamos());
        prestamo.setFechaDevolucionPactada(nuevaFechaDevolucion);

        Prestamo prestamoRenovado = prestamoRepository.save(prestamo);
        return circulacionMapper.toPrestamoDetalleDTO(prestamoRenovado);
    }

    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> findAllPrestamos() {
        return prestamoRepository.findAll().stream()
                .map(circulacionMapper::toPrestamoDetalleDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PrestamoResponseDTO buscarPrestamoPorId(Long id) {
        Prestamo prestamoEntidad = prestamoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + id));
        return circulacionMapper.toPrestamoDetalleDTO(prestamoEntidad);
    }

    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> buscarPrestamosActivosPorUsuario(Long usuarioId) {
        List<String> estados = List.of("Activo", "Con Retraso");
        List<Prestamo> prestamos = prestamoRepository.findByUsuarioIdAndEstadoIn(usuarioId, estados);
        return prestamos.stream()
                .map(circulacionMapper::toPrestamoDetalleDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> buscarHistorialPrestamosPorUsuario(Long usuarioId) {
        List<Prestamo> prestamos = prestamoRepository.findByUsuarioId(usuarioId);
        return prestamos.stream()
                .map(circulacionMapper::toPrestamoDetalleDTO)
                .collect(Collectors.toList());
    }
}
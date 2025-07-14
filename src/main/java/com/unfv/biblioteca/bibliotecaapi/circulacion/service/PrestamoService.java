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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestamoService {

    // Dependencias necesarias para la lógica de negocio de un préstamo
    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EjemplarRepository exemplarRepository;
    private final CirculacionMapper circulacionMapper;
    private final MultaService multaService;
    private final ReservaRepository reservaRepository;

    @Autowired
    public PrestamoService(PrestamoRepository prestamoRepository,
                           UsuarioRepository usuarioRepository,
                           EjemplarRepository exemplarRepository,
                           CirculacionMapper circulacionMapper,
                           MultaService multaService,
                           ReservaRepository reservaRepository) {
        this.prestamoRepository = prestamoRepository;
        this.usuarioRepository = usuarioRepository;
        this.exemplarRepository = exemplarRepository;
        this.circulacionMapper = circulacionMapper;
        this.multaService = multaService;
        this.reservaRepository = reservaRepository;
    }

    // Aquí irían los métodos para manejar la lógica de negocio de los préstamos
    // Por ejemplo: crear préstamo, devolver préstamo, calcular multas, etc.


    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> findAllPrestamos() {
        return prestamoRepository.findAll().stream()
                .map(circulacionMapper::toPrestamoDetalleDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PrestamoResponseDTO buscarPrestamoPorId(Long id) {
        // 1. Buscamos la entidad en la base de datos
        Prestamo prestamoEntidad = prestamoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + id));

        // 2. Usamos el mapper para convertir y devolver el DTO
        return circulacionMapper.toPrestamoDetalleDTO(prestamoEntidad);
    }

    @Transactional // Transacción de lectura y escritura
    public PrestamoResponseDTO crearPrestamo(CrearPrestamoRequestDTO request) {
        // 1. Buscamos las entidades principales a partir de los IDs del DTO
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Ejemplar ejemplar = exemplarRepository.findById(request.getEjemplarId())
                .orElseThrow(() -> new ResourceNotFoundException("Ejemplar no encontrado"));

        // === 2. APLICAMOS LAS REGLAS DE NEGOCIO ===
        // Regla 1: El ejemplar debe estar disponible
        if (!"Disponible".equalsIgnoreCase(ejemplar.getEstado())) {
            throw new BusinessRuleException("El ejemplar con código " + ejemplar.getCodigoBarras() + " no está disponible.");
        }

        // Regla 2: El usuario debe estar activo
        if (!"Activo".equalsIgnoreCase(usuario.getEstado())) {
            throw new BusinessRuleException("El usuario " + usuario.getNombres() + " no está activo.");
        }

        // Regla 3: El usuario no debe exceder su límite de préstamos
        // (Necesitaremos un nuevo metodo en PrestamoRepository)
        long prestamosActivos = prestamoRepository.countByUsuarioIdAndEstado(usuario.getId(), "Activo");
        if (prestamosActivos >= usuario.getTipoUsuario().getLimitePrestamos()) {
            throw new BusinessRuleException("El usuario ha alcanzado su límite de préstamos.");
        }

        // (Opcional) Regla 4: Verificar que el usuario no tenga multas pendientes
        // if (multaRepository.existsByUsuarioAndEstado(usuario, "Pendiente")) { ... }

        // === 3. SI TODAS LAS REGLAS PASAN, PROCEDEMOS A CREAR ===

        // Actualizamos el estado del ejemplar
        ejemplar.setEstado("En Prestamo");
        exemplarRepository.save(ejemplar);

        // Creamos la nueva entidad Prestamo
        Prestamo nuevoPrestamo = new Prestamo();
        nuevoPrestamo.setUsuario(usuario);
        nuevoPrestamo.setEjemplar(ejemplar);
        nuevoPrestamo.setEstado("Activo");
        nuevoPrestamo.setFechaPrestamo(LocalDateTime.now());

        // Calculamos la fecha de devolución
        LocalDate fechaDevolucion = LocalDate.now().plusDays(usuario.getTipoUsuario().getDiasPrestamos());
        nuevoPrestamo.setFechaDevolucionPactada(fechaDevolucion);

        // Guardamos el nuevo préstamo
        Prestamo prestamoGuardado = prestamoRepository.save(nuevoPrestamo);

        // 4. Devolvemos el DTO de respuesta usando el mapper
        return circulacionMapper.toPrestamoDetalleDTO(prestamoGuardado);
    }

    @Transactional
    public PrestamoResponseDTO registrarDevolucion(Long prestamoId) {
        // 1. Buscamos el préstamo
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + prestamoId));

        // 2. Regla de negocio: no se puede devolver un préstamo que ya está devuelto
        if ("Devuelto".equalsIgnoreCase(prestamo.getEstado())) {
            throw new BusinessRuleException("El préstamo ya ha sido devuelto.");
        }

        // 3. Actualizamos el préstamo
        prestamo.setEstado("Devuelto");
        prestamo.setFechaDevolucionReal(LocalDateTime.now());

        // 4. Actualizamos el estado del ejemplar a "Disponible"
        Ejemplar ejemplar = prestamo.getEjemplar();
        ejemplar.setEstado("Disponible");
        exemplarRepository.save(ejemplar);

        // 5. Verificamos si se debe generar una multa
        if (prestamo.getFechaDevolucionReal().toLocalDate().isAfter(prestamo.getFechaDevolucionPactada())) {
            multaService.generarMultaParaPrestamo(prestamo);
        }

        // 6. Guardamos los cambios en el préstamo
        Prestamo prestamoActualizado = prestamoRepository.save(prestamo);

        // 7. Gestionar cola de reservas
        gestionarColaDeReservas(ejemplar.getMaterial().getId());

        // 8. Devolvemos el DTO actualizado
        return circulacionMapper.toPrestamoDetalleDTO(prestamoActualizado);
    }

    private void gestionarColaDeReservas(Long materialId) {
        reservaRepository.findFirstByMaterialIdAndEstadoOrderByFechaReservaAsc(materialId, "Activa")
                .ifPresent(reserva -> {
                    // Aquí iría la lógica para notificar al usuario
                    // Por ejemplo, enviar un email
                    System.out.println("Notificando al usuario " + reserva.getUsuario().getEmail() + " que el material " + reserva.getMaterial().getTitulo() + " está disponible.");

                    reserva.setEstado("Pendiente de Recojo");
                    reservaRepository.save(reserva);
                });
    }

    @Transactional
    public PrestamoResponseDTO renovarPrestamo(Long prestamoId) {
        // 1. Buscamos el préstamo
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + prestamoId));

        // 2. Regla de negocio: no se puede renovar un préstamo que no está activo
        if (!"Activo".equalsIgnoreCase(prestamo.getEstado())) {
            throw new BusinessRuleException("El préstamo no está activo, no se puede renovar.");
        }

        // 3. Regla de negocio: no se puede renovar si el material tiene reservas activas
        if (reservaRepository.existsByMaterialIdAndEstado(prestamo.getEjemplar().getMaterial().getId(), "Activa")) {
            throw new BusinessRuleException("El material de este préstamo tiene reservas activas y no puede ser renovado.");
        }

        // 4. Regla de negocio: Limitar el número de renovaciones (ej. a 2)
        if (prestamo.getRenovaciones() >= 2) {
            throw new BusinessRuleException("Este préstamo ya ha alcanzado el límite máximo de renovaciones.");
        }

        // 5. Actualizamos el préstamo
        prestamo.setRenovaciones(prestamo.getRenovaciones() + 1);
        LocalDate nuevaFechaDevolucion = prestamo.getFechaDevolucionPactada().plusDays(prestamo.getUsuario().getTipoUsuario().getDiasPrestamos());
        prestamo.setFechaDevolucionPactada(nuevaFechaDevolucion);

        // 6. Guardamos los cambios
        Prestamo prestamoRenovado = prestamoRepository.save(prestamo);

        // 7. Devolvemos el DTO actualizado
        return circulacionMapper.toPrestamoDetalleDTO(prestamoRenovado);
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

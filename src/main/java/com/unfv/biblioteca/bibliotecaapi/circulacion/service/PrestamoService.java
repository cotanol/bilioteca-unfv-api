package com.unfv.biblioteca.bibliotecaapi.circulacion.service;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.Usuario;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.repository.UsuarioRepository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Ejemplar;
import com.unfv.biblioteca.bibliotecaapi.catalogo.repository.EjemplarRepository;
import com.unfv.biblioteca.bibliotecaapi.circulacion.domain.Prestamo;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.request.CrearPrestamoRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.PrestamoDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.mapper.CirculacionMapper;
import com.unfv.biblioteca.bibliotecaapi.circulacion.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class PrestamoService {

    // Dependencias necesarias para la lógica de negocio de un préstamo
    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EjemplarRepository exemplarRepository;
    private final CirculacionMapper circulacionMapper;
    // Podríamos inyectar MultaRepository/MultaService aquí si fuera necesario

    @Autowired
    public PrestamoService(PrestamoRepository prestamoRepository,
                           UsuarioRepository usuarioRepository,
                           EjemplarRepository exemplarRepository,
                           CirculacionMapper circulacionMapper) {
        this.prestamoRepository = prestamoRepository;
        this.usuarioRepository = usuarioRepository;
        this.exemplarRepository = exemplarRepository;
        this.circulacionMapper = circulacionMapper;
    }

    // Aquí irían los métodos para manejar la lógica de negocio de los préstamos
    // Por ejemplo: crear préstamo, devolver préstamo, calcular multas, etc.

    @Transactional(readOnly = true)
    public PrestamoDetalleDTO buscarPrestamoPorId(Long id) {
        // 1. Buscamos la entidad en la base de datos
        Prestamo prestamoEntidad = prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + id)); // Usar excepciones personalizadas es mejor

        // 2. Usamos el mapper para convertir y devolver el DTO
        return circulacionMapper.toPrestamoDetalleDTO(prestamoEntidad);
    }

    @Transactional // Transacción de lectura y escritura
    public PrestamoDetalleDTO crearPrestamo(CrearPrestamoRequestDTO request) {
        // 1. Buscamos las entidades principales a partir de los IDs del DTO
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Ejemplar ejemplar = exemplarRepository.findById(request.getEjemplarId())
                .orElseThrow(() -> new RuntimeException("Ejemplar no encontrado"));

        // === 2. APLICAMOS LAS REGLAS DE NEGOCIO ===
        // Regla 1: El ejemplar debe estar disponible
        if (!"Disponible".equalsIgnoreCase(ejemplar.getEstado())) {
            throw new IllegalStateException("El ejemplar con código " + ejemplar.getCodigoBarras() + " no está disponible.");
        }

        // Regla 2: El usuario debe estar activo
        if (!"Activo".equalsIgnoreCase(usuario.getEstado())) {
            throw new IllegalStateException("El usuario " + usuario.getNombres() + " no está activo.");
        }

        // Regla 3: El usuario no debe exceder su límite de préstamos
        // (Necesitaremos un nuevo método en PrestamoRepository)
        long prestamosActivos = prestamoRepository.countByUsuarioIdAndEstado(usuario.getId(), "Activo");
        if (prestamosActivos >= usuario.getTipoUsuario().getLimitePrestamos()) {
            throw new IllegalStateException("El usuario ha alcanzado su límite de préstamos.");
        }

        // (Opcional) Regla 4: Verificar que el usuario no tenga multas pendientes
        // if (multaRepository.existsByUsuarioAndEstado(usuario, "Pendiente")) { ... }

        // === 3. SI TODAS LAS REGLAS PASAN, PROCEDEMOS A CREAR ===

        // Actualizamos el estado del ejemplar
        ejemplar.setEstado("En Préstamo");
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

}

package com.unfv.biblioteca.bibliotecaapi.circulacion.service;

import com.unfv.biblioteca.bibliotecaapi.circulacion.domain.Multa;
import com.unfv.biblioteca.bibliotecaapi.circulacion.domain.Prestamo;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.MultaDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.mapper.CirculacionMapper;
import com.unfv.biblioteca.bibliotecaapi.circulacion.repository.MultaRepository;
import com.unfv.biblioteca.bibliotecaapi.circulacion.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MultaService {
    // Mas adelante se puede agregar el PrestamoRepository si es necesario para buscar préstamos por ID
    private final MultaRepository multaRepository;
    private final CirculacionMapper circulacionMapper;

    @Autowired
    public MultaService(MultaRepository multaRepository,
                        CirculacionMapper circulacionMapper) {
        this.multaRepository = multaRepository;
        this.circulacionMapper = circulacionMapper;
    }

    // Aquí irían los métodos para manejar la lógica de negocio de las multas

    // Definimos la tasa de la multa como una constante
    private static final BigDecimal TASA_DIARIA_MULTA = new BigDecimal("1.00"); // S/ 1.00 por día

    @Transactional
    public MultaDTO generarMultaParaPrestamo(Prestamo prestamo) {
        // Regla 1: Verificar si ya existe una multa para este préstamo para no duplicarla
        if (multaRepository.existsByPrestamoId(prestamo.getId())) {
            // Podríamos lanzar una excepción o simplemente registrar un log y salir
            System.out.println("Ya existe una multa para el préstamo ID: " + prestamo.getId());
            return null; // O devolver la multa existente
        }

        // Regla 2: Calcular los días de retraso
        LocalDate fechaPactada = prestamo.getFechaDevolucionPactada();
        // Usamos la fecha de devolución real si existe, si no, usamos la fecha actual.
        LocalDate fechaDevolucion = (prestamo.getFechaDevolucionReal() != null)
                ? prestamo.getFechaDevolucionReal().toLocalDate()
                : LocalDate.now();

        long diasDeRetraso = ChronoUnit.DAYS.between(fechaPactada, fechaDevolucion);

        // Si no hay días de retraso, no se genera multa
        if (diasDeRetraso <= 0) {
            return null;
        }

        // Regla 3: Calcular el monto
        BigDecimal monto = TASA_DIARIA_MULTA.multiply(new BigDecimal(diasDeRetraso));

        // Creamos la nueva entidad Multa
        Multa nuevaMulta = new Multa();
        nuevaMulta.setPrestamo(prestamo);
        nuevaMulta.setMonto(monto);
        nuevaMulta.setEstado("Pendiente");
        nuevaMulta.setFechaGeneracion(LocalDate.now());

        Multa multaGuardada = multaRepository.save(nuevaMulta);

        return circulacionMapper.toMultaDTO(multaGuardada);
    }

    @Transactional
    public MultaDTO registrarPagoDeMulta(Long multaId) {
        // 1. Buscamos la multa
        Multa multa = multaRepository.findById(multaId)
                .orElseThrow(() -> new RuntimeException("Multa no encontrada con ID: " + multaId));

        // 2. Regla de negocio: no se puede pagar una multa que ya está pagada
        if ("Pagada".equalsIgnoreCase(multa.getEstado())) {
            throw new IllegalStateException("La multa ya ha sido pagada.");
        }

        // 3. Actualizamos la entidad
        multa.setEstado("Pagada");
        multa.setFechaPago(LocalDate.now());

        Multa multaActualizada = multaRepository.save(multa);

        // 4. Devolvemos el DTO actualizado
        return circulacionMapper.toMultaDTO(multaActualizada);
    }

    @Transactional(readOnly = true)
    public List<MultaDTO> buscarMultasPendientesPorUsuario(Long usuarioId) {
        // 1. Llamamos al método del repositorio que atraviesa las relaciones
        List<Multa> multasEntidad = multaRepository.findByPrestamo_Usuario_IdAndEstado(usuarioId, "Pendiente");

        // 2. Mapeamos la lista de entidades a una lista de DTOs y la devolvemos
        return multasEntidad.stream()
                .map(circulacionMapper::toMultaDTO)
                .collect(Collectors.toList());
    }
}

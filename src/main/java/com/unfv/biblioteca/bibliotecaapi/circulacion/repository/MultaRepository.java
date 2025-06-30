package com.unfv.biblioteca.bibliotecaapi.circulacion.repository;
import org.springframework.stereotype.Repository;
import com.unfv.biblioteca.bibliotecaapi.circulacion.domain.Multa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface MultaRepository extends JpaRepository<Multa, Long> {

    // Aquí se pueden agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar multas por usuario o estado

    // public Optional<Multa> findByUsuarioId(Long usuarioId);
    // public List<Multa> findByEstado(String estado);

    /**
     * Verifica si ya existe una multa asociada a un ID de préstamo específico.
     * Útil para evitar crear multas duplicadas.
     */
    boolean existsByPrestamoId(Long prestamoId);

    /**
     * Busca todas las multas de un usuario específico que se encuentren
     * en un estado determinado (ej. "Pendiente").
     * Spring Data JPA entiende que debe navegar a través de la relación
     * Multa -> Prestamo -> Usuario.
     */
    List<Multa> findByPrestamo_Usuario_IdAndEstado(Long usuarioId, String estado);
}

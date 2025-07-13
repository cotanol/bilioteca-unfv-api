package com.unfv.biblioteca.bibliotecaapi.circulacion.repository;
import org.springframework.stereotype.Repository;
import com.unfv.biblioteca.bibliotecaapi.circulacion.domain.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    long countByUsuarioIdAndEstado(Long usuarioId, String estado);
    boolean existsByUsuarioIdAndEjemplar_MaterialIdAndEstado(Long usuarioId, Long materialId, String estado);
    long countByEjemplar_MaterialIdAndEstado(Long materialId, String estado);

    List<Prestamo> findByEstadoAndFechaDevolucionPactadaBefore(String estado, LocalDate fecha);
}

package com.unfv.biblioteca.bibliotecaapi.circulacion.repository;
import org.springframework.stereotype.Repository;
import com.unfv.biblioteca.bibliotecaapi.circulacion.domain.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    // Aquí se pueden agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar préstamos por usuario o estado

    // public Optional<Prestamo> findByUsuarioId(Long usuarioId);
    // public List<Prestamo> findByEstado(String estado);

    /**
     * Cuenta la cantidad de préstamos que tiene un usuario en un estado específico.
     */
    long countByUsuarioIdAndEstado(Long usuarioId, String estado);
    boolean existsByUsuarioIdAndEjemplar_MaterialIdAndEstado(Long usuarioId, Long materialId, String estado);
    long countByEjemplar_MaterialIdAndEstado(Long materialId, String estado);

}

package com.unfv.biblioteca.bibliotecaapi.catalogo.repository;
import org.springframework.stereotype.Repository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    // Aquí se pueden agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar materiales por tipo o estado

    // public Optional<Material> findByTipo(String tipo);
    // public List<Material> findByEstado(String estado);

    /**
     * Verifica si ya existe un material con un ISBN específico.
     * Spring Data JPA automáticamente genera una consulta SQL optimizada
     * (algo como SELECT COUNT(*) ... > 0) a partir del nombre de este metodo.
     *
     * @param isbn El ISBN a verificar en la base de datos.
     * @return true si un material con ese ISBN ya existe, false en caso contrario.
     */
    boolean existsByIsbn(String isbn);

}

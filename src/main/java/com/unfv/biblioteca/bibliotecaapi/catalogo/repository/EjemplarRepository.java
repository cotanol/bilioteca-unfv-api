package com.unfv.biblioteca.bibliotecaapi.catalogo.repository;
import org.springframework.stereotype.Repository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Ejemplar;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {

    // Aquí se pueden agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar ejemplares por estado o ubicación

    // public Optional<Ejemplar> findByEstado(String estado);
    // public List<Ejemplar> findByUbicacionContaining(String ubicacion);

    long countByMaterialId(Long materialId);
}

package com.unfv.biblioteca.bibliotecaapi.catalogo.repository;
import org.springframework.stereotype.Repository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {

    // Aquí se pueden agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar ubicaciones por nombre o tipo

    // public Optional<Ubicacion> findByNombre(String nombre);
    // public List<Ubicacion> findByTipo(String tipo);
}

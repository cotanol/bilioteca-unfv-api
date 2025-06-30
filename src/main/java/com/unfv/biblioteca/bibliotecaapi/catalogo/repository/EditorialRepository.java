package com.unfv.biblioteca.bibliotecaapi.catalogo.repository;
import org.springframework.stereotype.Repository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface EditorialRepository extends JpaRepository<Editorial, Long> {

    // Aquí se pueden agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar editoriales por nombre o ubicación

    // public Optional<Editorial> findByNombre(String nombre);
    // public List<Editorial> findByUbicacionContaining(String ubicacion);
}

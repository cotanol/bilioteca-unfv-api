package com.unfv.biblioteca.bibliotecaapi.catalogo.repository;
import org.springframework.stereotype.Repository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface EditorialRepository extends JpaRepository<Editorial, Long> {

    boolean existsByNombre(String nombre);
}

package com.unfv.biblioteca.bibliotecaapi.catalogo.repository;
import org.springframework.stereotype.Repository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Aquí se pueden agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar categorías por nombre o descripción

    // public Optional<Categoria> findByNombre(String nombre);
    // public List<Categoria> findByDescripcionContaining(String descripcion);
}

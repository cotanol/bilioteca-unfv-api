package com.unfv.biblioteca.bibliotecaapi.catalogo.repository;
import org.springframework.stereotype.Repository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByNombreCategoria(String nombreCategoria);
}

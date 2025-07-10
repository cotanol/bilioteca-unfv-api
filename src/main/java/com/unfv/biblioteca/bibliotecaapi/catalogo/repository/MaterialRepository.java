package com.unfv.biblioteca.bibliotecaapi.catalogo.repository;
import org.springframework.stereotype.Repository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    boolean existsByIsbn(String isbn);

    boolean existsByAutoresId(Long autorId);

    boolean existsByCategoriasId(Long categoriaId);

    boolean existsByEditorialId(Long editorialId);

}

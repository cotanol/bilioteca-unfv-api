package com.unfv.biblioteca.bibliotecaapi.catalogo.repository;

import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository  extends JpaRepository<Autor, Long> {

    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar autores por nombre:
    // List<Autor> findByNombre(String nombre);
}

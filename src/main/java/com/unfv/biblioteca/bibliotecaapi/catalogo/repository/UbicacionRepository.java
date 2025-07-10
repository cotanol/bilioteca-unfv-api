package com.unfv.biblioteca.bibliotecaapi.catalogo.repository;

import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
}
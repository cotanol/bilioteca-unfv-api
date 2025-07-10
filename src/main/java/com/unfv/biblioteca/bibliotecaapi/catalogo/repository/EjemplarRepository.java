package com.unfv.biblioteca.bibliotecaapi.catalogo.repository;

import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Ejemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {

    boolean existsByCodigoBarras(String codigoBarras);

    boolean existsByUbicacionId(Long ubicacionId);

    long countByMaterialId(Long materialId);
}
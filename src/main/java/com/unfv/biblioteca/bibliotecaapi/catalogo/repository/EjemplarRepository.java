package com.unfv.biblioteca.bibliotecaapi.catalogo.repository;

import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Ejemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {

    boolean existsByCodigoBarras(String codigoBarras);

    boolean existsByUbicacionId(Long ubicacionId);

    long countByMaterialIdAndEstado(Long materialId, String estado);

    long countByMaterialId(Long materialId);

    Optional<Ejemplar> findFirstByMaterialIdAndEstado(Long materialId, String estado);

    List<Ejemplar> findByMaterialId(Long materialId);

    Optional<Ejemplar> findByCodigoBarras(String codigoBarras);
}
package com.unfv.biblioteca.bibliotecaapi.reserva.repository;

import com.unfv.biblioteca.bibliotecaapi.reserva.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByMaterialIdAndEstado(Long materialId, String estado);

    Optional<Reserva> findFirstByMaterialIdAndEstadoOrderByFechaReservaAsc(Long materialId, String estado);

    List<Reserva> findByUsuarioIdAndEstado(Long usuarioId, String estado);
}

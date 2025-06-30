package com.unfv.biblioteca.bibliotecaapi.reserva.repository;
import org.springframework.stereotype.Repository;
import com.unfv.biblioteca.bibliotecaapi.reserva.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Aquí se pueden agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar reservas por usuario o libro

    // public Optional<Reserva> findByUsuarioId(Long usuarioId);
    // public List<Reserva> findByLibroId(Long libroId);

    boolean existsByUsuarioIdAndMaterialIdAndEstado(Long usuarioId, Long materialId, String estado);
}

package com.unfv.biblioteca.bibliotecaapi.reportes.repository;

import com.unfv.biblioteca.bibliotecaapi.reportes.dto.DisponibilidadMaterialDTO;
import com.unfv.biblioteca.bibliotecaapi.reportes.dto.PrestamoActivoDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReportesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Consulta la vista vw_disponibilidad_material
     */
    @SuppressWarnings("unchecked")
    public List<DisponibilidadMaterialDTO> obtenerDisponibilidadMateriales() {
        String sql = "SELECT id, titulo, isbn, total_ejemplares, ejemplares_disponibles, ejemplares_prestados " +
                     "FROM vw_disponibilidad_material";
        
        List<Object[]> results = entityManager.createNativeQuery(sql).getResultList();
        
        return results.stream()
                .map(row -> new DisponibilidadMaterialDTO(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        (String) row[2],
                        ((Number) row[3]).longValue(),
                        ((Number) row[4]).longValue(),
                        ((Number) row[5]).longValue()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Consulta la vista vw_disponibilidad_material filtrada por ID
     */
    @SuppressWarnings("unchecked")
    public DisponibilidadMaterialDTO obtenerDisponibilidadMaterialPorId(Long materialId) {
        String sql = "SELECT id, titulo, isbn, total_ejemplares, ejemplares_disponibles, ejemplares_prestados " +
                     "FROM vw_disponibilidad_material WHERE id = :materialId";
        
        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("materialId", materialId)
                .getResultList();
        
        if (results.isEmpty()) {
            return null;
        }
        
        Object[] row = results.get(0);
        return new DisponibilidadMaterialDTO(
                ((Number) row[0]).longValue(),
                (String) row[1],
                (String) row[2],
                ((Number) row[3]).longValue(),
                ((Number) row[4]).longValue(),
                ((Number) row[5]).longValue()
        );
    }

    /**
     * Consulta la vista vw_prestamos_activos
     */
    @SuppressWarnings("unchecked")
    public List<PrestamoActivoDTO> obtenerPrestamosActivos() {
        String sql = "SELECT id, usuario, titulo, fecha_prestamo, fecha_devolucion_pactada, estado " +
                     "FROM vw_prestamos_activos";
        
        List<Object[]> results = entityManager.createNativeQuery(sql).getResultList();
        
        return results.stream()
                .map(row -> new PrestamoActivoDTO(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        (String) row[2],
                        convertToLocalDateTime(row[3]),
                        convertToLocalDate(row[4]),
                        (String) row[5]
                ))
                .collect(Collectors.toList());
    }

    /**
     * Consulta la vista vw_prestamos_activos filtrada por estado
     */
    @SuppressWarnings("unchecked")
    public List<PrestamoActivoDTO> obtenerPrestamosActivosPorEstado(String estado) {
        String sql = "SELECT id, usuario, titulo, fecha_prestamo, fecha_devolucion_pactada, estado " +
                     "FROM vw_prestamos_activos WHERE estado = :estado";
        
        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("estado", estado)
                .getResultList();
        
        return results.stream()
                .map(row -> new PrestamoActivoDTO(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        (String) row[2],
                        convertToLocalDateTime(row[3]),
                        convertToLocalDate(row[4]),
                        (String) row[5]
                ))
                .collect(Collectors.toList());
    }

    /**
     * Llama al stored procedure sp_registrar_devolucion
     */
    @Transactional
    public void registrarDevolucion(Long prestamoId, LocalDateTime fechaDevolucion) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_registrar_devolucion");
        
        query.registerStoredProcedureParameter("p_prestamo_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_fecha_devolucion", java.sql.Timestamp.class, ParameterMode.IN);
        
        query.setParameter("p_prestamo_id", prestamoId);
        query.setParameter("p_fecha_devolucion", java.sql.Timestamp.valueOf(fechaDevolucion));
        
        query.execute();
    }

    /**
     * Llama al stored procedure sp_verificar_disponibilidad
     */
    public Integer verificarDisponibilidad(Long materialId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_verificar_disponibilidad");
        
        query.registerStoredProcedureParameter("p_material_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_disponible", Integer.class, ParameterMode.OUT);
        
        query.setParameter("p_material_id", materialId);
        query.execute();
        
        return (Integer) query.getOutputParameterValue("p_disponible");
    }

    // Métodos auxiliares para conversión de tipos
    private LocalDateTime convertToLocalDateTime(Object obj) {
        if (obj == null) return null;
        if (obj instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) obj).toLocalDateTime();
        }
        return null;
    }

    private LocalDate convertToLocalDate(Object obj) {
        if (obj == null) return null;
        if (obj instanceof java.sql.Date) {
            return ((java.sql.Date) obj).toLocalDate();
        }
        if (obj instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) obj).toLocalDateTime().toLocalDate();
        }
        return null;
    }
}


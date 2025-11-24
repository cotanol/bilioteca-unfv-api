package com.unfv.biblioteca.bibliotecaapi.reportes.service;

import com.unfv.biblioteca.bibliotecaapi.reportes.dto.DisponibilidadMaterialDTO;
import com.unfv.biblioteca.bibliotecaapi.reportes.dto.DisponibilidadResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.reportes.dto.PrestamoActivoDTO;
import com.unfv.biblioteca.bibliotecaapi.reportes.repository.ReportesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportesService {

    private final ReportesRepository reportesRepository;

    /**
     * Obtiene la disponibilidad de todos los materiales
     */
    @Transactional(readOnly = true)
    public List<DisponibilidadMaterialDTO> obtenerDisponibilidadMateriales() {
        return reportesRepository.obtenerDisponibilidadMateriales();
    }

    /**
     * Obtiene la disponibilidad de un material específico
     */
    @Transactional(readOnly = true)
    public DisponibilidadMaterialDTO obtenerDisponibilidadMaterialPorId(Long materialId) {
        DisponibilidadMaterialDTO resultado = reportesRepository.obtenerDisponibilidadMaterialPorId(materialId);
        if (resultado == null) {
            throw new RuntimeException("Material no encontrado con ID: " + materialId);
        }
        return resultado;
    }

    /**
     * Obtiene todos los préstamos activos
     */
    @Transactional(readOnly = true)
    public List<PrestamoActivoDTO> obtenerPrestamosActivos() {
        return reportesRepository.obtenerPrestamosActivos();
    }

    /**
     * Obtiene préstamos activos filtrados por estado (VIGENTE o VENCIDO)
     */
    @Transactional(readOnly = true)
    public List<PrestamoActivoDTO> obtenerPrestamosActivosPorEstado(String estado) {
        if (!estado.equals("VIGENTE") && !estado.equals("VENCIDO")) {
            throw new IllegalArgumentException("Estado debe ser VIGENTE o VENCIDO");
        }
        return reportesRepository.obtenerPrestamosActivosPorEstado(estado);
    }

    /**
     * Registra la devolución de un préstamo usando el stored procedure
     */
    @Transactional
    public void registrarDevolucion(Long prestamoId, LocalDateTime fechaDevolucion) {
        if (prestamoId == null || prestamoId <= 0) {
            throw new IllegalArgumentException("ID de préstamo inválido");
        }
        if (fechaDevolucion == null) {
            fechaDevolucion = LocalDateTime.now();
        }
        reportesRepository.registrarDevolucion(prestamoId, fechaDevolucion);
    }

    /**
     * Verifica la disponibilidad de ejemplares de un material
     */
    @Transactional(readOnly = true)
    public DisponibilidadResponseDTO verificarDisponibilidad(Long materialId) {
        if (materialId == null || materialId <= 0) {
            throw new IllegalArgumentException("ID de material inválido");
        }
        Integer disponibles = reportesRepository.verificarDisponibilidad(materialId);
        return new DisponibilidadResponseDTO(materialId, disponibles);
    }
}


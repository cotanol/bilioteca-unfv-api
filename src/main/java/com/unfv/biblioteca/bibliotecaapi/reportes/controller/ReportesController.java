package com.unfv.biblioteca.bibliotecaapi.reportes.controller;

import com.unfv.biblioteca.bibliotecaapi.reportes.dto.DisponibilidadMaterialDTO;
import com.unfv.biblioteca.bibliotecaapi.reportes.dto.DisponibilidadResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.reportes.dto.DevolucionRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.reportes.dto.PrestamoActivoDTO;
import com.unfv.biblioteca.bibliotecaapi.reportes.service.ReportesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReportesController {

    private final ReportesService reportesService;

    /**
     * GET /api/reportes/disponibilidad
     * Obtiene la disponibilidad de todos los materiales (usa la vista vw_disponibilidad_material)
     */
    @GetMapping("/disponibilidad")
    public ResponseEntity<List<DisponibilidadMaterialDTO>> obtenerDisponibilidadMateriales() {
        List<DisponibilidadMaterialDTO> disponibilidad = reportesService.obtenerDisponibilidadMateriales();
        return ResponseEntity.ok(disponibilidad);
    }

    /**
     * GET /api/reportes/disponibilidad/{materialId}
     * Obtiene la disponibilidad de un material específico
     */
    @GetMapping("/disponibilidad/{materialId}")
    public ResponseEntity<DisponibilidadMaterialDTO> obtenerDisponibilidadMaterialPorId(
            @PathVariable Long materialId) {
        try {
            DisponibilidadMaterialDTO disponibilidad = reportesService.obtenerDisponibilidadMaterialPorId(materialId);
            return ResponseEntity.ok(disponibilidad);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/reportes/prestamos-activos
     * Obtiene todos los préstamos activos (usa la vista vw_prestamos_activos)
     */
    @GetMapping("/prestamos-activos")
    public ResponseEntity<List<PrestamoActivoDTO>> obtenerPrestamosActivos(
            @RequestParam(required = false) String estado) {
        List<PrestamoActivoDTO> prestamos;
        
        if (estado != null && !estado.isEmpty()) {
            prestamos = reportesService.obtenerPrestamosActivosPorEstado(estado.toUpperCase());
        } else {
            prestamos = reportesService.obtenerPrestamosActivos();
        }
        
        return ResponseEntity.ok(prestamos);
    }

    /**
     * POST /api/reportes/devolucion
     * Registra la devolución de un préstamo (usa el SP sp_registrar_devolucion)
     */
    @PostMapping("/devolucion")
    public ResponseEntity<Map<String, String>> registrarDevolucion(
            @RequestBody DevolucionRequestDTO request) {
        try {
            LocalDateTime fechaDevolucion = request.getFechaDevolucion() != null 
                    ? request.getFechaDevolucion() 
                    : LocalDateTime.now();
            
            reportesService.registrarDevolucion(request.getPrestamoId(), fechaDevolucion);
            
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Devolución registrada exitosamente");
            response.put("prestamoId", request.getPrestamoId().toString());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al registrar la devolución: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET /api/reportes/verificar-disponibilidad/{materialId}
     * Verifica la disponibilidad de un material (usa el SP sp_verificar_disponibilidad)
     */
    @GetMapping("/verificar-disponibilidad/{materialId}")
    public ResponseEntity<DisponibilidadResponseDTO> verificarDisponibilidad(
            @PathVariable Long materialId) {
        try {
            DisponibilidadResponseDTO disponibilidad = reportesService.verificarDisponibilidad(materialId);
            return ResponseEntity.ok(disponibilidad);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


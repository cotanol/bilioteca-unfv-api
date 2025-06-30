package com.unfv.biblioteca.bibliotecaapi.catalogo.controller;

import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.request.CrearMaterialRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.MaterialDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.service.CatalogoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // 1. Combina @Controller y @ResponseBody. Indica que devolverá JSON.
@RequestMapping("/catalogo") // 2. Define una URL base para todos los endpoints de este controlador.
@RequiredArgsConstructor // 3. Lombok crea el constructor para inyectar las dependencias final.
public class CatalogoController {
    private final CatalogoService catalogoService; // 4. Inyectamos nuestro servicio.

    /**
     * Endpoint para crear un nuevo material.
     * Se accede a través de POST /api/catalogo/materiales
     */
    @PostMapping("/materiales")
    public ResponseEntity<MaterialDetalleDTO> crearMaterial(@Valid @RequestBody CrearMaterialRequestDTO request) {
        // 5. Llama al servicio para ejecutar la lógica de negocio
        MaterialDetalleDTO materialCreado = catalogoService.crearMaterial(request);

        // 6. Devuelve el DTO de respuesta con un código de estado 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(materialCreado);
    }

    /**
     * Endpoint para buscar un material por su ID.
     * Se accede a través de GET /api/catalogo/materiales/{id}
     */
    @GetMapping("/materiales/{id}")
    public ResponseEntity<MaterialDetalleDTO> buscarMaterialPorId(@PathVariable Long id) {
        // 7. Llama al servicio para buscar el material
        MaterialDetalleDTO materialEncontrado = catalogoService.buscarMaterialPorId(id);

        // 8. Devuelve el DTO de respuesta con un código de estado 200 OK
        return ResponseEntity.ok(materialEncontrado);
    }

    // --- Aquí irían otros endpoints para el catálogo ---

    // GET /api/catalogo/autores - Para listar todos los autores
    // GET /api/catalogo/categorias - Para listar todas las categorías
    // PUT /api/catalogo/materiales/{id} - Para actualizar un material
}

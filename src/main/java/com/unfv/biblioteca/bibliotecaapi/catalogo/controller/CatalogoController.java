package com.unfv.biblioteca.bibliotecaapi.catalogo.controller;

import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.request.*;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.AutorResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.CategoriaResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.EditorialResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.MaterialDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.service.CatalogoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalogo")
@RequiredArgsConstructor
public class CatalogoController {

    private final CatalogoService catalogoService;

    // =================================================================
    // ENDPOINTS PARA MATERIALES
    // =================================================================

    @PostMapping("/materiales")
    public ResponseEntity<MaterialDetalleDTO> crearMaterial(@Valid @RequestBody CrearMaterialRequestDTO request) {
        MaterialDetalleDTO materialCreado = catalogoService.crearMaterial(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(materialCreado);
    }

    @GetMapping("/materiales/{id}")
    public ResponseEntity<MaterialDetalleDTO> buscarMaterialPorId(@PathVariable Long id) {
        MaterialDetalleDTO materialEncontrado = catalogoService.buscarMaterialPorId(id);
        return ResponseEntity.ok(materialEncontrado);
    }

    // =================================================================
    // ENDPOINTS PARA AUTORES
    // =================================================================

    @GetMapping("/autores")
    public ResponseEntity<List<AutorResponseDTO>> listarAutores() {
        return ResponseEntity.ok(catalogoService.findAllAutores());
    }

    @GetMapping("/autores/{id}")
    public ResponseEntity<AutorResponseDTO> buscarAutorPorId(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.findAutorById(id));
    }

    @PostMapping("/autores")
    public ResponseEntity<AutorResponseDTO> crearAutor(@Valid @RequestBody CrearAutorRequestDTO request) {
        AutorResponseDTO autorCreado = catalogoService.crearAutor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(autorCreado);
    }

    @PutMapping("/autores/{id}")
    public ResponseEntity<AutorResponseDTO> actualizarAutor(@PathVariable Long id, @Valid @RequestBody ActualizarAutorRequestDTO request) {
        return ResponseEntity.ok(catalogoService.actualizarAutor(id, request));
    }

    @DeleteMapping("/autores/{id}")
    public ResponseEntity<Void> eliminarAutor(@PathVariable Long id) {
        catalogoService.eliminarAutor(id);
        return ResponseEntity.noContent().build();
    }

    // =================================================================
    // ENDPOINTS PARA CATEGORÍAS
    // =================================================================

    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        return ResponseEntity.ok(catalogoService.findAllCategorias());
    }

    @GetMapping("/categorias/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarCategoriaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.findCategoriaById(id));
    }

    @PostMapping("/categorias")
    public ResponseEntity<CategoriaResponseDTO> crearCategoria(@Valid @RequestBody CrearCategoriaRequestDTO request) {
        CategoriaResponseDTO categoriaCreada = catalogoService.crearCategoria(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaCreada);
    }

    @PutMapping("/categorias/{id}")
    public ResponseEntity<CategoriaResponseDTO> actualizarCategoria(@PathVariable Long id, @Valid @RequestBody ActualizarCategoriaRequestDTO request) {
        return ResponseEntity.ok(catalogoService.actualizarCategoria(id, request));
    }

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        catalogoService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }

    // =================================================================
    // ENDPOINTS PARA EDITORIALES
    // =================================================================

    @GetMapping("/editoriales")
    public ResponseEntity<List<EditorialResponseDTO>> listarEditoriales() {
        return ResponseEntity.ok(catalogoService.findAllEditoriales());
    }

    @GetMapping("/editoriales/{id}")
    public ResponseEntity<EditorialResponseDTO> buscarEditorialPorId(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.findEditorialById(id));
    }

    @PostMapping("/editoriales")
    public ResponseEntity<EditorialResponseDTO> crearEditorial(@Valid @RequestBody CrearEditorialRequestDTO request) {
        EditorialResponseDTO editorialCreada = catalogoService.crearEditorial(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(editorialCreada);
    }

    @PutMapping("/editoriales/{id}")
    public ResponseEntity<EditorialResponseDTO> actualizarEditorial(@PathVariable Long id, @Valid @RequestBody ActualizarEditorialRequestDTO request) {
        return ResponseEntity.ok(catalogoService.actualizarEditorial(id, request));
    }

    @DeleteMapping("/editoriales/{id}")
    public ResponseEntity<Void> eliminarEditorial(@PathVariable Long id) {
        catalogoService.eliminarEditorial(id);
        return ResponseEntity.noContent().build();
    }
}

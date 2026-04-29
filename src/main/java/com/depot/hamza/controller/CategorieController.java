package com.depot.hamza.controller;

import com.depot.hamza.dto.CategorieDTO;
import com.depot.hamza.service.CategorieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/categories")
public class CategorieController {

    private final CategorieService categorieService;

    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @GetMapping
    public Page<CategorieDTO> getAllCategories(@PageableDefault(size = 10) Pageable pageable) {
        return categorieService.getAllCategories(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategorieDTO> getCategorieById(@PathVariable Long id) {
        return categorieService.getCategorieById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CategorieDTO createCategorie(@RequestBody CategorieDTO categorieDTO) {
        return categorieService.saveCategorie(categorieDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategorieDTO> updateCategorie(@PathVariable Long id, @RequestBody CategorieDTO dto) {
        return categorieService.getCategorieById(id).map(existing -> {
            existing.setNom(dto.getNom());
            CategorieDTO updated = categorieService.saveCategorie(existing);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deleteCategorie(@PathVariable Long id) {
        categorieService.deleteCategorie(id);
    }
}

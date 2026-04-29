package com.depot.hamza.controller;

import com.depot.hamza.dto.FournisseurDTO;
import com.depot.hamza.model.Fournisseur;
import com.depot.hamza.service.FournisseurService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/fournisseurs")
public class FournisseurController {

    private final FournisseurService fournisseurService;

    public FournisseurController(FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }

    @GetMapping
    public Page<FournisseurDTO> getAllFournisseurs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return fournisseurService.getAllFournisseurs(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fournisseur> getFournisseurById(@PathVariable Long id) {
        Optional<Fournisseur> fournisseur = fournisseurService.getFournisseurById(id);
        return fournisseur.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Fournisseur createFournisseur(@RequestBody Fournisseur fournisseur) {
        return fournisseurService.saveFournisseur(fournisseur);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fournisseur> updateFournisseur(@PathVariable Long id,
                                                         @RequestBody Fournisseur fournisseurDetails) {
        return fournisseurService.getFournisseurById(id)
                .map(fournisseur -> {
                    fournisseur.setNom(fournisseurDetails.getNom());
                    fournisseur.setAdresse(fournisseurDetails.getAdresse());
                    fournisseur.setTelephone(fournisseurDetails.getTelephone());
                    fournisseur.setEmail(fournisseurDetails.getEmail());
                    Fournisseur updated = fournisseurService.saveFournisseur(fournisseur);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFournisseur(@PathVariable Long id) {
        if (fournisseurService.getFournisseurById(id).isPresent()) {
            fournisseurService.deleteFournisseur(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

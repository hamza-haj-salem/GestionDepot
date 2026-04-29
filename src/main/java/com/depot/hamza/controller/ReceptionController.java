package com.depot.hamza.controller;

import com.depot.hamza.dto.ReceptionCreationDTO;
import com.depot.hamza.dto.ReceptionDetailsDTO;
import com.depot.hamza.model.Reception;
import com.depot.hamza.service.ReceptionService;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/receptions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReceptionController {

    private final ReceptionService receptionService;

    public ReceptionController(ReceptionService receptionService) {
        this.receptionService = receptionService;
    }
    
    @GetMapping
    public ResponseEntity<Page<ReceptionDetailsDTO>> getAllReceptionsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Reception> receptionsPage = receptionService.getAllReceptionsPaged(page, size);

        Page<ReceptionDetailsDTO> dtoPage = receptionsPage.map(reception ->
                receptionService.getReceptionWithProduitsDTO(reception.getId()).orElse(null)
        );

        return ResponseEntity.ok(dtoPage);
    }

    @PostMapping
    public ResponseEntity<ReceptionDetailsDTO> ajouterReception(@Valid @RequestBody ReceptionCreationDTO dto) {
        ReceptionDetailsDTO savedDto = receptionService.createReceptionFromDTO(dto);
        return ResponseEntity.ok(savedDto);
    }


    // Liste paginée des réceptions par fournisseur avec DTOs
    @GetMapping("/fournisseur/{fournisseurId}")
    public ResponseEntity<Page<ReceptionDetailsDTO>> getReceptionsByFournisseur(
            @PathVariable Long fournisseurId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Reception> receptionsPage = receptionService.getReceptionsByFournisseurPaged(fournisseurId, page, size);
        Page<ReceptionDetailsDTO> dtoPage = receptionsPage.map(reception -> 
            receptionService.getReceptionWithProduitsDTO(reception.getId()).orElse(null)
        );
        return ResponseEntity.ok(dtoPage);
    }

    // Détails d'une réception avec produits (DTO)
    @GetMapping("/{id}")
    public ResponseEntity<ReceptionDetailsDTO> getReceptionDetails(@PathVariable Long id) {
        return receptionService.getReceptionWithProduitsDTO(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Supprimer une réception
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReception(@PathVariable Long id) {
        receptionService.supprimerReception(id);
        return ResponseEntity.noContent().build();
    }
}

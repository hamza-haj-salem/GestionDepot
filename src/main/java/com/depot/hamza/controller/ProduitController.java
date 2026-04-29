package com.depot.hamza.controller;

import com.depot.hamza.Exceptions.PrixConflitException;
import com.depot.hamza.dto.PrixConflitResponseDTO;
import com.depot.hamza.model.Produit;
import com.depot.hamza.service.ProduitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    // Get all produits (sans pagination, optionnel)
    @GetMapping
    public List<Produit> getAllProduits() {
    	 return produitService.getAllProduits();   
    }

    // Get produits avec pagination
    // URL exemple: /api/produits/page?page=0&size=10
    @GetMapping("/page")
	public Page<Produit> getProduitsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return produitService.getProduits(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
        return produitService.getProduitById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
      
    @PostMapping("/check-create")
    public ResponseEntity<?> checkAndCreateProduit(@RequestBody Produit produit) {
        try {
            Produit created = produitService.checkAndCreateProduit(produit);
            return ResponseEntity.status(201).body(created);
        } catch (PrixConflitException ex) {
            return ResponseEntity.status(409)
                    .body(new PrixConflitResponseDTO(ex.getMessage(), ex.getProduitsSimilaires()));
        }
    }
    
    @PostMapping
    public Produit createProduit(@RequestBody Produit produit) {
        if (produit.getQuantite() == null) {
            produit.setQuantite(0);
        }
        return produitService.createProduit(produit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produit> updateProduit(@PathVariable Long id, @RequestBody Produit produitDetails) {
        return produitService.getProduitById(id).map(produit -> {
            Produit updatedProduit = produitService.updateProduit(id, produitDetails);
            return ResponseEntity.ok(updatedProduit);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public void deleteProduit(@PathVariable Long id) {
    	try {
    		produitService.deleteProduit(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.getMessage();
		}    
    }

    @GetMapping("/search")
    public Page<Produit> searchProduits(
        @RequestParam(required = false) String searchTerm,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {

        return produitService.searchProduits(searchTerm, page, size);
    }
    
    
    
}

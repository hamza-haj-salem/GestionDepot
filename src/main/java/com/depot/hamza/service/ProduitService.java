package com.depot.hamza.service;

import com.depot.hamza.Exceptions.PrixConflitException;
import com.depot.hamza.model.Produit;
import com.depot.hamza.repository.ProduitRepository;
import com.depot.hamza.repository.ReceptionProduitRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;
    private final ReceptionProduitRepository receptionProduitRepository;

    public ProduitService(ProduitRepository produitRepository,
    		ReceptionProduitRepository receptionProduitRepository) {
    	
        this.produitRepository = produitRepository;
        this.receptionProduitRepository = receptionProduitRepository;
    }

    // Get all without pagination (optionnel)
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    // Get all with pagination
    public Page<Produit> getProduits(Pageable pageable) {
        return produitRepository.findAll(pageable);
    }

    public Optional<Produit> getProduitById(Long id) {
        return produitRepository.findById(id);
    }
    /*

    public Produit saveProduit(Produit produit) {
        // Chercher un produit existant par nom et catégorie (si catégorie non nulle)
        Optional<Produit> produitExistant = produitRepository.findByNomAndCategorie(produit.getNom(), produit.getCategorie());

        if (produitExistant.isPresent()) {
            Produit p = produitExistant.get();
            // On ajoute la quantité
            p.setQuantite(p.getQuantite() + produit.getQuantite());
            // Mettre à jour d'autres champs si nécessaire (prix, description)
            return produitRepository.save(p);
        } else {
            // Pas trouvé, créer nouveau produit
            return produitRepository.save(produit);
        }
    }
    
    public Produit updateProduit(Long id, Produit produitDetails) {
        Produit produitExistant = produitRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé avec id " + id));

        produitExistant.setNom(produitDetails.getNom());
        produitExistant.setDescription(produitDetails.getDescription());
        produitExistant.setPrixAchat(produitDetails.getPrixAchat());
        produitExistant.setPrixVente(produitDetails.getPrixVente());
        produitExistant.setQuantite(produitDetails.getQuantite() != null ? produitDetails.getQuantite() : produitExistant.getQuantite());
        produitExistant.setCategorie(produitDetails.getCategorie());

        return produitRepository.save(produitExistant);
    }*/

    public Produit createProduit(Produit produit) {
        Optional<Produit> produitExistant = produitRepository.findByNomAndCategorieAndPrixAchat(produit.getNom(), produit.getCategorie(), produit.getPrixAchat());

        if (produitExistant.isPresent()) {
            Produit p = produitExistant.get();
            // Additionne la quantité pour création
            p.setQuantite(p.getQuantite() + produit.getQuantite());
            // Mettre à jour d'autres champs si nécessaire (prix, description)
            p.setNom(produit.getNom());
            p.setDescription(produit.getDescription());
            p.setPrixAchat(produit.getPrixAchat());
            p.setPrixVente(produit.getPrixVente());
            p.setCategorie(produit.getCategorie());

            return produitRepository.save(p);
        } else {
            // Nouveau produit
            return produitRepository.save(produit);
        }
    }

    public Produit updateProduit(Long id, Produit produitDetails) {
        Produit produitExistant = produitRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé avec id " + id));

        produitExistant.setNom(produitDetails.getNom());
        produitExistant.setDescription(produitDetails.getDescription());
        produitExistant.setPrixAchat(produitDetails.getPrixAchat());
        produitExistant.setPrixVente(produitDetails.getPrixVente());
        // Ici on remplace la quantité (pas d’addition)
        produitExistant.setQuantite(produitDetails.getQuantite() != null ? produitDetails.getQuantite() : produitExistant.getQuantite());
        produitExistant.setCategorie(produitDetails.getCategorie());

        return produitRepository.save(produitExistant);
    }


    @Transactional
    public void deleteProduit(Long id) {
        // 1. Vérifier si le produit existe
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec id: " + id));

        // 2. Supprimer toutes les références dans reception_produit
        receptionProduitRepository.deleteByProduitId(id);

        // 3. Supprimer le produit
        produitRepository.delete(produit);
    }
    
    // Pagination simple sans recherche
    public Page<Produit> getProduitsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return produitRepository.findAll(pageable);
    }

    // Pagination avec recherche sur nom (par exemple)
    public Page<Produit> searchProduits(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (searchTerm == null || searchTerm.isEmpty()) {
            return produitRepository.findAll(pageable);
        } else {
            return produitRepository.findByNomContainingIgnoreCase(searchTerm, pageable);
        }
    }
    
 

    public Produit checkAndCreateProduit(Produit produit) {
        List<Produit> produitsSimilaires = produitRepository.findAllByNomAndCategorie(produit.getNom(), produit.getCategorie());

        if (produitsSimilaires.isEmpty()) {
            if (produit.getQuantite() == null) produit.setQuantite(0);
            return produitRepository.save(produit);
        }

        Optional<Produit> produitMemePrix = produitsSimilaires.stream()
                .filter(p -> p.getPrixAchat().equals(produit.getPrixAchat()))
                .findFirst();

        if (produitMemePrix.isPresent()) {
            Produit p = produitMemePrix.get();
            p.setQuantite(p.getQuantite() + (produit.getQuantite() != null ? produit.getQuantite() : 0));
            p.setDescription(produit.getDescription());
            p.setPrixVente(produit.getPrixVente());
            return produitRepository.save(p);
        } else {
            throw new PrixConflitException("Produit similaire existe avec un prix différent", produitsSimilaires);
        }
    }
    
    

}

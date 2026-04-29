package com.depot.hamza.service;

import com.depot.hamza.dto.ReceptionCreationDTO;
import com.depot.hamza.dto.ReceptionDetailsDTO;
import com.depot.hamza.mapper.ReceptionMapper;
import com.depot.hamza.model.Categorie;
import com.depot.hamza.model.Fournisseur;
import com.depot.hamza.model.Produit;
import com.depot.hamza.model.Reception;
import com.depot.hamza.model.ReceptionProduit;
import com.depot.hamza.repository.CategorieRepository;
import com.depot.hamza.repository.FournisseurRepository;
import com.depot.hamza.repository.ProduitRepository;
import com.depot.hamza.repository.ReceptionProduitRepository;
import com.depot.hamza.repository.ReceptionRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReceptionService {

    private final ReceptionRepository receptionRepository;
    private final ReceptionProduitRepository receptionProduitRepository;
    private final ProduitRepository produitRepository;
    private final FournisseurRepository fournisseurRepository;
    private final CategorieRepository categorieRepository;

    public ReceptionService(ReceptionRepository receptionRepository,
                            ReceptionProduitRepository receptionProduitRepository,
                            ProduitRepository produitRepository,
                            FournisseurRepository fournisseurRepository,
                            CategorieRepository categorieRepository) {
        this.receptionRepository = receptionRepository;
        this.receptionProduitRepository = receptionProduitRepository;
        this.produitRepository = produitRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.categorieRepository = categorieRepository;
    }
    
    
   
    

    @Transactional
    public ReceptionDetailsDTO createReceptionFromDTO(ReceptionCreationDTO dto) {
        Fournisseur fournisseur = fournisseurRepository.findById(dto.getFournisseurId())
            .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));

        Reception reception = new Reception();
        reception.setFournisseur(fournisseur);
        reception.setDateReception(LocalDateTime.now());

        List<ReceptionProduit> receptionProduits = dto.getProduits().stream().map(prodDto -> {
            Produit produit;

            if (prodDto.getProduitId() == null || Boolean.TRUE.equals(prodDto.getIsNew())) {
                // Nouveau produit à créer
                produit = new Produit();
                produit.setNom(prodDto.getNomProduit());
                produit.setPrixAchat(prodDto.getPrixAchat());
                produit.setQuantite(0); // Initialisé à 0, on va ajouter quantité ensuite
                Categorie cat = categorieRepository.findById(prodDto.getCategorie().getId())
                        .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
                produit.setCategorie(cat);
                produit = produitRepository.save(produit);
                
            } else {
                // Produit existant
                produit = produitRepository.findById(prodDto.getProduitId())
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé: " + prodDto.getProduitId()));
            }

            // Met à jour la quantité stockée
            int nouvelleQuantite = (produit.getQuantite() == null ? 0 : produit.getQuantite()) + prodDto.getQuantite();
            produit.setQuantite(nouvelleQuantite);
            produitRepository.save(produit);

            ReceptionProduit rp = new ReceptionProduit();
            rp.setProduit(produit);
            rp.setQuantite(prodDto.getQuantite());
            rp.setPrixAchat(prodDto.getPrixAchat());
            rp.setReception(reception);
            return rp;
        }).collect(Collectors.toList());

        reception.setReceptionProduits(receptionProduits);

        Reception savedReception = receptionRepository.save(reception);

        return getReceptionWithProduitsDTO(savedReception.getId())
                .orElseThrow(() -> new RuntimeException("Réception introuvable après création"));
    }



    public List<Reception> getReceptionsByFournisseur(Long fournisseurId) {
        return receptionRepository.findByFournisseurIdOrderByDateReceptionDesc(fournisseurId);
    }

    public Optional<Reception> getReceptionWithProduits(Long receptionId) {
        Optional<Reception> receptionOpt = receptionRepository.findById(receptionId);
        receptionOpt.ifPresent(r -> r.setReceptionProduits(receptionProduitRepository.findByReceptionId(receptionId)));
        return receptionOpt;
    }

    @Transactional
    public void supprimerReception(Long receptionId) {
        Optional<Reception> receptionOpt = receptionRepository.findById(receptionId);
        if (receptionOpt.isPresent()) {
            Reception reception = receptionOpt.get();
            receptionProduitRepository.findByReceptionId(receptionId).forEach(rp -> {
                produitRepository.findById(rp.getProduit().getId()).ifPresent(produit -> {
                    int quantiteAvant = produit.getQuantite() != null ? produit.getQuantite() : 0;
                    produit.setQuantite(quantiteAvant - rp.getQuantite());
                    produitRepository.save(produit);
                });
            });
            receptionProduitRepository.deleteAll(reception.getReceptionProduits());
            receptionRepository.deleteById(receptionId);
        }
    }

    public List<ReceptionDetailsDTO> getReceptionsByFournisseurDTO(Long fournisseurId) {
        List<Reception> receptions = receptionRepository.findByFournisseurIdOrderByDateReceptionDesc(fournisseurId);
        receptions.forEach(r -> r.setReceptionProduits(receptionProduitRepository.findByReceptionId(r.getId())));
        return receptions.stream()
            .map(r -> ReceptionMapper.toReceptionDetailsDTO(r, fournisseurRepository, produitRepository, r.getReceptionProduits()))
            .collect(Collectors.toList());
    }

    public Optional<ReceptionDetailsDTO> getReceptionWithProduitsDTO(Long receptionId) {
        Optional<Reception> receptionOpt = receptionRepository.findById(receptionId);
        receptionOpt.ifPresent(r -> {
            List<ReceptionProduit> rpList = receptionProduitRepository.findByReceptionId(receptionId);
            // charge le nom produit complet (optionnel)
            rpList.forEach(rp -> produitRepository.findById(rp.getProduit().getId()).ifPresent(produit -> {
                rp.getProduit().setNom(produit.getNom());
            }));
            r.setReceptionProduits(rpList);
        });

        return receptionOpt.map(r -> ReceptionMapper.toReceptionDetailsDTO(r, fournisseurRepository, produitRepository, r.getReceptionProduits()));
    }

    public Page<Reception> getReceptionsByFournisseurPaged(Long fournisseurId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return receptionRepository.findByFournisseurIdOrderByDateReceptionDesc(fournisseurId, pageable);
    }
    
    public List<ReceptionDetailsDTO> getAllReceptions() {
        return receptionRepository.findAll()
                .stream()
                .map(reception -> getReceptionWithProduitsDTO(reception.getId())
                        .orElseThrow(() -> new RuntimeException("Réception introuvable")))
                .collect(Collectors.toList());
    }
    public Page<Reception> getAllReceptionsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateReception").descending());
        return receptionRepository.findAll(pageable);
    }

}

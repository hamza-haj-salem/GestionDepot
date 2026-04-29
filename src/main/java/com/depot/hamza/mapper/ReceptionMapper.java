package com.depot.hamza.mapper;

import com.depot.hamza.dto.*;
import com.depot.hamza.model.*;
import com.depot.hamza.repository.ProduitRepository;
import com.depot.hamza.repository.FournisseurRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ReceptionMapper {

    public static ReceptionProduitDetailsDTO toReceptionProduitDetailsDTO(
            ReceptionProduit rp,
            ProduitRepository produitRepository) {
        
        Produit produitComplet = produitRepository.findById(rp.getProduit().getId()).orElse(null);

        String produitNom = produitComplet != null ? produitComplet.getNom() : null;
        Double prixAchat = rp.getPrixAchat() != null ? rp.getPrixAchat() : (produitComplet != null ? produitComplet.getPrixAchat() : null);

        ProduitDTO produitDTO = null;
        if (produitComplet != null) {
            produitDTO = new ProduitDTO(
                produitComplet.getId(),
                produitNom,
                prixAchat,
                produitComplet.getQuantite()
            );
        }

        return new ReceptionProduitDetailsDTO(
            rp.getId(),
            produitDTO,
            rp.getQuantite(),
            prixAchat
        );
    }

    public static ReceptionDetailsDTO toReceptionDetailsDTO(
            Reception reception,
            FournisseurRepository fournisseurRepository,
            ProduitRepository produitRepository,
            List<ReceptionProduit> receptionProduits) {

        Fournisseur fournisseurComplet = fournisseurRepository.findById(reception.getFournisseur().getId()).orElse(null);

        FournisseurDTO fournisseurDTO = null;
        if (fournisseurComplet != null) {
            fournisseurDTO = new FournisseurDTO(
                fournisseurComplet.getId(),
                fournisseurComplet.getNom()
            );
        }

        List<ReceptionProduitDetailsDTO> produitsDTO = receptionProduits.stream()
            .map(rp -> toReceptionProduitDetailsDTO(rp, produitRepository))
            .collect(Collectors.toList());

        return new ReceptionDetailsDTO(
            reception.getId(),
            reception.getDateReception(),
            fournisseurDTO,
            produitsDTO
        );
    }
}

package com.depot.hamza.Exceptions;

import java.util.List;

import com.depot.hamza.model.Produit;

public class PrixConflitException extends RuntimeException {

    private final List<Produit> produitsSimilaires;

    public PrixConflitException(String message, List<Produit> produitsSimilaires) {
        super(message);
        this.produitsSimilaires = produitsSimilaires;
    }

    public List<Produit> getProduitsSimilaires() {
        return produitsSimilaires;
    }
}
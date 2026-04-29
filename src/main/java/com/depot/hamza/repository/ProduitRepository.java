package com.depot.hamza.repository;

import com.depot.hamza.model.Categorie;
import com.depot.hamza.model.Produit;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    // تقدر تزيد دوال بحث خاصة هنا إذا حبيت
	 Page<Produit> findByNomContainingIgnoreCase(String nom, Pageable pageable);
	 Optional<Produit> findByNomAndCategorie(String nom, Categorie categorie); 
	 Optional<Produit> findByNomAndCategorieAndPrixAchat(String nom, Categorie categorie, Double prixAchat);
	 // IMPORTANT: méthode pour récupérer **tous** les produits avec même nom + catégorie (liste, pas Optional)
	    List<Produit> findAllByNomAndCategorie(String nom, Categorie categorie);
}




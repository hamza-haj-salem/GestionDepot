package com.depot.hamza.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.depot.hamza.model.Produit;
import com.depot.hamza.repository.ProduitRepository;

@SpringBootTest @Transactional  // @Transactional bch les données de test m yb9ouch 
//fl bd, 5ater f test spring fait rollback a la fin de test expret , donc ay suppression 
// wala ajout fl bd ymchi pendant le test seulement w apres transaction la bd revien 
//comme au debut ..
public class ProduitServiceTest {
	
	@Autowired
    private ProduitService produitService;
	
	@Autowired
	private ProduitRepository produitRepository;

	    @Test 
	    void testGetAllProduits() {
	    	
	    	// 0. Nettoyer la base , grace @Transactional elle va s'annule apres e test
	        produitRepository.deleteAll();
	        // 1. Préparer les données
	        Produit p1 = new Produit();
	        p1.setNom("Produit Test 1");
	        p1.setPrixAchat(10.0);
	        p1.setPrixVente(15.0);
	        p1.setQuantite(5);

	        Produit p2 = new Produit();
	        p2.setNom("Produit Test 2");
	        p2.setPrixAchat(20.0);
	        p2.setPrixVente(25.0);
	        p2.setQuantite(3);

	        produitRepository.save(p1);
	        produitRepository.save(p2);

	        // 2. Appeler la méthode à tester
	        List<Produit> produits = produitService.getAllProduits();

	        // 3. Vérifications
	        assertNotNull(produits);
	        assertTrue(produits.size() >= 2);
	    }

}

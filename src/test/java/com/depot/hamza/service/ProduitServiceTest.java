package com.depot.hamza.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.depot.hamza.Exceptions.PrixConflitException;
import com.depot.hamza.model.Produit;
import com.depot.hamza.repository.ProduitRepository;

@SpringBootTest @Transactional  // @Transactional bch les données de test m yb9ouch 
//fl bd, 5ater f test spring fait rollback a la fin de test expret , donc ay suppression 
// wala ajout fl bd ymchi pendant le test seulement w apres transaction la bd revien 
//comme au debut ..
//TW WALLET NE5dem BL H2 donc ma3ach nest7a9ha @transaction 5ater f lwl kont 
//ntesti 3l base reel .. juste n7eb ngardi l'explication hathi 
@org.springframework.test.context.ActiveProfiles("test")
public class ProduitServiceTest {
	
	@Autowired
    private ProduitService produitService;
	
	@Autowired
	private ProduitRepository produitRepository;

	@Test
	void testGetAllProduits() {

	    Produit p1 = new Produit();
	    p1.setNom("Produit1");
	    p1.setPrixAchat(10.0);
	    p1.setPrixVente(15.0);
	    p1.setQuantite(5);

	    Produit p2 = new Produit();
	    p2.setNom("Produit2");
	    p2.setPrixAchat(20.0);
	    p2.setPrixVente(25.0);
	    p2.setQuantite(3);

	    produitRepository.save(p1);
	    produitRepository.save(p2);

	    List<Produit> produits = produitService.getAllProduits();

	    assertNotNull(produits);
	    assertEquals(3, produits.size());
	}
	
	@Test
	void testCreateProduit_ProduitExistant_AjouteQuantite() {

	    // 1. Préparer produit existant en base
	    Produit existant = new Produit();
	    existant.setNom("ProduitTest");
	    existant.setPrixAchat(10.0);
	    existant.setPrixVente(15.0);
	    existant.setQuantite(5);

	    produitRepository.save(existant);

	    // 2. Nouveau produit avec même nom + catégorie + prix
	    Produit nouveau = new Produit();
	    nouveau.setNom("ProduitTest");
	    nouveau.setPrixAchat(10.0); // IMPORTANT : même prix
	    nouveau.setPrixVente(20.0); // peut changer
	    nouveau.setQuantite(3);

	    // 3. Appel
	    Produit result = produitService.createProduit(nouveau);

	    // 4. Vérifications
	    assertEquals(existant.getId(), result.getId()); // même produit
	    assertEquals(8, result.getQuantite()); // 5 + 3

	    // vérifier qu'il n'y a PAS de doublon
	    List<Produit> produits = produitRepository.findAll();
	    assertEquals(1, produits.size());
	}
	
	@Test
	void testCheckAndCreateProduit_PrixDifferent_ThrowsException() {

	    // 1. Produit existant
	    Produit existant = new Produit();
	    existant.setNom("ProduitTest");
	    existant.setPrixAchat(10.0);
	    existant.setPrixVente(15.0);
	    existant.setQuantite(5);

	    produitRepository.save(existant);

	    // 2. Nouveau produit avec même nom MAIS prix différent
	    Produit nouveau = new Produit();
	    nouveau.setNom("ProduitTest");
	    nouveau.setPrixAchat(20.0); // ⚠️ PRIX DIFFERENT
	    nouveau.setPrixVente(25.0);
	    nouveau.setQuantite(3);

	    // 3. Vérifier exception
	    assertThrows(PrixConflitException.class, () -> {
	        produitService.checkAndCreateProduit(nouveau);
	    });

	    // 4. Vérifier qu'on a toujours 1 seul produit en base
	    List<Produit> produits = produitRepository.findAll();
	    assertEquals(1, produits.size());
	}
}

package com.depot.hamza.repository;

import com.depot.hamza.model.ReceptionProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;

@Repository
public interface ReceptionProduitRepository extends JpaRepository<ReceptionProduit, Long> {
    List<ReceptionProduit> findByReceptionId(Long receptionId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM ReceptionProduit rp WHERE rp.produit.id = :produitId")
    void deleteByProduitId(@Param("produitId") Long produitId);
}

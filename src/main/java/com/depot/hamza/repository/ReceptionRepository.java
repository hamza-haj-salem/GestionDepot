package com.depot.hamza.repository;

import com.depot.hamza.model.Reception;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceptionRepository extends JpaRepository<Reception, Long> {
    List<Reception> findByFournisseurIdOrderByDateReceptionDesc(Long fournisseurId);
    Page<Reception> findByFournisseurIdOrderByDateReceptionDesc(Long fournisseurId, Pageable pageable);
    Page<Reception> findAll(Pageable pageable);
}


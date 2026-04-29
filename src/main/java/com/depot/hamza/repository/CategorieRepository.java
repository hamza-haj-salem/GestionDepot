package com.depot.hamza.repository;

import com.depot.hamza.model.Categorie;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
	Optional<Categorie> findByNom(String nom);}


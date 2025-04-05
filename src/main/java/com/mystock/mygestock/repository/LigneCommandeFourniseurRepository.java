package com.mystock.mygestock.repository;

import com.mystock.mygestock.model.LigneCommandeClient;
import com.mystock.mygestock.model.LigneCommandeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigneCommandeFourniseurRepository extends JpaRepository<LigneCommandeFournisseur, Long> {

    List<LigneCommandeFournisseur> findAllByCommandeFournisseurId(Long id);
    List<LigneCommandeFournisseur> findAllByArticleId(Long id);
}

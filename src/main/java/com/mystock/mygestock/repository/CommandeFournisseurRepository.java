package com.mystock.mygestock.repository;

import com.mystock.mygestock.model.CommandeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeFournisseurRepository extends JpaRepository<CommandeFournisseur, Long> {
    List<CommandeFournisseur> findAllByFournisseurId(Long id);
}

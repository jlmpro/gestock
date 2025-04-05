package com.mystock.mygestock.repository;

import com.mystock.mygestock.model.Client;
import com.mystock.mygestock.model.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FournisseurRepository extends JpaRepository<Fournisseur,Long> {
}

package com.mystock.mygestock.repository;

import com.mystock.mygestock.entity.Vente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenteRepository extends JpaRepository<Vente, Long> {
}

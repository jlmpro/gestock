package com.mystock.mygestock.repository;

import com.mystock.mygestock.dto.UtilisateurDto;
import com.mystock.mygestock.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {
    Optional<Utilisateur> findByEmail(String email);
}

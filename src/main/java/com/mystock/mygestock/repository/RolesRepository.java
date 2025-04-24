package com.mystock.mygestock.repository;

import com.mystock.mygestock.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles,Long> {
    Optional<Roles> findByRoleName(String roleName);


    // Vérifier si un rôle existe par son nom
    boolean existsByRoleName(String roleName);
}

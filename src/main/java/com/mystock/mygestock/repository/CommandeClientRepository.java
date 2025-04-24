package com.mystock.mygestock.repository;


import com.mystock.mygestock.entity.CommandeClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommandeClientRepository extends JpaRepository<CommandeClient,Long> {
    List<CommandeClient> findAllByClientId(Long idClient);

    @Query("""
    SELECT c FROM CommandeClient c
    LEFT JOIN FETCH c.client cl
    LEFT JOIN FETCH c.ligneCommandeClients l
    LEFT JOIN FETCH l.article a
    LEFT JOIN FETCH a.category cat
    LEFT JOIN FETCH a.utilisateur u
    WHERE c.id = :id
""")
    Optional<CommandeClient> findByIdWithAllRelations(@Param("id") Long id);

    @Query("SELECT c FROM CommandeClient c LEFT JOIN FETCH c.ligneCommandeClients WHERE c.id = :id")
    Optional<CommandeClient> findByIdWithLignes(@Param("id") Long id);


}

package com.mystock.mygestock.repository;


import com.mystock.mygestock.model.CommandeClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommandeClientRepository extends JpaRepository<CommandeClient,Long> {
    List<CommandeClient> findAllByClientId(Long idClient);
}

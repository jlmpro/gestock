package com.mystock.mygestock.repository;

import com.mystock.mygestock.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Long> {
}

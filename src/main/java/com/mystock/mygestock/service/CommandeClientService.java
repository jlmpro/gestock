package com.mystock.mygestock.service;



import com.mystock.mygestock.dto.CommandeClientDto;
import com.mystock.mygestock.dto.LigneCommandeClientDto;
import com.mystock.mygestock.model.EtatCommande;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeClientService {

    CommandeClientDto save(CommandeClientDto dto);

    CommandeClientDto updateEtatCommande(Long idCommande, EtatCommande etatCommande);

    CommandeClientDto updateQuantiteCommande(Long idCommande, Long idLigneCommande, BigDecimal quatite);
    CommandeClientDto updateClient(Long idCommande, Long idClient);

    CommandeClientDto updateArticle(Long idCommande, Long idLigneCommande,Long newArticle );

    CommandeClientDto findById(Long id);

    List<CommandeClientDto> findAll();

    List<LigneCommandeClientDto> findAllLigneCommandeClientByCommandeClientId(Long idCommande);

    CommandeClientDto deleteArticle(Long idCommande, Long idLigneCommande);

    void delete(Long id);
}

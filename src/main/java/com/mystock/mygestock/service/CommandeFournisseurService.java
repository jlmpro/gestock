package com.mystock.mygestock.service;

import com.mystock.mygestock.dto.CommandeFournisseurDto;
import com.mystock.mygestock.dto.LigneCommandeClientDto;
import com.mystock.mygestock.dto.LigneCommandeFournisseurDto;
import com.mystock.mygestock.model.EtatCommande;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeFournisseurService {

    CommandeFournisseurDto save(CommandeFournisseurDto dto);

    CommandeFournisseurDto updateEtatCommande(Long idCommande, EtatCommande etatCommande);
    CommandeFournisseurDto updateQuantiteCommande(Long idCommande, Long idLigneCommande, BigDecimal quatite);
    CommandeFournisseurDto updateFournisseur(Long idCommande, Long idFournisseur);

    CommandeFournisseurDto updateArticle(Long idCommande, Long idLigneCommande,Long newArticle );
    List<CommandeFournisseurDto> findAll();

    CommandeFournisseurDto findById(Long id);

    List<LigneCommandeFournisseurDto> findAllLigneCommandeFournisseurByCommandeFournisseurId(Long idCommande);

    CommandeFournisseurDto deleteArticle(Long idCommande, Long idLigneCommande);

    void delete(Long id);

}

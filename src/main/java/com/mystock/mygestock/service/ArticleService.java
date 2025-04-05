package com.mystock.mygestock.service;

import com.mystock.mygestock.dto.ArticleDto;
import com.mystock.mygestock.dto.LigneCommandeClientDto;
import com.mystock.mygestock.dto.LigneCommandeFournisseurDto;
import com.mystock.mygestock.dto.LigneVenteDto;

import java.util.List;

public interface ArticleService {
    ArticleDto save(ArticleDto dto);
    List<ArticleDto> findAll();
    ArticleDto findById(Long id);

    ArticleDto findArticleByCodeArticle(String codeArticle);
    List<LigneVenteDto> findHistoriqueVente(Long idArticle);
    List<LigneCommandeClientDto> findHistoriqueCommandeClient(Long idArticle);
    List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Long idArticle);
    List<ArticleDto> findAllArticleByIdCategory(Long idArticle);
    void delete(Long id);
}

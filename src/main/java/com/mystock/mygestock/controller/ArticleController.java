package com.mystock.mygestock.controller;

import com.mystock.mygestock.controller.api.ArticleApi;
import com.mystock.mygestock.dto.ArticleDto;
import com.mystock.mygestock.dto.LigneCommandeClientDto;
import com.mystock.mygestock.dto.LigneCommandeFournisseurDto;
import com.mystock.mygestock.dto.LigneVenteDto;
import com.mystock.mygestock.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController implements ArticleApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);
    @Autowired
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    public ArticleDto save(ArticleDto dto) {
        LOGGER.debug("Requête pour enregistrer un article : {}", dto);
        ArticleDto savedArticle = articleService.save(dto);
        LOGGER.debug("Article enregistré avec succès : {}", savedArticle);
        return savedArticle;
    }

    @Override
    public List<ArticleDto> findAll() {
        return articleService.findAll();
    }

    @Override
    public ArticleDto findById(Long id) {
        return articleService.findById(id);
    }

    @Override
    public List<LigneVenteDto> findHistoriqueVente(Long idArticle) {
        return articleService.findHistoriqueVente(idArticle);
    }

    @Override
    public List<LigneCommandeClientDto> findHistoriqueCommandeClient(Long idArticle) {
        return articleService.findHistoriqueCommandeClient(idArticle);
    }

    @Override
    public List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Long idArticle) {
        return articleService.findHistoriqueCommandeFournisseur(idArticle);
    }

    @Override
    public List<ArticleDto> findAllArticleByIdCategory(Long idArticle) {
        return articleService.findAllArticleByIdCategory(idArticle);
    }

    @Override
    public ArticleDto findArticleByCodeArticle(String codeArticle) {
        return articleService.findArticleByCodeArticle(codeArticle);
    }

    @Override
    public void delete(Long id) {
        articleService.delete(id);
    }
}
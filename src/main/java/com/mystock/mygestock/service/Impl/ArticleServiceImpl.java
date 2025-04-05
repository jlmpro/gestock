package com.mystock.mygestock.service.Impl;

import com.mystock.mygestock.dto.ArticleDto;
import com.mystock.mygestock.dto.LigneCommandeClientDto;
import com.mystock.mygestock.dto.LigneCommandeFournisseurDto;
import com.mystock.mygestock.dto.LigneVenteDto;
import com.mystock.mygestock.exception.EntityNotFoundException;
import com.mystock.mygestock.exception.ErrorCodes;
import com.mystock.mygestock.exception.InvalidEntityException;
import com.mystock.mygestock.model.Article;
import com.mystock.mygestock.repository.ArticleRepository;
import com.mystock.mygestock.repository.LigneCommandeClientRepository;
import com.mystock.mygestock.repository.LigneCommandeFourniseurRepository;
import com.mystock.mygestock.repository.LigneVenteRepository;
import com.mystock.mygestock.service.ArticleService;
import com.mystock.mygestock.validator.ArticleValidator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private ArticleRepository articleRepository;
    private LigneVenteRepository ligneVenteRepository;
    private LigneCommandeFourniseurRepository ligneCommandeFourniseurRepository;
    private LigneCommandeClientRepository ligneCommandeClientRepository;
    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, LigneVenteRepository ligneVenteRepository, LigneCommandeFourniseurRepository ligneCommandeFourniseurRepository, LigneCommandeClientRepository ligneCommandeClientRepository) {
        this.articleRepository = articleRepository;
        this.ligneVenteRepository = ligneVenteRepository;
        this.ligneCommandeFourniseurRepository = ligneCommandeFourniseurRepository;
        this.ligneCommandeClientRepository = ligneCommandeClientRepository;
    }

    @Override
    public ArticleDto save(ArticleDto dto) {
        log.debug("Saving article: " + dto);
        List<String> errors = ArticleValidator.validate(dto);
        if (!errors.isEmpty()){
            log.error("Article is not valid {}", dto);
            throw new InvalidEntityException("L'article n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID,errors);
        }
        return ArticleDto.fromEntity(
                articleRepository.save(
                        ArticleDto.toEntity(dto))
        );
    }

    @Override
    public List<ArticleDto> findAll() {
        List<Article> articles = articleRepository.findAll();
        return articles.stream()
                .map(ArticleDto::fromEntity)
                .collect(Collectors.toList());

    }

    @Override
    public ArticleDto findById(Long id) {
        if (id == null){
            log.error("Article ID is not null");
            throw new IllegalArgumentException("Article ID cannot be null");
        }
        Optional<Article> article = articleRepository.findById(id);

        if (!article.isPresent()) {
            throw new EntityNotFoundException("Aucun article avec l'ID = " + id + " n'a été trouvé dans la base", ErrorCodes.ARTICLE_NOT_FOUND);
        }

        ArticleDto dto = ArticleDto.fromEntity(article.get());

        return dto;
    }

    @Override
    public ArticleDto findArticleByCodeArticle(String codeArticle) {
        if (codeArticle == null){
            log.error("Code Article  is not null");
            throw new IllegalArgumentException("Code Article cannot be null");
        }
        Optional<Article> article = articleRepository.findArticleByCodeArticle(codeArticle);

        if (!article.isPresent()) {
            throw new EntityNotFoundException("Aucun article avec le code = " + codeArticle + " n'a été trouvé dans la base", ErrorCodes.ARTICLE_NOT_FOUND);
        }

        ArticleDto dto = ArticleDto.fromEntity(article.get());

        return dto;
    }

    @Override
    public List<LigneVenteDto> findHistoriqueVente(Long idArticle) {
        return ligneVenteRepository.findAllByArticleId(idArticle)
                .stream()
                .map(LigneVenteDto::fromEntity)
                .collect(Collectors.toList())
                ;
    }

    @Override
    public List<LigneCommandeClientDto> findHistoriqueCommandeClient(Long idArticle) {
        return ligneCommandeClientRepository.findAllByArticleId(idArticle)
                .stream()
                .map(LigneCommandeClientDto::fromEntity)
                .collect(Collectors.toList())
                ;
    }

    @Override
    public List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Long idArticle) {
        return ligneCommandeFourniseurRepository.findAllByArticleId(idArticle)
                .stream()
                .map(LigneCommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList())
                ;
    }


    @Override
    public List<ArticleDto> findAllArticleByIdCategory(Long idArticle) {
        return articleRepository.findAllByCategoryId(idArticle)
                .stream()
                .map(ArticleDto::fromEntity)
                .collect(Collectors.toList())
                ;
    }

    @Override
    public void delete(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun article avec l'ID = " + id + " n'a été trouvé dans la base",
                        ErrorCodes.ARTICLE_NOT_FOUND));

        articleRepository.delete(article);
    }
}

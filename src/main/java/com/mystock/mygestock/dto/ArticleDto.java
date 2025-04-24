package com.mystock.mygestock.dto;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mystock.mygestock.entity.Article;
import com.mystock.mygestock.entity.Category;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class ArticleDto {
    private Long id ;
    private String codeArticle;
    private  String designation ;
    private BigDecimal prixUnitaire ;
    @JsonIgnore
    private BigDecimal tauxTva ;
    private String photo ;
    private Long categoryId ;
    @JsonIgnore
    private Boolean tvaApplicable ;
    private Boolean actif ;


    public static ArticleDto fromEntity(Article article){
        if (article == null){
            return null ;
        }
        return   ArticleDto.builder()
                .id(article.getId())
                .codeArticle(article.getCodeArticle())
                .designation(article.getDesignation())
                .prixUnitaire(article.getPrixUnitaire())
                .tauxTva(article.getTauxTva())
                .photo(article.getPhoto())
                .tvaApplicable(article.getTvaApplicable())
                .categoryId(article.getCategory() != null ? article.getCategory().getId() : null)
                .actif(article.getActif() !=null ?article.getActif() : null)
                .build();
    }

    public static Article toEntity(ArticleDto dto) {
        if (dto == null) {
            return null;
        }

            Article article = new Article();
            article.setId(dto.getId());
            article.setCodeArticle(dto.getCodeArticle());
            article.setDesignation(dto.getDesignation());
            article.setPrixUnitaire(dto.getPrixUnitaire());
            article.setTauxTva(dto.getTauxTva());
            article.setPhoto(dto.getPhoto());
            article.setTvaApplicable(dto.getTvaApplicable());
            article.setActif(dto.getActif());

        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            article.setCategory(category);
        }

        return article;
    }

}



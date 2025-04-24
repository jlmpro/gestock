package com.mystock.mygestock.validator;

import com.mystock.mygestock.dto.ArticleDto;
import com.mystock.mygestock.service.Impl.ArticleServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArticleValidator  {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleValidator.class);
    public static List<String> validate(ArticleDto dto){
        List<String> errors = new ArrayList<>();
        if (dto == null) {
            errors.add("Veuillez renseigner le code de l'article");
            errors.add("Veuillez renseigner la désignation de l'article");
            errors.add("Veuillez renseigner le prix unitaire de l'article");
            errors.add("Veuillez renseigner le taux TVA de l'article");
            errors.add("Veuillez renseigner le prix unitaire TTC de l'article");
            errors.add("Veuillez sélectionner une catégorie");
            LOGGER.error("ArticleDto is null or invalid: {}", errors);
            return errors;
        }
        if (!StringUtils.hasText(dto.getCodeArticle())){
            errors.add("Veuillez renseigner le code de l'article");
            LOGGER.error("CodeArticle is null or empty");

        }
        if (!StringUtils.hasText(dto.getDesignation())){
            errors.add("Veuillez renseigner la désignation de l'article");
            LOGGER.error("Designation is null or empty");

        }
        if (dto.getPrixUnitaire() == null){
            errors.add("Veuillez renseigner le prix unitaire de l'article");
            LOGGER.error("PrixUnitaireTtc is null");

        }

        if (dto.getCategoryId() == null) {
            errors.add("Veuillez sélectionner une catégorie");
            LOGGER.error("Category is null");
        }

        return errors;
    }
}

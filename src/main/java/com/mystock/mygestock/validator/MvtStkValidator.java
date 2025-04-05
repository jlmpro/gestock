package com.mystock.mygestock.validator;

import com.mystock.mygestock.dto.CategoryDto;
import com.mystock.mygestock.dto.MvtStkDto;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MvtStkValidator {
    public static List<String> validate(MvtStkDto dto) {
        List<String> errors = new ArrayList<>();
        if (dto==null){
            errors.add("Veuiller renseigner la date du mouvement");
            errors.add("Veuiller renseigner la quantité du mouvement");
            errors.add("Veuiller renseigner l'article'");
            errors.add("Veuiller renseigner le type du mouvement");

            return errors;
        }

        if (dto.getDateMvt() == null){
            errors.add("Veuiller renseigner la date du mouvement");
        }
        if (dto.getQuantite() == null || dto.getQuantite().compareTo(BigDecimal.ZERO) == 0){
            errors.add("Veuiller renseigner la quantité du mouvement");
        }
        if (dto.getArticle() == null || dto.getArticle().getId()== null){
        }
        if (!StringUtils.hasLength(dto.getTypeMvtStk().name())){
            errors.add("Veuiller renseigner le type du mouvement");
        }

        return errors;
    }
}

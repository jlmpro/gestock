package com.mystock.mygestock.validator;

import com.mystock.mygestock.dto.CategoryDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CategoryValidator {
    public static List<String> validate(CategoryDto categorieDto){
        List<String> errors = new ArrayList<>();
            if (categorieDto == null || !StringUtils.hasLength(categorieDto.getCode())) {
                errors.add("Veuiller renseigner le code de la catégorie");
            }
        return errors;
    }
}

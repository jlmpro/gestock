package com.mystock.mygestock.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mystock.mygestock.model.Category;
import lombok.Builder;
import lombok.Data;


import java.util.List;
@Builder
@Data
public class CategoryDto {
    private Long id ;
    private String code;
    private String designation ;

    @JsonIgnore
    private List<ArticleDto> articles ;

    public static CategoryDto fromEntity(Category category){
        if (category == null){
            // return exception
            return null;
        }
        // Mapping de CategoryDto -> Category
        return CategoryDto.builder()
                .id(category.getId())
                .code(category.getCode())
                .designation(category.getDesignation())
                .build();
    }
    public static Category toEntity(CategoryDto categoryDto){
        if (categoryDto == null) {
            return  null;
            // throw ecxception
        }
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setCode(categoryDto.getCode());
        category.setDesignation(categoryDto.getDesignation());
        return category;
    }
}
